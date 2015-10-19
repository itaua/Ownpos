package itauamachado.ownpos.receiver;


import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.parse.ParsePushBroadcastReceiver;

import de.greenrobot.event.EventBus;
import itauamachado.ownpos.R;
import itauamachado.ownpos.extras.Util;

public class CustomPushReceiver extends ParsePushBroadcastReceiver {
/*

    @Override
    protected int getSmallIconId(Context context, Intent intent) {
        return(R.mipmap.ic_notification);
        //return super.getSmallIconId(context, intent);
    }


    @Override
    protected void onPushReceive(Context context, Intent intent) {

        if( intent == null ){
            return;
        }

        Util.savePrefKeyValue(context.getApplicationContext(),
                Util.PREF_KEY_DATA,
                intent.getExtras().getString("com.parse.Data"));

        if( Util.isMyApplicationTaskOnTop( context ) ){
            try{
                Gson gson = new Gson();
                EventBus.getDefault().post( intent.getStringExtra("com.parse.Data"));
            }
            catch( Exception e ){
                Util.log("Error: " + e.getMessage());
            }
        }
        else{
            super.onPushReceive(context, intent);
        }
    }
    */
}
