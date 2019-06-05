package org.mlmunozd.app.MessageDeliverySystem.Fragments;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import org.mlmunozd.app.MessageDeliverySystem.Interfaces.MensajeContract;
import org.mlmunozd.app.MessageDeliverySystem.Util.MensajesStore;
import org.mlmunozd.app.MessageDeliverySystem.Models.Mensaje;

import java.util.ArrayList;

/**Clase intermediaria entre la VISTA y el MODELO. Carga los mensajes y los muestra a la vista
 * Presentador para cargar la lista de las notificaciones o mensajes
 */
public class MensajesPresenter implements MensajeContract.Presenter {

    private static final String TAG = "MnensajePresenter";
    private final MensajeContract.View mMensajeView;
    private final FirebaseMessaging mFCMInteractor;


    public MensajesPresenter(MensajeContract.View mensajeView, FirebaseMessaging FCMInteractor) {
        mMensajeView = mensajeView;
        mFCMInteractor = FCMInteractor;
        mensajeView.setPresenter(this);
    }

    @Override
    public void start() {
        loadMensajes();
    }

    @Override
    public void loadMensajes() {
        MensajesStore.getInstance().getMensajesStore(
                new MensajesStore.LoadCallback() {
                    @Override
                    public void onLoaded(ArrayList<Mensaje> notifications) {
                        if (notifications.size() > 0) {
                            Log.e(TAG, "CANTIDAD DE MENSAJES EN EL STORE: "+ notifications.size());
                            mMensajeView.showEmptyState(false);
                            mMensajeView.showMensajes(notifications);
                        } else {
                            mMensajeView.showEmptyState(true);
                        }
                    }
                }
        );
    }

    @Override
    public void savePushMensajes(String title, String description, String phone) {
        Mensaje pushMessage = new Mensaje();
        pushMessage.setTitulo(title);
        pushMessage.setContenido(description);
        pushMessage.setMovil(phone);

        MensajesStore.getInstance().saveMensajesStore(pushMessage);
        mMensajeView.showEmptyState(false);

        //Si el almacen de mensajes tiene 5 0 más guardados, se borran para liberar memoria
        /*if(getCantidadMensajesStore()>=5){
            deleteMensajesStore();
        }*/
        mMensajeView.popPushMensajes(pushMessage);
    }

    @Override
    public int getCantidadMensajesStore(){
        return MensajesStore.getInstance().getCantidadMensajesStore();
    }

    public void deleteMensajesStore(){
        MensajesStore.getInstance().removeMensajesStore();
    }

}
