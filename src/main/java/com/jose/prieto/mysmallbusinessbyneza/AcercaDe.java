package com.jose.prieto.mysmallbusinessbyneza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AcercaDe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        String version = BuildConfig.VERSION_NAME;
        TextView acercaD = (TextView) findViewById(R.id.acerca_de);
        String mensaje = getResources().getString(R.string.msj_acerca_d_ini) + "\n" + "Version: " + version + "\n" +
                getResources().getString(R.string.msj_acerca_d_fin);
        acercaD.setText(mensaje);
    }

    public void onBackPressed(){
        finish();
    }
}
