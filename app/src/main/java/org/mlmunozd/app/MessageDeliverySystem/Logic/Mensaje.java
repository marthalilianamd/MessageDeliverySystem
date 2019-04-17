package org.mlmunozd.app.MessageDeliverySystem.Logic;

public class Mensaje {
    private String titulo;
    private String contenido;
    private String estado;
    private String fecha;
    private String icon;

    public Mensaje() {

    }

    public Mensaje(String titulo, String contenido, String estado, String fecha) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Mensaje(String titulo, String contenido, String estado, String fecha, String icon) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.estado = estado;
        this.fecha = fecha;
        this.icon = icon;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "titulo='" + titulo + '\'' +
                ", contenido='" + contenido + '\'' +
                ", estado='" + estado + '\'' +
                ", fecha='" + fecha + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
