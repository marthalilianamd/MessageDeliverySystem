package org.mlmunozd.app.MessageDeliverySystem.Fragments;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.mlmunozd.app.MessageDeliverySystem.Models.Enviado;
import org.mlmunozd.app.MessageDeliverySystem.Models.User;
import org.mlmunozd.app.MessageDeliverySystem.Persistence.SessionManager;
import org.mlmunozd.app.MessageDeliverySystem.Logic.Mensaje;
import org.mlmunozd.app.MessageDeliverySystem.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MensajeDetailsFragment extends Fragment {

    View viewFragDetailsMensajes;
    AppBarLayout appBarLayout;
    AppCompatImageView imgFoto;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    AppBarLayout appBar;
    TextInputEditText tvTituloMensaje;
    TextInputEditText tvContenidoMensaje;
    TextInputEditText tvFechaMensaje;
    NestedScrollView containerMain;
    Button btnEnviar;
    FloatingActionButton fab;

    private Mensaje mMensaje;

    public MensajeDetailsFragment() {
        // Required empty public constructors
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (viewFragDetailsMensajes != null) {
            ViewGroup parent = (ViewGroup) viewFragDetailsMensajes.getParent();
            if (parent != null){
                parent.removeView(viewFragDetailsMensajes);
            }
        }
        viewFragDetailsMensajes= inflater.inflate(R.layout.fragment_details_mensajes, container, false);
        return viewFragDetailsMensajes;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        containerMain = view.findViewById(R.id.containerMain);
        //fab = view.findViewById(R.id.fab);
        toolbar = view.findViewById(R.id.toolbar);
        appBar = view.findViewById(R.id.app_bar);
        appBarLayout = view.findViewById(R.id.app_bar);
        toolbarLayout = view.findViewById(R.id.toolbar_layout);
        imgFoto= view.findViewById(R.id.imgFoto);
        tvTituloMensaje = view.findViewById(R.id.tvTituloMensaje);

        tvContenidoMensaje= view.findViewById(R.id.tvContenidoMensaje);
        btnEnviar = view.findViewById(R.id.btnEnviar);

        configMensaje();
        configActionBar();
        configImageView(mMensaje.getIcon());

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setIcon(R.mipmap.applogo);
                dialog.setTitle("Mensaje enviado");
                dialog.setMessage("Está seguro de enviar el mensaje?");
                dialog.setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> userSession = SessionManager.getInstance(getContext()).getUserDetails();
                        String email= userSession.get(SessionManager.KEY_EMAIL);
                        User user = User.getInstance().getUser(email);
                        Enviado Enviado = new Enviado();
                        Enviado.setTituloMensaje(mMensaje.getTitulo());
                        Enviado.setMensajeDescription("reunión mañana 8pm");

                        Enviado.setUser(user);
                        try {
                            Enviado.save();

                            Snackbar.make(view, "Mensaje enviado #" +Enviado.getNumEnviado()+
                            " realizado exitosamente!", Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(getResources().getColor(R.color.greenBg))
                            .setAction("CONTINUAR", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("¿Si es un cita, Quiere añadir este evento en su Calendario?")
                                            .setTitle("Envio mensaje")
                                            .setPositiveButton("Si", new DialogInterface.OnClickListener()  {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Log.i("Dialogos", "Confirmacion Aceptada.");

                                                    CreateEventCalendar("Mensaje enviado :", mMensaje.getTitulo(),
                                                            "Recuerda la cita es a la hora indicada ",
                                                            "21","40");

                                                    EnviadoFragment EnviadoFragment = new EnviadoFragment();
                                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                    transaction.replace(R.id.main_layout, EnviadoFragment, "fragmento_Enviado")
                                                            .setTransition(transaction.TRANSIT_FRAGMENT_FADE);
                                                    transaction.addToBackStack("fragmento_Enviado");
                                                    transaction.commit();
                                                }
                                            })
                                            .setNegativeButton("No ahora", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Log.i("Dialogos", "Confirmacion Cancelada.");

                                                    EnviadoFragment EnviadoFragment = new EnviadoFragment();
                                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                    transaction.replace(R.id.main_layout, EnviadoFragment, "fragmento_Enviado")
                                                            .setTransition(transaction.TRANSIT_FRAGMENT_FADE);
                                                    transaction.addToBackStack("fragmento_Enviado");
                                                    transaction.commit();
                                                }
                                            }).show();
                                        }
                                    })
                                    .show();
                            dialog.cancel();
                            showNotificacion(Enviado);
                        }
                        catch (Exception e){
                            Log.e("Error:","No inserta mensaje en la BD ->" +e);
                        }
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void
                    onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    private void configActionBar() {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        configTitle();
    }

    private void configTitle() {
        toolbarLayout.setTitle(mMensaje.getTitulo());

    }
    private void configMensaje() {
        mMensaje = MensajesFragment.sMensaje;
        tvTituloMensaje.setText(mMensaje.getTitulo());
        tvContenidoMensaje.setText("contenido del mensaje");
        tvFechaMensaje.setText("11/04/2019");
    }

    private void configImageView(String fotoUrl) {
        if (fotoUrl != null){
            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop();

            Glide.with(this)
                    .load(fotoUrl)
                    .apply(options)
                    .into(imgFoto);
        } else {
            imgFoto.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_photo_size_select_actual));
        }

        mMensaje.setIcon(fotoUrl);
    }

    public void showNotificacion(Enviado Enviado) {
        NotificationManager notificationManager= (NotificationManager) getContext().
                getSystemService(NOTIFICATION_SERVICE);

        String message = "No olvidar la cita a las "+ Enviado.getFechaEnviado() +" sobre " + mMensaje.getTitulo()+
                " ubicado en "+ mMensaje.getTitulo();

        Notification.Builder builder = new Notification.Builder(getContext());
        builder.setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentTitle("MessageDeliverySystem: Aviso o recordatorio")
                .setStyle(new Notification.BigTextStyle().bigText(message))
                .setContentText(message).build();
        Notification notification = builder. getNotification();
        notificationManager.notify(R.drawable.ic_message_black_24dp, notification);
    }

    public void CreateEventCalendar(String title, String location, String description,
                                    String hora, String minute){
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setData(CalendarContract.Events.CONTENT_URI);
        calIntent.putExtra(CalendarContract.Events.TITLE,title);
        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION,location);
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION,description);

        Calendar calendar = Calendar.getInstance();
        int dia=calendar.get(Calendar.DAY_OF_MONTH);
        int mes=calendar.get(Calendar.MONTH);
        int año=calendar.get(Calendar.YEAR);
        GregorianCalendar calDate = new GregorianCalendar(año,mes,dia);

        int hourPick = Integer.parseInt(hora);
        int minutePick = Integer.parseInt(minute);

        calDate.set(Calendar.HOUR,hourPick);
        calDate.set(Calendar.MINUTE,minutePick);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,hourPick);
        startActivity(calIntent);
    }
}
