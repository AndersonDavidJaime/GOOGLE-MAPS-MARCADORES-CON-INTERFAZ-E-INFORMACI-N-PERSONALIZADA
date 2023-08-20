package com.example.myapplication_maps;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication_maps.web_service.Asynchtask;
import com.example.myapplication_maps.web_service.WebService;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Service implements Asynchtask {


    private String respuesta;
    public Service(){
        this.respuesta="N";
    }

    @Override
    public void processFinish(String result) throws JSONException {
        this.respuesta=result;
        //Log.i("fdgsfgsddg",result);
    }
    public String getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
