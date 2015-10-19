package itauamachado.ownpos.service;

import android.content.Context;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.indooratlas.android.CalibrationState;
import com.indooratlas.android.IndoorAtlas;
import com.indooratlas.android.IndoorAtlasException;
import com.indooratlas.android.IndoorAtlasFactory;
import com.indooratlas.android.IndoorAtlasListener;
import com.indooratlas.android.ServiceState;

import java.util.List;

import de.greenrobot.event.EventBus;
import itauamachado.ownpos.domain.ItensMapa;
import itauamachado.ownpos.domain.MessageEB;
import itauamachado.ownpos.extras.Util;

public class IndoorMap implements
        IndoorAtlasListener,
        SensorEventListener {

    //IndoorAtlas
        private IndoorAtlas mIndoorAtlas;
        private String mApiKey =  Util.ApiKey;
        private String mApiSecret = Util.ApiSecret;
        private String mVenueId = Util.VenueId;
        private String mFloorId;
        private String mFloorPlanId;
        private boolean mIsPositioning;

        private Context mContext;

        private final SensorManager mSensorManager;
        private boolean MagneticFiled = false;
        private boolean Accelerometer = false;
        private boolean Gyroscope = false;
        private float mPercentual;



    public IndoorMap(Context c, String FloorId, String FloorPlanId ){
        mContext = c;
        mFloorId = FloorId;
        mFloorPlanId = FloorPlanId;

        mSensorManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
        List<Sensor> lista = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        if (lista.size() != 0){

            for(int i = 0; i < lista.size(); i++){

                int sensor = lista.get(i).getType();

                switch (sensor){
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        MagneticFiled = true;
                        break;
                    case Sensor.TYPE_GYROSCOPE:
                        Gyroscope = true;
                        break;
                    case Sensor.TYPE_ACCELEROMETER:
                        Accelerometer = true;
                        break;
                }
            }
        }

        Util.log("MagneticFiled:"+MagneticFiled+ " Accelerometer:"+Accelerometer+" Gyroscope:"+Gyroscope);


        if(MagneticFiled==Accelerometer==Gyroscope == true){
            initIndoorAtlas();
            //stopPositioning();
        }
        //usar no onDestroy
        //tearDown();
    }

    //IndoorAtlas Method
    public void tearDown() {
        if (mIndoorAtlas != null) {
            mIndoorAtlas.tearDown();
        }
    }

    //IndoorAtlas Method - parar
    public void stopPositioning() {

        if(mIsPositioning){
            mIsPositioning = false;
            if (mIndoorAtlas != null) {
                Util.log("Stop positioning");
                mIndoorAtlas.tearDown();
            }
        }
    }

    //IndoorAtlas Method - Iniciar
    public void startPositioning() {
        if (mIndoorAtlas != null) {
            Util.log(String.format("startPositioning, venueId: %s, floorId: %s, floorPlanId: %s",
                    mVenueId,
                    mFloorId,
                    mFloorPlanId));
            try {
                mIndoorAtlas.startPositioning(mVenueId, mFloorId, mFloorPlanId);
                mIsPositioning = true;
            } catch (IndoorAtlasException e) {
                Util.log("startPositioning failed: " + e);
            }
        } else {
            Util.log("calibration not ready, cannot start positioning");
        }
    }

    //IndoorAtlas Method
    private void initIndoorAtlas() {

        mPercentual = 0.0f;
        try {
            Util.log("Connecting with IndoorAtlas, apiKey: " + mApiKey);

            // obtain instance to positioning service, note that calibrating might begin instantly
            mIndoorAtlas = IndoorAtlasFactory.createIndoorAtlas(
                    mContext,
                    this, // IndoorAtlasListener
                    mApiKey,
                    mApiSecret);

            Util.log("IndoorAtlas instance created");
            //togglePositioning();

        } catch (IndoorAtlasException ex) {
            Util.log("init IndoorAtlas failed, " + ex.toString());
        }

    }

    /* IndoorAtlasListener interface */
    /**
     * This is where you will handle location updates.
     */
    public void onServiceUpdate(ServiceState state) {


        if(mPercentual > 0.90f){
            MessageEB eb = new MessageEB();
            eb.setmTag(MessageEB.TAG_INDOOR_ATLAS);

            float x = (float) state.getMetricPoint().getX();
            float y = (float) state.getMetricPoint().getY();

            //Toast.makeText(mContext,"position Metric: ("+x+", "+y+")",Toast.LENGTH_SHORT  ).show();
            String result = "position Metric: (" + x + ", " + y + ")";
            x = (float) state.getImagePoint().getI();
            y = (float) state.getImagePoint().getJ();
            //Toast.makeText(mContext,"position ImagePoint: ("+x+", "+y+")",Toast.LENGTH_SHORT  ).show();
            result = result + "position ImagePoint: (" + x + ", " + y + ")";
            PointF ownpos = new PointF(x, y);
            eb.setOwnpos(ownpos);
            /**
             eb.setmResult( eb.getmResult()+
             " troundtrip : " + state.getRoundtrip()+"ms" +
             " lat : " + state.getGeoPoint().getLatitude() +
             " tlon : "+ state.getGeoPoint().getLongitude() +
             " X [meter] : "+ state.getMetricPoint().getX() +
             " Y [meter] : "+ state.getMetricPoint().getY() +
             " I [pixel] : "+ state.getImagePoint().getI() +
             " J [pixel] : "+ state.getImagePoint().getJ() +
             " heading : "+ state.getHeadingDegrees() +
             " uncertainty: "+ state.getUncertainty());
             **/
            eb.setmResult(result);
            EventBus.getDefault().post(eb);
            stopPositioning();
        }
    }


    @Override
    public void onServiceFailure(int errorCode, String reason) {
        Util.log("onServiceFailure: reason : " + reason);

    }

    @Override
    public void onServiceInitializing() {
        Util.log("onServiceInitializing");
    }

    @Override
    public void onServiceInitialized() {
        Util.log("onServiceInitialized");
    }

    @Override
    public void onInitializationFailed(final String reason) {
        Util.log("onInitializationFailed: " + reason);
    }

    @Override
    public void onServiceStopped() {
        Util.log("onServiceStopped");
    }

    @Override
    public void onCalibrationStatus(CalibrationState calibrationState) {

        mPercentual = calibrationState.getPercentage();
        Util.log("onCalibrationStatus, percentage: " + calibrationState.getPercentage());
    }

    /**
     * Notification that calibration has reached level of quality that provides best possible
     * positioning accuracy.
     */
    @Override
    public void onCalibrationReady() {
        Util.log("onCalibrationReady");
    }

    @Override
    public void onCalibrationInvalid() {
        Util.log("onCalibrationInvalid");
    }

    @Override
    public void onCalibrationFailed(String reason) {
        Util.log("onCalibrationFailed: " + reason);
    }



    //sensor listener
    @Override
    public void onNetworkChangeComplete(boolean success) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
