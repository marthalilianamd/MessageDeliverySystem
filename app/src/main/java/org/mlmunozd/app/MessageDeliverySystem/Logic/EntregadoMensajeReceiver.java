package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class EntregadoMensajeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch(getResultCode())
        {
            case Activity.RESULT_OK:
                Toast.makeText(context, "SMS enviado exitosamente!", Toast.LENGTH_LONG).show();
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(context, "SMS no entregado", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
