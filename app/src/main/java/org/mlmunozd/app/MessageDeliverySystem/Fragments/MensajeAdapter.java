package org.mlmunozd.app.MessageDeliverySystem.Fragments;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mlmunozd.app.MessageDeliverySystem.Models.Mensaje;
import org.mlmunozd.app.MessageDeliverySystem.R;

import java.util.ArrayList;
import java.util.List;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.ViewHolder> {

    //contendrá el listado de Mensajees que se irán agregando
    private ArrayList<Mensaje> mensajes = new ArrayList<>();
    private Context context;

    public MensajeAdapter() { }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_mensaje, parent, false);
        return new ViewHolder(view);
    }

    //es donde se vincula nuestra vista de cada elemento y darle valor a los componentes
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);

        holder.tvTituloMensaje.setText(mensaje.getTitulo());
        holder.tvContenidoMensaje.setText(mensaje.getContenido());
        holder.imgMensaje.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_message_black_80dp));
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public void addMensaje(Mensaje message){
        //if(!mensajes.contains(message)) {
            mensajes.add(0,message);
            notifyItemInserted(0);
        //}
    }

    public void setList(ArrayList<Mensaje> list) {
        this.mensajes = list;
    }

    public void replaceData(ArrayList<Mensaje> items) {
        setList(items);
        notifyDataSetChanged();
    }

    //Main class
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTituloMensaje;
        TextView tvContenidoMensaje;
        ImageView imgMensaje;

        ViewHolder(View itemView) {
            super(itemView);
            tvTituloMensaje = itemView.findViewById(R.id.tvTituloMensaje);
            tvContenidoMensaje = itemView.findViewById(R.id.tvContenidoMensaje);
            //containerMain = itemView.findViewById(R.id.containerMain);
            imgMensaje = itemView.findViewById(R.id.imgMensaje);
        }
    }
}
