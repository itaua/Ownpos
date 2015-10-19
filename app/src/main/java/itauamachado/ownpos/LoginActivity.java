package itauamachado.ownpos;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.objUsuario;
import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.network.NetworkConnection;
import itauamachado.ownpos.network.Transaction;
import itauamachado.ownpos.service.JobSchedulerService;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class LoginActivity  extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Transaction {

    // Login facebook
    private CallbackManager mcallbackManager;
    //private LoginButton mLoginFace;
    private Button mLoginFace;

    // Login GooglePlus
    //private SignInButton mLoginGoogle;
    private Button mLoginGoogle;
    private static final int SIGN_IN_CODE = 0;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult connectionResult;
    private boolean isConsentScreenOpened;
    private boolean isSignInButtonClicked;


    //LoginActivity
    private ProgressBar mPbLoad;
    private objUsuario mProfile = new objUsuario();
    private Intent mIntent;

    private boolean logar = true;
    private objUsuario lastUserLogged;

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
                        new GraphRequest(
                                loginResult.getAccessToken(),
                                loginResult.getAccessToken().getUserId(),

                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {

                                        try {
                                            String id = response.getJSONObject().getString("id");
                                            mProfile.setNome(response.getJSONObject().getString("name"));
                                            mProfile.setUrlSocial(response.getJSONObject().getString("link"));
                                            mProfile.setUrlPhoto("http://graph.facebook.com/" + id + "/picture");
                                            mProfile.setEmail(response.getJSONObject().getString("email"));
                                            mProfile.setLoginSocial("FACEBOOK");
                                            callVolleyRequest();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        ).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                        mLoginFace.setText("LOGIN FACEBOOK");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        setContentView(R.layout.activity_login);



        if (getIntent().getBooleanExtra("EXIT", false)) {
            logar = false;
        }

        lastUserLogged = new SQLiteConn(this).getUserLogged();
        mPbLoad = (ProgressBar) findViewById(R.id.pb_load);


        //LOGIN GOOPLE
        mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .addConnectionCallbacks(LoginActivity.this)
                .addOnConnectionFailedListener(LoginActivity.this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        Button btnAnonimo = (Button) findViewById(R.id.SignInVisitante);
        btnAnonimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProfile.setPerfil(Util.PERFIL_VISITANTE);
                mProfile.setId_senac("0");
                mProfile.setNome("Anônimo");
                mProfile.setEmail("sem email");
                mProfile.setStatus("Logado como visitante");
                mProfile.setLoginSocial(Util.PERFIL_VISITANTE);
                mProfile.setUrlPhoto("");
                mProfile.setUrlSocial("");
                logar = true;
                lastUserLogged = null;
                login();
            }
        });

        mLoginFace = (Button) findViewById(R.id.SignInFBook);
        mLoginFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLoginFace.getText().toString()=="LOGOUT"){
                    LoginManager.getInstance().logOut();
                }else{
                    lastUserLogged = null;
                    logar = true;
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
                }
            }
        });

        mLoginGoogle = (Button) findViewById(R.id.SignInGPlu);
        mLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    mLoginGoogle.setText("LOGIN GOOGLE");
                } else {
                    lastUserLogged = null;
                    logar = true;
                    isSignInButtonClicked = true;
                    resolveSignIn();
                }
            }
        });

        if(lastUserLogged == null){
            Toast.makeText(this, "PRIMEIRO LOGIN", Toast.LENGTH_SHORT).show();
        }else{
            login();
        }

        //EventBus.getDefault().register(this);
        //onJobLocation();
        //onJobContext();
    }

    public void login(){
        if(logar){
            if((lastUserLogged == null) && (mProfile != null)){
                new SQLiteConn(getBaseContext()).setUserLogged(mProfile);
            }
                mIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mIntent);
        }
    }

    //CICLO DE VIDA
    @Override
    public void onResume() {
        super.onResume();
        //se estiver pressionado o botão back na tela do main, fecha direto o programa.
        //desde que se coloque a tag "EXIT" no intent
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        //Facebook
        Profile.getCurrentProfile();
        //Google
        mGoogleApiClient.connect();
    }



    @Override
    public void onStart(){
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //FACEBOOK
        mcallbackManager.onActivityResult(requestCode, resultCode, data);


        //GOOGLEPLAY
        if(requestCode == SIGN_IN_CODE){
            isConsentScreenOpened = false;

            if(resultCode != RESULT_OK){
                isSignInButtonClicked = false;
            }

            if(!mGoogleApiClient.isConnecting()){
                mGoogleApiClient.connect();
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLoginGoogle.setText("LOGOUT");
        //isSignInButtonClicked = false;
        getDataProfile();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if(!result.hasResolution()){
            Util.log("Não pode logar: "+result.getErrorCode());
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), LoginActivity.this, 0).show();
            return;
        }

        Util.log("onConnectionFailed: " + isConsentScreenOpened);
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
                connectionResult.startResolutionForResult(LoginActivity.this, SIGN_IN_CODE);
            }
            catch(IntentSender.SendIntentException e) {
                isConsentScreenOpened = false;
                mGoogleApiClient.connect();
            }
        }
    }

    public void getDataProfile(){
        Person p = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if(p != null){
            String imageUrl = p.getImage().getUrl();
            imageUrl = imageUrl.substring(0, imageUrl.length() - 2)+"200";
            mProfile.setNome(p.getDisplayName());
            mProfile.setUrlSocial(p.getUrl());
            mProfile.setUrlPhoto(imageUrl);
            mProfile.setEmail(Plus.AccountApi.getAccountName(mGoogleApiClient));
            mProfile.setLoginSocial("GOOGLE");

            if(isSignInButtonClicked){
                isSignInButtonClicked = false;
                callVolleyRequest();
            }
        } else{
            Toast.makeText(LoginActivity.this, "Dados não liberados", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public WrapObjToNetwork doBefore() {
        mPbLoad.setVisibility(View.VISIBLE);
        if( Util.verifyConnection(this) ){
            return( new WrapObjToNetwork(mProfile, "get-login"));
        }
        return null;
    }

    @Override
    public void doAfter(JSONArray jsonArray) {
        mPbLoad.setVisibility(View.GONE);

        if (jsonArray != null) {
            JSONObject xtra;
            try {
                for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {
                    xtra = jsonArray.getJSONObject(i);
                    mProfile.setId_senac(xtra.getString("id"));
                    mProfile.setPerfil(xtra.getString("perfil"));
                    mProfile.setNome(xtra.getString("nome"));
                    mProfile.setStatus(xtra.getString("status"));
                    login();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void callVolleyRequest() {
        if (lastUserLogged == null)
            NetworkConnection.getInstance(this).execute(this, LoginActivity.class.getName());
    }
/*
    public void onJobLocation(){
        ComponentName cp = new ComponentName(this, JobSchedulerService.class);
        JobInfo jb = new JobInfo.Builder (Util.JOB_LOCATION_CODE, cp)
                .setBackoffCriteria(5000, JobInfo.BACKOFF_POLICY_LINEAR)
                //.setExtras(b)
                .setPersisted(true)
                .setPeriodic(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .build();

        JobScheduler js = JobScheduler.getInstance(this);
        js.schedule(jb);
    }

    public void onJobContext(){
        ComponentName cp = new ComponentName(this, JobSchedulerService.class);


        JobInfo jb = new JobInfo.Builder (Util.JOB_CONTEXT_CODE, cp)
                .setBackoffCriteria(5000, JobInfo.BACKOFF_POLICY_LINEAR)
                //.setExtras(b)
                .setPersisted(true)
                .setPeriodic(2000)
                //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .build();

        JobScheduler js = JobScheduler.getInstance(this);
        js.schedule(jb);
    }

    public void onCancelAll(){
        JobScheduler js = JobScheduler.getInstance(this);
        js.cancelAll();
    }
    */
}