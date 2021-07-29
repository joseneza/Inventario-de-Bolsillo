package com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Articulo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.LeerTitulos;
import com.jose.prieto.mysmallbusinessbyneza.R;

import java.text.DecimalFormat;

/**
 * Created by Jose on 06/03/2016.
 */
public class BuscarArticulo {

    AdmSQLite admSQLite;
    SQLiteDatabase db;
    Context context;
    private long codigo = 0;
    private String ean = "";
    private String descripcion = "";
    private String exist = "";
    private String precio = "";
    private String precioAnt = "";
    private String urlImg = "";
    private String datosXtras = "";
    public Boolean buscar = false;
    public String titColumnas [];
    private String textColumnas;
    private String columXtra;
    private DecimalFormat df = new DecimalFormat("0.00");


    public BuscarArticulo(Context context, long codigo){
        this.context = context;
        LeerTitulos titulos = new LeerTitulos(context);
        titColumnas = titulos.getDatosXtras();
        textColumnas = "";
        columXtra = "";
        for (int n = 0; n<titColumnas.length; n++){
            textColumnas = textColumnas + ", " + titColumnas[n] + " text";
            columXtra = columXtra + "," + titColumnas[n];
        }
        buscar(context, codigo);
    }

    private void buscar(Context context, long codigo){
        admSQLite = new AdmSQLite(context, textColumnas);//Se abre el adm de la bade se datos
        db = admSQLite.getWritableDatabase();//Se le indica que se va a escribir o leer en ella
        Cursor fila = db.rawQuery("select ean,descripcion,existencia,precio,precio_anterior,urlArt" +
                columXtra + " from articulos where codigo="+codigo,null);
        if(fila.moveToFirst()){
            this.codigo = codigo;
            ean = fila.getString(0);
            descripcion = fila.getString(1);
            exist = fila.getString(2);
            precio = fila.getString(3);
            precioAnt = fila.getString(4);
            urlImg = fila.getString(5);
            datosXtras = "";
            for(int n = 6 ;n < (titColumnas.length + 6); n++){
                if(n>6) datosXtras = datosXtras + "|";
                datosXtras = datosXtras + fila.getString(n);
            }
            buscar = true;
        }else{
            fila = db.rawQuery("select codigo,descripcion,existencia,precio,precio_anterior,urlArt" +
                    columXtra + " from articulos where ean='"+codigo+"'",null);
            if(fila.moveToFirst()){
                ean = Long.toString(codigo);
                this.codigo = Long.parseLong(fila.getString(0));
                descripcion = fila.getString(1);
                exist = fila.getString(2);
                precio = fila.getString(3);
                precioAnt = fila.getString(4);
                urlImg = fila.getString(5);
                datosXtras = "";
                for(int n = 6 ;n < (titColumnas.length + 6); n++){
                    if(n>6) datosXtras = datosXtras + "|";
                    datosXtras = datosXtras + fila.getString(n);
                }
                buscar=true;
            }else{
                this.codigo = codigo;
            }
        }
        db.close();//Se cierra la escritira de la Base de Datos
    }

    public long getCodigo(){
        return codigo;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public String getExist(){
        return exist;
    }

    public String getUrlImg(){
        return urlImg;
    }

    public String getPrecio(){
        double nvoPrecio = 0.0;
        try{
            nvoPrecio = Double.parseDouble(precio);
        }catch (NumberFormatException ex){}
        return df.format(nvoPrecio);
    }

    public String getPrecioSinCeros(){
        return precio;
    }

    public String getPrecioAnt() {
        double nvoPrecio = 0.0;
        try{
            nvoPrecio = Double.parseDouble(precioAnt);
        }catch (NumberFormatException ex){}
        return df.format(nvoPrecio);
    }

    public String getPrecioAntSinCeros(){ return precioAnt; }

    public String [] getTiulosDatos(){
        String lista = "";
        lista = lista + context.getResources().getString(R.string.column_precio);
        if (!precioAnt.isEmpty()){
            lista = lista + "|" + context.getResources().getString(R.string.column_precio_ant);
        }
        if (!ean.isEmpty()){
            lista = lista + "|" + context.getResources().getString(R.string.column_ean);
        }
        if(!datosXtras.isEmpty()){
            String xtras []= datosXtras.split("\\|");
            for(int n = 0; n < xtras.length; n++){
                if(!xtras[n].isEmpty())lista = lista + "|" + titColumnas[n];
            }
        }
        if(lista.isEmpty()){
            return null;
        }else{
            String  listArray []= lista.split("\\|");
            return listArray;
        }
    }

    public String [] getListaDatos(){
        String lista = "";
        lista = lista + "$ " + getPrecio();
        if (!precioAnt.isEmpty()){
            lista = lista + "|$ " + getPrecioAnt();
        }
        if (!ean.isEmpty()){
            lista = lista + "|" + ean;
        }
        if(!datosXtras.isEmpty()){
            String xtras []= datosXtras.split("\\|");
            for(int n = 0; n < xtras.length; n++){
                if(!xtras[n].isEmpty())lista = lista + "|"+ xtras[n];
            }
        }
        if(lista.isEmpty()){
            return null;
        }else{
            String  listArray []= lista.split("\\|");
            return listArray;
        }
    }

    public Articulo getArticulo(){
        int existencia = 0;
        try {
            existencia = Integer.parseInt(exist);
        }catch (NumberFormatException ex){}
        return new Articulo(codigo, ean, descripcion, existencia, getPrecioSinCeros(), getPrecioAntSinCeros(), urlImg, datosXtras);
    }

}
