package com.jose.prieto.mysmallbusinessbyneza.BusqAvanzada;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.AdmSQLite;

import java.util.ArrayList;

/**
 * Created by Jose on 21/06/2016.
 */
public class BusquedaAvanz {
    private AdmSQLite admSQLite;
    private SQLiteDatabase db;
    private Cursor fila;
    private int totFilas;
    private String lineaArticulo;
    private String lineaDescripcion;
    private ArrayList<ColBusqAvanzada> colBusqAvanzada;
    private String textColumnas = "";
    private String columnas = "";

    public BusquedaAvanz(Context context, ArrayList<ColBusqAvanzada> colBusqAvanzada, String [] arrayColumnas){
        this.colBusqAvanzada = colBusqAvanzada;
        lineaArticulo = "";
        lineaDescripcion = "";
        if(colBusqAvanzada.size()>1){
            for(int n = 1; n < colBusqAvanzada.size(); n++){
                columnas = columnas + "," + colBusqAvanzada.get(n).getTitulo();
            }
        }
        for (int n = 0; n<arrayColumnas.length; n++){
            textColumnas = textColumnas + ", " + arrayColumnas[n] + " text";
        }
        admSQLite = new AdmSQLite(context, textColumnas);//Se abre el adm de la bade se datos
        db = admSQLite.getWritableDatabase();//Se le indica que se va a escribir o leer en ella
        //String [] buscarDato = {dato};
        totFilas = 0;
        fila = db.rawQuery("select codigo,descripcion" + columnas + " from articulos where "
                + colBusqAvanzada.get(0).getTitulo() + "='" + colBusqAvanzada.get(0).getDato().toUpperCase() + "'",null);
    }

    public BusquedaAvanz(Context context, String titulo, String dato, String [] arrayColumnas){
        colBusqAvanzada = new ArrayList<ColBusqAvanzada>();
        ColBusqAvanzada columna = new ColBusqAvanzada();
        columna.setTitulo(titulo);
        columna.setDato(dato);
        columna.setSeleccion(true);
        colBusqAvanzada.add(columna);
        lineaArticulo = "";
        lineaDescripcion = "";
        columnas = "," + titulo;
        for (int n = 0; n<arrayColumnas.length; n++){
            textColumnas = textColumnas + ", " + arrayColumnas[n] + " text";
        }
        admSQLite = new AdmSQLite(context, textColumnas);//Se abre el adm de la bade se datos
        db = admSQLite.getWritableDatabase();//Se le indica que se va a escribir o leer en ella
        //String [] buscarDato = {dato};
        totFilas = 0;
        fila = db.rawQuery("select codigo,descripcion" + columnas + " from articulos where "
                + titulo + "='" + dato.toUpperCase() + "'",null);
    }

    public Cursor getFila(){
        return fila;
    }

    public void cerrarTabla(){
        db.close();//Se cierra la escritira de la Base de Datos
    }

    public void buscarCoincidencias(){
        Boolean correcto = true;
        String articulo = fila.getString(0);
        String descripcion =fila.getString(1);
        if(colBusqAvanzada.size()>1){
            for (int n=1;n < colBusqAvanzada.size(); n++){
                int b = fila.getString(n+1).indexOf(colBusqAvanzada.get(n).getDato().toUpperCase());
                if(b<0) {
                    correcto = false;
                }else{

                }
            }
        }
        if(correcto && totFilas <= 4000) {
            if(!lineaArticulo.isEmpty()) lineaArticulo = lineaArticulo + "|";
            lineaArticulo = lineaArticulo + articulo;
            if(!lineaDescripcion.isEmpty()) lineaDescripcion = lineaDescripcion + "|";
            lineaDescripcion = lineaDescripcion + descripcion;
            totFilas++;
        }
    }

    public int getTotFilas(){
        return totFilas;
    }

    public String[] getResultadoArticulo(){
        if(!lineaArticulo.isEmpty()){
            return lineaArticulo.split("\\|");
        }else{
            return null;
        }
    }

    public String [] getResultadoDescripcion(){
        if(!lineaDescripcion.isEmpty()){
            return lineaDescripcion.split("\\|");
        }else{
            return null;
        }
    }
}
