package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class MensajeReceiver extends BroadcastReceiver {

    public static final String ACTION_NOTIFY_NEW_MESSAGE = "NOTIFY_NEW_MESSAGE";
    private static final String TAG = "BROADCASTRECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "ENTRRRRRROOOOOOOO");

        if(intent.getAction().equals(ACTION_NOTIFY_NEW_MESSAGE)){
            Log.d(TAG, "ACCCION RECIBIDA");
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");
            String phoneNo = intent.getStringExtra("phone");

            //ENVIO DEL SMS
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNo, null, title + " \n " + message, null, null);
            Toast.makeText(context, "SMS Enviado!  \n " + title + " \n " + message + " \n Para el móvil " + phoneNo, Toast.LENGTH_LONG).show();
        }
    }
}
