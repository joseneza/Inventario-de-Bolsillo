package com.jose.prieto.mysmallbusinessbyneza;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.AdapterInicio;
import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.LeerArchivo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesLista.AgregarLista;
import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.BuscarArticulo;
import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.AdmSQLite;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Articulo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.LeerTitulos;
import com.jose.prieto.mysmallbusinessbyneza.BusqAvanzada.BusquedaAvanz;

import java.io.File;
import java.util.ArrayList;

public class Inicio extends AppCompatActivity {
    //Directorio de la base de datos
    public static final String DIRECTORY_THIS_PACKAGE_DATA_BASE =
            "/data/data/com.jose.prieto.mysmallbusinessbyneza/databases";
    //Directorio de almacenamiento externo
    public static final File DIRECTORIO_PAQUETE_EXTERNO =
            new File(Environment.getExternalStorageDirectory().getPath()+
            "/My Small Business by Neza/.data");
    //Archivo de la lista
    public static final File ARCHLISTA = new File(DIRECTORIO_PAQUETE_EXTERNO, "Lista de Articulos.txt");
    public static final int USAR_SCANNER = 1;
    public static final int BUSQUEDA_AVANZADA = 2;
    public static final int MOSTRAR_LISTA = 5;
    public static final int COMPARAR_LISTA = 10;
    public static final int EXPORTAR_ARCHIVO = 15;
    public static final int COMPARTIR_LISTA = 20;
    public static final int BORRAR_LISTA = 25;
    public static final int NVA_BASE = 30;
    public static final int NVA_BASE_CONF = 35;
    public static final int AJUSTES = 100;
    public static final int ACERCA_D = 101;
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0;
    public static final String OPCION = "opcion";
    public static final String ARCH_AJUSTES = "Ajuste";
    public SharedPreferences preferencias;
    private MenuItem exportar_archivo;
    private MenuItem modo_lista, lista;
    private MenuItem compartir_lista;
    private MenuItem borrar_lista;
    private EditText edtArt, edtCant;
    private ListView listInicio;
    private TextView txtDescripcion, txtExist;
    private Button btnImagen, btnAgregar;
    private Boolean modoLista = false;
    private Articulo art = new Articulo();
    private int valDataBase = 0;
    private int opcion = -1;
    private Boolean permisoEscritura = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferencias = getSharedPreferences(ARCH_AJUSTES, Context.MODE_PRIVATE);
        cargarPreferencias();
        edtArt = (EditText) findViewById(R.id.edtArt);
        edtCant = (EditText) findViewById(R.id.edtCant);
        edtCant.setEnabled(false);
        edtCant.setVisibility(View.INVISIBLE);
        listInicio = (ListView) findViewById(R.id.listInicio);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        txtExist = (TextView) findViewById(R.id.txtExist);
        btnImagen = (Button) findViewById(R.id.btnImagen);
        btnImagen.setEnabled(false);
        btnImagen.setVisibility(View.INVISIBLE);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnAgregar.setEnabled(false);
        btnAgregar.setVisibility(View.INVISIBLE);
        //setTitle(getResources().getString(R.string.str_label_inicio));
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(existeDBase()){
                    if(edtArt.getText().length()!=0){
                        if(valDataBase == 0){
                            metodoBuscar(edtArt.getText().toString());
                        } else {
                            if(valDataBase == 1) Recursos.mostrarMensaje(Inicio.this, R.string.msj_servidor_desarrollo);
                        }
                        if (modoLista) {
                            edtCant.setText("");
                            if(edtArt.isFocusable()) edtCant.requestFocus();
                        } else {//Ocultar Teclado
                            InputMethodManager inputMethodManager = (InputMethodManager)
                                    getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }else{
                        Recursos.mostrarMensaje(Inicio.this,R.string.msj_not_added_any_code);
                    }
                }else{
                    Recursos.mostrarMensaje(Inicio.this,R.string.mensaje_no_DB);
                }
            }
        });
        if(!getPermisoEscritura(this)) setPermisoEscritura(this);


    }

    private void cargarPreferencias(){
        valDataBase = preferencias.getInt(Ajustes.VAL_DATA_BASE, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        modo_lista = menu.findItem(R.id.modo_lista);
        lista = menu.findItem(R.id.lista);
        exportar_archivo = menu.findItem(R.id.exportar_lista);
        compartir_lista = menu.findItem(R.id.compartir_lista);
        borrar_lista = menu.findItem(R.id.borrar_lista);
        mostrarMenus(ARCHLISTA.exists());
        if(ARCHLISTA.exists()){
            cargarModoLista();
            setTitle(R.string.str_lista_activo);
        }else{
            cargarModoConsulta();
            setTitle(R.string.str_lista_inactivo);
        }
        if (!existeDBase()){
            mostrarMenus(false);
            String titulo = getResources().getString(R.string.title_there_is_no_data);
            String mensaje = getResources().getString(R.string.msj_there_is_no_data);
            Recursos.mensajeDialog(this, titulo, mensaje, getResources().getString(R.string.btn_aceptar)).show();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String mensaje;
        if(getPermisoEscritura(this)){
            switch (item.getItemId()){
                case R.id.busqueda_avanzada://Opcion 2
                    opcion = BUSQUEDA_AVANZADA;
                    if(existeDBase()){
                        limpiarPantalla();
                        Intent busqAvanz = new Intent(Inicio.this, BusquedaAvanzada.class);
                        startActivityForResult(busqAvanz, BUSQUEDA_AVANZADA);
                    }else{
                        Recursos.mostrarMensaje(Inicio.this,R.string.mensaje_no_DB);
                    }
                    return true;
                case R.id.modo_lista://Opcion 3
                /* Activa y desactiva el modo Lista */
                    if(!modoLista){
                        if(ARCHLISTA.exists()){
                            mensaje = getResources().getString(R.string.msj_list_exist);
                            final AlertDialog pregunta = new AlertDialog.Builder(this).create();
                            pregunta.setTitle(R.string.str_lista_activo);
                            pregunta.setMessage(mensaje);
                            pregunta.setCancelable(false);
                            pregunta.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.btn_continue_list),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            pregunta.dismiss();
                                        }
                                    });
                            pregunta.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_new_list),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            borrarLista();
                                            mostrarMenus(false);
                                            pregunta.dismiss();
                                        }
                                    });
                            pregunta.show();
                        }
                        cargarModoLista();
                    }else{
                        cargarModoConsulta();
                    }
                    return true;

                case R.id.mostrar_lista://Opcion 4
                    opcion = MOSTRAR_LISTA;
                    if(existeDBase()){
                        limpiarPantalla();
                        Intent mostrarLista = new Intent(Inicio.this, MostrarLista.class);
                        mostrarLista.putExtra(OPCION, MOSTRAR_LISTA);
                        startActivity(mostrarLista);
                    }else{
                        Recursos.mostrarMensaje(Inicio.this,R.string.mensaje_no_DB);
                    }
                    return true;
                case R.id.comparar_lista:
                    opcion = COMPARAR_LISTA;
                    CharSequence [] seleccion = {
                            getResources().getString(R.string.msj_line_separated_file),
                            getResources().getString(R.string.msj_tab_separated_file),
                            getResources().getString(R.string.msj_comma_separated_file)};
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle(R.string.title_tipo_archivo);
                    alertDialog.setItems(seleccion, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            opcion = Inicio.COMPARAR_LISTA;
                            Intent comparar = new Intent(Inicio.this, Explorar.class);
                            comparar.putExtra(OPCION, COMPARAR_LISTA);
                            comparar.putExtra(Explorar.TIPO_ARCHIVO, item);
                            startActivityForResult(comparar, opcion);
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.setNeutralButton(getResources().getText(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.create();
                    alertDialog.show();

                    return true;
                case R.id.exportar_lista://Opcion 6
                    opcion = EXPORTAR_ARCHIVO;
                    if(existeDBase()){
                        limpiarPantalla();
                        Intent datExport = new Intent(Inicio.this, DatosExportar.class);
                        datExport.putExtra(OPCION, opcion);
                        startActivity(datExport);
                    }else{
                        Recursos.mostrarMensaje(Inicio.this,R.string.mensaje_no_DB);
                    }
                    return  true;
                case R.id.compartir_lista://Opcion 7
                    opcion = COMPARTIR_LISTA;
                    if(existeDBase()){
                        limpiarPantalla();
                        Intent datCompartir = new Intent(Inicio.this, DatosExportar.class);
                        datCompartir.putExtra(OPCION, opcion);
                        startActivity(datCompartir);

                    }else{
                        Recursos.mostrarMensaje(Inicio.this,R.string.mensaje_no_DB);
                    }
                    return true;
                case R.id.borrar_lista://Opcion 8
                    opcion = BORRAR_LISTA;
                    mensaje = getResources().getString(R.string.msj_conf_delete_list);
                    final AlertDialog borrarList = new AlertDialog.Builder(this).create();
                    borrarList.setTitle(R.string.action_delete_list);
                    borrarList.setMessage(mensaje);
                    borrarList.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    borrarLista();
                                    mostrarMenus(false);
                                    borrarList.dismiss();
                                }
                            });
                    borrarList.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_no),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    borrarList.dismiss();
                                }
                            });
                    borrarList.show();
                    return true;
                case R.id.action_settings://Opcion 100
                    opcion = AJUSTES;
                    Intent ajustes = new Intent(Inicio.this, Ajustes.class);
                    startActivityForResult(ajustes, opcion);
                    return true;
                case R.id.acerca_de:
                    opcion = ACERCA_D;
                    Intent acerca = new Intent(Inicio.this, AcercaDe.class);
                    startActivity(acerca);
                    return true;
            }
        }else{
            setPermisoEscritura(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        String msj = getResources().getString(R.string.msj_conf_exit_app);
        final AlertDialog confSalir = new AlertDialog.Builder(this).create();
        confSalir.setTitle(R.string.title_conf_exit_app);
        confSalir.setMessage(msj);
        confSalir.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_yes),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confSalir.dismiss();
                finish();
            }
        });
        confSalir.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_no),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confSalir.dismiss();
            }
        });
        confSalir.show();
    }

    /* Metodos para Click*/

    public void usarScanner(View v){
        if(existeDBase()){
            opcion = USAR_SCANNER;
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }else{
            Recursos.mostrarMensaje(Inicio.this,R.string.mensaje_no_DB);
        }
    }

    public void mostrarImagen(View v){
        if (!art.getUrlImg().isEmpty()) {
            Intent mostrarImg = new Intent(Inicio.this, MostrarImagen.class);
            mostrarImg.putExtra(MostrarImagen.SKU, Long.toString(art.getSku()));
            mostrarImg.putExtra(MostrarImagen.URL, art.getUrlImg());
            mostrarImg.putExtra(MostrarImagen.DESCRIPCION, art.getDescipcion());
            startActivity(mostrarImg);
        } else {
            Recursos.mostrarMensaje(Inicio.this,R.string.msj_there_is_no_img_url);
        }
    }

    public void clickCant(View v){
        edtCant.setSelection(edtCant.length());
    }

    public void agregarLista(View v){
        if(art.getSku()>0){
            if(preferencias.getBoolean(Ajustes.AGREGAR_UNIDAD, false) && edtCant.getText().toString().isEmpty()){
                edtCant.setText("1");
            }
            if(edtCant.getText().toString().length()>0){
                int exist = 0;
                try{
                    exist = Integer.parseInt(edtCant.getText().toString());
                }catch (NumberFormatException ex){}
                art.setExist(exist);
                AgregarLista agrLista = new AgregarLista(this, art);
                if(agrLista.error){
                    Recursos.mostrarMensaje(Inicio.this,R.string.msj_error_save_list);
                }else{
                    Recursos.mostrarMensaje(Inicio.this,"Articulo " + art.getSku() + " agregado");
                }
                limpiarPantalla();
                art = new Articulo();
                if(ARCHLISTA.exists()){
                    mostrarMenus(true);
                }
                if(edtCant.isFocusable()) edtArt.requestFocus();
            }else{
                Recursos.mostrarMensaje(Inicio.this,R.string.msj_cant_empty);
                if(edtArt.isFocusable()) edtCant.requestFocus();
            }
        }else{
            Recursos.mostrarMensaje(Inicio.this,R.string.msj_empty_fields);
            if (edtCant.isFocusable()) edtArt.requestFocus();
        }
    }

    /* Metodos para el Modo Lista */
    private void mostrarMenus(Boolean mostrar){
        lista.setEnabled(mostrar);
        lista.setVisible(mostrar);
        exportar_archivo.setEnabled(mostrar);
        exportar_archivo.setVisible(mostrar);
        compartir_lista.setEnabled(mostrar);
        compartir_lista.setVisible(mostrar);
        borrar_lista.setEnabled(mostrar);
        borrar_lista.setVisible(mostrar);
    }

    private void cargarModoLista(){
        modoLista = true;
        edtCant.setEnabled(true);
        edtCant.setVisibility(View.VISIBLE);
        edtCant.setHint(getResources().getString(R.string.str_cant));
        txtExist.setVisibility(View.INVISIBLE);
        btnAgregar.setEnabled(true);
        btnAgregar.setVisibility(View.VISIBLE);
        setTitle(R.string.str_lista_activo);
        modo_lista.setTitle(R.string.str_lista_inactivo);
    }

    private  void cargarModoConsulta(){
        modoLista = false;
        edtCant.setEnabled(false);
        edtCant.setVisibility(View.INVISIBLE);
        txtExist.setVisibility(View.VISIBLE);
        btnAgregar.setEnabled(false);
        btnAgregar.setVisibility(View.INVISIBLE);
        setTitle(R.string.str_lista_inactivo);
        modo_lista.setTitle(R.string.str_lista_activo);

    }

    private void borrarLista(){
        File [] listaArch = DIRECTORIO_PAQUETE_EXTERNO.listFiles();
        for(int n = 0; n < listaArch.length; n++){
            if(listaArch[n].isFile()){
                listaArch[n].delete();
            }
        }
        listaArch = DIRECTORIO_PAQUETE_EXTERNO.listFiles();
        if(listaArch.length == 0){
            Recursos.mostrarMensaje(Inicio.this,R.string.msj_delete_list_ok);
        }
    }

    /* Metodo para buscar articulo */
    private void metodoBuscar(String numArticulo){
        AdapterInicio adapter;
        long articulo = 0;
        try{
            articulo = Long.parseLong(numArticulo);
        }catch (NumberFormatException ex){}
        if(articulo!=0){
            final BuscarArticulo buscarArticulo = new BuscarArticulo(this, articulo);
            art = buscarArticulo.getArticulo();
            if(buscarArticulo.buscar){
                edtArt.setText(Long.toString(buscarArticulo.getCodigo()));
                txtDescripcion.setText(buscarArticulo.getDescripcion());
                txtExist.setText(getResources().getString(R.string.str_cant) + ": " + buscarArticulo.getExist());
                if (preferencias.getBoolean(Ajustes.AGREGAR_UNIDAD, false)) edtCant.setHint("1");
                if(!buscarArticulo.getUrlImg().isEmpty()){
                    btnImagen.setEnabled(true);
                    btnImagen.setVisibility(View.VISIBLE);
                }else{
                    btnImagen.setEnabled(false);
                    btnImagen.setVisibility(View.INVISIBLE);
                }
                String listaArticulos [] = {};
                String tituloArticulos [] = {};
                if(buscarArticulo.getListaDatos()!=null){
                    listaArticulos = buscarArticulo.getListaDatos();
                    tituloArticulos = buscarArticulo.getTiulosDatos();
                }
                adapter = new AdapterInicio(this, tituloArticulos, listaArticulos);
                listInicio.setAdapter(adapter);
                final String[] finalTituloArticulos = tituloArticulos;
                final String[] finalListaArticulos = listaArticulos;
                listInicio.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        String titul = finalTituloArticulos[position];
                        String dat = finalListaArticulos[position];
                        if(titul.equals(getResources().getString(R.string.column_precio))) dat =
                                buscarArticulo.getPrecioSinCeros();
                        if(titul.equals(getResources().getString(R.string.column_precio_ant))) dat =
                                buscarArticulo.getPrecioAntSinCeros();
                        titul = titul.replace(' ','_');
                        accionBuscarRelacion(titul, dat).show();
                        return true;
                    }
                });
            }else{
                limpiarPantalla();
                edtArt.setText(Long.toString(art.getSku()));
                txtDescripcion.setText(getResources().getString(R.string.msj_article_not_found));
            }
        }else{
            Recursos.mostrarMensaje(Inicio.this,R.string.msj_only_num);
        }

    }

    private void limpiarPantalla(){
        ArrayAdapter <String> adapter;
        edtArt.setText("");
        edtCant.setText("");
        edtCant.setHint(getResources().getString(R.string.str_cant));
        txtDescripcion.setText("");
        txtExist.setText("");
        btnImagen.setVisibility(View.INVISIBLE);
        btnImagen.setEnabled(false);
        String lista[] = {};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        listInicio.setAdapter(adapter);
    }

    public static Boolean existeDBase(){
        File dbArchivo = new File(DIRECTORY_THIS_PACKAGE_DATA_BASE, AdmSQLite.NOMBRE_BASE_DATOS);
        return dbArchivo.exists();
    }

    public static Boolean getPermisoEscritura(Context context){
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void setPermisoEscritura(Activity activity){
        ActivityCompat.requestPermissions(activity, new String [] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(opcion == USAR_SCANNER){
            IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(resultado!=null){
                String scanContent = resultado.getContents();
                edtArt.setText(scanContent);
                if (valDataBase == 0){
                    metodoBuscar(scanContent);
                } else {
                    if(valDataBase == 1) Recursos.mostrarMensaje(Inicio.this,R.string.msj_servidor_desarrollo);
                }
                //Ocultar teclado
                if (modoLista==true) {
                    edtCant.setText("");
                    edtCant.requestFocus();
                } else {//Ocultar Teclado
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        }else{
            String dir;
            switch (opcion) {
                case BUSQUEDA_AVANZADA:
                    if(resultCode == RESULT_OK){
                        dir = data.getExtras().getString(BusquedaAvanzada.No_ARTICULO);
                        metodoBuscar(dir);
                    }else {
                        Recursos.mostrarMensaje(Inicio.this,R.string.msj_cancel_oper);
                    }
                    break;
                case COMPARAR_LISTA:
                    if(resultCode == RESULT_OK){
                        dir = data.getExtras().getString(Explorar.DIRECCION_ARCHIVO);
                        int [] numColumn = data.getIntArrayExtra(ColumnSelect.NUM_COLUMN);
                        int numFArch = data.getIntExtra(Explorar.TIPO_ARCHIVO, Explorar.TIPO_ARCH_LINEA);
                        CompararLista compararLista = new CompararLista(numColumn [0], numColumn [1], numColumn[2], numFArch);
                        compararLista.execute(dir);
                    }else{
                        Recursos.mostrarMensaje(Inicio.this,R.string.msj_cancel_oper);
                    }
                    break;
                case AJUSTES:
                    valDataBase = preferencias.getInt(Ajustes.VAL_DATA_BASE, 0);
                    mostrarMenus(ARCHLISTA.exists());
                    break;
            }


        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(!DIRECTORIO_PAQUETE_EXTERNO.exists()){
                        DIRECTORIO_PAQUETE_EXTERNO.mkdirs();
                    }
                }else{
                    Recursos.mostrarMensaje(Inicio.this,R.string.msj_write_external_storage_permission_denied);
                }
                return;
            // Gestionar el resto de permisos
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /*Mensaje para Buscar la relacion de un articulo con algun otro*/
    public AlertDialog accionBuscarRelacion(final String titulo, final String dato){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getResources().getString(R.string.msj_find_relation_for) + " " + titulo.replace('_',' '));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_yes),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                new AvanceBusquedaRelacion().execute(titulo, dato);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_no),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }

    private class AvanceBusquedaRelacion extends AsyncTask<String, Void, Boolean> {
        //AsyncTask para crear un avance de busqueda
        LeerTitulos titulos = new LeerTitulos(Inicio.this);
        ProgressDialog pDialog;
        BusquedaAvanz busquedaAvanz;
        String [] datos;
        String [] listArticulos = null;
        String [] listDescripcion = null;

        @Override
        protected void onPreExecute() {//Preparacion de avance
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(Inicio.this);
            pDialog.setMessage(getResources().getString(R.string.msj_find_relation));
            pDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_cancel),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancel(true);
                }
            });
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(String... params) {//Metodo para la operacion
            datos = params;
            busquedaAvanz = new BusquedaAvanz(Inicio.this, params[0], params[1], titulos.getDatosXtras());
            if(busquedaAvanz.getFila().moveToFirst()){
                do{
                    busquedaAvanz.buscarCoincidencias();
                }while (busquedaAvanz.getFila().moveToNext() && !isCancelled());
            }
            busquedaAvanz.cerrarTabla();
            listArticulos = busquedaAvanz.getResultadoArticulo();
            listDescripcion = busquedaAvanz.getResultadoDescripcion();
            return true;
        }
        @Override
        protected void onPostExecute(Boolean par) {//Metodo al finalizar la operacion
            pDialog.dismiss();
            if(listArticulos!=null){
                if(listArticulos.length>1){
                    opcion = BUSQUEDA_AVANZADA;
                    Intent buscarRelacion = new Intent(Inicio.this, ResultadosBusqueda.class);
                    buscarRelacion.putExtra(BusquedaAvanzada.RESULTADOS_DE_BUSQUEDA_ARTICULO, listArticulos);
                    buscarRelacion.putExtra(BusquedaAvanzada.RESULTADOS_DE_BUSQUEDA_DESCRIPCION, listDescripcion);
                    startActivityForResult(buscarRelacion, BUSQUEDA_AVANZADA);
                }else{
                    Recursos.mostrarMensaje(Inicio.this,getResources().getString(R.string.msj_art_sin_relacion_ini) + " " + datos[0].replace('_', ' ')
                            + " " + getResources().getString(R.string.msj_art_sin_relacion_fin));
                }
            }else{
                Recursos.mostrarMensaje(Inicio.this,R.string.msj_sin_relacion);
            }
        }

        @Override
        protected void onCancelled(){
            pDialog.dismiss();
            Recursos.mostrarMensaje(Inicio.this,R.string.msj_find_cancel);
        }
    }


    private class CompararLista extends AsyncTask<String, Void, Boolean> {
        int error = 0;
        ArrayList<Articulo> resultado = new ArrayList<Articulo>();
        private ProgressDialog pDialog;
        //File archComparador;
        private int colArticulo;
        private int colCantidad;
        private int colReferencia;
        private String columnArt = "";
        private String columnLista = "";
        private String columnComparador = "";
        private String columnDif = "";
        private String columnRef = "";
        private int numFArch;
        //private String lineaTitulos = "";

        public CompararLista(int colArticulo, int colCantidad, int colReferencia, int numFArch){
            this.colArticulo = colArticulo;
            this.colCantidad = colCantidad;
            this.colReferencia = colReferencia;
            this.numFArch = numFArch;
        }

        @Override
        protected void onPreExecute() {//Preparacion de avance
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(Inicio.this);
            pDialog.setMessage(getResources().getString(R.string.msj_comparing_list));
            pDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancel(true);
                        }
                    });
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... dir) {
            String archComparador = new File(dir[0]).getName();
            if (new File(dir[0]).exists()) {
                pDialog.setMessage(getResources().getString(R.string.msj_compare_list_inicio) + " " + archComparador +
                        " "+getResources().getString(R.string.msj_compare_list_final));
                ArrayList <Articulo> comparador = abrirArchivo(dir[0], colArticulo, colCantidad,
                        colReferencia, numFArch);
                if(comparador.size()>0){
                    ArrayList <Articulo> lista = abrirArchivo(Inicio.ARCHLISTA.getAbsolutePath(), 0, 1, -1, Explorar.TIPO_ARCH_LINEA);
                    if(lista.size()>0){
                        resultado = compararLista(comparador, lista);
                        for(int n = 0; n < resultado.size(); n++){
                            if(n!=0){
                                columnArt = columnArt + "|";
                                columnLista = columnLista + "|";
                                columnComparador = columnComparador + "|";
                                columnDif = columnDif + "|";
                                columnRef = columnRef + "|";
                            }
                            columnArt = columnArt + resultado.get(n).getSku();
                            columnLista = columnLista + resultado.get(n).getExist();
                            columnComparador = columnComparador + resultado.get(n).getCantXtra();
                            columnDif = columnDif + resultado.get(n).getDif();
                            columnRef = columnRef + resultado.get(n).getReferencia();
                        }
                        return true;
                    } else {
                        error = 3;
                        return false;
                    }
                } else {
                    error = 2;
                    return false;
                }
            } else {
                error = 1;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean terminado) {//Metodo al finalizar la operacion
            pDialog.dismiss();
            if(terminado){
                if(resultado.size() > 0){
                    Intent mostrarLista = new Intent(Inicio.this, MostrarLista.class);
                    mostrarLista.putExtra(Inicio.OPCION, Inicio.COMPARAR_LISTA);
                    mostrarLista.putExtra(MostrarLista.COLUMN_ART, columnArt);
                    mostrarLista.putExtra(MostrarLista.COLUMN_LISTA, columnLista);
                    mostrarLista.putExtra(MostrarLista.COLUMN_COMPARADOR, columnComparador);
                    mostrarLista.putExtra(MostrarLista.COLUMN_DIFERENCIA, columnDif);
                    mostrarLista.putExtra(MostrarLista.COLUMN_REFERENCIA, columnRef);
                    startActivity(mostrarLista);
                }else{
                    Recursos.mensajeDialog(Inicio.this, getResources().getString(R.string.action_compare_list),
                            getResources().getString(R.string.msj_compared_with_no_difference), getResources().getString(R.string.btn_aceptar)).show();
                }
            } else {
                String mensaje = "";
                switch (error){
                    case 1:
                        mensaje = getResources().getString(R.string.msj_file_does_not_exist);
                        break;
                    case 2:
                        mensaje = getResources().getString(R.string.msj_empty_file);
                        break;
                    case 3:
                        mensaje = getResources().getString(R.string.msj_empty_file);
                        break;
                }
                Recursos.mensajeDialog(Inicio.this, getResources().getString(R.string.action_compare_list),
                        mensaje, getResources().getString(R.string.btn_aceptar)).show();
            }
        }

        @Override
        protected void onCancelled(){
            pDialog.dismiss();
        }

        private ArrayList <Articulo> abrirArchivo(String dir, int colSku, int colCant, int colReferencia,
                                                  int numFArch){
            LeerArchivo leerArchivo = new LeerArchivo(dir, numFArch);
            ArrayList <Articulo> arrayTemporal = new ArrayList<Articulo>();;
            if(leerArchivo.getLectuta()){
                String [] arrayLinea = leerArchivo.getArrayLinea();
                while (arrayLinea!=null) {
                    Articulo art = new Articulo();
                    if(Recursos.esNumero(arrayLinea[colSku])){
                        art.setSku(Long.parseLong(arrayLinea[colSku]));
                        try{
                            int exist = Integer.parseInt(arrayLinea[colCant]);
                            art.setExist(exist);
                        }catch (NumberFormatException ex){}
                        if(colReferencia>-1) art.setReferencia(arrayLinea[colReferencia]);
                        arrayTemporal.add(art);
                    }
                    arrayLinea = leerArchivo.getArrayLinea();
                }
            }
            return Recursos.consentrarLista(arrayTemporal);
        }

        private ArrayList<Articulo> compararLista(ArrayList<Articulo> comparador, ArrayList<Articulo> lista){
            ArrayList<Long> numArt = new ArrayList<Long>();
            ArrayList<Articulo> resultado = new ArrayList<Articulo>();
            for(int n=0;n<lista.size();n++){
                numArt.add(lista.get(n).getSku());
            }
            for(int n=0;n<comparador.size();n++){
                int a= numArt.indexOf(comparador.get(n).getSku());
                if(a < 0){
                    Articulo newArt = new Articulo();
                    newArt.setSku(comparador.get(n).getSku());
                    newArt.setCantXtra(comparador.get(n).getExist());
                    newArt.setExist(0);
                    newArt.setReferencia(comparador.get(n).getReferencia());
                    lista.add(newArt);
                } else {
                    lista.get(a).setCantXtra(comparador.get(n).getExist());;
                }
            }
            for(int n=0; n<lista.size();n++){
                lista.get(n).setDif(lista.get(n).getExist() - lista.get(n).getCantXtra());
                if(lista.get(n).getDif()!=0) resultado.add(lista.get(n));
            }
            return resultado;
        }
    }

}
