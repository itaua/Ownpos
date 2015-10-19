package itauamachado.ownpos.network;

import android.content.Context;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.util.HashMap;
import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.extras.Util;

public class NetworkConnection {
    private static NetworkConnection instance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    public NetworkConnection(Context c){
        mContext = c;
        mRequestQueue = getRequestQueue();
    }


    public static NetworkConnection getInstance( Context c ){
        if( instance == null ){
            instance = new NetworkConnection( c.getApplicationContext() );
        }
        return( instance );
    }


    public RequestQueue getRequestQueue(){
        if( mRequestQueue == null ){
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return(mRequestQueue);
    }


    public <T> void addRequestQueue( Request<T> request ){
        getRequestQueue().add(request);
    }


    public void execute( final Transaction transaction, final String tag ){
        WrapObjToNetwork obj = transaction.doBefore();
        Gson gson = new Gson();

        if( obj == null ){
            return;
        }

        Util.log(gson.toString());
        HashMap<String, String> params = new HashMap<>();
        params.put("jsonObject", gson.toJson(obj));


        if(obj.getMethod().equalsIgnoreCase("get-biblioteca")){
            Util.log("LastId: "+obj.getLastId());
        }

        Util.log(obj.getMethod());
        CustomRequest request = new CustomRequest(Request.Method.POST,
            Util.WEBSERVER,
            params,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Util.log("onResponse(): " + response);
                    transaction.doAfter(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Util.log("onErrorResponse(): " + error.getMessage());
                    transaction.doAfter(null);
                }
            });

        request.setTag(tag);
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addRequestQueue(request);
    }
}
