package org.mlmunozd.app.MessageDeliverySystem.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
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
import org.mlmunozd.app.MessageDeliverySystem.Util.NotificationUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {
    }

    private static final String TAG = "MyFirebaseMsgService";
    public User usermodel;
    private NotificationUtils notificationUtils;


    //Método cuando se recibe el mensaje
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null) {
            return;
        }

        // Compruebe si el mensaje contiene una carga útil de datos(data payload.)
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Datos del mensaje con carga útil: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

            /*if (*//* Compruebe si los datos deben ser procesados ​​por un trabajo de larga ejecución*//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }*/

        }

        // Compruebe si el mensaje contiene una carga útil de notificación (notification payload.)
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Cuerpo de notificación de mensajes: " + remoteMessage.getNotification().getBody());
            String titulo = remoteMessage.getNotification().getTitle();
            String texto = remoteMessage.getNotification().getBody();
            handleNotification(titulo, texto);
        }

        // Además, si pretende generar sus propias notificaciones como resultado de una FCM recibida
        // mensaje, aquí es donde se debe iniciar. Ver el método de envío de notificación a continuación.
    }

    //Cuando se envía un mensaje de tipo de notificación,
    // firebase muestra automáticamente la notificación
    // cuando la aplicación está en segundo plano.
    //
    // Si la aplicación está en primer plano,
    // el método handleNotification maneja el mensaje de notificación.

    private void handleNotification(String title, String message) {
        Log.d(TAG, "NOTIFICACION RECIBIDA");
        Log.d(TAG, "Título: " + title);
        Log.d(TAG, "Texto: " + message);
        // La aplicación está en primer plano, emite el mensaje
        Intent notificationintent = new Intent();
        ///notification.putExtra("title", title);
        //notification.putExtra("message", message);

        showNotificationMessage(getApplicationContext(),title,message, notificationintent);
        //LocalBroadcastManager:
        // se utiliza para transmitir el mensaje a todas las actividades registradas para el receptor de difusión.
        //LocalBroadcastManager.getInstance(this).sendBroadcast(notification);

    }

    // Este método mostrará la notificación.
    // Maneja la carga útil independientemente del estado de la aplicación (primer plano / fondo).
    // Estamos pasando el objeto JSON que se recibe de
    // Firebase Cloud Messaging
    private void handleDataMessage(JSONObject json) {
        //Opcionalmente se muestra el json en el registro.
        Log.e(TAG, "Datos JSON RECIBIDOS" + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json datos
            String title = data.getString("titulo");
            String message = data.getString("mensaje");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // La aplicación está en primer plano, emite el mensaje
                Intent notification = new Intent();
                notification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(notification);

            } else {
                // La aplicación está en segundo plano, muestra la notificación en la bandeja de notificaciones.
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Muestra la notificación
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }

    /**
     * Se llama si el token de InstanceID se actualiza. Esto puede ocurrir si la seguridad de
     * El token anterior había sido comprometido. Tenga en cuenta que esto se llama cuando el token de InstanceID
     * se genera inicialmente por lo que aquí es donde se recuperaría el token
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

    public void registrarMovil(final Activity activity, final String email) {
        Log.e(TAG, SessionManager.getInstance(activity.getApplicationContext()).getTokenMovil());
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(activity,
                    new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String newToken = instanceIdResult.getToken();
                            //Log.e("Registro Token ",newToken);
                            usermodel= User.getInstance();
                            usermodel.setToken_movil(newToken);
                            SessionManager.getInstance(activity.getApplicationContext()).saveTokenMovil(newToken);
                            Toast.makeText(activity.getApplicationContext(), "Token de registro generado : " + newToken, Toast.LENGTH_LONG).show();
                            MyServerRequests myServerRequests = new MyServerRequests();
                            myServerRequests.enviarRegistroMovilAlServidor(activity, email);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Registro movil no fue posible", e);
            Toast.makeText(activity.getApplicationContext(), "Registro movil no fue posible ", Toast.LENGTH_LONG).show();
        }
    }

}