package com.jose.prieto.mysmallbusinessbyneza.AccionesLista;


import android.content.Context;

import com.jose.prieto.mysmallbusinessbyneza.Inicio;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Articulo;
import com.jose.prieto.mysmallbusinessbyneza.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by Jose on 13/03/2016.
 */
public class AgregarLista {
    public Context context;
    public Boolean error = false;
    public AgregarLista(Context context, Articulo articulo){
        this.context = context;
        abrirArchivo(articulo);
    }

    private void abrirArchivo(Articulo articulo){
        File archivo = new File (Inicio.DIRECTORIO_PAQUETE_EXTERNO, "Lista de Articulos.txt");
        Boolean nuevo = false;
        if(!archivo.exists()) nuevo = true;
        PrintStream scr;
        try {
            scr = new PrintStream(new FileOutputStream(archivo,true));
            if(nuevo==true)scr.println(cargarTitulos());
            scr.println(cargarLinea(articulo));
            scr.close();
        } catch (FileNotFoundException e) {
            error = true;
        }

    }

    private String cargarLinea(Articulo art){
        String linea = "";
        linea = String.valueOf(art.getSku());
        linea = linea+"|"+art.getExist();
        linea = linea+"|"+art.getDescipcion();
        linea = linea+"|"+art.getPrecio();
        return linea;
    }

    private String cargarTitulos(){
        String linea = context.getResources().getString(R.string.column_art);
        linea = linea+"|"+context.getResources().getString(R.string.column_cant);
        linea = linea+"|"+context.getResources().getString(R.string.column_descrip);
        linea = linea+"|"+context.getResources().getString(R.string.column_precio);
        return linea;
    }

}
