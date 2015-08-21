package itauamachado.ownpos;

import android.content.Intent;

import android.content.IntentSender;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
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

import org.json.JSONException;


public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
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
        private Button btSignOut;
        private Button btRevokeAccess;
        private SignInButton btSignInDefault;

    // ControleActivity
        private ListView listView;
        private String[] activities = {"LastLocationActivity", "UpdateLocationActivity",  "AddressLocationActivity"};
        private static final String TAG = "Ownpos_Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook
            FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activities);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        // LOGIN FACEBOOK
        mcallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        loginButton.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
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
                                    String imageUrl = "http://graph.facebook.com/"+id+"/picture";
                                    String email = response.getJSONObject().getString("email");
                                    String msg = id+" - "+name+" - "+profileUrl+" - "+imageUrl+" - "+email;

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

            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        });

        //LOGIN GOOPLE
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        btSignOut = (Button) findViewById(R.id.btSignOut);
        btSignOut.setOnClickListener(MainActivity.this);
        btRevokeAccess = (Button) findViewById(R.id.btRevokeAccess);
        btRevokeAccess.setOnClickListener(MainActivity.this);
        btSignInDefault = (SignInButton) findViewById(R.id.btSignInDefault);
        btSignInDefault.setOnClickListener(MainActivity.this);


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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;

        switch(position){
            case 0:
                intent = new Intent(this, LastLocationActivity.class);
                break;
            case 1:
                intent = new Intent(this, UpdateLocationActivity.class);
                break;
            case 2:
                intent = new Intent(this, AddressLocationActivity.class);
                break;
        }
        startActivity(intent);
    }

    //CICLO DE VIDA
    @Override
    public void onResume() {
        super.onResume();
        message(Profile.getCurrentProfile());
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

    private void message(Profile profile) {
        if (profile != null) {
            Log.i(TAG, profile.toString());
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btSignInDefault){
            if(!googleApiClient.isConnecting()){
                isSignInButtonClicked = true;
                resolveSignIn();
            }
        }
        else if(v.getId() == R.id.btSignOut){
            if(googleApiClient.isConnected()){
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                googleApiClient.disconnect();
                googleApiClient.connect();
            }
        }
        else if(v.getId() == R.id.btRevokeAccess){
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
    }
}