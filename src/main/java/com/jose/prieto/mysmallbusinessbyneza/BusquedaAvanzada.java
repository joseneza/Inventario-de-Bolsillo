package com.jose.prieto.mysmallbusinessbyneza;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.LeerTitulos;
import com.jose.prieto.mysmallbusinessbyneza.BusqAvanzada.BusquedaAvanz;
import com.jose.prieto.mysmallbusinessbyneza.BusqAvanzada.ColBusqAvanzada;

import java.util.ArrayList;

public class BusquedaAvanzada extends AppCompatActivity {
    private ListView listBusqueda;
    private ArrayList<ColBusqAvanzada> columnas;
    public static final String No_ARTICULO = "articulo";
    //Se cargan los titulos
    private LeerTitulos titulos;
    private MenuItem menu_search;
    public static final String RESULTADOS_DE_BUSQUEDA_ARTICULO = "resultados articulo";
    public static final String RESULTADOS_DE_BUSQUEDA_DESCRIPCION = "resultados descripcion";
    String msg = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_avanzada);
        setTitle(R.string.action_advanced_search);
        titulos = new LeerTitulos(this);
        listBusqueda = (ListView) findViewById(R.id.listBusquedaAvanz);
        columnas = cargarTitulos(titulos.getDatosXtras());
        AdaptadorBusquedaAvanz adapterAvanz = new AdaptadorBusquedaAvanz();
        listBusqueda.setAdapter(adapterAvanz);
    }
    @Override
    public void onBackPressed(){
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(R.string.action_advanced_search);
        dialog.setMessage(getResources().getString(R.string.msj_conf_exit_advance_find));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.btn_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buscar, menu);
        menu_search = (MenuItem) menu.findItem(R.id.menu_search);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                new AvanceBusqueda().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<ColBusqAvanzada> cargarTitulos(String[] titulosXtras){
        ArrayList<ColBusqAvanzada> columnas = new ArrayList<ColBusqAvanzada>();
        for (int n = 0; n < titulosXtras.length; n++){
            ColBusqAvanzada columna = new ColBusqAvanzada();
            columna.setSeleccion(false);
            columna.setTitulo(titulosXtras[n]);
            columna.setDato("");
            columna.setColumna(n);
            columnas.add(columna);
        }
        ColBusqAvanzada columna = new ColBusqAvanzada();
        columna.setSeleccion(false);
        columna.setTitulo(getResources().getString(R.string.column_descrip));
        columna.setDato("");
        columna.setColumna(columnas.size());
        columnas.add(columna);
        return columnas;
    }

    private class AdaptadorBusquedaAvanz extends BaseAdapter {//Adaptador para la ListView de la actividad

        private LayoutInflater layoutInflater;

        public AdaptadorBusquedaAvanz() {
            layoutInflater = LayoutInflater.from(BusquedaAvanzada.this);
        }

        @Override
        public int getCount() {
            return columnas.size();
        }

        @Override
        public Object getItem(int position) {
            return columnas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup grupoView){
            Fila fila = null;
            if(convertView == null){
                fila = new Fila();
                convertView = layoutInflater.inflate(R.layout.col_busqueda_avanzada, null);
                fila.setCheckBox((CheckBox) convertView.findViewById(R.id.chBoxTituloBusqAvanz));
                fila.setEditText((EditText) convertView.findViewById(R.id.editValorBusqAvanz));
                convertView.setTag(fila);
            }else{
                fila = (Fila) convertView.getTag();
            }
            fila.referencia = position;
            final ColBusqAvanzada columna = (ColBusqAvanzada) getItem(position);
            final int numColumna = columna.getColumna();
            fila.getCheckBox().setText(columna.getTitulo().replace('_', ' '));//Se agrega nombre al CheckBox
            //Se indican estas instancias para que no se duplique los valores del CheckBox y EditText
            fila.getCheckBox().setChecked(columnas.get(numColumna).getSeleccion());
            if(fila.getCheckBox().isChecked()){
                fila.getEditText().setVisibility(View.VISIBLE);
            }else{
                fila.getEditText().setVisibility(View.INVISIBLE);
            }
            final Fila finalFila = fila;
            fila.getCheckBox().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalFila.getCheckBox().isChecked()) {
                        columnas.get(numColumna).setSeleccion(true);
                        finalFila.getEditText().setVisibility(View.VISIBLE);
                        finalFila.getEditText().requestFocus();
                    } else {
                        finalFila.getEditText().setVisibility(View.INVISIBLE);
                        columnas.get(numColumna).setSeleccion(false);
                    }
                }
            });
            fila.getEditText().setText(columna.getDato());
            fila.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    columnas.get(finalFila.referencia).setDato(s.toString());
                }
            });
            return convertView;
        }

    }

    private static class Fila{//Subclase para manejar las filas seleccionadas
        CheckBox checkBox;
        EditText editText;
        public CheckBox getCheckBox(){ return checkBox; }
        public void setCheckBox(CheckBox checkBox){ this.checkBox = checkBox; }
        public EditText getEditText(){ return editText; }
        public void setEditText(EditText editText){ this.editText = editText; }
        int referencia;//Captura el numero de columna
    }

    private void mostrarMensaje(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String no_articulo = data.getExtras().getString(No_ARTICULO);
            Intent i = getIntent();
            i.putExtra(No_ARTICULO, no_articulo);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    private class AvanceBusqueda extends AsyncTask<Void, Void, Boolean>{
        //AsyncTask para crear un avance de busqueda
        ProgressDialog pDialog;
        BusquedaAvanz buscar;
        ArrayList<ColBusqAvanzada> nvaColumnas;
        String arrayArtuculo [] = null;
        String arrayDescripcion [] = null;
        String mensajeError;

        @Override
        protected void onPreExecute() {//Preparacion de avance
            // TODO Auto-generated method stub
            super.onPreExecute();
            nvaColumnas = new ArrayList<ColBusqAvanzada>();
            for(int n = 0; n < columnas.size(); n++){
                if(columnas.get(n).getSeleccion() && !columnas.get(n).getDato().isEmpty()){
                    nvaColumnas.add(columnas.get(n));
                }
            }
            pDialog = new ProgressDialog(BusquedaAvanzada.this);
            pDialog.setMessage(getResources().getString(R.string.msj_find));
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
        protected Boolean doInBackground(Void... params) {//Metodo para la operacion
            if(!nvaColumnas.isEmpty()){
                buscar = new BusquedaAvanz(BusquedaAvanzada.this, nvaColumnas, titulos.getDatosXtras());
                if(buscar.getFila().moveToFirst()){
                    do{
                        buscar.buscarCoincidencias();
                    }while (buscar.getFila().moveToNext() && !isCancelled());
                }
                buscar.cerrarTabla();
                arrayArtuculo = buscar.getResultadoArticulo();
                arrayDescripcion = buscar.getResultadoDescripcion();
            }else{
                mensajeError = getResources().getString(R.string.msj_is_not_selected_value);
                return false;
            }
            if(arrayArtuculo==null){
                mensajeError = getResources().getString(R.string.msj_no_match_was_found_not);
                return false;
            }else{
                return true;
            }
        }
        @Override
        protected void onPostExecute(Boolean par) {//Metodo al finalizar la operacion
            pDialog.dismiss();
            if (par) {
                if (arrayArtuculo.length == 1) {
                    //Para un solo valor regresa a la actividad principal para mostrarla
                    Intent i = getIntent();
                    i.putExtra(No_ARTICULO, arrayArtuculo[0]);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    if (arrayArtuculo.length > 1) {
                    /*Para mas de uno lanza otra actividad (ResultadosBusqueda)
                    para mostrar los resultados*/
                        Intent resultBusqueda = new Intent(BusquedaAvanzada.this, ResultadosBusqueda.class);
                        resultBusqueda.putExtra(RESULTADOS_DE_BUSQUEDA_ARTICULO, arrayArtuculo);
                        resultBusqueda.putExtra(RESULTADOS_DE_BUSQUEDA_DESCRIPCION, arrayDescripcion);
                        startActivityForResult(resultBusqueda, Inicio.BUSQUEDA_AVANZADA);
                    }
                }
            }else{
                mostrarMensaje(mensajeError);
            }
        }

        @Override
        protected void onCancelled(){
            pDialog.dismiss();
            mostrarMensaje(getResources().getString(R.string.msj_find_cancel));
        }
    }
}

