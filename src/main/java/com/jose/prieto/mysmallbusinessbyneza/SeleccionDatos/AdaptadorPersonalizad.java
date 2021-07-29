package com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jose.prieto.mysmallbusinessbyneza.R;

import java.util.ArrayList;

/**
 * Created by Jose on 24/04/2016.
 * Extension de un arrayAdapter para la Activity Datos a Importar o Exportar
 */
public class AdaptadorPersonalizado extends ArrayAdapter<Columna> {

    private LayoutInflater layoutInflater;
    private Context context;

    public AdaptadorPersonalizado(Context context, ArrayList<Columna> columnas) {
        super(context, 0, columnas);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getView(final int posision, View convertView, ViewGroup grupoView){
        Fila fila = null;
        if(convertView == null){
            fila = new Fila();
            convertView = layoutInflater.inflate(R.layout.columnas, null);
            fila.setCheckBox((CheckBox) convertView.findViewById(R.id.chBoxSelec));
            convertView.setTag(fila);
        }else{
            fila = (Fila) convertView.getTag();
        }
        final Columna columna = getItem(posision);
        fila.getCheckBox().setText(columna.getNombre().replace('_',' '));//Se agrega nombre al CheckBox
        fila.getCheckBox().setChecked(columna.getchecked());
        final Fila finalFila = fila;
        fila.getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalFila.getCheckBox().isChecked()){
                    columna.setChecked(true);
                }else{
                    columna.setChecked(false);
                }
            }
        });
        return convertView;
    }

    static class Fila{//Subclase para manejar las filas seleccionadas
        CheckBox checkBox;
        public CheckBox getCheckBox(){ return checkBox; }
        public void setCheckBox(CheckBox checkBox){ this.checkBox = checkBox; }
    }
}
