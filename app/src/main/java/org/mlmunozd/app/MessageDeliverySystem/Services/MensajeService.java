package org.mlmunozd.app.MessageDeliverySystem.Services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.mlmunozd.app.MessageDeliverySystem.Logic.MensajeReceiver;


public class MensajeService extends Service {

    public static final String ACTION_NOTIFY_NEW_MESSAGE = "NOTIFY_NEW_MESSAGE";
    private static final String TAG = "SERVICIO MENSAJES";
    public MensajeReceiver mensajeReceiver;

    public MensajeService() { }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SERVICIO SMS CREADO");
        mensajeReceiver = new MensajeReceiver();
        registerReceiver(mensajeReceiver,new IntentFilter(ACTION_NOTIFY_NEW_MESSAGE));
        Toast.makeText(getApplicationContext(),"Servicio SMS activo!", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "STARTCOMMAND");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mensajeReceiver,
                new IntentFilter(ACTION_NOTIFY_NEW_MESSAGE));
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SERVICIO SMS CANCELADO");
        Toast.makeText(getApplicationContext(),"Servicio SMS cancelado!", Toast.LENGTH_LONG).show();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mensajeReceiver);
    }
}
