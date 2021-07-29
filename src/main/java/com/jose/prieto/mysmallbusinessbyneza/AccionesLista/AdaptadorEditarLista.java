package com.jose.prieto.mysmallbusinessbyneza.AccionesLista;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.R;

import java.util.ArrayList;

/**
 * Created by jose on 28/10/16.
 */

public class AdaptadorEditarLista extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<LineaDLista> listaArt;
    private Context context;
    public AdaptadorEditarLista(Context context, ArrayList<LineaDLista> listaArt){
        this.listaArt = listaArt;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaArt.size();
    }

    @Override
    public Object getItem(int i) {
        return listaArt.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Fila fila = null;
        if(view == null){
            fila = new Fila();
            view = layoutInflater.inflate(R.layout.linea_edit_lista, null);
            fila.setArt((TextView) view.findViewById(R.id.edt_linea_Art));
            fila.setCant((EditText) view.findViewById(R.id.edt_linea_Cant));
            fila.setDescrip((TextView) view.findViewById(R.id.edt_linea_Descr));
            view.setTag(fila);
        }else{
            fila = (Fila) view.getTag();
        }
        fila.setReferencia(i);
        final LineaDLista lineaDLista = (LineaDLista) getItem(i);
        final int numLinea = lineaDLista.getConsec();
        fila.getArt().setText(lineaDLista.getArt());
        fila.getCant().setText(lineaDLista.getCant());
        fila.getDescrip().setText(lineaDLista.getDescrip());
        final Fila finalFila = fila;
        fila.getCant().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                int cantFila = 0;
                try {
                    cantFila = Integer.parseInt(s.toString());
                } catch (NumberFormatException ex) {}
                listaArt.get(finalFila.getReferencia()).setCant(Integer.toString(cantFila));
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalFila.getCant().requestFocus();
            }
        });
        view.setMinimumHeight(65);
        return view;
    }
    private static class Fila{//Subclase para manejar las filas seleccionadas
        private TextView art;
        private EditText cant;
        private TextView descrip;
        private int referencia;

        public TextView getArt(){ return art; }
        public void setArt(TextView art){ this.art = art; }

        public EditText getCant(){ return cant; }
        public void setCant(EditText cant){ this.cant = cant; }

        public TextView getDescrip(){ return descrip; }
        public void setDescrip(TextView descrip){ this.descrip = descrip; }

        public int getReferencia(){
            return referencia;
        }
        public void setReferencia(int referencia){
            this.referencia = referencia;
        }




    }
}
