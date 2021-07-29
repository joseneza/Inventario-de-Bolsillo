package com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal;

import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Articulo;
import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;
import com.jose.prieto.mysmallbusinessbyneza.Explorar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

/**
 * Created by jose on 22/10/16.
 */

public class GuardarArchivo {

    public static final String SEPARADOR_LINEA = "|";
    public static final String SEPARADOR_TAB = "\t";
    public static final String SEPARADOR_COMA = ",";
    private PrintStream scr;
    private String separador;
    private String msjError;
    private Boolean nvoArchivo = false;

    public GuardarArchivo(String dir, int formArch, Boolean sobreEscribir){
        try {
            scr = new PrintStream(new FileOutputStream(dir, sobreEscribir));
            switch (formArch) {
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
            nvoArchivo = true;
        } catch (FileNotFoundException e) {
            nvoArchivo = false;
            msjError = e.getMessage();
        }
    }

    public Boolean getNvoArchivo(){
        return nvoArchivo;
    }
    public void escribirLinea(String linea){
        scr.println(linea);
    }

    public void escribirLinea(String... linea){
        String nvaLinea = "";
        for (int n = 0; n < linea.length; n++) {
            if (nvaLinea.length() > 0) nvaLinea = nvaLinea + separador;
            nvaLinea = nvaLinea + linea[n];
        }
        scr.println(nvaLinea);
    }

    public String getMsjError(){
        return msjError;
    }

    public void cerrarArchivo(){
        scr.close();
    }
}
