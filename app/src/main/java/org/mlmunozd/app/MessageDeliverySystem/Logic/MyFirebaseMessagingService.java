package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.service.notification.StatusBarNotification;
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

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {}

    private static final String TAG = "MyFirebaseMsgService";
    public User usermodel;

    /**Controlador que recibe los mensajes*/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getData() != null){
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage){
        if (remoteMessage == null)
            return;
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("body");
        String phone = remoteMessage.getData().get("phone");
        Log.d(TAG, "NOTIFICACION RECIBIDA");

        Intent notificationintent = new Intent();
        notificationintent.setAction(MensajesFragment.ACTION_NOTIFY_NEW_MESSAGE);
        notificationintent.putExtra("title", title);
        notificationintent.putExtra("message", message);
        notificationintent.putExtra("phone", phone);
        notificationintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //CREACION DE LA NOTIFICACION
        PendingIntent contentPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationintent, PendingIntent.FLAG_UPDATE_CURRENT);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(notificationintent);

        String NOTIFICATION_CHANNEL_ID = "CHANNEL1";
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        final int icon = R.mipmap.applogo;
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(inboxStyle)
                .setSmallIcon(R.mipmap.applogo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), icon))
                .setContentText(message)
                .setSound(soundUri)
                .setContentInfo(phone)
                .setContentIntent(contentPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
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