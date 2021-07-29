package com.jose.prieto.mysmallbusinessbyneza.SeleccionDatos;

/**
 * Created by Jose on 24/04/2016.
 *
 */
public class Columna {

    private String nombre;
    private Boolean checked;

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public void setChecked(Boolean checked){
        this.checked = checked;
    }

    public Boolean getchecked(){
        return checked;
    }

}
