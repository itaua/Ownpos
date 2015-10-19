package itauamachado.ownpos.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.indooratlas.internal.core.WifiReceiver;


import java.util.List;

import de.greenrobot.event.EventBus;
import itauamachado.ownpos.MainActivity;
import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.MessageEB;
import itauamachado.ownpos.domain.NavigationWiFi;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.extras.Util;


public class BReceiver_Location extends BroadcastReceiver implements
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		LocationListener{

	private GoogleApiClient mGoogleApiClient;
	private MessageEB mMessageEB;
	private LocationRequest mLocationRequest;



	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {

		mContext = context;

		Util.log("onReceive LOCATION - BUSCANDO");

		callConnection();

	}


	//METHODOS LOCATION
	//Methods API Location
	private synchronized void callConnection(){
		mGoogleApiClient = new GoogleApiClient.Builder(mContext)
				.addOnConnectionFailedListener(this)
				.addConnectionCallbacks(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}

	private void initLocationRequest(){
		//Log.i(TAG, "initLocationRequest()");
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(15000);
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

		Util.log("onLocationChanged()");

		mMessageEB = new MessageEB();
		mMessageEB.setmTag(MessageEB.TAG_LOCATION);
		mMessageEB.setLocation(location);
		Double d = (getDistancia(location,null));
		mMessageEB.setDistancia(d);
		EventBus.getDefault().post(mMessageEB);
		new SQLiteConn(mContext).savePosition(location);

		if(d < 500){
			Util.getWiFi(mContext);
		}
		stopLocationUpdate();
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



}
