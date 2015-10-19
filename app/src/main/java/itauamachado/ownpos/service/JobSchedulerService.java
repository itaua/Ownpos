package itauamachado.ownpos.service;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import itauamachado.ownpos.domain.MessageEB;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.objUsuario;
import itauamachado.ownpos.extras.Util;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;



@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private JobParameters mJobParameters;
    private MessageEB mMessageEB;
    private LocationRequest mLocationRequest;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onStartJob(JobParameters params) {

        Util.log("onStartJob id (" + Util.JOB_NAME[params.getJobId() - 1] + ")");

        mJobParameters = params;

        if (params.getJobId() == Util.JOB_CONTEXT_CODE){
            Util.log("JOB_CONTEXT_CODE");
            ContentValues cv = new SQLiteConn(getBaseContext()).getLastGeoPosition();
            objUsuario user = new SQLiteConn(getBaseContext()).getUserLogged();
            if(cv != null){
                if(user == null){
                    user = new objUsuario();
                    user.setPerfil(Util.PERFIL_VISITANTE);
                    user.setNome("Anônimo");
                    user.setEmail("sem email");
                    user.setStatus("Logado como visitante");
                    user.setLoginSocial(Util.PERFIL_VISITANTE);
                    user.setUrlPhoto("");
                    user.setUrlSocial("");
                }


                double dist = getDistancia(
                        null,
                        new LatLng (Double.valueOf(cv.getAsString("longitude")), Double.valueOf(cv.getAsString("latitude"))));


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                double diferenca = 0.0;
                try {
                    Date dtOrigem= sdf.parse(cv.getAsString("verificado"));
                    Date agora = sdf.parse(sdf.format(new Date()));
                    long result = agora.getTime() - dtOrigem.getTime();
                    Util.log("result: "+result);
                    diferenca = result/(60 * 60 * 1000);

                    MessageEB mb = new MessageEB();
                    if(diferenca > 20){
                        mb.setmResult("diferença: "+diferenca);
                        mb.setDistancia(dist);
                        mb.setmTag(MessageEB.TAG_LOCATION);
                        mb.setIdJobSchedule(params.getJobId());
                    }else{
                        mb.setmResult("diferença: "+diferenca);
                        mb.setDistancia(dist);
                        mb.setmTag(MessageEB.TAG_LOCATION);
                        mb.setIdJobSchedule(params.getJobId());
                    }
                    EventBus.getDefault().post(mb);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(diferenca > 20){
                    Util.log("JOB_CONTEXT_CODE - PARA");
                    onStopJob(params);
                }else{
                    Util.log("JOB_CONTEXT_CODE - REINICIO");
                    this.jobFinished(params, true);
                }
            }else{
                Util.log("JOB_CONTEXT_CODE - REINICIO");
                this.jobFinished(params, true);
            }
        }else
        if(params.getJobId() == Util.JOB_LOCATION_CODE){
            Util.log("JOB_LOCATION_CODE");
            callConnection();
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Util.log("onStopJob()");
        return true;
    }

    //METHODOS LOCATION
    //Methods API Location
    private synchronized void callConnection(){
        //Log.i(TAG, "callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void initLocationRequest(){
        //Log.i(TAG, "initLocationRequest()");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); //Precisão Disponível > precisão Baixa
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //Sempre Precisão Alta
    }

    private void startLocationUpdate(){
        //Log.i(TAG, "startLocationUpdate()");
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this);
    }

    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this);
    }

    //API LOCATION - METHODOS
    @Override
    public void onConnected(Bundle bundle) {

        try{
            Location l = LocationServices
                    .FusedLocationApi
                    .getLastLocation(mGoogleApiClient); // PARA JÁ TER UMA COORDENADA PARA O UPDATE FEATURE UTILIZAR
            startLocationUpdate();
        }catch (NullPointerException e){
            Util.log("startLocationUpdate() - NullPointerException");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.i(TAG, ".onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Log.i(TAG, ".onConnectionFailed(" + connectionResult + ")");
    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.i(TAG, "JobSchedulerService.onLocationChanged()");

        mMessageEB = new MessageEB();
        mMessageEB.setmTag(MessageEB.TAG_LOCATION);
        mMessageEB.setLocation(location);
        mMessageEB.setIdJobSchedule(mJobParameters.getJobId());

        mMessageEB.setDistancia(getDistancia(
                location,
                null));
        EventBus.getDefault().post(mMessageEB);
        //Log.i(TAG, "Distancia: "+mMessageEB.getDistancia()+"m");
        new SQLiteConn(this).savePosition(location);

        //chama MyAsyncTask se quiser realizar outra ação.
        new MyAsyncTaskLocation(this).execute(mJobParameters);
        stopLocationUpdate();
    }

    private static class MyAsyncTaskLocation extends AsyncTask<JobParameters, Void, Void>{
        private JobSchedulerService jss;

        public MyAsyncTaskLocation(JobSchedulerService j){
            jss = j;
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Void doInBackground(JobParameters... params) {
            Util.log("JobScheduler Code: " + params[0].getJobId() + "Sleep por 30 minutos.");
            SystemClock.sleep(1800000);
            jss.onStopJob(params[0]);
            jss.jobFinished(params[0], true);
            return null;
        }
    }

    public double getDistancia(Location origem, LatLng latLng){

        //variaveis
        double dlon, dlat, a, distancia;
        double longitude;
        double latitude;

        if(origem != null) {
            longitude = origem.getLongitude();
            latitude = origem.getLatitude();
        }else{
            longitude = latLng.longitude;
            latitude = latLng.latitude;
        }

        //posição do senac
        double latitudePto = -30.035251;
        double longitudePto = -51.226525;

        latitudePto = Math.toRadians(latitudePto);
        longitudePto = Math.toRadians(longitudePto);
        longitude = Math.toRadians(longitude);
        latitude = Math.toRadians(latitude);


        dlon = longitudePto - longitude;
        dlat = latitudePto - latitude;

        a = Math.pow(Math.sin(dlat/2),2) + Math.cos(latitude) * Math.cos(latitudePto) * Math.pow(Math.sin(dlon/2),2);
        distancia = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return 6378140 * distancia; /* 6378140 is the radius of the Earth in meters*/

    }



    //METHODs CONTEXTS

}
