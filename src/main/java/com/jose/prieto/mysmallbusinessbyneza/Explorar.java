package com.jose.prieto.mysmallbusinessbyneza;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.AccionExplorar.ArrayAdapterExplorar;
import com.jose.prieto.mysmallbusinessbyneza.AccionExplorar.DatExplorar;
import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.LeerArchivo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.LeerTitulos;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Explorar extends AppCompatActivity {

    public static final String DIRECCION_ARCHIVO = "DIRECCION";
    public static final String TIPO_ARCHIVO = "tipoArch";
    public static final String TIPO_TEXTO = "txt";
    public static final String TIPO_COMA = "csv";
    public static final int TIPO_ARCH_LINEA = 0;
    public static final int TIPO_ARCH_TAB = 1;
    public static final int TIPO_ARCH_COMA = 2;
    private ArrayList<DatExplorar> listaNombresArchivos;
    private List<String> listaRutaArchivos;
    private ArrayAdapterExplorar adaptador;
    private String directorioRaiz;
    private TextView carpetaActual, txt;
    private String dirActual;
    private EditText nomArchivo;
    private ListView listDir;
    private MenuItem menu_guardar;
    private int opcion = -1;
    private String tipoArch;
    int numFArch = 0;
    private File archivoFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorar);
        carpetaActual = (TextView) findViewById(R.id.carpetaActual);
        directorioRaiz = Environment.getExternalStorageDirectory().getPath();//Ubicacion raiz: 0
        listDir = (ListView) findViewById(R.id.listDirectorio);
        nomArchivo = (EditText) findViewById(R.id.nomArchivo);
        txt = (TextView) findViewById(R.id.txt);
        Intent i = getIntent();
        opcion = i.getExtras().getInt(Inicio.OPCION);
        numFArch = i.getExtras().getInt(TIPO_ARCHIVO);
        switch (numFArch) {
            case TIPO_ARCH_LINEA:
                tipoArch = TIPO_TEXTO;
                break;
            case TIPO_ARCH_TAB:
                tipoArch = TIPO_TEXTO;
                break;
            case TIPO_ARCH_COMA:
                tipoArch = TIPO_COMA;
                break;
        }
        txt.setText("." + tipoArch);
        if (opcion != Inicio.EXPORTAR_ARCHIVO) {
            if (opcion != MostrarLista.GUARDAR_DIF_COMPARAR) {
                nomArchivo.setVisibility(View.INVISIBLE);
                txt.setVisibility(View.INVISIBLE);
                nomArchivo.setEnabled(false);
                txt.setEnabled(false);
            }
        }
        verArchivoDirectorio(directorioRaiz);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guardar, menu);
        menu_guardar = (MenuItem) menu.findItem(R.id.menu_save);
        if (opcion != Inicio.EXPORTAR_ARCHIVO) {
            if (opcion != MostrarLista.GUARDAR_DIF_COMPARAR) {
                menu_guardar.setVisible(false);
                menu_guardar.setEnabled(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                if (nomArchivo.getText().toString().isEmpty()) {
                    Recursos.mostrarMensaje(this, R.string.msj_no_filename);
                } else {
                    String dirArchivo = dirActual + "/" + nomArchivo.getText().toString() + "." + tipoArch;
                    guardarArchivo(dirArchivo, 0);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (dirActual.equals(directorioRaiz)) {
            final AlertDialog confSalir = new AlertDialog.Builder(this).create();
            confSalir.setTitle(getTitle());
            confSalir.setMessage(getResources().getString(R.string.msj_conf_cancel));
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
        } else {
            verArchivoDirectorio(listaRutaArchivos.get(0));//Regresa la carpeta
        }
    }

    public static String getTipoArchivo(int numFArch) {
        String tipo = TIPO_TEXTO;
        if(numFArch==2){
            tipo = TIPO_COMA;
        }
        return tipo;
    }

    private void verArchivoDirectorio(String rutaDir) {
        dirActual = rutaDir;
        carpetaActual.setText(getResources().getString(R.string.msj_you_are_in) + rutaDir);
        listaNombresArchivos = new ArrayList<DatExplorar>();
        listaRutaArchivos = new ArrayList<String>();//Ruta de la carpeta actual
        File directorioActual = new File(rutaDir);
        File[] listArchivos = directorioActual.listFiles();//Lista de archivos de la ruta actual
        int x = 0;
        if (!rutaDir.equals(directorioRaiz)) {
            listaNombresArchivos.add(new DatExplorar("../", R.drawable.ic_folder_blue));
            listaRutaArchivos.add(directorioActual.getParent());//Carga la direccion hacia arriba
            x = 1;
        }
        for (File archivos : listArchivos) {
            if (!archivos.getName().startsWith(".")) {
                listaRutaArchivos.add(archivos.getPath());//Se carga la direccion de los Archivos o directorios
            }
        }
        Collections.sort(listaRutaArchivos, String.CASE_INSENSITIVE_ORDER);//Se ordena la Ruta de archivos
        for (int i = x; i < listaRutaArchivos.size(); i++) {//Se inicia un ciclo para separar archivos de carpetas
            //Se crea instancia para optener el nombre del archivo
            File archivo = new File(listaRutaArchivos.get(i));
            if (archivo.isFile()) {
                DatExplorar datExplorar = new DatExplorar();
                datExplorar.setDirArchivo(archivo.getName());
                if (archivo.getName().endsWith(TIPO_COMA)) {
                    datExplorar.setImg(R.drawable.ic_text_csv);
                } else {
                    if (archivo.getName().endsWith(TIPO_TEXTO)) {
                        datExplorar.setImg(R.drawable.ic_text_txt);
                    } else {
                        datExplorar.setImg(R.drawable.ic_office_document);
                    }
                }
                listaNombresArchivos.add(datExplorar);//Si es archivo se pasa igual
            } else {
                listaNombresArchivos.add(new DatExplorar(archivo.getName(), R.drawable.ic_folder_blue));//Si es directorio se agrega una diagonal
            }
        }
        if (listArchivos.length < 1) {//Si la carpeta esta vacia se agrega un mensaje
            listaNombresArchivos.add(new DatExplorar(getResources().getString(R.string.msj_no_archive),
                    R.drawable.ic_office_document));
            listaRutaArchivos.add(rutaDir);
        }
        //Se agregan los datos a la listView con un adaptador
        adaptador = new ArrayAdapterExplorar(this, listaNombresArchivos);
        listDir.setAdapter(adaptador);
        listDir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                archivoFinal = new File(listaRutaArchivos.get(position));
                if (archivoFinal.isFile()) {//Si es un archivo el seleccionado lo regresa en un string
                    if (archivoFinal.getName().endsWith(tipoArch)) {
                        switch (opcion) {
                            case Inicio.EXPORTAR_ARCHIVO:
                                nomArchivo.setText(archivoFinal.getName().substring(0, archivoFinal.getName().length() - 4));
                                break;
                            case MostrarLista.GUARDAR_DIF_COMPARAR:
                                nomArchivo.setText(archivoFinal.getName().substring(0, archivoFinal.getName().length() - 4));
                                break;
                            case Inicio.COMPARAR_LISTA:
                                String[] arrayTitulos = LeerArchivo.getPrimeraLinea(archivoFinal.getAbsolutePath(), numFArch);
                                if (arrayTitulos.length > 0) {
                                    Intent selecColumnas = new Intent(Explorar.this, ColumnSelect.class);
                                    selecColumnas.putExtra(ColumnSelect.TITULOS, arrayTitulos);
                                    selecColumnas.putExtra(Inicio.OPCION, opcion);
                                    startActivityForResult(selecColumnas, Inicio.COMPARAR_LISTA);
                                }
                                break;
                            case Inicio.NVA_BASE:
                                Intent i = getIntent();
                                i.putExtra(DIRECCION_ARCHIVO, archivoFinal.getAbsolutePath());
                                setResult(RESULT_OK, i);
                                finish();
                                break;
                        }
                    } else {
                        Recursos.mostrarMensaje(Explorar.this, R.string.msj_unsupported_file);
                        nomArchivo.setText("");
                    }
                } else {//Si es una carpeta la se pasa a esta
                    verArchivoDirectorio(listaRutaArchivos.get(position));//Muestra la sig carpeta
                }
            }
        });
    }

    public void guardarArchivo(String dirArchivo, int num) {
        final int numArch = num;
        File comparar = new File(dirArchivo);
        if (comparar.exists()) {
            if (num == 0) {
                final AlertDialog archExist = new AlertDialog.Builder(this).create();
                archExist.setMessage(getResources().getString(R.string.msj_exists_filename_ini) +
                        " " + nomArchivo.getText().toString() + "." + tipoArch +
                        " " + getResources().getString(R.string.msj_exists_filename_fin));
                archExist.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_overriding),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                archExist.dismiss();
                                Intent i = getIntent();
                                i.putExtra(DIRECCION_ARCHIVO, dirActual + "/" + nomArchivo.getText().toString() + "." + tipoArch);
                                i.putExtra(TIPO_ARCHIVO, numFArch);
                                setResult(RESULT_OK, i);
                                finish();
                            }
                        });
                archExist.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_rename),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                guardarArchivo(dirActual + "/" + nomArchivo.getText().toString() + "(" + (numArch + 1) + ")."
                                        + tipoArch, numArch + 1);
                            }
                        });
                archExist.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        archExist.dismiss();
                    }
                });
                archExist.show();
            } else {
                guardarArchivo(dirActual + "/" + nomArchivo.getText().toString() + "(" + (numArch + 1) + ")" + tipoArch, numArch + 1);
            }
        } else {
            Intent i = getIntent();
            i.putExtra(DIRECCION_ARCHIVO, dirArchivo);
            i.putExtra(TIPO_ARCHIVO, numFArch);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (opcion) {
                case Inicio.COMPARAR_LISTA:
                    int[] numColumn = data.getExtras().getIntArray(ColumnSelect.NUM_COLUMN);
                    Intent reg = getIntent();
                    reg.putExtra(ColumnSelect.NUM_COLUMN, numColumn);
                    reg.putExtra(DIRECCION_ARCHIVO, archivoFinal.getAbsolutePath());
                    reg.putExtra(TIPO_ARCHIVO, numFArch);
                    setResult(RESULT_OK, reg);
                    finish();
                    break;
            }
        }

    }


}