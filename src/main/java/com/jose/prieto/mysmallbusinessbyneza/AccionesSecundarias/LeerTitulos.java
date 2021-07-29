package com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias;

import android.content.Context;
import android.content.SharedPreferences;

import com.jose.prieto.mysmallbusinessbyneza.Inicio;
import java.io.File;

/**
 * Created by Jose on 09/05/2016.
 */
public class LeerTitulos {

    public static final String PRECIO = "precio";
    public static final String PRECIO_ANT = "precio_ant";
    public static final String EAN = "ean";
    public static final String URL_IMG = "url_img";
    public static final String DATOS_EXTRAS = "datosXtras";
    private boolean precio;
    private boolean precioAnt;
    private boolean ean;
    private boolean url_img;
    private String datosXtras = "";

    public LeerTitulos(Context context){
        //Carga los datos del titulo desde preferencias
        SharedPreferences cargarTitulos = context.getSharedPreferences(Inicio.ARCH_AJUSTES, Context.MODE_PRIVATE);
        precio = cargarTitulos.getBoolean(PRECIO, false);
        precioAnt = cargarTitulos.getBoolean(PRECIO_ANT, false);
        ean = cargarTitulos.getBoolean(EAN, false);
        url_img = cargarTitulos.getBoolean(URL_IMG, false);
        datosXtras = cargarTitulos.getString(DATOS_EXTRAS, "");
    }

    public Boolean getPrecio(){
        return precio;
    }

    public Boolean getPrecioAnt(){
        return precioAnt;
    }

    public Boolean getEan(){
        return ean;
    }

    public Boolean getUrl(){
        return url_img;
    }

    private void cargar(File archTitulos){
        String arrayLinea [] = null;
        if(archTitulos.exists()){
            /*BufferedReader leerArch;
            try {
                leerArch = new BufferedReader(new InputStreamReader(new FileInputStream(archTitulos)));
                String linea = leerArch.readLine();//  0
                if(linea!=null) precio = Integer.parseInt(linea);
                linea = leerArch.readLine();//  1
                if(linea!=null) precioAnt = Integer.parseInt(linea);
                linea = leerArch.readLine();//  2
                if(linea!=null) ean = Integer.parseInt(linea);
                linea = leerArch.readLine();//  3
                if(linea!=null) url = Integer.parseInt(linea);
                linea = leerArch.readLine();//  4
                if(linea!=null){
                    datosXtras = linea;
                }
            } catch (FileNotFoundException e) {
            } catch (IOException io) {
            }*/
        }

    }


    /*public Boolean getPrecio(){
        if(precio==1){
            return true;
        }else{
            return false;
        }
    }

    public Boolean getPrecioAnt(){
        if(precioAnt==1){
            return true;
        }else{
            return false;
        }
    }

    public Boolean getEan(){
        if(ean==1){
            return true;
        }else{
            return false;
        }
    }

    public Boolean getUrl(){
        if(url==1){
            return true;
        }else{
            return false;
        }
    }*/

    public String[] getDatosXtras(){
        return datosXtras.split("\\|");
    }
}
