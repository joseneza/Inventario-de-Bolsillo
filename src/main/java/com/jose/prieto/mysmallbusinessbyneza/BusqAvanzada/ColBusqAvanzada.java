package com.jose.prieto.mysmallbusinessbyneza.BusqAvanzada;

/**
 * Created by Jose on 21/06/2016.
 */
public class ColBusqAvanzada {
    private Boolean seleccion;
    private String titulo;
    private String dato;
    private int columna;

    public void setSeleccion(Boolean seleccion){
        this.seleccion = seleccion;
    }

    public Boolean getSeleccion(){
        return seleccion;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public String getTitulo(){
        return titulo;
    }

    public void setDato(String dato){
        this.dato = dato;
    }

    public String getDato(){
        return dato;
    }

    public void setColumna(int columna){
        this.columna = columna;
    }

    public int getColumna(){
        return columna;
    }
}
