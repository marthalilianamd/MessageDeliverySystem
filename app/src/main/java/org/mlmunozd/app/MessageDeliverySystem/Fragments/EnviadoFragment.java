package org.mlmunozd.app.MessageDeliverySystem.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mlmunozd.app.MessageDeliverySystem.Models.Enviado;
import org.mlmunozd.app.MessageDeliverySystem.Models.User;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;
import org.mlmunozd.app.MessageDeliverySystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnviadoFragment extends Fragment implements OnItemClickListenerEnviado {

    View viewFragEnviados;
    RecyclerView recyclerview;
    private EnviadoAdapter EnviadoAdapter;
    List<Enviado> listEnviados;
    public static final String SESSION_DATA="";

    public EnviadoFragment() {
        // Required empty public constructors
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        viewFragEnviados= inflater.inflate(R.layout.fragment_enviado, container, false);
        return viewFragEnviados;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.recycleviewenviados);
        configAdpater();
        configRecyclerView();
        consultEnviado();
    }

    private void configAdpater() {
        EnviadoAdapter = new EnviadoAdapter(new ArrayList<Enviado>(),this);
    }

    //podemos configurarlo como un listado o un grid, en este caso un linearLayout
    private void configRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(EnviadoAdapter);
    }

    private void consultEnviado() {
        HashMap<String, String> userSession = SessionManager.getInstance(getContext()).getUserDetails();
        String email= userSession.get(SessionManager.KEY_EMAIL);
        User user = User.getInstance().getUser(email);
        listEnviados = user.getEnviados();
         for (int i=0; i< listEnviados.size(); i++){
             EnviadoAdapter.addEnviado(listEnviados.get(i));
         }
    }

    @Override
    public void onLongItemClick(Enviado Enviado) {

    }
    @Override
    public void OnItemClick(Enviado Enviado) {

    }

    @Override
    public void onResume() {
        super.onResume();
        listEnviados.clear();
        configAdpater();
        configRecyclerView();
        consultEnviado();
    }

}
