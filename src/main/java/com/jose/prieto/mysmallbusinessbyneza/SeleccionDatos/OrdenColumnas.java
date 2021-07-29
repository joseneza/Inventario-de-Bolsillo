package com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos;

import java.util.ArrayList;

/**
 * Created by Jose on 04/05/2016.
 */
public class OrdenColumnas {
    private String nombre;
    private int consecutivo;
    private Boolean agregar;
    public OrdenColumnas(String nombre, int consecutivo){
        this.nombre = nombre;
        this.consecutivo = consecutivo;
        agregar = false;
    }
    public String getNombre(){
        return nombre;
    }
    public int getConsecutivo(){
        return consecutivo;
    }
    public Boolean getAgregar(){
        return agregar;
    }
    public void setAgregar(Boolean agregar){
        this.agregar = agregar;
    }

    public static ArrayList<OrdenColumnas> getArrayOrdenColumnas(String [] titulosColumnas){
        ArrayList<OrdenColumnas> titulos = new ArrayList<OrdenColumnas>();
        for (int n=0; n < titulosColumnas.length; n++){
            if(!titulosColumnas[n].isEmpty()) titulos.add(new OrdenColumnas(titulosColumnas[n], n));
        }
        return titulos;
    }
}
