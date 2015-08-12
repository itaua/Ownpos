package itauamachado.ownpos.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import de.greenrobot.event.EventBus;
import itauamachado.ownpos.LocationAPI.AddressLocationActivity;
import itauamachado.ownpos.domain.MessageEB;


public class LocationIntentService extends IntentService {


    public LocationIntentService() {
        super("worker_thread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Location location = intent.getParcelableExtra(AddressLocationActivity.LOCATION);
        int type = intent.getIntExtra(AddressLocationActivity.TYPE, 1);
        String address = intent.getStringExtra(AddressLocationActivity.ADDRESS);

        List<Address> list = new ArrayList<>();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String error = "";
        String resultAddress = "";


        try {
            if(type == 2 || address == null) {
                list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            }
            else{
                list = geocoder.getFromLocationName(address, 1);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            error = "Network problem";
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            error = "Illegal arguments";
        }


        if(list != null && list.size() > 0){
            Address a = list.get(0);


            if(type == 2 || address == null){
                for(int i = 0, tam = a.getMaxAddressLineIndex(); i < tam; i++){
                    resultAddress += a.getAddressLine(i);
                    resultAddress += i < tam - 1 ? ", " : "";
                }
            }
            else{
                resultAddress += a.getLatitude()+"\n";
                resultAddress += a.getLongitude();
            }
        }
        else{
            resultAddress = error;
        }


        MessageEB m = new MessageEB();
        m.setResultMessage(resultAddress);

        EventBus.getDefault().post(m);
    }
}
