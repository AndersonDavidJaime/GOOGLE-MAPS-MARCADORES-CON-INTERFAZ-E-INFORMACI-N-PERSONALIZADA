package com.example.myapplication_maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.myapplication_maps.web_service.Asynchtask;
import com.example.myapplication_maps.web_service.WebService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    GoogleMap mapa;
    Datos datos;
    List<Datos> listadatos;

    String HOA="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        datos=new Datos();
        listadatos=new ArrayList<>();



    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        HOA="";
        mapa.clear();
        mapa.addMarker(new MarkerOptions().position(latLng).title("Punto"));
        obtener_resultados(latLng);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.setOnMapClickListener(this);

        mapa.setInfoWindowAdapter(new Windows(MainActivity.this, LayoutInflater.from(MainActivity.this).inflate(R.layout.ventana, null)));


        LatLng posisicion = new LatLng(-1.0125144372171426, -79.46953106309826);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(posisicion)
                .zoom(17)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mapa.animateCamera(camUpd3);
    }

    String direccion;
    List<String> horario;
    public void obtener_resultados(LatLng ln)
    {
        Thread thread_response=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> lista_de_lugares=new ArrayList<>();
                    lista_de_lugares.add("store");
                    lista_de_lugares.add("restaurant");
                    lista_de_lugares.add("lodging");

                    for(int x=0;x<lista_de_lugares.size();x++) {

                        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + ln.latitude + "," + ln.longitude + "&radius=300&type="+lista_de_lugares.get(x)+"&key=AIzaSyAZmpF3k0bcm-3c-f_0feLZQZRwYu-gdr0";
                        Service serv = new Service();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, String> datos = new HashMap<String, String>();
                                WebService ws = new WebService(
                                        url,
                                        datos, MainActivity.this, serv);
                                ws.execute("GET");
                            }
                        });
                        while (serv.getRespuesta().equals("N"))
                            Thread.sleep(1000);
                        List<Datos> principal=leer_json(serv.getRespuesta());
                        for(int gg=0;gg<principal.size();gg++) {
                            String url1 = "https://maps.googleapis.com/maps/api/place/details/json?place_id="+principal.get(gg).getPlace_id()+"&key=AIzaSyAZmpF3k0bcm-3c-f_0feLZQZRwYu-gdr0";
                            Service serv1 = new Service();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Map<String, String> datos = new HashMap<String, String>();
                                    WebService ws = new WebService(
                                            url1,
                                            datos, MainActivity.this, serv1);
                                    ws.execute("GET");
                                }
                            });


                            while (serv1.getRespuesta().equals("N"))
                                Thread.sleep(1000);
                            JSONObject princ = new JSONObject(serv1.getRespuesta());
                            JSONObject jsondetalle = princ.getJSONObject("result");
                            direccion = jsondetalle.getString("formatted_address");
                            horario = new ArrayList<>();
                            if (jsondetalle.has("current_opening_hours")) {

                                JSONArray ARRAY = jsondetalle.getJSONObject("current_opening_hours").getJSONArray("weekday_text");
                                for (int n = 0; n < ARRAY.length(); n++) {
                                    horario.add(ARRAY.getString(n));
                                }
                            }
                        }

                        runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   for(int h=0;h<principal.size();h++) {
                                       Datosreq dr = new Datosreq();
                                       dr.setNombre(principal.get(h).getNombre());
                                       dr.setLogo(principal.get(h).getIcono());
                                       dr.setHorario(horario);
                                       dr.setUbicacion(direccion);
                                       mapa.addMarker(new MarkerOptions().position(principal.get(h).getLocalizacion())).setTag(dr);

                                   }
                               }
                           });




                    }


                }catch (Exception ex){
                }
            }
        });
        thread_response.start();
    }



    public List<Datos> leer_json(String json) throws JSONException {

        JSONObject jsonprincipal=new JSONObject(json);
        JSONArray JSONA=jsonprincipal.getJSONArray("results");
        for(Integer x=0;x<JSONA.length();x++)
        {
            datos=new Datos();
            JSONObject JSS=JSONA.getJSONObject(x).getJSONObject("geometry").getJSONObject("location");
            datos.setLocalizacion(new LatLng(JSS.getDouble("lat"),JSS.getDouble("lng")));
            datos.setNombre(JSONA.getJSONObject(x).getString("name"));
            datos.setPlace_id(JSONA.getJSONObject(x).getString("place_id"));
            JSONObject js=JSONA.getJSONObject(x);
            if(js.has("photos"))
            {
                JSONArray ja=js.getJSONArray("photos");
                String ref="";
                if(ja.length()>0)
                {
                    ref=ja.getJSONObject(0).getString("photo_reference");
                }
                String urlimg="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="+ref+"&key=AIzaSyAZmpF3k0bcm-3c-f_0feLZQZRwYu-gdr0";
                datos.setIcono(urlimg);
            }
            listadatos.add(datos);
        }
        return listadatos;
    }



}