package com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal;

import com.jose.prieto.mysmallbusinessbyneza.Explorar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Jose on 05/11/2016.
 */

public class LeerArchivo {
    public static final String SEPARADOR_LINEA = "\\|";
    public static final String SEPARADOR_TAB = "\\t";
    public static final String SEPARADOR_COMA = ",";
    private String separador;
    private BufferedReader leerArch;
    private Boolean lectura = false;
    private int tipoArch;
    private double tamArchivo;
    private double tamLinea;
    private String msjError = "";

    public LeerArchivo(String dir, int tipoArch){
        File archLectura = new File(dir);
        try {
            leerArch = new BufferedReader(new InputStreamReader(new FileInputStream(archLectura)));
            switch (tipoArch){
                case Explorar.TIPO_ARCH_LINEA:
                    separador = SEPARADOR_LINEA;
                    break;
                case Explorar.TIPO_ARCH_TAB:
                    separador = SEPARADOR_TAB;
                    break;
                case Explorar.TIPO_ARCH_COMA:
                    separador = SEPARADOR_COMA;
                    break;
            }
            this.tipoArch = tipoArch;
            tamArchivo = archLectura.length();
            lectura = true;
        } catch (FileNotFoundException e) {
            msjError = e.getMessage();
        }
    }

    public String getMsjError(){
        return msjError;
    }

    public Boolean getLectuta(){
        return lectura;
    }

    public void cerrar(){
        try {
            leerArch.close();
        } catch (IOException e) {}
    }

    public String getLinea(){
        String linea = null;
        try {
            linea = leerArch.readLine();
            if(linea!=null){
                tamLinea = linea.getBytes().length;
                linea = linea.replaceAll("\"", "");
                if(tipoArch!=2)linea = linea.replaceAll(",", "");
            }
        } catch (IOException e) {
            msjError = e.getMessage();
        }
        return linea;
    }

    public String [] getArrayLinea(){
        String [] arraylinea = null;
        try {
            String linea = leerArch.readLine();
            if(linea!=null){
                tamLinea = linea.getBytes().length;
                linea = linea.replaceAll("\"", "");
                if(tipoArch!=2)linea = linea.replaceAll(",", "");
                arraylinea = linea.split(separador);
            }
        } catch (IOException e) {
            msjError = e.getMessage();
        }
        return arraylinea;
    }

    public double getTamArchivo(){
        return tamArchivo;
    }

    public double getTamLinea(){
        return tamLinea;
    }

    public static String getPrimeraLinea(String dir){
        String separador;
        String linea = null;
        try {
            BufferedReader leerArch = new BufferedReader(new InputStreamReader(new FileInputStream(dir)));
            linea = leerArch.readLine();
            if(linea!=null){
                linea = linea.replaceAll("\"", "");
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {}
        return linea;
    }

    public static String [] getPrimeraLinea(String dir, int tipoArch){
        String [] arrayLinea = null;
        try {
            BufferedReader leerArch = new BufferedReader(new InputStreamReader(new FileInputStream(dir)));
            String linea = leerArch.readLine();
            if(linea!=null){
                linea = linea.replaceAll("\"", "");
                if(tipoArch!=2)linea = linea.replaceAll(",", "");
                linea = linea.toUpperCase();
                switch (tipoArch){
                    case Explorar.TIPO_ARCH_LINEA:
                        arrayLinea = linea.split(SEPARADOR_LINEA);
                        break;
                    case Explorar.TIPO_ARCH_TAB:
                        arrayLinea = linea.split(SEPARADOR_TAB);
                        break;
                    case Explorar.TIPO_ARCH_COMA:
                        arrayLinea = linea.split(SEPARADOR_COMA);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {}
        return arrayLinea;
    }
}
