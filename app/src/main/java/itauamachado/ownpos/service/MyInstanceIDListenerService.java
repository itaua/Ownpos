package itauamachado.ownpos.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.iid.InstanceIDListenerService;

import itauamachado.ownpos.extras.Util;


public class MyInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = "LOG";

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        Util.savePrefKeyValue(this, Util.PREF_KEY_ID, "");
        Intent it = new Intent(this, RegistrationIntentService.class);
        startService(it);
    }
}
