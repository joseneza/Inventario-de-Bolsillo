package com.jose.prieto.mysmallbusinessbyneza;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.jose.prieto.mysmallbusinessbyneza.AccionesSecundarias.Recursos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MostrarImagen extends AppCompatActivity {
    private Toolbar toolbar;
    private HorizontalScrollView horizView;
    private ScrollView vertView;
    private LinearLayout layoutImgH, layoutImgV;
    private TextView descrip;
    private ImageView img;
    private Button btnUp, btnDown;
    private String art;
    private String dir;
    private String descripcion;
    private String msjError;
    private float escala = 1f;
    private MenuItem compartirImg;
    private Bitmap imagen = null;
    public static final String SKU = "sku";
    public static final String DESCRIPCION = "descripcion";
    public static final String URL = "url";
    public static final float minSize = 0.2f;
    public static final float maxSize = 5.0f;

    private float mx, my;
    private float curX, curY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_imagen);
        toolbar = (Toolbar) findViewById(R.id.toolbar_imagen); // Conectamos el Layout al objeto Toolbar
        setSupportActionBar(toolbar);
        horizView = (HorizontalScrollView) findViewById(R.id.horizView);
        vertView = (ScrollView) findViewById(R.id.vertView);
        layoutImgH = (LinearLayout) findViewById(R.id.layout_img_h);
        layoutImgV = (LinearLayout) findViewById(R.id.layout_img_v);
        descrip = (TextView) findViewById(R.id.txtDescripImg);
        img = (ImageView) findViewById(R.id.img);
        btnUp = (Button) findViewById(R.id.btn_img_up);
        btnDown = (Button) findViewById(R.id.btn_img_down);
        agregarAcciones();
        Intent i = getIntent();
        art = i.getExtras().getString(SKU);
        dir = i.getExtras().getString(URL);
        setTitle(getResources().getString(R.string.column_art) + ": " + art);
        descripcion = i.getExtras().getString(DESCRIPCION);
        descrip.setText(descripcion);
        msjError = "";
        new DescargarImagen().execute(dir);
    }

    private void agregarAcciones(){
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escala = zoomUp();
                img.setImageBitmap(zoomBitmap(imagen, escala));
                layoutImgH.setLayoutParams(getParamsH());
                layoutImgV.setLayoutParams(getParamsV());
            }
        });
        btnUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(escala>=1.0f){
                    float nvaEscala = escala*1.5f;
                    if(nvaEscala < maxSize) {
                        escala = nvaEscala;
                    } else {
                        escala = maxSize;
                    }
                } else {
                    escala = 1.0f;
                }
                img.setImageBitmap(zoomBitmap(imagen, escala));
                layoutImgH.setLayoutParams(getParamsH());
                layoutImgV.setLayoutParams(getParamsV());
                return true;
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escala = zoomDown();
                img.setImageBitmap(zoomBitmap(imagen, escala));
                layoutImgH.setLayoutParams(getParamsH());
                layoutImgV.setLayoutParams(getParamsV());
            }
        });
        btnDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                float nvaEscala;
                if(escala<=1.0f){
                    nvaEscala = escala/2;
                    if(nvaEscala>=minSize){
                        escala = nvaEscala;
                    } else {
                        escala = minSize;
                    }
                } else {
                    escala = 1.0f;
                }
                img.setImageBitmap(zoomBitmap(imagen, escala));
                layoutImgH.setLayoutParams(getParamsH());
                layoutImgV.setLayoutParams(getParamsV());
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_imagen, menu);
        compartirImg = menu.findItem(R.id.img_compartir);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.img_compartir:
                new CompartirImagen().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    private Bitmap DownloadImage (String url){//Metodo para cargar Imagen
        URL imageUrl = null;
        try{
            imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            msjError = ex.getMessage();
        }
        return imagen;
    }

    private class CompartirImagen extends AsyncTask<Void, Void, File>{
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MostrarImagen.this);
            pDialog.setMessage(getResources().getString(R.string.msj_preparing_image));
            pDialog.setCancelable(false);
            pDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    cancel(true);
                }
            });
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected File doInBackground(Void... nada) {
            File fileImg=new File(Inicio.DIRECTORIO_PAQUETE_EXTERNO, art+".png");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(fileImg);
                // Use the compress method on the BitMap object to write image to the OutputStream
                imagen.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                return fileImg;
            } catch (Exception e) {
                pDialog.setMessage(getResources().getString(R.string.title_error)+": "+e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(File fileImg) {
            super.onPostExecute(fileImg);
            pDialog.dismiss();
            if(fileImg!=null){
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileImg));
                intent.putExtra(Intent.EXTRA_TEXT, descripcion);
                intent.setType("image/png");
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.action_share)));
            } else {
                Recursos.mostrarMensaje(MostrarImagen.this, getResources().getString(R.string.msj_error_sharing_image));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            pDialog.dismiss();
            Recursos.mostrarMensaje(MostrarImagen.this, getResources().getString(R.string.msj_cancel_oper));
        }
    }

    private class DescargarImagen extends AsyncTask<String, Void, Bitmap> {

        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(MostrarImagen.this);
            pDialog.setMessage(getResources().getString(R.string.msj_load_image));
            pDialog.setCancelable(false);
            pDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    cancel(true);
                }
            });
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground ", "Entra en doInBackground");
            String url = params[0];
            Bitmap imagen = DownloadImage(url);
            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(result!=null){
                img.setImageBitmap(result);
                pDialog.dismiss();
            }else{
                pDialog.dismiss();
                final AlertDialog error = new AlertDialog.Builder(MostrarImagen.this).create();
                error.setTitle(getTitle());
                if(msjError.isEmpty()) {
                    error.setMessage(getResources().getString(R.string.msj_picture_not_found)+"\nURL: "+dir);
                } else {
                    error.setMessage(getResources().getString(R.string.title_error)+": "+msjError);
                }
                error.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.btn_exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        error.dismiss();
                        finish();
                    }
                });
                error.show();
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            pDialog.dismiss();
            finish();
        }
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, float p) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(p, p);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }

    private Float zoomUp(){
        float nvaScala = escala + 0.05f;
        if(nvaScala<=maxSize)
            return nvaScala;
        else
            return maxSize;
    }

    private Float zoomDown(){
        float nvaScala = escala - 0.05f;
        if(nvaScala<=minSize)
            return minSize;
        else
            return nvaScala;
    }

    private HorizontalScrollView.LayoutParams getParamsH(){
        HorizontalScrollView.LayoutParams params =
                new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.MATCH_PARENT,
                        HorizontalScrollView.LayoutParams.MATCH_PARENT);
        if(vertView.getWidth()<imagen.getWidth()*escala){
            params.gravity = Gravity.CENTER_VERTICAL;
        } else {
            params.gravity = Gravity.CENTER;
        }
        return params;
    }

    private ScrollView.LayoutParams getParamsV(){
        ScrollView.LayoutParams params =
                new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
                        ScrollView.LayoutParams.MATCH_PARENT);
        if(vertView.getHeight()<imagen.getHeight()*escala){
            params.gravity = Gravity.CENTER_HORIZONTAL;
        } else {
            params.gravity = Gravity.CENTER;
        }
        return params;
    }

}
