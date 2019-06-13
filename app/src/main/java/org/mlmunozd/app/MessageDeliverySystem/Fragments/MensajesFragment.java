package org.mlmunozd.app.MessageDeliverySystem.Fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.mlmunozd.app.MessageDeliverySystem.Interfaces.MensajeContract;
import org.mlmunozd.app.MessageDeliverySystem.Models.Mensaje;
import org.mlmunozd.app.MessageDeliverySystem.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MensajesFragment extends Fragment implements MensajeContract.View {

    public static final String ACTION_NOTIFY_NEW_MESSAGE = "NOTIFY_NEW_MESSAGE";
    private static final String TAG = "MENSAJEFRGAMENT";
    View viewFragMensajes;
    RecyclerView recyclerview;
    private MensajeAdapter mensajeAdapter;
    private LinearLayout mNoMessagesView;
    private MensajesPresenter mPresenter;
    public BroadcastReceiver mensajeBroadcastReceiver;

    public MensajesFragment() {
        // Required empty public constructors
    }

    public static MensajesFragment newInstance() {
        MensajesFragment fragment = new MensajesFragment();
        // Setup de Argumentos
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Todo esto que esta abajo debe ir e el evento onCreate del Servicio
        if (getArguments() != null) { }

        mensajeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Mostrar mensaje en lista ");

                if(intent.getAction().equals(ACTION_NOTIFY_NEW_MESSAGE)){
                    String title = intent.getStringExtra("title");
                    String message = intent.getStringExtra("message");
                    String phoneNo = intent.getStringExtra("phone");

                    mPresenter.savePushMensajes(title, message, phoneNo);
                    Toast.makeText(context, "Mensaje listado", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewFragMensajes != null) {
            ViewGroup parent = (ViewGroup) viewFragMensajes.getParent();
            if (parent != null){
                parent.removeView(viewFragMensajes);
            }
        }
        viewFragMensajes= inflater.inflate(R.layout.fragment_mensajes, container, false);
        mensajeAdapter = new MensajeAdapter();
        recyclerview =  viewFragMensajes.findViewById(R.id.recycleview);
        mNoMessagesView = viewFragMensajes.findViewById(R.id.noMessages);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(mensajeAdapter);
        return viewFragMensajes;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter = new MensajesPresenter(this, FirebaseMessaging.getInstance());
        mPresenter.start();
        Log.d(TAG, "LocalBroadcastManager ONRESUME");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(mensajeBroadcastReceiver,
                new IntentFilter(ACTION_NOTIFY_NEW_MESSAGE));
        //LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(mensajeBroadcastReceiver);
    }

    @Override
    public void onPause() {
        //El BroadcastReceiver (mMensajesReceiver) se detiene mientras la app está en pausa para que no reciba ninguna
        //Actualización de mensajes mientras este en este estado y asi evitar la duplicación
        super.onPause();
        Log.d(TAG, "LocalBroadcastManager ONPAUSA");
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(mensajeBroadcastReceiver);
    }

    @Override
    public void showMensajes(ArrayList<Mensaje> pushMensajes) {
        mensajeAdapter.replaceData(pushMensajes);
    }

    @Override
    public void showEmptyState(boolean empty) {
        recyclerview.setVisibility(empty ? View.GONE : View.VISIBLE);
        mNoMessagesView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void popPushMensajes(Mensaje message) {
        mensajeAdapter.addMensaje(message);
    }

    @Override
    public void setPresenter(MensajeContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = (MensajesPresenter) presenter;
        } else {
            throw new RuntimeException("El presenter de mensajes no puede ser null");
        }
    }

}
