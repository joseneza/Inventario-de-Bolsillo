package com.jose.prieto.mysmallbusinessbyneza;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.BuscarArticulo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;


public class ResultadosBusqueda extends AppCompatActivity {

    String [] articulos;
    String [] descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_busqueda);
        setTitle(R.string.action_advanced_search_result);
        Intent i = getIntent();
        articulos= i.getStringArrayExtra(BusquedaAvanzada.RESULTADOS_DE_BUSQUEDA_ARTICULO);
        descripcion=i.getStringArrayExtra(BusquedaAvanzada.RESULTADOS_DE_BUSQUEDA_DESCRIPCION);
        ListView listView = (ListView) findViewById(R.id.listViewResultadoBusqueda);
        ResultadoArrayAdapter adapter = new ResultadoArrayAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String no_articulo = articulos[position];
                BuscarArticulo busca = new BuscarArticulo(ResultadosBusqueda.this,
                        Long.parseLong(no_articulo));
                if (!busca.getUrlImg().isEmpty()) {
                    preguntaImagen(Long.toString(busca.getCodigo()), busca.getDescripcion(), busca.getUrlImg()).show();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String no_articulo = articulos[position];
                Intent i = getIntent();
                i.putExtra(BusquedaAvanzada.No_ARTICULO, no_articulo);
                setResult(RESULT_OK, i);
                finish();
                return true;
            }
        });
        if(articulos.length >= 4000){
            Recursos.mostrarMensaje(this, R.string.msj_many_matches);
        }
    }
    @Override
    public void onBackPressed(){
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getTitle());
        dialog.setMessage(getResources().getString(R.string.msj_exit_without_selecting_an_option));
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

    private AlertDialog preguntaImagen(final String codigo, final String descripcion, final String urlImg){
        final AlertDialog pregunta = new AlertDialog.Builder(this).create();
        pregunta.setTitle(R.string.label_mostrar_imagen);
        pregunta.setMessage(getResources().getString(R.string.msj_question_image_art));
        pregunta.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.btn_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mostrarImagen = new Intent(ResultadosBusqueda.this, MostrarImagen.class);
                        mostrarImagen.putExtra(MostrarImagen.SKU, codigo);
                        mostrarImagen.putExtra(MostrarImagen.DESCRIPCION, descripcion);
                        mostrarImagen.putExtra(MostrarImagen.URL, urlImg);
                        startActivity(mostrarImagen);
                        pregunta.dismiss();
                    }
                });
        pregunta.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.btn_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pregunta.dismiss();
                    }
                });
        return pregunta;
    }

    class ResultadoArrayAdapter extends BaseAdapter {
        LayoutInflater layoutInflater = LayoutInflater.from(ResultadosBusqueda.this);

        @Override
        public int getCount() {
            return articulos.length;
        }

        @Override
        public Object getItem(int position) {
            return articulos[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Fila fila = null;
            if(convertView == null){
                fila = new Fila();
                convertView = layoutInflater.inflate(R.layout.linea_doble, null);
                fila.setArticulo((TextView) convertView.findViewById(R.id.text_doble_titulo));
                fila.setDescripcion((TextView) convertView.findViewById(R.id.text_doble_sub_titulo));
                convertView.setBackgroundColor(ContextCompat.getColor(ResultadosBusqueda.this, R.color.colorBackground2));
                convertView.setTag(fila);
            }else{
                fila = (Fila) convertView.getTag();
            }
            fila.getArticulo().setText(articulos[position]);//Se agrega numero de SKU
            fila.getDescripcion().setText(descripcion[position]);//Se agrega la descripcion
            return convertView;
        }
    }

    class Fila{
        TextView articulo;
        TextView descripcion;
        public TextView getArticulo(){return articulo;}
        public void setArticulo(TextView articulo){
            this.articulo = articulo;
        }
        public TextView getDescripcion(){return descripcion;}
        public void setDescripcion(TextView descripcion){
            this.descripcion = descripcion;
        }
    }
}
