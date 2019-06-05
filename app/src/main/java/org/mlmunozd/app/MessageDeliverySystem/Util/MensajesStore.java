package org.mlmunozd.app.MessageDeliverySystem.Util;
import android.support.v4.util.ArrayMap;

import org.mlmunozd.app.MessageDeliverySystem.Models.Mensaje;

import java.util.ArrayList;

public class MensajesStore {

    private static ArrayMap<String, Mensaje> LOCAL_MESSAGES_STORE = new ArrayMap<>();
    private static MensajesStore INSTANCE;

    private MensajesStore() {
    }

    public static MensajesStore getInstance() {
        if (INSTANCE == null) {
            return new MensajesStore();
        } else {
            return INSTANCE;
        }
    }

    public void getMensajesStore(LoadCallback callback) {
        callback.onLoaded(new ArrayList<>(LOCAL_MESSAGES_STORE.values()));
    }

    public void saveMensajesStore(Mensaje mensaje) {
        LOCAL_MESSAGES_STORE.put(mensaje.getId(), mensaje);
    }

    public interface LoadCallback {
        void onLoaded(ArrayList<Mensaje> messages);
    }

    public int getCantidadMensajesStore() {
        return LOCAL_MESSAGES_STORE.size();
    }

    public void removeMensajesStore(){
        LOCAL_MESSAGES_STORE.removeAll(new ArrayList<>(LOCAL_MESSAGES_STORE.values()));
    }
}
