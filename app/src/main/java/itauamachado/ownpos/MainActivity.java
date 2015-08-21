package itauamachado.ownpos;

import android.content.Intent;

import android.content.IntentSender;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import java.util.Arrays;

import itauamachado.ownpos.LocationAPI.AddressLocationActivity;
import itauamachado.ownpos.LocationAPI.LastLocationActivity;
import itauamachado.ownpos.LocationAPI.UpdateLocationActivity;
import itauamachado.ownpos.service.IndoorService;


//GooglePlay
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener {

    // Login facebook
        private CallbackManager mcallbackManager;
        private LoginButton loginButton;

    // Login GooglePlus
        private static final int SIGN_IN_CODE = 56465;
        private GoogleApiClient googleApiClient;
        private ConnectionResult connectionResult;
        private boolean isConsentScreenOpened;
        private boolean isSignInButtonClicked;


    // MainActivity
        private static final String TAG = "Ownpos_Log";
        private Toolbar mToolbar;
        private Toolbar mToolbar_Botton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //facebook
            FacebookSdk.sdkInitialize(getApplicationContext());
            mcallbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(mcallbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Log.i(TAG, loginResult.getAccessToken().getUserId());
                            new GraphRequest(
                                    loginResult.getAccessToken(),
                                    loginResult.getAccessToken().getUserId(),

                                    null,
                                    HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        public void onCompleted(GraphResponse response) {

                                            try {
                                                String id = response.getJSONObject().getString("id");
                                                String name = response.getJSONObject().getString("name");
                                                String profileUrl = response.getJSONObject().getString("link");
                                                String imageUrl = "http://graph.facebook.com/" + id + "/picture";
                                                String email = response.getJSONObject().getString("email");
                                                String msg = id + " - " + name + " - " + profileUrl + " - " + imageUrl + " - " + email;

                                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                            ).executeAsync();
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(MainActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        setContentView(R.layout.activity_main);

        //viewMainActivity
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setTitle(getString(R.string.app_name));
        mToolbar.setSubtitle("Navegação Indoor");
        setSupportActionBar(mToolbar);

        mToolbar_Botton = (Toolbar) findViewById(R.id.inc_toolbar_botton);
        mToolbar_Botton.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), "clicked menu botton", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mToolbar_Botton.inflateMenu(R.menu.menu_booton);
        mToolbar_Botton.findViewById(R.id.iv_setting).setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Toast.makeText(getApplicationContext(), "clicked settings", Toast.LENGTH_SHORT).show();
                 }
             }
        );




        //LOGIN GOOPLE
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addConnectionCallbacks(MainActivity.this)
                    .addOnConnectionFailedListener(MainActivity.this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.mMenu_openmapa){
            startActivity(new Intent(this, IndoorService.class));
        }
        else
            if(id ==R.id.mMenu_loginGoogle){
                if(!googleApiClient.isConnecting()){
                    isSignInButtonClicked = true;
                    resolveSignIn();
                }
            }
        else
            if(id ==R.id.mMenu_logoutGoogle){
                if(googleApiClient.isConnected()){
                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    googleApiClient.disconnect();
                    googleApiClient.connect();
                }
            }
        else
            if(id ==R.id.mMenu_revokeGoogle){
                if(googleApiClient.isConnected()){
                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient).setResultCallback(new ResultCallback<Status>(){
                        @Override
                        public void onResult(Status result) {
                            finish();
                        }
                    });
                }
            }
        else
            if(id== R.id.mMenu_loginFacebook){
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
            }
        else
            if(id==R.id.mMenu_logoutFacebook){
                LoginManager.getInstance().logOut();

            }
        return super.onOptionsItemSelected(item);
    }

    //CICLO DE VIDA
    @Override
    public void onResume() {
        super.onResume();
        Profile.getCurrentProfile();
        Log.i(TAG, "onResume()");
    }

    @Override
    public void onStart(){
        super.onStart();
        googleApiClient.connect();
        Log.i(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
        Log.i(TAG, "onStop()");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult()");
        //FACEBOOK
        mcallbackManager.onActivityResult(requestCode, resultCode, data);

        //GOOGLEPLAY
        if(requestCode == SIGN_IN_CODE){
            isConsentScreenOpened = false;

            if(resultCode != RESULT_OK){
                isSignInButtonClicked = false;
            }

            if(!googleApiClient.isConnecting()){
                googleApiClient.connect();
            }
        }
    }




    // Metodos GooglePlus
    @Override
    public void onConnected(Bundle connectionHint) {
        isSignInButtonClicked = false;
        getDataProfile();
    }
    @Override
    public void onConnectionSuspended(int cause) {
        googleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if(!result.hasResolution()){
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), MainActivity.this, 0).show();
            return;
        }

        if(!isConsentScreenOpened){
            connectionResult = result;

            if(isSignInButtonClicked){
                resolveSignIn();
            }
        }
    }
    public void resolveSignIn(){
        if(connectionResult != null && connectionResult.hasResolution()){
            try {
                isConsentScreenOpened = true;
                connectionResult.startResolutionForResult(MainActivity.this, SIGN_IN_CODE);
            }
            catch(IntentSender.SendIntentException e) {
                isConsentScreenOpened = false;
                googleApiClient.connect();
            }
        }
    }
    public void getDataProfile(){
        Person p = Plus.PeopleApi.getCurrentPerson(googleApiClient);

        if(p != null){
            String id = p.getId();
            String name = p.getDisplayName();
            String profileUrl = p.getUrl();
            String imageUrl = p.getImage().getUrl();
            String email = Plus.AccountApi.getAccountName(googleApiClient);
            imageUrl = imageUrl.substring(0, imageUrl.length() - 2)+"200";

            String msg = id+" - "+name+" - "+profileUrl+" - "+imageUrl+" - "+email;
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(MainActivity.this, "Dados não liberados", Toast.LENGTH_SHORT).show();
        }
    }
}