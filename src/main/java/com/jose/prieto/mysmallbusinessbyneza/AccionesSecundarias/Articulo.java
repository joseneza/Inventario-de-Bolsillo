package com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias;

/**
 * Created by Jose on 05/03/2016.
 */
public class Articulo {

    private long sku;
    private String ean;
    private String descipcion;
    private int exist;
    private String precio;
    private String precioAnt;
    private String urlImg;
    private String datosXtras;
    private String referencia = "";
    private int cantXtra = 0;
    private int dif = 0;

    public Articulo(long sku, String ean, String descipcion, int exist, String precio, String precioAnt,
                    String urlImg, String datosXtras){
        this.sku=sku;
        this.ean=ean;
        this.descipcion=descipcion;
        this.exist=exist;
        this.precio=precio;
        this.precioAnt=precioAnt;
        this.urlImg=urlImg;
        this.datosXtras=datosXtras;
    }

    public Articulo(){
        this.sku = 0;
        this.ean = "";
        this.descipcion="";
        this.exist = 0;
        this.precio = "";
        this.precioAnt = "";
        this.urlImg = "";
        this.datosXtras = "";
    }

    public void setSku(long sku){
        this.sku=sku;
    }

    public long getSku(){ return sku; }

    public void setEan(String ean){
        this.ean=ean;
    }

    public String getEan(){
        return ean;
    }

    public void setDescipcion(String descipcion){
        this.descipcion=descipcion;
    }

    public String getDescipcion(){
        return descipcion;
    }

    public void setExist(int exist){
        this.exist=exist;
    }

    public int getExist(){
        return exist;
    }

    public void setPrecio(String precio){
        this.precio=precio;
    }

    public String getPrecio(){
        return precio;
    }

    public void setPrecioAnt(String precioAnt){ this.precioAnt=precioAnt; }

    public String getPrecioAnt(){ return precioAnt; }

    public void setUrlImg(String urlImg){
        this.urlImg=urlImg;
    }

    public String getUrlImg(){
        return urlImg;
    }

    public void setDatosXtras(String datosXtras){
        this.datosXtras=datosXtras;
    }

    public String getDatosXtras(){ return datosXtras; }

    public void setReferencia(String referencia){
        this.referencia = referencia;
    }

    public String getReferencia(){
        return referencia;
    }

    public int getCantXtra(){
        return cantXtra;
    }

    public void setCantXtra(int cantXtra){
        this.cantXtra = cantXtra;
    }

    public int getDif(){
        return dif;
    }

    public void setDif(int dif){
        this.dif = dif;
    }

}
