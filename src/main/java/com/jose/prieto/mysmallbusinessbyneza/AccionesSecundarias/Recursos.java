package com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.BuscarArticulo;
import com.jose.prieto.mysmallbusinessbyneza.DatosExportar;
import com.jose.prieto.mysmallbusinessbyneza.R;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.Columna;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Jose on 23/09/2016.
 */
public class Recursos {

    private static DecimalFormat df = new DecimalFormat("0.00");

    /*Mensaje normal de aviso*/
    public static AlertDialog mensajeDialog(Context context, String titulo, String mensaje, String boton){
        final AlertDialog alertDialog =new AlertDialog.Builder(context).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(mensaje);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, boton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

    public static Boolean esNumero(String num){
        try{
            Long.parseLong(num);
            return true;
        }catch (NumberFormatException ex){
            return false;
        }
    }

    public static ArrayList<Articulo> consentrarLista(ArrayList<Articulo> parConsentrar){
        Comparator<Articulo> compMarb=new Comparator<Articulo>() {
            public int compare(Articulo o1, Articulo o2) {
                return new Long(o1.getSku()).compareTo(o2.getSku());
            }
        };
        Collections.sort(parConsentrar, compMarb);
        ArrayList<Articulo> concent = new ArrayList<Articulo>();
        Iterator<Articulo> revArt = parConsentrar.iterator();
        Articulo linAgr = null;
        while (revArt.hasNext()) {
            Articulo linea=revArt.next();
            if(linAgr==null){
                linAgr=linea;
            }else{
                if(linea.getSku() == linAgr.getSku()){
                    linAgr.setExist(linAgr.getExist() + linea.getExist());
                }else{
                    concent.add(linAgr);
                    linAgr=linea;
                }
            }
        }
        if(linAgr!=null) concent.add(linAgr);
        return concent;
    }

    public static Articulo cargarArticulo(Context contexts, String [] articulo){
        Articulo art = new Articulo();
        BuscarArticulo buscarArt = new BuscarArticulo(contexts, Long.parseLong(articulo[0]));
        if(buscarArt.buscar){
            art = buscarArt.getArticulo();
            int exist = 0;
            try{
                exist = Integer.parseInt(articulo[1]);
            }catch (NumberFormatException ex){}
            art.setExist(exist);
        }else {
            art.setSku(Long.parseLong(articulo[0]));
            int exist = 0;
            try{
                exist = Integer.parseInt(articulo[1]);
            }catch (NumberFormatException ex){}
            art.setExist(exist);
            art.setDescipcion(contexts.getResources().getString(R.string.msj_article_no_description));
        }
        return art;
    }

    public static String [] cargarTitulos(Context context, ArrayList<Columna> datUsar,
                                          LeerTitulos titulos, Boolean tipoPrecio){
        String linea = context.getResources().getString(R.string.column_art);
        linea = linea + "|" + context.getResources().getString(R.string.column_cant);
        linea = linea + "|" + context.getResources().getString(R.string.column_descrip);
        int consc = 0;
        if(titulos.getPrecio()){
            if(datUsar.get(consc).getchecked()){
                linea = linea + "|" + context.getResources().getString(R.string.column_precio_ant);
                linea = linea + "|" + context.getResources().getString(R.string.column_precio_act);
            }
            consc++;
        }
        if(titulos.getEan()){
            if(datUsar.get(consc).getchecked()) linea = linea + "|" +
                    context.getResources().getString(R.string.column_ean);
            consc++;
        }
        for(int n = consc; n < datUsar.size(); n++){
            if(datUsar.get(n).getchecked()) linea = linea + "|" + datUsar.get(n).getNombre().replace('_',' ');
        }
        if(tipoPrecio)linea = linea + "|" + context.getResources().getString(R.string.column_tipo_precio);
        return linea.split("\\|");
    }

    public static String [] cargarLinea(Context context, Articulo art, ArrayList<Columna> datUsar,
                                        LeerTitulos titulos, Boolean tipoPrecio){
        String linea = "";
        linea = String.valueOf(art.getSku());
        linea = linea + "|" + art.getExist();
        linea = linea + "|" + art.getDescipcion();
        int consc = 0;
        if(titulos.getPrecio()){
            if(datUsar.get(consc).getchecked()) {//Se carga precio
                if(art.getPrecioAnt().isEmpty()){
                    linea = linea + "|";
                }else {
                    double nvoPrecioAnt = 0.0;
                    try{
                        nvoPrecioAnt = Double.parseDouble(art.getPrecioAnt());
                    }catch (NumberFormatException ex){}
                    linea = linea + "|" + "$" + df.format(nvoPrecioAnt);
                }
                if(art.getPrecio().isEmpty())art.setPrecio("0.00");
                double nvoPrecio = 0.0;
                try{
                    nvoPrecio = Double.parseDouble(art.getPrecio());
                }catch (NumberFormatException ex){}
                linea = linea + "|" + "$"  + df.format(nvoPrecio);
            }
            consc++;
        }
        if(titulos.getEan()){
            if(datUsar.get(consc).getchecked())linea = linea + "|" + art.getEan();
            consc++;
        }
        String datXtras[] = {};
        int xt = 0;
        if(!art.getDatosXtras().isEmpty()) datXtras = art.getDatosXtras().split("\\|");
        for(int n = consc; n < datUsar.size(); n++){
            String valor = "";
            if(datUsar.get(n).getchecked()) {
                if(xt < datXtras.length) valor =datXtras[xt];
                linea = linea + "|" + valor;
            }
            xt++;
        }
        if(tipoPrecio){
            int precioAnt = 0;
            int precio = 0;
            try{
                precioAnt = Integer.parseInt(art.getPrecioAnt());
            }catch (NumberFormatException ex){}
            try{
                precio = Integer.parseInt(art.getPrecio());
            }catch (NumberFormatException ex){}
            if (precioAnt > precio) {
                linea = linea + "|" + context.getResources().getString(R.string.column_tipo_precio_rebaja);
            }else{
                linea = linea + "|" + context.getResources().getString(R.string.column_tipo_precio_normal);
            }
        }
        return linea.split("\\|");
    }



    public static void mostrarMensajelong(Context context, String mensaje){
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    public static void mostrarMensajelong(Context context, int mensaje){
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    public static void mostrarMensaje(Context context, String mensaje){
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }

    public static void mostrarMensaje(Context context, int mensaje){
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
}
