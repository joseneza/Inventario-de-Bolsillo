package com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.R;

import java.util.ArrayList;

/**
 * Created by Jose on 21/09/2016.
 */
public class AdapterColumnSelect extends ArrayAdapter<OrdenColumnas>{

    Context context;
    ArrayList<OrdenColumnas> titulos;
    LayoutInflater layoutInflater;

    public AdapterColumnSelect(Context context, ArrayList<OrdenColumnas> titulos) {
        super(context, 0, titulos);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.titulos = titulos;

    }

    @Override
    public View getView(final int posision, View convertView, ViewGroup grupoView){
        Fila fila = null;
        if(convertView == null){
            fila = new Fila();
            convertView = layoutInflater.inflate(R.layout.linea_simple, null);
            fila.setTitulo((TextView) convertView.findViewById(R.id.simple_linea_TextView));
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground2));
            convertView.setTag(fila);
        }else{
            fila = (Fila) convertView.getTag();
        }
        final OrdenColumnas dat = getItem(posision);
        fila.getTitulo().setText(dat.getNombre());//Se agrega nombre al CheckBox
        return convertView;
    }



    class Fila{
        TextView titulo;
        public TextView getTitulo(){return titulo;}
        public void setTitulo(TextView titulo){
            this.titulo = titulo;
        }
    }

}
