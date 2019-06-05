package org.mlmunozd.app.MessageDeliverySystem.Interfaces;

import org.mlmunozd.app.MessageDeliverySystem.Models.Mensaje;

import java.util.ArrayList;

//Interfaz para la representación de la vista y el presentador
public interface MensajeContract {

    interface View extends BaseView<Presenter>{
        void showMensajes(ArrayList<Mensaje> message);
        void showEmptyState(boolean empty);
        void popPushMensajes(Mensaje message);
    }

    interface Presenter extends BasePresenter{
        void loadMensajes();
        void savePushMensajes(String title, String description, String movil);
        int getCantidadMensajesStore();
        void deleteMensajesStore();
    }
}
