package org.mlmunozd.app.MessageDeliverySystem.Models;

public class Mensaje {
    private String id;
    private String titulo;
    private String contenido;
    private String estado;
    private String fecha;
    private String icon;
    private String movil;

    public Mensaje() { }

    public Mensaje(String titulo, String contenido, String estado, String fecha, String movil) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.estado = estado;
        this.fecha = fecha;
        this.movil = movil;
    }

    public Mensaje(String titulo, String contenido, String estado, String fecha, String icon, String movil) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.estado = estado;
        this.fecha = fecha;
        this.icon = icon;
        this.movil= movil;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "titulo='" + titulo + '\'' +
                ", contenido='" + contenido + '\'' +
                ", estado='" + estado + '\'' +
                ", fecha='" + fecha + '\'' +
                ", icon='" + icon + '\'' +
                ", movil='" + icon + '\'' +
                '}';
    }
}
