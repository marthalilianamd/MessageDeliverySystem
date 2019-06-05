package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.app.Activity;
import android.content.Context;
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
    public void enviarRegistroMovilAlServidor(final Context context, String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, EndPoints.URL_REGISTER_DEVICE + email + ".json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Respuesta en JSON: " + error.getMessage());
                        Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            // Mapeo de los pares clave-valor que se enviarán al sistema web externo
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.e(TAG, SessionManager.getInstance(context.getApplicationContext()).getTokenMovil());
                params.put("fcm_registro", SessionManager.getInstance(context.getApplicationContext()).getTokenMovil());
                return params;
            }
        };
        MyVolleyRequest.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }


    /*
    * Funcion que solicita datos de usuario al servidor web para su autorizacion de registro
    * @param Activity
    * @param String
    */

    public JSONObject obtenerDatosUsuarioDelServidor(final Context context, String email, String contrasena) {
        final RequestFuture<JSONObject> futureRequest = RequestFuture.newFuture();
        Log.d(TAG, "ENTROOOOO A SOLICITAR DATOS ");
        RequestQueue mQueue = MyVolleyRequest.getInstance(context.getApplicationContext()).getRequestQueue();

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                EndPoints.URL_GET_DATA_REGISTER + email + "-" + contrasena + ".json", new JSONObject(), futureRequest, futureRequest);
        Log.d(TAG, "HIZOOOOO  LA SOLICITUD : " + jsonRequest);

        JSONObject response = null;
        //jsonRequest.setRetryPolicy(new DefaultRetryPolicy(3 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // request queue
        mQueue.add(jsonRequest);
        //futureRequest.setRequest(mQueue.add(jsonRequest));

        Log.d(TAG, "ENCOLOOOOOOOOOOOOOOOOOOO");
        try {
            Log.d(TAG, "TRAYENDO LOS DATOS");
            //while (response == null) {
                try {
                    //Log.d(TAG, "Response-->" + response);
                    return futureRequest.get(1, TimeUnit.SECONDS); // Bloqueo del hilo, esperando respuesta, tiempo de espera después de xx segundos
                    //return futureRequest.get(5, TimeUnit.SECONDS);
                    //Log.d(TAG, "Response after-->" + response);
                    //return response;
                } catch (InterruptedException e) {
                    //Recibe la señal de interrupción, pero aún no se ha tenido respuesta
                    // Restaura el estado interrumpido del hilo para usarlo más arriba en la pila de llamadas
                    Thread.currentThread().interrupt();
                    // Continúe esperando la respuesta
                }
           // }
            //response = futureRequest.get(timeout, TimeUnit.SECONDS);
            //response = (JSONObject)futureRequest.get(1, TimeUnit.SECONDS);
            Log.d(TAG, "RESPONSE---->>" + response.toString());

            Log.d(TAG, "JSSSSSONNNNNNNNNN ESEEEE " + response.toString());
        } catch (ExecutionException e) {
            Log.e(TAG, " - ExecutionException - " + e);
            e.printStackTrace();
        } catch (TimeoutException e) {
            Log.e(TAG, " - TimeoutException - " + e);
            e.printStackTrace();
        }
        Log.d(TAG, " END");
        return null;
    }

}