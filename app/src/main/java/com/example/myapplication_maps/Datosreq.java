package com.example.myapplication_maps;

import java.util.List;

public class Datosreq {

    private String nombre;
    private String ubicacion;
    private String Logo;
    private List<String> horario;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public List<String> getHorario() {
        return horario;
    }

    public void setHorario(List<String> horario) {
        this.horario = horario;
    }
}
