package com.jose.prieto.mysmallbusinessbyneza;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.GuardarArchivo;
import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.LeerArchivo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Articulo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.LeerTitulos;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.AdaptadorPersonalizado;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.Columna;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.ValoresColumnas;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DatosExportar extends AppCompatActivity {


    LeerTitulos titulos;
    int opcion;
    int tipoArc = 0;
    ArrayList<Columna> columnas;
    ListView listView;
    MenuItem menu_item_aceptar;
    CheckBox ckboxConsentrado;
    CheckBox ckboxTipoPrecio;
    RadioButton rdBtnTxtLineaV;
    RadioButton rdBtnTxtTab;
    RadioButton rdBtnCvs;
    Boolean consentrado = false;
    Boolean tipoPrecio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_exportar);
        Intent intent = getIntent();
        opcion = intent.getExtras().getInt(Inicio.OPCION);
        if(opcion == Inicio.COMPARTIR_LISTA){
            setTitle(R.string.action_share);
        }
        titulos = new LeerTitulos(this);
        ckboxConsentrado = (CheckBox) findViewById(R.id.chBoxExportarConsentrado);
        ckboxTipoPrecio = (CheckBox) findViewById(R.id.chBoxExportarTipoPrecio);
        if(!titulos.getPrecio()) ckboxTipoPrecio.setVisibility(View.INVISIBLE);
        rdBtnTxtLineaV = (RadioButton) findViewById(R.id.radBtnTxtLineaV);
        rdBtnTxtTab = (RadioButton) findViewById(R.id.radBtnTxtTab);
        rdBtnCvs = (RadioButton) findViewById(R.id.radBtncsv);
        listView= (ListView) findViewById(R.id.listViewExportar);
        columnas = agregarTitulos();
        final AdaptadorPersonalizado adapter = new AdaptadorPersonalizado(this, columnas);
        listView.setAdapter(adapter);
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
                exportarDatos();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Columna> agregarTitulos(){
        return ValoresColumnas.getValoresColumnas(titulos);
    }
    @Override
    public void onBackPressed(){
        finalizarActivity();
    }

    public void btnCkboxConsentrado(View v){
        if(ckboxConsentrado.isChecked()){
            consentrado = true;
        } else {
            consentrado = false;
        }
    }

    public void btnCkboxTipoPrecio(View v){
        if(ckboxTipoPrecio.isChecked()){
            tipoPrecio = true;
        } else {
            tipoPrecio = false;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void exportarDatos(){
        if(rdBtnTxtLineaV.isChecked()){
            tipoArc = Explorar.TIPO_ARCH_LINEA;
        }else{
            if(rdBtnTxtTab.isChecked()){
                tipoArc = Explorar.TIPO_ARCH_TAB;
            }else{
                if(rdBtnCvs.isChecked()){
                    tipoArc = Explorar.TIPO_ARCH_COMA;
                }
            }
        }
        if(opcion == Inicio.EXPORTAR_ARCHIVO){
            Intent explorar = new Intent(DatosExportar.this, Explorar.class);
            explorar.putExtra(Inicio.OPCION, opcion);
            explorar.putExtra("tipoArch", tipoArc);
            startActivityForResult(explorar, opcion);
        }else{
            if(opcion == Inicio.COMPARTIR_LISTA){
                final AlertDialog nombrArchivo = new AlertDialog.Builder(this).create();
                final EditText nombre = new EditText(this);
                nombre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                nombre.setHint(R.string.msj_filename);
                nombrArchivo.setTitle(R.string.action_share);
                nombrArchivo.setMessage(getResources().getString(R.string.msj_filename_to_share));
                nombrArchivo.setView(nombre);
                nombrArchivo.setCancelable(false);
                nombrArchivo.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_share),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (nombre.getText().toString().length() > 0) {
                                    if (nombre.getText().toString().equals(R.string.str_list_filename)) {
                                        Recursos.mostrarMensaje(DatosExportar.this, R.string.msj_different_name);
                                    } else {
                                        nombrArchivo.dismiss();
                                        String tipoArch = "";
                                        switch (tipoArc) {
                                            case Explorar.TIPO_ARCH_LINEA:
                                                tipoArch = Explorar.TIPO_TEXTO;
                                                break;
                                            case Explorar.TIPO_ARCH_TAB:
                                                tipoArch = Explorar.TIPO_TEXTO;
                                                break;
                                            case Explorar.TIPO_ARCH_COMA:
                                                tipoArch = Explorar.TIPO_COMA;
                                                break;
                                        }
                                        String dir = Inicio.DIRECTORIO_PAQUETE_EXTERNO + "/" + nombre.getText().toString() +
                                                "." + tipoArch;
                                        ExportarLista exportarLista = new ExportarLista();
                                        exportarLista.execute(dir);
                                    }
                                }
                            }
                        });
                nombrArchivo.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nombrArchivo.dismiss();
                            }
                        });
                nombrArchivo.show();

            }
        }

    }



    private void finalizarActivity(){
        final AlertDialog confCerrar = new AlertDialog.Builder(this).create();
        confCerrar.setTitle(getTitle());
        confCerrar.setMessage(getResources().getString(R.string.msj_conf_cancel));
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            Recursos.mostrarMensaje(this, R.string.msj_cancel_oper);
        }else{
            String dir = data.getExtras().getString(Explorar.DIRECCION_ARCHIVO);
            ExportarLista exportarLista = new ExportarLista();
            exportarLista.execute(dir);
        }
    }

    class ExportarLista extends AsyncTask<String, Integer, Boolean> {
        //AsyncTask para crear un avance de busqueda
        ProgressDialog pDialog;
        String msjError;
        File archExportar;

        @Override
        protected void onPreExecute() {//Preparacion de avance
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(DatosExportar.this);
            pDialog.setMessage(getResources().getString(R.string.msj_find_relation));
            pDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_cancel),
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
        protected Boolean doInBackground(String... dir) {//Metodo para la operacion
            ArrayList<Articulo> arrayArticulo = new ArrayList<Articulo>();
            LeerArchivo leerLista = new LeerArchivo(Inicio.ARCHLISTA.getAbsolutePath(), 0);
            if(leerLista.getLectuta()){
                double tamArchivo = leerLista.getTamArchivo();
                double tamAvance = 0.0;
                String [] articulo = leerLista.getArrayLinea();
                while (articulo!=null) {
                    publishProgress((int)(tamAvance/tamArchivo*70), R.string.msj_reading_list);
                    if(Recursos.esNumero(articulo[0])){
                        arrayArticulo.add(Recursos.cargarArticulo(DatosExportar.this, articulo));
                    }
                    tamAvance = tamAvance + leerLista.getTamLinea();
                    articulo = leerLista.getArrayLinea();
                }
                if(consentrado){
                    publishProgress((int)(tamAvance/tamArchivo*70), R.string.msj_concentrating_list);
                    arrayArticulo = Recursos.consentrarLista(arrayArticulo);
                }
                if(arrayArticulo.size()>0){
                    GuardarArchivo guardarlista = new GuardarArchivo(dir[0], tipoArc, false);
                    if(guardarlista.getNvoArchivo()){
                        archExportar = new File(dir[0]);
                        guardarlista.escribirLinea(Recursos.cargarTitulos(DatosExportar.this, columnas, titulos,
                                tipoPrecio));
                        for (int n=0; n<arrayArticulo.size(); n++){
                            publishProgress((int)(70+(n/arrayArticulo.size()*30)), R.string.msj_saving_lista);
                            guardarlista.escribirLinea(Recursos.cargarLinea(DatosExportar.this, arrayArticulo.get(n),
                                    columnas, titulos, tipoPrecio));
                        }
                        guardarlista.cerrarArchivo();
                    } else {
                        msjError = guardarlista.getMsjError();
                    }
                } else {
                    msjError = getResources().getString(R.string.msj_not_read_list_of_items);
                }
                leerLista.cerrar();
            } else {
                msjError = leerLista.getMsjError();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... avance){//Actualizacion de avance
            pDialog.setMessage(getResources().getString(avance[1]));
            pDialog.setProgress(avance[0]);
        }
        @Override
        protected void onPostExecute(Boolean par) {//Metodo al finalizar la operacion
            pDialog.dismiss();
            if(par){
                switch (opcion){
                    case Inicio.COMPARTIR_LISTA:
                        Intent compartir = new Intent(Intent.ACTION_SEND);
                        compartir.setType("text/" + Explorar.getTipoArchivo(tipoArc));
                        compartir.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(archExportar));
                        startActivity(Intent.createChooser(compartir, getString(R.string.action_share)));
                        break;
                    case Inicio.EXPORTAR_ARCHIVO:
                        Recursos.mostrarMensaje(DatosExportar.this, getResources().getString(R.string.msj_save_file_ok)+" "+
                                archExportar.getAbsolutePath());
                        break;
                }
            } else {
                Recursos.mostrarMensaje(DatosExportar.this, msjError);
            }
            finish();
        }

        @Override
        protected void onCancelled(){
            pDialog.dismiss();
        }
    }
}
