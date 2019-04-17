package org.mlmunozd.app.MessageDeliverySystem.Fragments;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.mlmunozd.app.MessageDeliverySystem.Models.Enviado;
import org.mlmunozd.app.MessageDeliverySystem.R;
import java.util.List;

public class EnviadoAdapter extends RecyclerView.Adapter<EnviadoAdapter.ViewHolder> {

    //contendrá el listado de mensajes que se irán agregando
    private List<Enviado> enviados;
    private Context context;
    //interface
    private OnItemClickListenerEnviado listener;

    public EnviadoAdapter(List<Enviado> enviados, OnItemClickListenerEnviado listener) {
        this.enviados = enviados;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enviado,
                parent, false);
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    //es donde se vincula nuestra vista de cada elemento y darle valor a los componentes
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Enviado enviado = enviados.get(position);
        //configuramos el listener a cada elemento para los eventos Click y LongClick
        holder.setListener(enviado,listener);

        holder.tvAsuntoEnviado.setText(enviado.getTituloMensaje());
        holder.tvContenidoMensaje.setText(enviado.getMensajeDescription());

        if(enviado.getIconEnviado()!=null){
            RequestOptions requestOptions = new RequestOptions();
            //guardar la imagen origen y transformada en cache.
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            //recprtar la imagen y centrarla
            requestOptions.centerCrop();
            //requestOptions.placeholder(R.drawable.ic_image_black_24dp);

            Glide.with(context).load(enviado.getIconEnviado())
                    .apply(requestOptions)
                    .into(holder.imgEnviado);
        }
        else{
            holder.imgEnviado.setImageDrawable(ContextCompat.getDrawable
                    (context,R.drawable.ic_room_service_black_80dp));
        }
    }

    @Override
    public int getItemCount() {
        return this.enviados.size();
    }

    public void addEnviado(Enviado Enviado){
        if(!enviados.contains(Enviado)) {
            enviados.add(Enviado);
        }
        notifyDataSetChanged();
    }

    //Main class
    class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout containerMainEnviado;
        ImageView imgEnviado;
        TextView tvAsuntoEnviado;
        TextView tvContenidoMensaje;

        ViewHolder(View itemView) {
            super(itemView);
            containerMainEnviado = itemView.findViewById(R.id.containerMainEnviado);
            imgEnviado = itemView.findViewById(R.id.imgEnviado);
            tvAsuntoEnviado = itemView.findViewById(R.id.tvAsuntoEnviado);
            tvContenidoMensaje = itemView.findViewById(R.id.tvContenidoMensaje);
        }

        void setListener(final Enviado enviado, final OnItemClickListenerEnviado listener){
            containerMainEnviado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(enviado);
                }
            });

            containerMainEnviado.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongItemClick(enviado);
                    return true;
                }
            });
        }
    }
}