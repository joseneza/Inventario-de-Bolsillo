package com.jose.prieto.mysmallbusinessbyneza;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.AdmSQLite;
import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.LeerArchivo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.LeerTitulos;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.OrdenColumnas;
import com.jose.prieto.mysmallbusinessbyneza.AccionAjustes.ImportarInfo;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.AdaptadorPersonalizado;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.Columna;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.ValoresColumnas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DatosImportar extends AppCompatActivity {

    TextView textView;
    ListView listView;
    ImportarInfo impInf;
    ArrayList<OrdenColumnas> ordenCol;
    ArrayList<Columna> columnas;
    MenuItem menu_item_aceptar;
    int numFArch;
    String archImportar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_importar);
        textView = (TextView) findViewById(R.id.txtDatImportar);
        listView = (ListView) findViewById(R.id.listViewImportar);
        Intent i = getIntent();
        archImportar = i.getExtras().getString(Explorar.DIRECCION_ARCHIVO);
        numFArch = i.getExtras().getInt(Explorar.TIPO_ARCHIVO);
        int [] columnPrincipal = i.getExtras().getIntArray(ColumnSelect.NUM_COLUMN);
        impInf = new ImportarInfo(this, archImportar, columnPrincipal);
        if(impInf.getTitulosOk()){
            String mensj_ini = getResources().getString(R.string.msj_selected_columns_ini);
            String mensj_fin = getResources().getString(R.string.msj_selected_columns_fin);
            textView.setText(mensj_ini + impInf.getMensajeOk() + ".\n" + mensj_fin);
            ordenCol = impInf.getOrdenColumnas(numFArch);
            columnas = ValoresColumnas.getValoresColumnas(ordenCol);
            AdaptadorPersonalizado adapter = new AdaptadorPersonalizado(this, columnas);
            listView.setAdapter(adapter);
        }else{
            textView.setText("");
            String mensj_ini = getResources().getString(R.string.msj_column_no_encontradas_ini);
            String mensj_fin = getResources().getString(R.string.msj_column_no_encontradas_fin);
            AlertDialog msgError = new AlertDialog.Builder(this).create();
            msgError.setTitle(R.string.title_column_no_encontradas);
            msgError.setMessage(mensj_ini + impInf.getErrorTitulos() + mensj_fin);
            msgError.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_aceptar),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            msgError.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aceptar, menu);
        menu_item_aceptar = (MenuItem) menu.findItem(R.id.menu_aceptar);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_aceptar:
                new AvanceCargarDatos().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        confirmarCierre();
    }

    private void confirmarCierre(){
        String mensaje = getResources().getString(R.string.msj_conf_cancel);
        final AlertDialog confCerrar = new AlertDialog.Builder(this).create();
        confCerrar.setTitle(R.string.action_new_data_base);
        confCerrar.setMessage(mensaje);
        confCerrar.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_cancel_and_exit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confCerrar.dismiss();
                        finish();
                    }
                });
        confCerrar.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_no_exit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confCerrar.dismiss();
                    }
                });
        confCerrar.show();
    }

    class AvanceCargarDatos extends AsyncTask<Void, Double, Boolean> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {//Preparacion de avance
            // TODO Auto-generated method stub
            super.onPreExecute();
            String numArticulo = "";
            pDialog = new ProgressDialog(DatosImportar.this);
            pDialog.setMessage(getResources().getString(R.string.msj_load_dat));
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setMax(100);
            pDialog.setProgress(0);
            pDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.btn_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancel(true);
                        }
                    });
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {//Hilo principal a ejecutar
            impInf.guardarTitulos(columnas, ordenCol);
            LeerArchivo leerArchivo = new LeerArchivo(archImportar, numFArch);
            if(leerArchivo.getLectuta()){
                double tamArchImprot = leerArchivo.getTamArchivo();
                double tamAvance = 0.0;
                AdmSQLite adm = new AdmSQLite(DatosImportar.this, impInf.getTitulosDB());//Se habre el adm de la bade se datos
                SQLiteDatabase db = adm.getWritableDatabase();//Se le indica que se va a escribir o leer en ella
                String [] linea = leerArchivo.getArrayLinea();
                while (linea!=null) {
                    tamAvance = tamAvance + leerArchivo.getTamLinea();
                    ContentValues cargarLinea = impInf.cargarLinea(linea, columnas);
                    if (cargarLinea != null) {
                        db.insert(ImportarInfo.ARTICULO, null, cargarLinea);
                    }
                    publishProgress(tamAvance, tamArchImprot);
                    linea = leerArchivo.getArrayLinea();
                }
                db.close();
            }
            leerArchivo.cerrar();
            return true;
        }

        protected void onProgressUpdate(Double... a){//Actualizacion de avance
            DecimalFormat df = new DecimalFormat("0");
            double avance = a[0]/a[1]*100;
            pDialog.setMessage(getResources().getString(R.string.msj_load_dat)+"\n"+df.format(a[0]/1024)+" "
                    +getResources().getString(R.string.msj_of)+" "+df.format(a[1]/1024)+" "+getResources().getString(R.string.msj_bytes));
            pDialog.setProgress((int)(avance));
        }

        @Override
        protected void onPostExecute(Boolean par) {//Metodo para finalizar la operacion
            // TODO Auto-generated method stub
            //pDialog.dismiss();
            Intent i = getIntent();
            setResult(RESULT_OK, i);
            finish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            pDialog.dismiss();
            Intent i = getIntent();
            setResult(RESULT_CANCELED, i);
            finish();
        }
    }
}
