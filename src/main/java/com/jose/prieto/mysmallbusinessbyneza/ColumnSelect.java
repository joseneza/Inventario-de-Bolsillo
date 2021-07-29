package com.jose.prieto.mysmallbusinessbyneza;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.AdapterColumnSelect;
import com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos.OrdenColumnas;

import java.util.ArrayList;

public class ColumnSelect extends AppCompatActivity {

    public static final String TITULOS = "titulos";
    public static final String NUM_COLUMN = "numColumn";
    TextView msjselectColumn;
    ListView listViewColumn;
    AdapterColumnSelect adapter;
    String [] titulosColumnas;
    Toolbar toolbar;
    MenuItem menu_skip;
    int consColumn = 0;
    int columnArt = -1;
    int columnCant = -1;
    int columnDescrip = -1;
    int columnEan = -1;
    int columnUrlImg = -1;
    int columnPrecioAnt = -1;
    int columnPrecio = -1;
    int maxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_select);
        toolbar = (Toolbar) findViewById(R.id.toolbar_column); // Conectamos el Layout al objeto Toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(consColumn == 0){
                    finalizarActivity();
                } else {
                    selectBack();
                }
            }
        });
        msjselectColumn = (TextView) findViewById(R.id.column_select_TextView);
        listViewColumn = (ListView) findViewById(R.id.column_select_ListView);
        Intent i = getIntent();
        titulosColumnas = i.getExtras().getStringArray(TITULOS);
        int tipoAct = i.getExtras().getInt(Inicio.OPCION);
        if(tipoAct==Inicio.NVA_BASE){
            maxi = 5;
        } else {
            if(tipoAct == Inicio.COMPARAR_LISTA){
                maxi = 1;
            }
        }
        msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for)+" "+
                getResources().getString(R.string.column_art));
        cargarLista(OrdenColumnas.getArrayOrdenColumnas(titulosColumnas));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skip, menu);
        menu_skip = (MenuItem) menu.findItem(R.id.menu_sig);
        menu_skip.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sig:
                selectSckip();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        finalizarActivity();
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

    private void cargarLista(final ArrayList<OrdenColumnas> titulosLista){
        if(consColumn > 2) {
            menu_skip.setVisible(true);
        }
        adapter = new AdapterColumnSelect(this, titulosLista);
        listViewColumn.setAdapter(adapter);
        listViewColumn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrdenColumnas linea = titulosLista.get(position);
                switch (consColumn){
                    case 0:
                        columnArt = linea.getConsecutivo();
                        msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for)+" "+
                                getResources().getString(R.string.column_cant));
                        break;
                    case 1:
                        columnCant = linea.getConsecutivo();
                        if(maxi > 2){
                            msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for)+" "+
                                    getResources().getString(R.string.column_descrip));
                        } else {
                            msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for)+" "+
                                    getResources().getString(R.string.column_reference));
                        }
                        break;
                    case 2:
                        columnDescrip = linea.getConsecutivo();
                        msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for)+" "+
                                getResources().getString(R.string.column_ean));
                        break;
                    case 3:
                        columnEan = linea.getConsecutivo();
                        msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for)+" "+
                                getResources().getString(R.string.column_imgUrl));
                        break;
                    case 4:
                        columnUrlImg = linea.getConsecutivo();
                        msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for)+" "+
                                getResources().getString(R.string.column_precio_ant));
                        break;
                    case 5:
                        columnPrecioAnt = linea.getConsecutivo();
                        msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for)+" "+
                                getResources().getString(R.string.column_precio));
                        break;
                    case 6:
                        columnPrecio = linea.getConsecutivo();
                        break;
                }
                titulosLista.remove(position);
                if(titulosLista.size()>0 && consColumn > maxi){
                    finalizarActividad();
                }else{
                    consColumn++;
                    cargarLista(titulosLista);
                }
            }
        });

    }

    private void finalizarActividad(){
        int [] numColum = {columnArt, columnCant, columnDescrip, columnEan, columnUrlImg, columnPrecioAnt, columnPrecio};
        Intent i = getIntent();
        i.putExtra(NUM_COLUMN, numColum);
        setResult(RESULT_OK, i);
        finish();
    }

    public void columnSelectBack(View v) {
        selectBack();
    }

    private void selectBack(){
        consColumn = 0;
        menu_skip.setVisible(false);
        msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for)+" "+
                getResources().getString(R.string.column_art));
        cargarLista(OrdenColumnas.getArrayOrdenColumnas(titulosColumnas));
    }

    public void columnSelectSkip(View v) {
        selectSckip();
    }

    private void selectSckip(){
        switch (consColumn) {
            case 0:
                msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for) + " " +
                        getResources().getString(R.string.column_cant));
                break;
            case 1:
                msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for) + " " +
                        getResources().getString(R.string.column_descrip));
                break;
            case 2:
                msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for) + " " +
                        getResources().getString(R.string.column_ean));
                break;
            case 3:
                msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for) + " " +
                        getResources().getString(R.string.column_imgUrl));
                break;
            case 4:
                msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for) + " " +
                        getResources().getString(R.string.column_precio_ant));
                break;
            case 5:
                msjselectColumn.setText(getResources().getString(R.string.msj_select_the_column_for) + " " +
                        getResources().getString(R.string.column_precio));
                break;
        }
        if(consColumn > maxi){
            finalizarActividad();
        }else{
            consColumn++;
        }
    }


}
