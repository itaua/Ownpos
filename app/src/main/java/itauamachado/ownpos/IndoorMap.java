package itauamachado.ownpos;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.indooratlas.android.CalibrationState;
import com.indooratlas.android.IndoorAtlas;
import com.indooratlas.android.IndoorAtlasException;
import com.indooratlas.android.IndoorAtlasFactory;
import com.indooratlas.android.IndoorAtlasListener;
import com.indooratlas.android.ServiceState;

import java.text.DateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import itauamachado.ownpos.domain.MessageEB;
import itauamachado.ownpos.fragments.MapaIndoorFragment;
import itauamachado.ownpos.service.LocationIntentService;

public class IndoorMap extends AppCompatActivity implements
        IndoorAtlasListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //IndoorAtlas
        private IndoorAtlas mIndoorAtlas;
        private boolean mIsPositioning;
        private StringBuilder mSharedBuilder = new StringBuilder();
        private String mApiKey = "3f55ee6b-a7a1-4cab-8d2f-dec4e0ed34b4";
        private String mApiSecret = "Z!Is0QvX4anu3PRiCfDrSx6rYt%cIRIosrKO%7yVug0b6NAy2%1SoOe6)FKnfduEQ49rCEwjPB91X)yr&0cDReuf7A5OH3s!2a4p24x5tGiazalXCww(JlDMWd0etWSP";
        private String mVenueId = "855c9dfc-7bbb-48b9-b706-68c751678a4b";
        private String mFloorId = "98ea6a7c-f93f-4b9c-9681-14912bbee64f";
        private String mFloorPlanId = "adb2fdb5-ae60-45d7-850e-4ba94ede084d";

    //viewActivity
        private static final String TAG = "Ownpos_Log";
        private Toolbar mToolbar;
        private Toolbar mToolbar_Botton;


    //Api Location
        public static final String LOCATION = "location";
        public static final String TYPE = "type";
        public static final String ADDRESS = "address";

        private GoogleApiClient mGoogleApiClient;
        private Location mLastLocation;
        private LocationRequest mLocationRequest;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        //IndoorAtlas
            initIndoorAtlas();

        //Api Location
            callConnection();
            EventBus.getDefault().register(this);

        //viewActivity
            mToolbar = (Toolbar) findViewById(R.id.mToolbar);
            mToolbar.setTitle(getString(R.string.app_name));
            mToolbar.setSubtitle("Navegação Indoor");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            mToolbar_Botton = (Toolbar) findViewById(R.id.inc_toolbar_botton);
            mToolbar_Botton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.buscaCoordenada ){


                        String endereco = "miguel tostes, 656, Rio Branco, Porto alegre";
                        callIntentService(1, endereco);
                        log("Busca Coordenada");
                    }else
                    if(id == R.id.buscaEndereco ) {

                        log("Busca Endereço");
                        if (mLastLocation != null) {
                            callIntentService(2, null);
                        } else {
                            log("impossivel determinar endereço");
                        }
                    }
                    return false;
                }
            });
            mToolbar_Botton.inflateMenu(R.menu.menu_booton);
            mToolbar_Botton.findViewById(R.id.iv_setting).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "clicked settings", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

        MapaIndoorFragment mapaIndoorFragment = (MapaIndoorFragment) getSupportFragmentManager().findFragmentByTag("mapIndoor");
        if (mapaIndoorFragment == null){
            mapaIndoorFragment = new MapaIndoorFragment();
            FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, mapaIndoorFragment, "mapIndoor");
            ft.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_indoor_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_toggle_positioning){
            togglePositioning();
        }else
        if(id == android.R.id.home ){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //Ciclo de Vida Activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tearDown();
    }


    @Override
    public void onResume(){
        super.onResume();
        if(mGoogleApiClient !=null && mGoogleApiClient.isConnected()){
            startLocationUpdate();
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        if(mGoogleApiClient != null){
            stopLocationUpdate();
        }
    }

    //IndoorAtlas Method
    private void tearDown() {
        if (mIndoorAtlas != null) {
            mIndoorAtlas.tearDown();
        }
    }

    //IndoorAtlas Method
    private void stopPositioning() {
        mIsPositioning = false;
        if (mIndoorAtlas != null) {
            log("Stop positioning");
            mIndoorAtlas.stopPositioning();
        }
    }

    //IndoorAtlas Method
    private void startPositioning() {
        if (mIndoorAtlas != null) {
            log(String.format("startPositioning, venueId: %s, floorId: %s, floorPlanId: %s",
                    mVenueId,
                    mFloorId,
                    mFloorPlanId));
            try {
                mIndoorAtlas.startPositioning(mVenueId, mFloorId, mFloorPlanId);
                mIsPositioning = true;
            } catch (IndoorAtlasException e) {
                log("startPositioning failed: " + e);
            }
        } else {
            log("calibration not ready, cannot start positioning");
        }
    }

    //IndoorAtlas Method
    private void togglePositioning() {
        if (mIsPositioning) {
            stopPositioning();
        } else {
            startPositioning();
        }
    }

    //IndoorAtlas Method
    private void initIndoorAtlas() {

        try {

            log("Connecting with IndoorAtlas, apiKey: " + mApiKey);

            // obtain instance to positioning service, note that calibrating might begin instantly
            mIndoorAtlas = IndoorAtlasFactory.createIndoorAtlas(
                    getApplicationContext(),
                    this, // IndoorAtlasListener
                    mApiKey,
                    mApiSecret);

            log("IndoorAtlas instance created");
            //togglePositioning();

        } catch (IndoorAtlasException ex) {
            log("init IndoorAtlas failed, " + ex.toString());
        }

    }

    /* IndoorAtlasListener interface */
    /**
     * This is where you will handle location updates.
     */
    public void onServiceUpdate(ServiceState state) {

        mSharedBuilder.setLength(0);
        mSharedBuilder.append("Location: ")
                .append("\n\troundtrip : ").append(state.getRoundtrip()).append("ms")
                .append("\n\tlat : ").append(state.getGeoPoint().getLatitude())
                .append("\n\tlon : ").append(state.getGeoPoint().getLongitude())
                .append("\n\tX [meter] : ").append(state.getMetricPoint().getX())
                .append("\n\tY [meter] : ").append(state.getMetricPoint().getY())
                .append("\n\tI [pixel] : ").append(state.getImagePoint().getI())
                .append("\n\tJ [pixel] : ").append(state.getImagePoint().getJ())
                .append("\n\theading : ").append(state.getHeadingDegrees())
                .append("\n\tuncertainty: ").append(state.getUncertainty());

        log(mSharedBuilder.toString());
    }


    @Override
    public void onServiceFailure(int errorCode, String reason) {
        log("onServiceFailure: reason : " + reason);
    }

    @Override
    public void onServiceInitializing() {
        log("onServiceInitializing");
    }

    @Override
    public void onServiceInitialized() {
        log("onServiceInitialized");
    }

    @Override
    public void onInitializationFailed(final String reason) {
        log("onInitializationFailed: " + reason);
    }

    @Override
    public void onServiceStopped() {
        log("onServiceStopped");
    }

    @Override
    public void onCalibrationStatus(CalibrationState calibrationState) {
        log("onCalibrationStatus, percentage: " + calibrationState.getPercentage());
    }

    /**
     * Notification that calibration has reached level of quality that provides best possible
     * positioning accuracy.
     */
    @Override
    public void onCalibrationReady() {
        log("onCalibrationReady");
    }

    @Override
    public void onCalibrationInvalid() {
        log("onCalibrationInvalid");
    }

    @Override
    public void onCalibrationFailed(String reason) {
        log("onCalibrationFailed: "+reason);
    }

    @Override
    public void onNetworkChangeComplete(boolean success) {
    }

    //Methods API Location
    private synchronized void callConnection(){
       log("callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    private void initLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private void startLocationUpdate(){
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    //Interfaces API Location
    @Override
    public void onConnected(Bundle bundle) {
       log(".onConnected(" + bundle + ")");

        Location l = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient); // PARA JÁ TER UMA COORDENADA PARA O UPDATE FEATURE UTILIZAR

        if(l != null){
            mLastLocation = l;
        }
        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
       log(".onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onLocationChanged(Location location) {
        mSharedBuilder.setLength(0);
        mSharedBuilder.append("\n\tLocation: ")
                .append("\n\tLatitude:" + location.getLatitude())
                .append("\n\tLongitude: " + location.getLongitude())
                .append("\n\tBearing: " + location.getBearing())
                .append("\n\tAltitude: " + location.getAltitude())
                .append("\n\tSpeed: " + location.getSpeed())
                .append("\n\tProvider: " + location.getProvider())
                .append("\n\tAccuracy: " + location.getAccuracy())
                .append("\n\tSpeed: " + DateFormat.getTimeInstance().format(new Date()));
        mLastLocation = location;
        log(mSharedBuilder.toString());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        log(".onConnectionFailed(" + connectionResult + ")");
    }

    public void callIntentService(int type, String address){
        Intent it = new Intent(this, LocationIntentService.class);
        it.putExtra(TYPE, type);
        it.putExtra(ADDRESS, address);
        it.putExtra(LOCATION, mLastLocation);
        startService(it);
    }

    public void onEvent(final MessageEB m){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                log ("EventBus"+m.getResultMessage());
            }
        });
    }

    //Util
    private void log(final String msg) {
        Log.d(TAG, msg);
    }
}
