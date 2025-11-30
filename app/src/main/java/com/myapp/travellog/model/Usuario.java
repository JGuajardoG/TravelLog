package com.myapp.travellog.model;

/**
 * Clase POJO (Plain Old Java Object) que representa un Usuario.
 * Contiene los getters y setters para acceder a sus atributos.
 */
public class Usuario {
    private int id;
    private String nombre;
    private String usuario;
    private String password;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
