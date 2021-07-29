package com.jose.prieto.mysmallbusinessbyneza.AccionesLista;

/**
 * Created by Jose on 10/09/2016.
 */
public class LineaDLista {

    private String art;
    private String cant;
    private String descrip;
    private int consec = -1;

    public LineaDLista(String art, String cant, String descrip){
        this.art = art;
        this.cant = cant;
        this.descrip = descrip;
    }
    public LineaDLista(){
        this.art = "";
        this.cant = "";
        this.descrip = "";
    }

    public String getArt(){
        return art;
    }

    public void setArt(String art){
        this.art = art;
    }

    public String getCant(){
        return cant;
    }

    public void setCant(String cant){
        this.cant = cant;
    }

    public String getDescrip(){
        return descrip;
    }

    public void setDescrip(String descrip){
        this.descrip = descrip;
    }

    public int getConsec(){
        return consec;
    }

    public void setConsec(int consec){
        this.consec = consec;
    }
}
