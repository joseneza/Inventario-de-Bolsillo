package com.jose.prieto.mysmallbusinessbyneza.AccionesLista;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.R;

import java.util.ArrayList;

/**
 * Created by Jose on 23/09/2016.
 */
public class AdaptadorMostrarLista extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<LineaDLista> listaArt;

    public AdaptadorMostrarLista(Context context, ArrayList<LineaDLista> listaArt){
        this.listaArt = listaArt;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listaArt.size();
    }

    @Override
    public Object getItem(int position) {
        return listaArt.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fila fila = null;
        if(convertView == null){
            fila = new Fila();
            convertView = layoutInflater.inflate(R.layout.linea_lista, null);
            fila.setArt((TextView) convertView.findViewById(R.id.linea_Art));
            fila.setCant((TextView) convertView.findViewById(R.id.linea_Cant));
            fila.setDescrip((TextView) convertView.findViewById(R.id.linea_Descr));
            convertView.setTag(fila);
        }else{
            fila = (Fila) convertView.getTag();
        }
        fila.getArt().setText(listaArt.get(position).getArt());
        fila.getCant().setText(listaArt.get(position).getCant());
        fila.getDescrip().setText(listaArt.get(position).getDescrip());
        convertView.setMinimumHeight(65);
        return convertView;
    }

    private static class Fila{//Subclase para manejar las filas seleccionadas
        TextView art;
        TextView cant;
        TextView descrip;
        public TextView getArt(){ return art; }
        public void setArt(TextView art){ this.art = art; }
        public TextView getCant(){ return cant; }
        public void setCant(TextView cant){ this.cant = cant; }
        public TextView getDescrip(){ return descrip; }
        public void setDescrip(TextView descrip){ this.descrip = descrip; }

    }
}
