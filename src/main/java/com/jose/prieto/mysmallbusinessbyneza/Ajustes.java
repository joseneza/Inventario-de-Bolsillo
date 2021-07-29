package com.jose.prieto.mysmallbusinessbyneza;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.AccionAjustes.ImportarInfo;
import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.AdmSQLite;
import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.LeerArchivo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.LeerTitulos;

import java.io.File;

public class Ajustes extends AppCompatActivity {

    private int opcion = 0;
    public static final int IMPORTAR_DB = 31;
    public static final String VAL_DATA_BASE = "valDataBase";
    public static final String DIR_SERVIDOR = "dirServidor";
    public static final String AGREGAR_UNIDAD = "agregar_unidad";
    private File archDB = new File(Inicio.DIRECTORY_THIS_PACKAGE_DATA_BASE, AdmSQLite.NOMBRE_BASE_DATOS);
    private File archTemp = new File(Inicio.DIRECTORY_THIS_PACKAGE_DATA_BASE, AdmSQLite.NOMBRE_BASE_DATOS + ".temp");
    private File archDB_journal = new File(Inicio.DIRECTORY_THIS_PACKAGE_DATA_BASE, AdmSQLite.NOMBRE_BASE_DATOS+"-journal");
    private File archTemp_journal = new File(Inicio.DIRECTORY_THIS_PACKAGE_DATA_BASE, AdmSQLite.NOMBRE_BASE_DATOS+"-journal.temp");
    private String titulosDataBase;
    private TextView datoModo, nvaBaseDatos, borrarBaseDatos, tituloServidor, datoservidor;
    private LinearLayout layoutServidor;
    private SharedPreferences preferencias;
    private String dirServidor = "";
    private int valDataBase = 0;
    private int tipoArch = 0;
    private Boolean valorUnidad;
    private CheckBox chckBoxUnidad;
    private String dirArchivo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        datoModo = (TextView) findViewById(R.id.text_ajuste_ModoCargaSub);
        nvaBaseDatos = (TextView) findViewById(R.id.text_ajuste_nva_data_base);
        borrarBaseDatos = (TextView) findViewById(R.id.text_ajuste_delete_data_base);
        tituloServidor= (TextView) findViewById(R.id.text_ajuste_titulo_servidor);
        datoservidor = (TextView) findViewById(R.id.text_ajuste_ip_servidor);
        layoutServidor = (LinearLayout) findViewById(R.id.lyt_servidor);
        preferencias = getSharedPreferences(Inicio.ARCH_AJUSTES, Context.MODE_PRIVATE);
        dirServidor = preferencias.getString(DIR_SERVIDOR, "");
        valDataBase = preferencias.getInt(VAL_DATA_BASE, 0);
        valorUnidad = preferencias.getBoolean(AGREGAR_UNIDAD, false);
        chckBoxUnidad = (CheckBox) findViewById(R.id.chckBoxUnidad);
        chckBoxUnidad.setChecked(valorUnidad);
        setTituloAjustes(valDataBase);
    }

    public void ajustesAgregarUnidad(View v) {
        SharedPreferences.Editor agregar = preferencias.edit();
        if(chckBoxUnidad.isChecked()){
            valorUnidad = true;
        } else {
            valorUnidad = false;
        }
        agregar.commit();
    }

    public void ajustesModoCarga(View v){
        cargarOrigenCatalogo(R.string.msj_select_cargar_catalogo).show();
    }

    public void ajustesNvaBaseDatos(View v){
        if (Inicio.existeDBase()) {
            confirmarNuevaBaseDatos().show();
        }else{
            importarInfo();
        }
    }

    public void ajustesBorrarBasedatos(View v){
        String mensaje = getResources().getString(R.string.msj_alert_conf_delete_data_base);
        final AlertDialog preguntaBorrar = new AlertDialog.Builder(this).create();
        preguntaBorrar.setTitle(R.string.title_conf_delete_data_base);
        preguntaBorrar.setMessage(mensaje);
        preguntaBorrar.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_cancel),
                new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preguntaBorrar.dismiss();
            }
        });
        preguntaBorrar.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_delete),
                new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preguntaBorrar.dismiss();
                borrarDB();
            }
        });
        preguntaBorrar.show();
    }

    public void ajusteIpServidor(View v){
        Recursos.mostrarMensaje(this, R.string.msj_servidor_desarrollo);
    }


    public void onBackPressed(){
        if (preferencias.getString(DIR_SERVIDOR, "") != dirServidor ||
                preferencias.getInt(VAL_DATA_BASE, 0) != valDataBase ||
                preferencias.getBoolean(AGREGAR_UNIDAD, false) != valorUnidad) {
            final AlertDialog confSalir = new AlertDialog.Builder(this).create();
            confSalir.setTitle(getTitle());
            confSalir.setMessage(getResources().getString(R.string.msj_confirm_out_settings));
            confSalir.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_save_and_exit),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confSalir.dismiss();
                            guardarPreferencias();
                            finish();
                        }
                    });
            confSalir.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_exit_without_saving),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confSalir.dismiss();
                            finish();
                        }
                    });
            confSalir.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.btn_no_exit),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            confSalir.dismiss();
                        }
                    });
            confSalir.show();
        } else {
            finish();
        }

    }

    private void guardarPreferencias(){
        SharedPreferences.Editor grabarTitulos = preferencias.edit();
        grabarTitulos.putInt(VAL_DATA_BASE, valDataBase);
        grabarTitulos.putString(DIR_SERVIDOR, dirServidor);
        grabarTitulos.putBoolean(AGREGAR_UNIDAD, valorUnidad);
        grabarTitulos.commit();
    }
    /* Cargar lista de ajustes */
    private void setTituloAjustes(final int valDataBase){
        switch (valDataBase){
            case 0:
                datoModo.setText(R.string.str_archivo_local);
                layoutServidor.setEnabled(false);
                nvaBaseDatos.setEnabled(true);
                borrarBaseDatos.setEnabled(true);
                nvaBaseDatos.setTextColor(ContextCompat.getColor(Ajustes.this, R.color.colorText));
                borrarBaseDatos.setTextColor(ContextCompat.getColor(Ajustes.this, R.color.colorText));
                tituloServidor.setTextColor(ContextCompat.getColor(Ajustes.this, R.color.colorText_disable));
                datoservidor.setTextColor(ContextCompat.getColor(Ajustes.this, R.color.colorText_disable));
                break;
            case 1:
                datoModo.setText(R.string.str_servidor);
                layoutServidor.setEnabled(true);
                nvaBaseDatos.setEnabled(false);
                borrarBaseDatos.setEnabled(false);
                nvaBaseDatos.setTextColor(ContextCompat.getColor(Ajustes.this, R.color.colorText_disable));
                borrarBaseDatos.setTextColor(ContextCompat.getColor(Ajustes.this, R.color.colorText_disable));
                tituloServidor.setTextColor(ContextCompat.getColor(Ajustes.this, R.color.colorText));
                datoservidor.setTextColor(ContextCompat.getColor(Ajustes.this, R.color.colorText_sub));
                break;
        }
    }

    private AlertDialog cargarOrigenCatalogo (int titulo){
        CharSequence [] seleccion = {
                getResources().getString(R.string.str_archivo_local),
                getResources().getString(R.string.str_servidor)};
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(titulo);
        alertDialog.setItems(seleccion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                valDataBase = item;
                setTituloAjustes(valDataBase);
                dialog.dismiss();
            }
        });
        return alertDialog.create();
    }

    /* Cargar o borrar Catalogo*/

    private void importarInfo(){
        CharSequence [] seleccion = {
                getResources().getString(R.string.msj_line_separated_file),
                getResources().getString(R.string.msj_tab_separated_file),
                getResources().getString(R.string.msj_comma_separated_file)};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.action_new_data_base);
        alertDialog.setItems(seleccion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                renombrarDB();
                tipoArch = item;
                opcion = Inicio.NVA_BASE;
                Intent nvaBase = new Intent(Ajustes.this, Explorar.class);
                nvaBase.putExtra(Inicio.OPCION, Inicio.NVA_BASE);
                nvaBase.putExtra(Explorar.TIPO_ARCHIVO, item);
                startActivityForResult(nvaBase, Inicio.NVA_BASE);
                dialog.dismiss();
            }
        });
        alertDialog.setNeutralButton(getResources().getText(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create();
        alertDialog.show();

    }

    private AlertDialog confirmarNuevaBaseDatos(){
        final AlertDialog confImport = new AlertDialog.Builder(this).create();
        confImport.setTitle(R.string.action_new_data_base);
        confImport.setMessage(getResources().getString(R.string.msj_data_base_exists));
        confImport.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confImport.dismiss();
                        importarInfo();
                    }
                });
        confImport.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confImport.dismiss();
                    }
                });
        return confImport;
    }


    private void renombrarDB(){
        try{
            if(archDB.exists()) archDB.renameTo(archTemp);
            if(archDB_journal.exists()) archDB_journal.renameTo(archTemp_journal);
            titulosDataBase = preferencias.getString(LeerTitulos.DATOS_EXTRAS, "");
        }catch (Exception e){}
    }

    private void restaurarDB(){
        try {
            if(archTemp.exists()) archTemp.renameTo(archDB);
            if(archTemp_journal.exists()) archTemp_journal.renameTo(archDB_journal);
            SharedPreferences.Editor grabarTitulos = preferencias.edit();
            grabarTitulos.putString(LeerTitulos.DATOS_EXTRAS, titulosDataBase);
            grabarTitulos.commit();
        }catch (Exception e){}
    }

    private void borrarDB(){
        int borrar = 0;
        if(archDB.exists()) if(archDB.delete()) borrar++;
        if(archDB_journal.exists()) if(archDB_journal.delete()) borrar++;
        SharedPreferences.Editor grabarTitulos = preferencias.edit();
        grabarTitulos.putString(LeerTitulos.DATOS_EXTRAS, "");
        grabarTitulos.commit();
        if(borrar==3) {
            Recursos.mostrarMensaje(this, R.string.msj_delete_data_base_ok);
        }
    }

    private void borrarTempDB(){
        int borrar = 0;
        if(archTemp.exists()) if(archTemp.delete()) borrar++;
        if(archTemp_journal.exists()) if(archTemp_journal.delete()) borrar++;
        if(borrar==2) Recursos.mostrarMensaje(this, R.string.msj_delete_file_tmp_ok);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (opcion) {
                case Inicio.NVA_BASE:
                    dirArchivo = data.getExtras().getString(Explorar.DIRECCION_ARCHIVO);
                    String [] lineaTitulos = LeerArchivo.getPrimeraLinea(dirArchivo, tipoArch);
                    if(lineaTitulos.length>0){
                        Intent selecColumn = new Intent(Ajustes.this, ColumnSelect.class);
                        selecColumn.putExtra(Inicio.OPCION, Inicio.NVA_BASE);
                        selecColumn.putExtra(ColumnSelect.TITULOS, lineaTitulos);
                        opcion = IMPORTAR_DB;
                        startActivityForResult(selecColumn, opcion);
                    }else{
                        Recursos.mostrarMensaje(this, R.string.msj_no_column_titles_found);
                    }
                    break;
                case IMPORTAR_DB:
                    if (new File(dirArchivo).exists()) {
                        int [] columnPrincipal = data.getExtras().getIntArray(ColumnSelect.NUM_COLUMN);
                        Intent importInfo = new Intent(Ajustes.this, DatosImportar.class);
                        importInfo.putExtra(ColumnSelect.NUM_COLUMN, columnPrincipal);
                        importInfo.putExtra(Explorar.DIRECCION_ARCHIVO, dirArchivo);
                        importInfo.putExtra(Explorar.TIPO_ARCHIVO, tipoArch);
                        opcion = Inicio.NVA_BASE_CONF;
                        startActivityForResult(importInfo, opcion);
                    }
                    break;
                case Inicio.NVA_BASE_CONF:
                    String title;
                    String msj;
                    if(Inicio.existeDBase()){
                        title = getResources().getString(R.string.title_importar_informacion);
                        msj = getResources().getString(R.string.msj_carga_exitosa);
                        borrarTempDB();
                    }else{
                        borrarDB();
                        restaurarDB();
                        title = getResources().getString(R.string.title_importar_informacion);
                        msj = getResources().getString(R.string.msj_cargar_error);
                    }
                    Recursos.mensajeDialog(this, title, msj, getResources().getString(R.string.btn_aceptar)).show();
                    break;
            }
        }else {
            borrarDB();
            restaurarDB();
            Recursos.mostrarMensaje(this, R.string.msj_cancel_oper);
        }
    }


}
