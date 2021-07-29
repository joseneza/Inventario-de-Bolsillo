package com.jose.prieto.mysmallbusinessbyneza;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.GuardarArchivo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesLista.AdaptadorCompararLista;
import com.jose.prieto.mysmallbusinessbyneza.AccionesLista.AdaptadorEditarLista;
import com.jose.prieto.mysmallbusinessbyneza.AccionesLista.AdaptadorMostrarLista;
import com.jose.prieto.mysmallbusinessbyneza.AccionesLista.GuardarDiferencias;
import com.jose.prieto.mysmallbusinessbyneza.AccionesLista.LineaDLista;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MostrarLista extends AppCompatActivity {
    public static final int GUARDAR_DIF_COMPARAR = 16;
    public static final int COMPARTIR_DIF_COMPARAR = 21;
    public static final String COLUMN_ART = "articulo";
    public static final String COLUMN_LISTA = "lista";
    public static final String COLUMN_COMPARADOR = "comparador";
    public static final String COLUMN_DIFERENCIA = "diferencia";
    public static final String COLUMN_REFERENCIA = "referencia";
    private File archLista = Inicio.ARCHLISTA;
    private ArrayList<LineaDLista> listaArt = new ArrayList<LineaDLista>();
    private ListView lista;
    private int opcion;
    private int totLista;
    private MenuItem editar_lista, guardar_cambios, compartir_lista, guardar_lista;
    private Intent i;
    private LinearLayout layoutMostrarLista;
    private LinearLayout layoutResultadoLista;
    private TextView totalLista;
    private String columnArt;
    private String columnLista;
    private String columnComparador;
    private String columnDif;
    private String columnRef;
    private Boolean editar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_lista);
        i = getIntent();
        opcion = i.getExtras().getInt(Inicio.OPCION);
        lista = (ListView) findViewById(R.id.listViewLista);
        totalLista = (TextView) findViewById(R.id.totalLista);
        layoutMostrarLista = (LinearLayout) findViewById(R.id.layoutMostrarLista);
        layoutResultadoLista = (LinearLayout) findViewById(R.id.layoutResultadoLista);
        switch (opcion){
            case Inicio.COMPARAR_LISTA:
                setTitle(R.string.label_listing_results);
                cargarResultadosCompararLista();
                break;
            case Inicio.MOSTRAR_LISTA:
                cargarArchivoLista();
                cargarMostrarLista();
                break;
        }
    }

    public void onBackPressed(){
        if(editar){
            final AlertDialog confSalir = new AlertDialog.Builder(this).create();
            confSalir.setTitle(R.string.action_edit_list);
            confSalir.setMessage(getResources().getString(R.string.msj_conf_salir_sin_guardar));
            confSalir.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_exit),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            confSalir.dismiss();
                            finish();
                        }
                    });
            confSalir.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_no_exit),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            confSalir.dismiss();
                        }
                    });
            confSalir.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.btn_save_and_exit),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            confSalir.dismiss();
                            guardarArchivoLista();
                            finish();
                        }
                    });
            confSalir.show();
        }else{
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        editar_lista = (MenuItem) menu.findItem(R.id.ml_editar_lista);
        guardar_cambios = (MenuItem) menu.findItem(R.id.ml_guardar_cambios);
        compartir_lista = (MenuItem) menu.findItem(R.id.ml_compartir_lista);
        guardar_lista = (MenuItem) menu.findItem(R.id.ml_exportar_lista);
        switch (opcion){
            case Inicio.COMPARAR_LISTA:
                editar_lista.setVisible(false);
                guardar_cambios.setVisible(false);
                break;
            case Inicio.MOSTRAR_LISTA:
                guardar_cambios.setVisible(false);
                break;
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ml_editar_lista:
                editar = true;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                editar_lista.setVisible(false);
                guardar_cambios.setVisible(true);
                compartir_lista.setVisible(false);
                guardar_lista.setVisible(false);
                editarArchivoLista();
                return true;
            case R.id.ml_guardar_cambios:
                editar = false;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
                editar_lista.setVisible(true);
                guardar_cambios.setVisible(false);
                compartir_lista.setVisible(true);
                guardar_lista.setVisible(true);
                guardarArchivoLista();
                cargarMostrarLista();
                return true;
            case R.id.ml_exportar_lista:
                if(opcion == Inicio.MOSTRAR_LISTA) {
                    Intent datExport = new Intent(MostrarLista.this, DatosExportar.class);
                    opcion = Inicio.EXPORTAR_ARCHIVO;
                    datExport.putExtra(Inicio.OPCION, opcion);
                    startActivity(datExport);
                } else {
                    if(opcion == Inicio.COMPARAR_LISTA) {
                        opcion = GUARDAR_DIF_COMPARAR;
                        guardarDif();
                    }
                }
                return true;
            case R.id.ml_compartir_lista:
                if(opcion == Inicio.MOSTRAR_LISTA) {
                    Intent datCompartir = new Intent(MostrarLista.this, DatosExportar.class);
                    opcion = Inicio.COMPARTIR_LISTA;
                    datCompartir.putExtra(Inicio.OPCION, opcion);
                    startActivity(datCompartir);
                } else {
                    if(opcion == Inicio.COMPARAR_LISTA) {
                        opcion = COMPARTIR_DIF_COMPARAR;
                        guardarDif();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargarMostrarLista(){
        AdaptadorMostrarLista adaptador = new AdaptadorMostrarLista(this, listaArt);
        totalLista.setText(totLista+" "+getResources().getString(R.string.articulos));
        lista.setItemsCanFocus(true);
        lista.setAdapter(adaptador);
    }


    private void editarArchivoLista(){
        AdaptadorEditarLista adaptador = new AdaptadorEditarLista(this, listaArt);
        lista.setAdapter(adaptador);
    }

    private void guardarArchivoLista(){
        GuardarArchivo nvoArchivo = new GuardarArchivo(Inicio.ARCHLISTA.getAbsolutePath(), 0, false);
        nvoArchivo.escribirLinea(getResources().getString(R.string.column_art),getResources().getString(R.string.column_cant),
                getResources().getString(R.string.column_descrip));
        totLista = 0;
        for(int n=0; n<listaArt.size(); n++){
            nvoArchivo.escribirLinea(listaArt.get(n).getArt(),listaArt.get(n).getCant(),
                    listaArt.get(n).getDescrip());
            totLista = totLista + Integer.parseInt(listaArt.get(n).getCant().toString());
        }
        nvoArchivo.cerrarArchivo();
    }

    private void cargarArchivoLista(){
        totLista = 0;
        BufferedReader leerArchLista;
        listaArt = new ArrayList<LineaDLista>();
        try {
            leerArchLista = new BufferedReader(new InputStreamReader(new FileInputStream(archLista)));
            String linea = leerArchLista.readLine();
            int consec = 0;
            while (linea!=null) {
                String[] articulo = linea.split("\\|");
                if(Recursos.esNumero(articulo[0])){
                    LineaDLista lineaDLista = new LineaDLista();
                    lineaDLista.setArt(articulo[0]);
                    if(articulo.length>1) {
                        lineaDLista.setCant(articulo[1]);
                        if(Recursos.esNumero(articulo[1])){
                            totLista = totLista + Integer.parseInt(articulo[1]);
                        }
                    }
                    if(articulo.length>2) lineaDLista.setDescrip(articulo[2]);
                    lineaDLista.setConsec(consec);
                    listaArt.add(lineaDLista);
                }
                linea = leerArchLista.readLine();
                consec++;
            }
            leerArchLista.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarResultadosCompararLista(){
        layoutMostrarLista.setVisibility(View.INVISIBLE);
        layoutResultadoLista.setVisibility(View.VISIBLE);
        totalLista.setVisibility(View.INVISIBLE);
        columnArt = i.getExtras().getString(COLUMN_ART);
        columnLista = i.getExtras().getString(COLUMN_LISTA);
        columnComparador = i.getExtras().getString(COLUMN_COMPARADOR);
        columnDif = i.getExtras().getString(COLUMN_DIFERENCIA);
        columnRef = i.getExtras().getString(COLUMN_REFERENCIA);
        AdaptadorCompararLista adaptador = new AdaptadorCompararLista(this, columnArt.split("\\|"),
                columnLista.split("\\|"), columnComparador.split("\\|"), columnDif.split("\\|"));
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String [] arrayRef = columnRef.split("\\|");
                if(arrayRef[i].length()>0){
                    Recursos.mostrarMensaje(MostrarLista.this,
                            getResources().getString(R.string.column_reference)+": "+arrayRef[i]);
                } else {
                    Recursos.mostrarMensaje(MostrarLista.this,
                            getResources().getString(R.string.msj_no_reference));
                }
            }
        });
    }

    private void guardarDif(){
        CharSequence [] seleccion = {
                getResources().getString(R.string.msj_line_separated_file),
                getResources().getString(R.string.msj_tab_separated_file),
                getResources().getString(R.string.msj_comma_separated_file)};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.action_save_differences);
        alertDialog.setItems(seleccion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(opcion == GUARDAR_DIF_COMPARAR){
                    Intent comparar = new Intent(MostrarLista.this, Explorar.class);
                    comparar.putExtra(Inicio.OPCION, opcion);
                    comparar.putExtra(Explorar.TIPO_ARCHIVO, item);
                    startActivityForResult(comparar, opcion);
                    dialog.dismiss();
                } else {
                    if (opcion == COMPARTIR_DIF_COMPARAR) {
                        compartirDif(item);
                    }
                }
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void compartirDif(final int tipoArc){
        final AlertDialog nombrArchivo = new AlertDialog.Builder(this).create();
        final EditText nombre = new EditText(this);
        nombre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        nombre.setHint(R.string.msj_filename);
        nombrArchivo.setTitle(R.string.action_share);
        nombrArchivo.setMessage(getResources().getString(R.string.msj_filename_to_share));
        nombrArchivo.setView(nombre);
        nombrArchivo.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_share),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (nombre.getText().toString().length() > 0) {
                            if (nombre.getText().toString().equals(R.string.str_list_filename)) {
                                Recursos.mostrarMensaje(MostrarLista.this, R.string.msj_different_name);
                            } else {
                                nombrArchivo.dismiss();
                                String tipoExtenc = "";
                                switch (tipoArc) {
                                    case Explorar.TIPO_ARCH_LINEA:
                                        tipoExtenc = Explorar.TIPO_TEXTO;
                                        break;
                                    case Explorar.TIPO_ARCH_TAB:
                                        tipoExtenc = Explorar.TIPO_TEXTO;
                                        break;
                                    case Explorar.TIPO_ARCH_COMA:
                                        tipoExtenc = Explorar.TIPO_COMA;
                                        break;
                                }
                                String dir = Inicio.DIRECTORIO_PAQUETE_EXTERNO + "/" + nombre.getText().toString() +
                                        "." + tipoExtenc;
                                guardarCompartirDif(dir, tipoArc);
                            }
                        }
                        finish();
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

    private void guardarCompartirDif(String dir, int tipoArch){
        GuardarDiferencias saveDif = new GuardarDiferencias(MostrarLista.this, opcion, dir, tipoArch);
        saveDif.execute(columnArt, columnLista, columnComparador,
                columnDif, columnRef);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(opcion == GUARDAR_DIF_COMPARAR) {
                guardarCompartirDif(data.getExtras().getString(Explorar.DIRECCION_ARCHIVO),
                        data.getExtras().getInt(Explorar.TIPO_ARCHIVO));
            }
        }else{
            Recursos.mostrarMensaje(this, R.string.msj_error_save_dif);
        }
        finish();
    }

}
