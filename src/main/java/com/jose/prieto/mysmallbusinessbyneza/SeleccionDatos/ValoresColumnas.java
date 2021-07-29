package com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos;

import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.LeerTitulos;

import java.util.ArrayList;

/**
 * Created by Jose on 27/04/2016.
 */
public class ValoresColumnas {

    /*private ArrayList<Columna> titColumnas;

    public ValoresColumnas(LeerTitulos titulos){//Constructor para Exportar Datos
        titColumnas = new ArrayList<Columna>();
        if(titulos.getPrecio()){
            Columna columna = new Columna();
            columna.setNombre("PRECIO");
            columna.setChecked(false);
            titColumnas.add(columna);
        }
        if(titulos.getEan()){
            Columna columna = new Columna();
            columna.setNombre("EAN");
            columna.setChecked(false);
            titColumnas.add(columna);
        }
        for(int n=0; n<titulos.getDatosXtras().length;n++){
            Columna columna = new Columna();
            columna.setNombre(titulos.getDatosXtras()[n]);
            columna.setChecked(false);
            titColumnas.add(columna);
        }
    }
    public ValoresColumnas(ArrayList<OrdenColumnas> ordenCol){//Metodo para Importar Datos
        titColumnas = new ArrayList<Columna>();
        for(int n=0; n<ordenCol.size();n++){
            Columna columna = new Columna();
            columna.setNombre(ordenCol.get(n).getNombre());
            columna.setChecked(false);
            titColumnas.add(columna);
        }
    }

    public ArrayList<Columna> getTitulos(){//Metodo par exportar Datos
        return titColumnas;
    }*/

    public static ArrayList<Columna> getValoresColumnas(LeerTitulos titulos){//Constructor para Exportar Datos
        ArrayList<Columna> titColumnas = new ArrayList<Columna>();
        if(titulos.getPrecio()){
            Columna columna = new Columna();
            columna.setNombre("PRECIO");
            columna.setChecked(false);
            titColumnas.add(columna);
        }
        if(titulos.getEan()){
            Columna columna = new Columna();
            columna.setNombre("EAN");
            columna.setChecked(false);
            titColumnas.add(columna);
        }
        for(int n=0; n<titulos.getDatosXtras().length;n++){
            Columna columna = new Columna();
            columna.setNombre(titulos.getDatosXtras()[n]);
            columna.setChecked(false);
            titColumnas.add(columna);
        }
        return titColumnas;
    }

    public static ArrayList<Columna> getValoresColumnas(ArrayList<OrdenColumnas> ordenCol){
        ArrayList<Columna> titColumnas = new ArrayList<Columna>();
        for(int n=0; n<ordenCol.size();n++){
            Columna columna = new Columna();
            columna.setNombre(ordenCol.get(n).getNombre());
            columna.setChecked(false);
            titColumnas.add(columna);
        }
        return titColumnas;
    }

}
