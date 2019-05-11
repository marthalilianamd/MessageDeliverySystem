package org.mlmunozd.app.MessageDeliverySystem.Services;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONException;
import org.json.JSONObject;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyServerRequests {

    private static final String TAG = "MyServerRequest";

    public MyServerRequests(){}

    public void obtenerDatosUsuarioDelServidor(final Activity activity, String email){
        final RequestFuture<JSONObject> futureRequest =  RequestFuture.newFuture();
        Log.d(TAG, "ENTROOOOO A SOLICITAR DATOS ");
        RequestQueue mQueue = MyVolleyRequest.getInstance(activity.getApplicationContext()).getRequestQueue();
        JSONObject response = null;
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                EndPoints.URL_GET_DATA_REGISTER + email + ".json", new JSONObject(), futureRequest, futureRequest);
        Log.d(TAG, "HIZOOOOO  LA SOLICITUD ");
        // request queue
        mQueue.add(jsonRequest);
        Log.d(TAG, "ENCOLOOOOOOOOOOOOOOOOOOO");
        try{
            response = futureRequest.get(1, TimeUnit.SECONDS);//
            Log.d(TAG, "JSSSSSONNNNNNNNNN ESEEEE "+response);
        }catch (InterruptedException e){
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        if(response != null) {
            try {
                SessionManager.getInstance(activity.getApplicationContext()).saveEmail(response.getString("email"));
                SessionManager.getInstance(activity.getApplicationContext()).saveTokenMovil(response.getString("token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //return response;
        }else {
            SessionManager.getInstance(activity.getApplicationContext()).saveEmail("");
            SessionManager.getInstance(activity.getApplicationContext()).saveTokenMovil("");
        }
    }
}