package com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.Inicio;
import com.jose.prieto.mysmallbusinessbyneza.R;

/**
 * Created by Jose on 26/09/2016.
 */

public class AdapterInicio extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    String [] titulo;
    String [] datos;

    public AdapterInicio(Context context, String []titulo, String [] datos){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.titulo = titulo;
        this.datos = datos;
    }

    @Override
    public int getCount() {
        return datos.length;
    }

    @Override
    public Object getItem(int position) {
        return datos[position];
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
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground2));
            fila.setTitulo((TextView) convertView.findViewById(R.id.text_doble_titulo));
            fila.setDato((TextView) convertView.findViewById(R.id.text_doble_sub_titulo));
            convertView.setTag(fila);
        }else{
            fila = (Fila) convertView.getTag();
        }
        fila.getTitulo().setText(titulo[position].replace('_',' '));//Se agregan las columnas encontradas
        fila.getDato().setText(datos[position]);//Se agregan los datos encontrados
        return convertView;
    }

    private class Fila{
        TextView titulo;
        TextView dato;
        public TextView getTitulo(){return titulo;}
        public void setTitulo(TextView titulo){
            this.titulo = titulo;
        }
        public TextView getDato(){return dato;}
        public void setDato(TextView dato){
            this.dato = dato;
        }
    }
}
