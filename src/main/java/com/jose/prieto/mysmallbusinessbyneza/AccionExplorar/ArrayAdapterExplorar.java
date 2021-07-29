package com.jose.prieto.mysmallbusinessbyneza.AccionExplorar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jose.prieto.mysmallbusinessbyneza.R;

import java.util.ArrayList;

/**
 * Created by Jose on 20/05/2016.
 */
public class ArrayAdapterExplorar extends ArrayAdapter<DatExplorar> {
    private Context context;
    private LayoutInflater layoutInflater;
    public ArrayAdapterExplorar(Context context, ArrayList<DatExplorar> datExplorar) {
        super(context, 0, datExplorar);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(final int posision, View convertView, ViewGroup grupoView){
        Fila fila = null;
        if(convertView == null){
            fila = new Fila();
            convertView = layoutInflater.inflate(R.layout.col_explorar, null);
            fila.setTextView((TextView) convertView.findViewById(R.id.txtDir));
            fila.setImageView((ImageView) convertView.findViewById(R.id.imgDir));
            convertView.setTag(fila);
        }else{
            fila = (Fila) convertView.getTag();
        }
        final DatExplorar datExplorar = getItem(posision);
        fila.getTextView().setText(datExplorar.getDirArchivo());//Se agrega nombre al CheckBox
        fila.getImageView().setImageResource(datExplorar.getImg());
        return convertView;
    }

    static class Fila{//Subclase para manejar las filas seleccionadas
        TextView textView;
        ImageView imageView;
        public TextView getTextView(){ return textView; }
        public void setTextView(TextView textView){ this.textView = textView; }
        public ImageView getImageView(){ return imageView; }
        public void setImageView(ImageView imageView){ this.imageView = imageView; }
    }

}
