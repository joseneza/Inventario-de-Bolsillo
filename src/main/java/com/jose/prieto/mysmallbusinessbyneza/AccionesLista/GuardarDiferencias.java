package com.jose.prieto.mysmallbusinessbyneza.AccionesLista;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.BuscarArticulo;
import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.GuardarArchivo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;
import com.jose.prieto.mysmallbusinessbyneza.MostrarLista;
import com.jose.prieto.mysmallbusinessbyneza.R;

import java.io.File;

/**
 * Created by jose on 21/10/16.
 */

public class GuardarDiferencias extends AsyncTask<String, Void, Boolean>{
    Context context;
    int opcion;
    ProgressDialog pDialog;
    GuardarArchivo guardarArchivo;
    String dir;
    int tipoArch;
    int tamañoTotal;
    int tamañoAvance = 0;
    public GuardarDiferencias(Context context, int opcion, String dir, int tipoArch){
        this.context = context;
        this.opcion = opcion;
        this.dir = dir;
        this.tipoArch = tipoArch;
    }
    @Override
    protected void onPreExecute() {//Preparacion de avance
        // TODO Auto-generated method stub
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getResources().getString(R.string.msj_saving_differences));
        pDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.btn_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancel(true);
                    }
                });
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMax(100);
        pDialog.setProgress(0);
        pDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        guardarArchivo = new GuardarArchivo(dir, tipoArch, false);
        if (guardarArchivo.getNvoArchivo()) {
            String [] columnArt = strings[0].split("\\|");
            String [] columnLista = strings[1].split("\\|");
            String [] columnComparador = strings[2].split("\\|");
            String [] columnDif = strings[3].split("\\|");
            String [] columnRef = strings[4].split("\\|");
            tamañoTotal = columnArt.length;
            guardarArchivo.escribirLinea(context.getResources().getString(R.string.column_art),
                context.getResources().getString(R.string.column_lista), context.getResources().getString(R.string.column_comparado),
                context.getResources().getString(R.string.column_dif), context.getResources().getString(R.string.column_descrip),
                context.getResources().getString(R.string.column_reference));
            for (int n=0; n<columnArt.length; n++) {
                tamañoAvance++;
                String descrip = context.getResources().getString(R.string.msj_article_no_description);
                String lista = "";
                String comparador = "";
                String dif = "";
                String ref = "";
                if(columnLista.length>n) lista = columnLista[n];
                if(columnComparador.length>n) comparador = columnComparador[n];
                if(columnDif.length>n) dif = columnDif[n];
                if(columnRef.length>n) ref = columnRef[n];
                BuscarArticulo busqArt = new BuscarArticulo(context, Long.parseLong(columnArt[n]));
                if(busqArt.buscar) descrip = busqArt.getDescripcion();
                guardarArchivo.escribirLinea(columnArt[n], lista, comparador, dif, descrip, ref);
                publishProgress();
            }
            tamañoAvance++;
            publishProgress();
            guardarArchivo.cerrarArchivo();
            return true;
        } else {
            return false;
        }
    }

    protected void onProgressUpdate(Void... avance){//Actualizacion de avance
        int nvoAvance = (int) ((tamañoAvance/tamañoTotal)*100);
        pDialog.setProgress(nvoAvance);
    }
    @Override
    protected void onPostExecute(Boolean par) {//Metodo al finalizar la operacion
        pDialog.dismiss();
        if(par){
            if(opcion == MostrarLista.GUARDAR_DIF_COMPARAR){
                Recursos.mostrarMensaje(context, context.getResources().getString(R.string.msj_save_file_ok)+" "+dir);
            } else {
                if(opcion == MostrarLista.COMPARTIR_DIF_COMPARAR){
                    pDialog.dismiss();
                    Intent compartir = new Intent(Intent.ACTION_SEND);
                    compartir.setType("text/" + tipoArch);
                    compartir.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(dir)));
                    context.startActivity(Intent.createChooser(compartir, context.getResources().getString(R.string.action_share)));
                }
            }
        } else {
            Recursos.mostrarMensaje(context, context.getResources().getString(R.string.msj_error_save_dif));
        }
    }

    @Override
    protected void onCancelled(){
        pDialog.dismiss();
        Recursos.mostrarMensaje(context, R.string.msj_cancel_oper);
    }
}
