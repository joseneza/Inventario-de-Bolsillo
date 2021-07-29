package com.jose.prieto.mysmallbusinessbyneza.AccionAjustes;

import android.content.Context;

import com.jose.prieto.mysmallbusinessbyneza.Inicio;

/**
 * Created by Jose on 10/09/2016.
 */
public class ListaAjustes {
    Context context;
    String titulo;
    String lista;

    public ListaAjustes(Context context, int resources_titulo, int resources_lista){
        this.context = context;
        this.titulo = context.getResources().getString(resources_titulo);
        this.lista = context.getResources().getString(resources_lista);
    }

    public ListaAjustes(Context context, int resources_titulo, String lista){
        this.context = context;
        this.titulo = context.getResources().getString(resources_titulo);
        this.lista = lista;
    }

    public String getTitulo(){
        return titulo;
    }

    public void setTitulo(int resources_titulo){
        this.titulo = context.getResources().getString(resources_titulo);
    }

    public String getLista(){
        return lista;
    }

    public void setLista(int resources_lista){
        this.lista = context.getResources().getString(resources_lista);
    }

    public void setLista(String lista){
        this.lista = lista;
    }
}
