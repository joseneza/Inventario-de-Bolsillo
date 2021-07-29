package com.jose.prieto.mysmallbusinessbyneza.AccionAjustes;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.LeerArchivo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.LeerTitulos;
import com.jose.prieto.mysmallbusinessbyneza.Explorar;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.OrdenColumnas;
import com.jose.prieto.mysmallbusinessbyneza.Inicio;
import com.jose.prieto.mysmallbusinessbyneza.R;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.Columna;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Jose on 03/03/2016.
 * Esta Clase sirve para Ingresar una base de datos nueva
 * o para actualizarla
 */
public class ImportarInfo {
    private String dirArch;
    private Context context;
    private ArrayList<OrdenColumnas> titulo;
    private String arrayTitulos[];
    private String numArticulo = "";
    private String titulosDB = "";
    private int sku = -1, ean = -1, descr = -1, exist = -1, precio = -1, precioAnt = -1, urlArt = -1;
    private Boolean titulosOk = false;
    private String mensajeError, mensajeOk;
    public static String ARTICULO = "articulos";
    //
    private File archTit = new File(Inicio.DIRECTORY_THIS_PACKAGE_DATA_BASE, "titulos.dat");

    public ImportarInfo(Context context, String dirArch, int [] columnPrincipal){
        //Constructor para una nva Base de datos
        this.context = context;
        this.dirArch = dirArch;
        mensajeError = "";
        mensajeOk = "";
        sku = columnPrincipal[0];
        if (sku>-1) mensajeOk = " " + context.getResources().getString(R.string.column_art) + mensajeOk;
        exist = columnPrincipal[1];
        if (exist>-1) mensajeOk = " " + context.getResources().getString(R.string.column_cant) + mensajeOk;
        descr = columnPrincipal[2];
        if (descr>-1) mensajeOk = " " + context.getResources().getString(R.string.column_descrip) + mensajeOk;
        ean = columnPrincipal[3];
        if (ean>-1) mensajeOk = " " + context.getResources().getString(R.string.column_ean) + mensajeOk;
        urlArt = columnPrincipal[4];
        if (urlArt>-1) mensajeOk = " " + context.getResources().getString(R.string.column_imgUrl) + mensajeOk;
        precioAnt = columnPrincipal[5];
        if (precioAnt>-1) mensajeOk = " " + context.getResources().getString(R.string.column_precio_ant) + mensajeOk;
        precio = columnPrincipal[6];
        if (precio>-1) mensajeOk = " " + context.getResources().getString(R.string.column_precio) + mensajeOk;
        if(sku > -1 && descr > -1){
            titulosOk = true;
        }

    }

    public ArrayList<OrdenColumnas> getOrdenColumnas(int tipoArch){
        //Toma la primera linea del archivo para crear los titulos de las columnas
        ArrayList<OrdenColumnas> ordenColumnas = new ArrayList<OrdenColumnas>();
        arrayTitulos = LeerArchivo.getPrimeraLinea(dirArch, tipoArch);
        if(arrayTitulos!=null){
            for(int n = 0; arrayTitulos.length > n; n++) {
                if (!arrayTitulos[n].isEmpty() && n!=sku && n!=exist && n!=descr && n!=ean && n!=urlArt
                        && n!=precioAnt && n!=precio) {
                    String tituloDB = "";
                    char caracteres[] = arrayTitulos[n].toCharArray();
                    for (int num = 0; num < caracteres.length; num++) {
                        if (caracteres[num] >= 48 && caracteres[num] <= 57 ||
                                caracteres[num] >= 64 && caracteres[num] <= 90 ||
                                caracteres[num] >= 97 && caracteres[num] <= 122 ) {
                            tituloDB = tituloDB + caracteres[num];
                        } else {
                            if (caracteres[num] >= 192 && caracteres[num] <= 197) {
                                tituloDB = tituloDB + "A";
                            } else {
                                if (caracteres[num] >= 200 && caracteres[num] <= 203) {
                                    tituloDB = tituloDB + "E";
                                } else {
                                    if (caracteres[num] >= 204 && caracteres[num] <= 207) {
                                        tituloDB = tituloDB + "I";
                                    } else {
                                        if (caracteres[num] >= 210 && caracteres[num] <= 214) {
                                            tituloDB = tituloDB + "O";
                                        } else {
                                            if (caracteres[num] >= 217 && caracteres[num] <= 220) {
                                                tituloDB = tituloDB + "U";
                                            } else {
                                                if (caracteres[num] >= 224 && caracteres[num] <= 229) {
                                                    tituloDB = tituloDB + "a";
                                                } else {
                                                    if (caracteres[num] >= 232 && caracteres[num] <= 235) {
                                                        tituloDB = tituloDB + "e";
                                                    } else {
                                                        if (caracteres[num] >= 236 && caracteres[num] <= 239) {
                                                            tituloDB = tituloDB + "i";
                                                        } else {
                                                            if (caracteres[num] >= 242 && caracteres[num] <= 246) {
                                                                tituloDB = tituloDB + "o";
                                                            } else {
                                                                if (caracteres[num] >= 249 && caracteres[num] <= 252) {
                                                                    tituloDB = tituloDB + "u";
                                                                } else {
                                                                    if (caracteres[num] == 209) {
                                                                        tituloDB = tituloDB + "N";
                                                                    } else {
                                                                        if (caracteres[num] == 241) {
                                                                            tituloDB = tituloDB + "n";
                                                                        } else {
                                                                            tituloDB = tituloDB + "_";
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ordenColumnas.add(new OrdenColumnas(tituloDB, n));
                }
            }
        } else {
            Recursos.mensajeDialog(context, context.getResources().getString(R.string.action_new_data_base),
                    context.getResources().getString(R.string.msj_cargar_error),context.getResources().getString(R.string.btn_aceptar));
        }
        titulo = ordenColumnas;
        return ordenColumnas;
    }

    public Boolean getTitulosOk(){
        //Regresa si los titulos principales son correctos
        return titulosOk;
    }

    public void guardarTitulos(ArrayList<Columna> columnas, ArrayList<OrdenColumnas> ordenColumnas){//Metodo para una base de datos nueva
        String titulos = "";
        SharedPreferences arcTitulos = context.getSharedPreferences(Inicio.ARCH_AJUSTES, Context.MODE_PRIVATE);
        SharedPreferences.Editor grabarTitulos = arcTitulos.edit();
        for(int n=0; n<titulo.size(); n++){
            if(columnas.get(n).getchecked()){
                if(titulos.length()!=0) titulos = titulos + "|";
                titulos = titulos + titulo.get(n).getNombre();
                titulosDB = titulosDB + ", " + titulo.get(n).getNombre() + " text";
            }
        }
        if(precio >= 0){
            grabarTitulos.putBoolean(LeerTitulos.PRECIO, true);
        }else{
            grabarTitulos.putBoolean(LeerTitulos.PRECIO, false);
        }
        if(precioAnt >= 0){
            grabarTitulos.putBoolean(LeerTitulos.PRECIO_ANT, true);
        }else{
            grabarTitulos.putBoolean(LeerTitulos.PRECIO_ANT, false);
        }
        if(ean >= 0){
            grabarTitulos.putBoolean(LeerTitulos.EAN, true);
        }else{
            grabarTitulos.putBoolean(LeerTitulos.EAN, false);
        }
        if(urlArt >= 0){
            grabarTitulos.putBoolean(LeerTitulos.URL_IMG, true);
        }else{
            grabarTitulos.putBoolean(LeerTitulos.URL_IMG, false);
        }
        grabarTitulos.putString(LeerTitulos.DATOS_EXTRAS, titulos);
        grabarTitulos.commit();
    }

    public String getTitulosDB(){
        return titulosDB;
    }
    public String getNumArticulo(){
        return numArticulo;
    }

    public ContentValues cargarLinea(String [] lineaArray, ArrayList<Columna> columnas){
        ContentValues contentValues = null;
        if (Recursos.esNumero(lineaArray[sku])){
            contentValues = new ContentValues();
            numArticulo = lineaArray[sku];
            contentValues.put("codigo", lineaArray[sku]);
            contentValues.put("descripcion", lineaArray[descr].toUpperCase());
            contentValues.put("existencia", lineaArray[exist]);
            if(precio > -1 && precio < lineaArray.length) {
                contentValues.put("precio", lineaArray[precio]);
            }else{
                contentValues.put("precio", "");
            }
            if(precioAnt > -1 && precioAnt < lineaArray.length) {
                contentValues.put("precio_anterior", lineaArray[precioAnt]);
            }else{
                contentValues.put("precio_anterior", "");
            }
            if(ean > -1 && ean < lineaArray.length) {
                if(Recursos.esNumero(lineaArray[ean])){
                    contentValues.put("ean", lineaArray[ean]);
                }else {
                    contentValues.put("ean", "");
                }
            } else {
                contentValues.put("ean", "");
            }
            if(urlArt > -1 && urlArt < lineaArray.length) {
                contentValues.put("urlArt", lineaArray[urlArt]);
            } else {
                contentValues.put("urlArt", "");
            }
            for(int n=0; n<titulo.size(); n++){
                if(columnas.get(n).getchecked()){
                    if(titulo.get(n).getConsecutivo()<lineaArray.length){
                        contentValues.put(columnas.get(n).getNombre(), lineaArray[titulo.get(n).getConsecutivo()].toUpperCase());
                    }else{
                        contentValues.put(columnas.get(n).getNombre(), "");
                    }
                }
            }
        }
        return contentValues;
    }

    public String getErrorTitulos(){
        return mensajeError;
    }

    public String getMensajeOk() { return mensajeOk; }

}
