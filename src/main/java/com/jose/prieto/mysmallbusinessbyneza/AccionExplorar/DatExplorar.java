package com.jose.prieto.mysmallbusinessbyneza.AccionExplorar;

/**
 * Created by Jose on 20/05/2016.
 */
public class DatExplorar {

    private String dirArchivo;
    private int img;

    public DatExplorar(){;};

    public DatExplorar(String dirArchivo, int img){
        this.dirArchivo = dirArchivo;
        this.img = img;
    }

    public void setDirArchivo(String dirArchivo){
        this.dirArchivo = dirArchivo;
    }
    public void setImg(int img){
        this.img = img;
    }
    public String getDirArchivo(){
        return dirArchivo;
    }
    public int getImg(){
        return img;
    }

}
