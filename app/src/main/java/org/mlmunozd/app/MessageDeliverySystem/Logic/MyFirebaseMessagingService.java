package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.mlmunozd.app.MessageDeliverySystem.Fragments.MensajesFragment;
import org.mlmunozd.app.MessageDeliverySystem.Models.User;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;
import org.mlmunozd.app.MessageDeliverySystem.R;
import org.mlmunozd.app.MessageDeliverySystem.Util.NotificationUtils;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {}

    private static final String TAG = "MyFirebaseMsgService";
    public User usermodel;
    public static final int NOTIFICATION_ID = 1;

    /**Controlador que recibe los mensajes*/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getData() !=null){
            sendNotification(remoteMessage);
        }

        /*
         *//**Comprueba si el mensaje contiene una carga útil de datos(data payload.)*//*
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Datos del mensaje con carga útil: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            *//*if (*//**//* Compruebe si los datos deben ser procesados ​​por un trabajo de larga ejecución*//**//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }*//*
        }*/

        /**Compruebe si el mensaje contiene una carga útil de notificación (notification payload.) y de datos(data payload.)*/
        /*if (remoteMessage.getNotification() != null && remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Cuerpo de notificación de mensajes: " + remoteMessage.getNotification().getBody());
            String titulo = remoteMessage.getNotification().getTitle();
            String mensaje = remoteMessage.getNotification().getBody();
            String phone = remoteMessage.getData().get("phone");
            handleNotification(titulo, mensaje,phone);
        }*/
    }

    private void sendNotification(RemoteMessage remoteMessage){
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("body");
        String phone = remoteMessage.getData().get("phone");
        Log.d(TAG, "NOTIFICACION RECIBIDA");
        Log.d(TAG, "Título: " + title);
        Log.d(TAG, "Mensaje: " + message);
        Log.d(TAG, "Movil: " + phone);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "CHANNEL1";


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            Log.d(TAG, "ENTRAAAAAAAAAAAAAAAAAAAA A COMPARARRARRARAR ");
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"MDS Notification",
                    NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("MSD Channel de la App Message Delivery System");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent notificationintent = new Intent(MensajesFragment.ACTION_NOTIFY_NEW_MESSAGE);
        notificationintent.putExtra("title", title);
        notificationintent.putExtra("message", message);
        notificationintent.putExtra("phone", phone);
        notificationintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationintent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        final int icon = R.mipmap.applogo;
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(inboxStyle)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.mipmap.applogo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), icon))
                .setContentText(message)
                .setSound(soundUri)
                .setContentInfo(phone);
        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());

        Log.d(TAG, "ENTROOOOOOOOOOOOOOOOOOOOOO A ENVIAR INTENNTTTTT");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(notificationintent);

    }


    /**Cuando se envía un mensaje de tipo de notificación,
     * firebase muestra automáticamente la notificación
     * cuando la aplicación está en segundo plano.
     * Si la aplicación está en primer plano,
     * el método handleNotification maneja el mensaje de notificación.
     **/
    private void handleNotification(String title, String message, String phone) {
      /*  Log.d(TAG, "NOTIFICACION RECIBIDA");
        Log.d(TAG, "Título: " + title);
        Log.d(TAG, "Mensaje: " + message);
        Log.d(TAG, "Movil: " + phone);

        *//**La aplicación está en primer plano, emite el mensaje*//*
        Intent notificationintent = new Intent(MensajesFragment.ACTION_NOTIFY_NEW_MESSAGE);
        notificationintent.putExtra("title", title);
        notificationintent.putExtra("message", message);
        notificationintent.putExtra("phone", phone);

        *//**se muestra la notificación push*//*
        showNotificationMessage(getApplicationContext(),title,message,phone, notificationintent);
*/
        /**LocalBroadcastManager:
         * se utiliza para transmitir el mensaje a todas las actividades registradas para el receptor de difusión
         * en otras palabras, sirve para reportar la nueva aparición de una notificación a la UI. permite enviar un
         * Intent hacia otros componentes Android para procesar llamadas. La idea es enviar en los extras del intent
         * los datos que conforman la notificación
         */
        //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(notificationintent);

    }

    /** Este método mostrará la notificación
     *  Maneja la carga útil independientemente del estado de la aplicación (primer plano / fondo).
     *  Estamos pasando el objeto JSON que se recibe por el canal Firebase Cloud Messaging desde la web
     */
    /*private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "Datos JSON RECIBIDOS" + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");
            //parsing json data
            String phone = data.getString("phone");

            Log.e(TAG, "Movil: " + phone);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                *//**La aplicación está en primer plano, emite el mensaje*//*
                Intent notification = new Intent(MensajesFragment.ACTION_NOTIFY_NEW_MESSAGE);
                notification.putExtra("phone", phone);
                notification.putExtra("message", message);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(notification);

            } else {
                *//**La aplicación está en segundo plano, muestra la notificación en la bandeja de notificaciones*//*

                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("message", message);
                showNotificationMessage(getApplicationContext(),title,message, phone, resultIntent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }*/


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
        // super.onNewToken(token);
        //Log.e("TOKEN",token);
    }

    public void onTokenRefresh(String email, Activity activity, String newToken){
        usermodel = User.getInstance();
        usermodel.setToken_movil(newToken);
        SessionManager.getInstance(activity.getApplicationContext()).saveTokenMovil(newToken);
        MyServerRequests myServerRequests = new MyServerRequests();
        myServerRequests.enviarRegistroMovilAlServidor(activity, email);
    }

    /**
     * Funcion que genera el token de registro del movil desde Firebase
     */
    public void registrarMovil(final Activity activity, final String email) {
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(activity,
                    new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String newToken = instanceIdResult.getToken();
                            //Log.e("Registro Token ",newToken);
                            usermodel = User.getInstance();
                            usermodel.setToken_movil(newToken);
                            SessionManager.getInstance(activity.getApplicationContext()).saveTokenMovil(newToken);
                            Toast.makeText(activity.getApplicationContext(), "Token móvil: " + newToken, Toast.LENGTH_LONG).show();
                            MyServerRequests myServerRequests = new MyServerRequests();
                            myServerRequests.enviarRegistroMovilAlServidor(activity, email);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Registro móvil no fue posible", e);
            Toast.makeText(activity.getApplicationContext(), "Registro móvil no fue posible ", Toast.LENGTH_LONG).show();
        }
    }

}