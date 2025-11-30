package com.myapp.travellog.model;

/**
 * Clase POJO (Plain Old Java Object) que representa un Viaje.
 * Contiene los getters y setters para acceder a sus atributos.
 */
public class Viaje {
    private int id;
    private int idUsuario;
    private String nombreViaje;
    private String fecha;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreViaje() {
        return nombreViaje;
    }

    public void setNombreViaje(String nombreViaje) {
        this.nombreViaje = nombreViaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
