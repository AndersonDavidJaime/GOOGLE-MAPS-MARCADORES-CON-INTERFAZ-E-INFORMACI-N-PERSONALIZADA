package com.example.myapplication_maps;

import com.google.android.gms.maps.model.LatLng;

public class Datos {

    private String nombre;
    private LatLng localizacion;
    private String place_id;
    private String icono;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LatLng getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(LatLng localizacion) {
        this.localizacion = localizacion;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
