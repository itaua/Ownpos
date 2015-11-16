package itauamachado.ownpos.service;


import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import itauamachado.ownpos.domain.objGcmNotification;
import itauamachado.ownpos.extras.Util;


public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Util.log("onMessageReceived --> "+data);
        objGcmNotification notification = new objGcmNotification();

    }

    private void setNotificationApp( final Bundle data ){

    }


    private void setNotificationApp_OLD( final Bundle data ){

    }

}
