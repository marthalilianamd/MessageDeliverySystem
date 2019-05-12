package org.mlmunozd.app.MessageDeliverySystem.Services;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;
import org.mlmunozd.app.MessageDeliverySystem.Util.EndPoints;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyServerRequests {

    private static final String TAG = "MyServerRequest";

    public MyServerRequests(){}

    /*
     * Funcion que envia el token de registro del movil al servidor web para la autorizacion de envio de mensajes
     * @param Activity
     * @param String
     */
    public void enviarRegistroMovilAlServidor(final Activity activity, String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, EndPoints.URL_REGISTER_DEVICE + email + ".json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(activity.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());
                        Toast.makeText(activity.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            // Mapeo de los pares clave-valor que se enviarán al sistema web externo
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.e(TAG, SessionManager.getInstance(activity.getApplicationContext()).getTokenMovil());
                params.put("fcm_registro", SessionManager.getInstance(activity.getApplicationContext()).getTokenMovil());
                return params;
            }
        };
        MyVolleyRequest.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
    }



    /*
    * Funcion que solicita datos de usuario al servidor web para su autorizacion de registro
    * @param Activity
    * @param String
    */

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