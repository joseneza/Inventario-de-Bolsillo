package com.jose.prieto.mysmallbusinessbyneza.AccionPrincipal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jose on 03/03/2016.
 */
public class AdmSQLite extends SQLiteOpenHelper{

    private static final int VERSION_BASE_DATOS = 1;

    public static final String NOMBRE_BASE_DATOS = "datosArticulos";

    private static final String TABLA_ARTICULOS = "create table articulos(codigo long primary key," +
            "ean text, descripcion text, existencia int, precio text, precio_anterior text, urlArt text";

    private String columnasAdd;

    public AdmSQLite(Context context, String columnasAdd) {
        super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
        this.columnasAdd = columnasAdd;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_ARTICULOS + columnasAdd + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
