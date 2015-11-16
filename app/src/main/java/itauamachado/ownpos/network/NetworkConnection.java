package itauamachado.ownpos.network;

import android.content.ContentValues;
import android.content.Context;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.service.BReceiver_Context;

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

    public void execute( final Transaction transaction, final String tag) {
        WrapObjToNetwork obj = transaction.doBefore();
        Gson gson = new Gson();
        if (obj == null) {return;}
        HashMap<String, String> params = new HashMap<>();
        params.put("jsonObject", gson.toJson(obj));


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

    public void execute( final Transaction transaction, final String tag, final String valor ) {

        String url = "";
        final HashMap<String, String> params = new HashMap<>();

        if(tag.equalsIgnoreCase(BReceiver_Context.ACERVO)){
            url = Util.URL_ACERVO_DISPONIVEL;
            params.put("_senac", valor);
            params.put("notification", BReceiver_Context.ACERVO);


        }else
        if(tag.equalsIgnoreCase(BReceiver_Context.AULA)){
            url = Util.URL_ACERVO_DISPONIVEL;
            params.put("_senac", valor);
            params.put("notification", BReceiver_Context.AULA);


        }else
        if(tag.equalsIgnoreCase(BReceiver_Context.COMPROMISSO)){
            url = Util.URL_ACERVO_DISPONIVEL;
            params.put("_senac", valor);
            params.put("notification", BReceiver_Context.COMPROMISSO);

        }

            CustomRequest request = new CustomRequest(Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        JSONObject jsonObject = new JSONObject(params);
                        response.put(jsonObject);

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
