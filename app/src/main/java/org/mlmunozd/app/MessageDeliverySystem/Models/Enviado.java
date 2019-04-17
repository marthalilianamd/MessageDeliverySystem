package org.mlmunozd.app.MessageDeliverySystem.Models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.mlmunozd.app.MessageDeliverySystem.Persistence.MessageDeliverySystemDatabase;

@Table(database = MessageDeliverySystemDatabase.class)
public class Enviado extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private int numEnviado;

    @Column
    private String tituloMensaje;
    @Column
    private String mensajeDescription;
    @Column
    private String fechaEnviado;

    private String iconEnviado;

    @ForeignKey(stubbedRelationship = true)
    User user;

    /*
    * Implementations Singleton
    */
    private static Enviado singletonEnviado;

     /*public static Enviado getInstance(){
        if(singletonEnviado == null){
            singletonEnviado = new Enviado();
        }
        return singletonEnviado;
    }*/

    public Enviado() {
    }

    public int getNumEnviado() {
        return numEnviado;
    }

    public void setNumEnviado(int numEnviado) {
        this.numEnviado = numEnviado;
    }

    public String getTituloMensaje() {
        return tituloMensaje;
    }

    public void setTituloMensaje(String tituloMensaje) {
        this.tituloMensaje = tituloMensaje;
    }

    public String getMensajeDescription() {
        return mensajeDescription;
    }

    public void setMensajeDescription(String mensajeDescription) {
        this.mensajeDescription = mensajeDescription;
    }

    public String getFechaEnviado() {
        return fechaEnviado;
    }

    public void setFechaEnviado(String fechaEnviado) {
        this.fechaEnviado = fechaEnviado;
    }

    public String getIconEnviado() {
        return iconEnviado;
    }

    public void setIconEnviado(String iconEnviado) {
        this.iconEnviado = iconEnviado;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Enviado{" +
                "numEnviado=" + numEnviado +
                ", tituloMensaje='" + tituloMensaje + '\'' +
                ", mensajeDescription='" + mensajeDescription + '\'' +
                ", fechaEnviado='" + fechaEnviado + '\'' +
                ", iconEnviado='" + iconEnviado + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enviado)) return false;

        Enviado enviado = (Enviado) o;

        if (getNumEnviado() != enviado.getNumEnviado()) return false;
        if (getTituloMensaje() != null ? !getTituloMensaje().equals(enviado.getTituloMensaje()) : enviado.getTituloMensaje() != null)
            return false;
        if (getMensajeDescription() != null ? !getMensajeDescription().equals(enviado.getMensajeDescription()) : enviado.getMensajeDescription() != null)
            return false;
        if (getFechaEnviado() != null ? !getFechaEnviado().equals(enviado.getFechaEnviado()) : enviado.getFechaEnviado() != null)
            return false;
        if (getIconEnviado() != null ? !getIconEnviado().equals(enviado.getIconEnviado()) : enviado.getIconEnviado() != null)
            return false;
        return getUser() != null ? getUser().equals(enviado.getUser()) : enviado.getUser() == null;
    }

    @Override
    public int hashCode() {
        int result = getNumEnviado();
        result = 31 * result + (getTituloMensaje() != null ? getTituloMensaje().hashCode() : 0);
        result = 31 * result + (getMensajeDescription() != null ? getMensajeDescription().hashCode() : 0);
        result = 31 * result + (getFechaEnviado() != null ? getFechaEnviado().hashCode() : 0);
        result = 31 * result + (getIconEnviado() != null ? getIconEnviado().hashCode() : 0);
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        return result;
    }

    /* @Override
    public String toString() {
        return "Enviado{" +
                "numEnviado=" + numEnviado +
                ", nombreMensaje='" + nombreMensaje + '\'' +
                ", foodDescription='" + foodDescription + '\'' +
                ", address='" + address + '\'' +
                ", cost='" + cost + '\'' +
                ", portions='" + portions + '\'' +
                ", state='" + state + '\'' +
                ", RecogerAntesHora='" + RecogerAntesHora + '\'' +
                ", iconEnviado='" + iconEnviado + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enviado)) return false;

        Enviado Enviado = (Enviado) o;

        if (numEnviado != Enviado.numEnviado) return false;
        if (nombreMensaje != null ? !nombreMensaje.equals(Enviado.nombreMensaje) : Enviado.nombreMensaje != null)
            return false;
        if (foodDescription != null ? !foodDescription.equals(Enviado.foodDescription) : Enviado.foodDescription != null)
            return false;
        if (address != null ? !address.equals(Enviado.address) : Enviado.address != null) return false;
        if (cost != null ? !cost.equals(Enviado.cost) : Enviado.cost != null) return false;
        if (portions != null ? !portions.equals(Enviado.portions) : Enviado.portions != null)
            return false;
        if (state != null ? !state.equals(Enviado.state) : Enviado.state != null) return false;
        if (RecogerAntesHora != null ? !RecogerAntesHora.equals(Enviado.RecogerAntesHora) : Enviado.RecogerAntesHora != null)
            return false;
        if (iconEnviado != null ? !iconEnviado.equals(Enviado.iconEnviado) : Enviado.iconEnviado != null)
            return false;
        return user != null ? user.equals(Enviado.user) : Enviado.user == null;
    }

    @Override
    public int hashCode() {
        int result = numEnviado;
        result = 31 * result + (nombreMensaje != null ? nombreMensaje.hashCode() : 0);
        result = 31 * result + (foodDescription != null ? foodDescription.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (portions != null ? portions.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (RecogerAntesHora != null ? RecogerAntesHora.hashCode() : 0);
        result = 31 * result + (iconEnviado != null ? iconEnviado.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }*/
}