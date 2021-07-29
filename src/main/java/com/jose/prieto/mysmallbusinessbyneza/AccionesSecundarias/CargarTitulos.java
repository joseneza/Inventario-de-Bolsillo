package com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias;

import com.jose.prieto.mysmallbusinessbyneza.Inicio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Jose on 06/05/2016.
 */
public class CargarTitulos {
    private String titulos = "";
    public CargarTitulos(){
        File arcConfig = new File(Inicio.DIRECTORIO_PAQUETE_EXTERNO, "titulos.dat");
        if(arcConfig.exists()){
            BufferedReader leerArchLista;
            ArrayList<Articulo> arrayArticulo = new ArrayList<Articulo>();
            try {
                leerArchLista = new BufferedReader(new InputStreamReader(new FileInputStream(arcConfig)));
                String linea = leerArchLista.readLine();
                while(linea!=null){
                    titulos = linea;
                }
                leerArchLista.close();
            } catch (FileNotFoundException e) {
            } catch (IOException io) {
            }
        }
    }
}
