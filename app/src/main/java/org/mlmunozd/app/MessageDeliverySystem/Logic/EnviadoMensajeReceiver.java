package org.mlmunozd.app.MessageDeliverySystem.Logic;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.telephony.SmsManager;

public class EnviadoMensajeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String estadoenviomensaje = "No enviado";
        MyServerRequests myServerRequests = new MyServerRequests();
        switch(getResultCode())
        {
            case Activity.RESULT_OK:
                Toast.makeText(context, "SMS ENVIADO!", Toast.LENGTH_LONG).show();
                estadoenviomensaje ="Enviado";
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(context, "SMS no enviado. El servicio SMS no está disponible", Toast.LENGTH_LONG).show();
                estadoenviomensaje ="No enviado";
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Toast.makeText(context, "SMS no enviado. Protocolo PDU no proporcionado", Toast.LENGTH_LONG).show();
                estadoenviomensaje ="No enviado";
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Toast.makeText(context, "MS no enviado. No se encontró servicio de mensajería en tu móvil", Toast.LENGTH_LONG).show();
                estadoenviomensaje ="No enviado";;
                break;
            case SmsManager.RESULT_ERROR_LIMIT_EXCEEDED:
                Toast.makeText(context, "SMS no enviado. Falló porque llegamos al límite de la cola de envío", Toast.LENGTH_LONG).show();
                estadoenviomensaje ="No enviado";
                break;
            case SmsManager.RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED:
                Toast.makeText(context, "SMS no enviado. Falló porque el usuario ha negado que esta aplicación envíe códigos cortos de calidad superior.", Toast.LENGTH_LONG).show();
                estadoenviomensaje ="No enviado";
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Toast.makeText(context, "SMS no enviado. Error genérico. Respuesta del proveedor de servicios", Toast.LENGTH_LONG).show();
                estadoenviomensaje ="No enviado";
                break;
            case SmsManager.RESULT_ERROR_SHORT_CODE_NOT_ALLOWED:
                Toast.makeText(context, "SMS no enviado. Falló porque el usuario negó el envío de este código corto.", Toast.LENGTH_LONG).show();
                estadoenviomensaje ="No enviado";
                break;
        }
        myServerRequests.enviarRegistroEstadoMensaje(context, estadoenviomensaje);

    }
}
