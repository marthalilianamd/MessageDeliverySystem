package org.mlmunozd.app.MessageDeliverySystem.Service;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;
import org.mlmunozd.app.MessageDeliverySystem.MainActivity;
import org.mlmunozd.app.MessageDeliverySystem.Models.User;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {}

    private static final String TAG = "MyFirebaseMsgService";
    public User usermodel;
    public String token ="";

    //Method when the message is received
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Datos del mensaje con carga útil: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotificationtoMovil(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

            /*if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }*/

        }

       // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Cuerpo de notificación de mensajes: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotificationtoMovil(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            //if there is no image
            if(imageUrl.equals("null")){
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
        //sendRegistrationToServer(token);
    }

    public void registrarMovil(final Activity activity, final String email){
        //ProgressDialog progressDialog = new ProgressDialog(activity);
        //progressDialog.setMessage("Procesando registro del movil...");
        //progressDialog.show();

        Log.e(TAG, SessionManager.getInstance(activity.getApplicationContext()).getTokenMovil() + " JAJAJAJAJ ENTREE");
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(activity,
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        //Log.e("Registro Token ",newToken);
                        usermodel.getInstance().setToken_movil(newToken);
                        SessionManager.getInstance(activity.getApplicationContext()).saveTokenMovil(newToken);
                        Toast.makeText(activity.getApplicationContext(), "Token de registro generado : " + newToken, Toast.LENGTH_LONG).show();
                        enviarRegistroMovilAlServidor(activity, email);
                    }
                });
        } catch (Exception e) {
            Log.e(TAG, "Registro movil no fue posible", e);
            Toast.makeText(activity.getApplicationContext(), "Registro movil no fue posible ", Toast.LENGTH_LONG).show();
        }
        //progressDialog.dismiss();
    }

    public void enviarRegistroMovilAlServidor(final Activity activity,String email ){
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
                params.put("fcm_registro_id", SessionManager.getInstance(activity.getApplicationContext()).getTokenMovil());
                return params;
            }
        };
        FcmVolley.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public String obtenerTokenDelServidor(final Activity activity,String email ){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_GET_TOKEN_REGISTER + email + ".json",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        token = obj.getString("token");
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
            });
        FcmVolley.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);
        return token;
    }
}