package com.jose.prieto.mysmallbusinessbyneza.AccionesLista;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal.BuscarArticulo;
import com.jose.prieto.mysmallbusinessbyneza.R;

/**
 * Created by Jose on 23/09/2016.
 */
public class AdaptadorCompararLista extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private String [] columnArt;
    private String [] columnLista;
    private String [] columnComparador;
    private String [] columnDif;

    public AdaptadorCompararLista(Context context, String [] columnArt, String [] columnLista,
                                  String [] columnComparador, String [] columnDif){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.columnArt = columnArt;
        this.columnLista = columnLista;
        this.columnComparador = columnComparador;
        this.columnDif = columnDif;
    }
    @Override
    public int getCount() {
        return columnArt.length;
    }

    @Override
    public Object getItem(int position) {
        return columnArt[position];
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
            convertView = layoutInflater.inflate(R.layout.col_resultados_comparar, null);
            fila.setTextViewArt((TextView) convertView.findViewById(R.id.col_resultados_comparar_Art));
            fila.setTextViewLista((TextView) convertView.findViewById(R.id.col_resultados_comparar_Lista));
            fila.setTextViewComparador((TextView) convertView.findViewById(R.id.col_resultados_comparar_Comparador));
            fila.setTextViewDif((TextView) convertView.findViewById(R.id.col_resultados_comparar_dif));
            fila.setTextViewDescripcion((TextView) convertView.findViewById(R.id.col_resultados_comparar_Descrip));
            convertView.setTag(fila);
        }else{
            fila = (Fila) convertView.getTag();
        }
        fila.getTextViewArt().setText(columnArt[position]);
        fila.getTextViewLista().setText(columnLista[position]);
        fila.getTextViewComparador().setText(columnComparador[position]);
        fila.getTextViewDif().setText(columnDif[position]);
        BuscarArticulo buscar = new BuscarArticulo(context, Long.parseLong(columnArt[position]));
        if(buscar.buscar){
            fila.getTextViewDescripcion().setText(buscar.getDescripcion());
        } else {
            fila.getTextViewDescripcion().setText("");
        }
        if (Integer.parseInt(fila.getTextViewDif().getText().toString())>0) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground2));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorNegativo));
        }
        return convertView;
    }

    static class Fila{//Subclase para manejar las filas seleccionadas
        TextView textViewArt;
        TextView textViewLista;
        TextView textViewComparador;
        TextView textViewDif;
        TextView textViewDescripcion;
        public TextView getTextViewArt(){ return textViewArt; }
        public void setTextViewArt(TextView textViewArt){ this.textViewArt = textViewArt; }
        public TextView getTextViewLista(){ return textViewLista; }
        public void setTextViewLista(TextView textViewLista){ this.textViewLista = textViewLista; }
        public TextView getTextViewComparador(){ return textViewComparador; }
        public void setTextViewComparador(TextView textViewComparador){ this.textViewComparador = textViewComparador; }
        public TextView getTextViewDif(){ return textViewDif; }
        public void setTextViewDif(TextView textViewDif){ this.textViewDif = textViewDif; }
        public TextView getTextViewDescripcion(){ return textViewDescripcion; }
        public void setTextViewDescripcion(TextView textViewDescripcion){ this.textViewDescripcion = textViewDescripcion; }
    }
}
