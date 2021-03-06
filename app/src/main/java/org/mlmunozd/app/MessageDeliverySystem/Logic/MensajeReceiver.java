package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;


public class MensajeReceiver extends BroadcastReceiver {

    public static final String ACTION_NOTIFY_NEW_MESSAGE = "NOTIFY_NEW_MESSAGE";
    private static final String TAG = "BROADCASTRECEIVER";
    String ACTION_SENDING_SMS_STATUS ="SEND";
    String ACTION_DELIVERY_SMS_STATUS = "DELIVERED";
    PendingIntent send, delivered;
    public EntregadoMensajeReceiver entregadoMensajeReceiver;
    public EnviadoMensajeReceiver enviadoMensajeReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_NOTIFY_NEW_MESSAGE)){

            entregadoMensajeReceiver = new EntregadoMensajeReceiver();
            context.registerReceiver(entregadoMensajeReceiver,new IntentFilter(ACTION_DELIVERY_SMS_STATUS));

            enviadoMensajeReceiver = new EnviadoMensajeReceiver();
            context.registerReceiver(enviadoMensajeReceiver,new IntentFilter(ACTION_SENDING_SMS_STATUS));

            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");
            String phoneNo = intent.getStringExtra("phone");

            //ENVIO DEL SMS
            send = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_SENDING_SMS_STATUS), 0);
            delivered = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_DELIVERY_SMS_STATUS), 0);

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNo, null, title + " \n " + message, send, delivered);
            Toast.makeText(context, "SMS Enviado!  \n " + title + " \n " + message + " \n Para el móvil " + phoneNo, Toast.LENGTH_LONG).show();
        }
    }
}
