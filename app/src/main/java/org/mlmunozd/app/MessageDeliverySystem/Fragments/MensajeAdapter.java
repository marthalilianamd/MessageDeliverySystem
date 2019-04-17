package org.mlmunozd.app.MessageDeliverySystem.Fragments;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.mlmunozd.app.MessageDeliverySystem.Logic.Mensaje;
import org.mlmunozd.app.MessageDeliverySystem.R;

import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder> {

    //contendrá el listado de Mensajees que se irán agregando
    private List<Mensaje> mensajes;
    private Context context;
    //interface
    private OnItemClickListener listener;

    public MensajeAdapter(List<Mensaje> mensajes, OnItemClickListener listener) {
        this.mensajes = mensajes;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje,
                parent, false);
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    //es donde se vincula nuestra vista de cada elemento y darle valor a los componentes
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Mensaje mensaje = mensajes.get(position);
        //configuramos el listener a cada elemento para los eventos Click y LongClick
        holder.setListener(mensaje,listener);

        holder.tvTituloMensaje.setText(mensaje.getTitulo());
        holder.tvEstado.setText(mensaje.getEstado());

        if(mensaje.getIcon()!=null){
            RequestOptions requestOptions = new RequestOptions();
            //guardar la imagen origen y transformada en cache.
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            //recprtar la imagen y centrarla
            requestOptions.centerCrop();
            //requestOptions.placeholder(R.drawable.ic_image_black_24dp);

            Glide.with(context).load(mensaje.getIcon())
                    .apply(requestOptions)
                    .into(holder.imgMensaje);
        }
        else{
            holder.imgMensaje.setImageDrawable(ContextCompat.getDrawable
                    (context,R.drawable.ic_mensaje_black_80dp));
        }
    }

    @Override
    public int getItemCount() {
        return this.mensajes.size();
    }

    public void addMensaje(Mensaje mensaje){
        if(!mensajes.contains(mensaje)) {
            mensajes.add(mensaje);
            notifyDataSetChanged();
        }
    }

    //Main class
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTituloMensaje;
        TextView tvEstado;
        RelativeLayout containerMain;
        ImageView imgMensaje;

        ViewHolder(View itemView) {
            super(itemView);
            tvTituloMensaje = itemView.findViewById(R.id.tvTituloMensaje);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            containerMain = itemView.findViewById(R.id.containerMain);
            imgMensaje = itemView.findViewById(R.id.imgMensaje);
        }

        void setListener(final Mensaje mensaje, final OnItemClickListener listener){
            containerMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(mensaje);
                }
            });

            containerMain.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongItemClick(mensaje);
                    return true;
                }
            });
        }
    }
}
