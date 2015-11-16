package itauamachado.ownpos.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;

import java.io.IOException;

import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.domain.objUsuario;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.network.NetworkConnection;
import itauamachado.ownpos.network.Transaction;


public class RegistrationIntentService extends IntentService implements Transaction {
    public static final String OP = "Ownpos";
    public String mToken;
    private static final String METHOD = "set-gcm";
    private objUsuario mLogado;

    public RegistrationIntentService(){
        super(OP);
    }

    @Override
    protected void onHandleIntent( Intent intent) {

        mToken = Util.retrievePrefKeyValue(this, Util.PREF_KEY_ID);
        mLogado = new SQLiteConn(getApplicationContext()).getUserLogged();
        if(mToken.trim().length() != 0){
            synchronized (OP){
                InstanceID instanceID = InstanceID.getInstance(this);
                try {
                    mToken = instanceID.getToken(Util.SenderID,
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                            null);
                    Util.savePrefKeyValue(this, Util.PREF_KEY_ID, mToken);
                    callVolleyRequest();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void callVolleyRequest(){
        NetworkConnection.getInstance(this).execute(this, RegistrationIntentService.class.getName());
    }




    @Override
    public WrapObjToNetwork doBefore() {
        if( Util.verifyConnection(this) ){
            return( new WrapObjToNetwork(mLogado.getId_senac() , METHOD, mToken));
        }
        return null;
    }

    @Override
    public void doAfter(JSONArray jsonArray) {

    }
}
