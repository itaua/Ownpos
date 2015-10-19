package itauamachado.ownpos.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


public class CustomApplication extends Application {

/*
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, Util.APPLICATION_ID, Util.CLIENT_KEY);
                ParseInstallation.getCurrentInstallation().saveInBackground();

    }
*/

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
