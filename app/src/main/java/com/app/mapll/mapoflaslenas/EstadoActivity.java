package com.app.mapll.mapoflaslenas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class EstadoActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener{

    String ur = "https://extraction.import.io/query/extractor/6d1134c2-f2af-4c3f-8d64-66a7a17ded3d?_apikey=da928c064d634c0c83f09c35212ab6314c3247884f7699cb58118ea5463778ca09396f2a85cba1889d8712c890dc8e98b1afed8174810dbf2f06a4f42e5389867ba467d3a5aa8031bbde3a483556ec4b&url=https%3A%2F%2Fwww.laslenas.com%2Fesp%2Fmontana%2Festado_de_pistas.php";
    String ur2 = "https://extraction.import.io/query/extractor/79eeb103-1775-47af-a422-ae955a791c5d?_apikey=da928c064d634c0c83f09c35212ab6314c3247884f7699cb58118ea5463778ca09396f2a85cba1889d8712c890dc8e98b1afed8174810dbf2f06a4f42e5389867ba467d3a5aa8031bbde3a483556ec4b&url=https%3A%2F%2Fwww.laslenas.com%2Fesp%2Fmontana%2Festado_de_pistas.php";
    String ur3 = "https://extraction.import.io/query/extractor/e7118f73-9292-4088-b68f-7a8057c2894a?_apikey=da928c064d634c0c83f09c35212ab6314c3247884f7699cb58118ea5463778ca09396f2a85cba1889d8712c890dc8e98b1afed8174810dbf2f06a4f42e5389867ba467d3a5aa8031bbde3a483556ec4b&url=https%3A%2F%2Fwww.laslenas.com%2Fesp%2Fmontana%2Festado_de_pistas.php";
    GridView gridT;
    GridView gridT2;
    ProgressDialog pDialog;
    Boolean finalizo = false;
    TextView e;
    TextView o1;
    TextView o2;

    InterstitialAd mInterstitialAd;

    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.estados_main);

        /*mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9503849625074367/7434724136");
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });*/


        TabHost tabs = (TabHost) this.findViewById(R.id.tabhost);

        tabs.setup();

        TabHost.TabSpec tabpage1 = tabs.newTabSpec("Pistas");
        tabpage1.setContent(R.id.tab1);
        tabpage1.setIndicator(getString(R.string.nav_pistas));

        TabHost.TabSpec tabpage2 = tabs.newTabSpec("Medios");
        tabpage2.setContent(R.id.tab2);
        tabpage2.setIndicator(getString(R.string.nav_medios));

        TabHost.TabSpec tabpage3 = tabs.newTabSpec("Fuera");
        tabpage3.setContent(R.id.tab3);
        tabpage3.setIndicator(getString(R.string.fueradepista));

        tabs.addTab(tabpage1);
        tabs.addTab(tabpage2);
        tabs.addTab(tabpage3);


        for(int i=0;i<tabs.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabs.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
        TextView tv = (TextView) tabs.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#ffffff"));



        gridT = (GridView) findViewById(R.id.tabla);
        gridT2 = (GridView) findViewById(R.id.tabla2);

        e = (TextView) findViewById(R.id.textViewE);
        o1  = (TextView) findViewById(R.id.textViewO1);
        o2  = (TextView) findViewById(R.id.textViewO2);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.nav_estado);

        if (conexion()) {

            pDialog = new ProgressDialog(EstadoActivity.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(getString(R.string.dialog_sppiner));
            pDialog.setCancelable(true);
            pDialog.show();



            new JSONTask().execute(ur);
            new JSONTask2().execute(ur2);
            new JSONTask3().execute(ur3);
        }else{
            Snackbar.make(findViewById(R.id.linear1), R.string.snackbar2, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private boolean conexion() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public class JSONTask extends AsyncTask<String,String, String[]>{

        @Override
        protected String[] doInBackground(String... params) {

            Log.i("ESTADO","Comenzando");

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            String[] t;
            try {

                Log.i("FLAG","1");
                URL url2 = new URL(params[0]);
                connection = (HttpURLConnection) url2.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";

                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                Log.i("DATOS","   " + finalJson);

                JSONObject parentObjet = new JSONObject(finalJson);
                JSONObject parentArray = parentObjet.getJSONObject("extractorData");
                JSONArray a1 = parentArray.getJSONArray("data");
                JSONObject o1 = a1.getJSONObject(0);
                JSONArray a2 = o1.getJSONArray("group");

                String[] j = new String[(a2.length()*3)];
                int k = 0;
                Log.i("LENGTH", j.length + "");

                for (int i = 0; i < a2.length(); i++){

                    JSONObject o2 = a2.getJSONObject(i);

                    JSONArray a3 = o2.getJSONArray("pista");
                    JSONObject o3 = a3.getJSONObject(0);
                    j[k] = o3.getString("text");
                    Log.i("DATO PISTA", "" + j[k]);
                    k++;


                    JSONArray a4 = o2.getJSONArray("estado");
                    JSONObject o4 = a4.getJSONObject(0);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Objects.equals(o4.getString("text"), "Cerrado")){
                            j[k] = getString(R.string.estado0);
                            k++;
                        }else{
                            j[k] = getString(R.string.estado1);
                            k++;
                        }
                    }else{
                        j[k] = o4.getString("text");
                        k++;
                    }

                    Log.i("DATO ESTADO", "" + j[k-1]);

                    JSONArray a5 = o2.getJSONArray("condicion");
                    JSONObject o5 = a5.getJSONObject(0);
                    j[k] = o5.getString("text");
                    Log.i("DATO CONDICION", "" + j[k]);
                    k++;


                }

                return j;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("ERROR","2");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERROR", "3");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR", "4");
            } finally {
                Log.i("FLAG","4");
                if(connection != null){
                    connection.disconnect();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "5");
                }
            }
            Log.i("FLAG","5");
            return null;

        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            ArrayAdapter<String> ad = new ArrayAdapter<String>(EstadoActivity.this, android.R.layout.simple_list_item_1, result);
            gridT.setAdapter(ad);
            //tabla1(datosTabla);
            finalizo = true;
        }
    }

    public class JSONTask2 extends AsyncTask<String,String, String[]>{

        @Override
        protected String[] doInBackground(String... params) {

            Log.i("ESTADO","Comenzando segundo");

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            String[] t;
            try {

                Log.i("FLAG","1");
                URL url2 = new URL(params[0]);
                connection = (HttpURLConnection) url2.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";

                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                Log.i("DATOS","   " + finalJson);

                JSONObject parentObjet = new JSONObject(finalJson);
                JSONObject object1 = parentObjet.getJSONObject("extractorData");
                JSONArray array1 = object1.getJSONArray("data");
                JSONObject object2 = array1.getJSONObject(0);
                JSONArray array2 = object2.getJSONArray("group");

                t = new String[array2.length()*2];
                int a = 0;


                for (int i = 0; i < array2.length(); i++) {

                    JSONObject object3 = array2.getJSONObject(i);
                    JSONArray array3 = object3.getJSONArray("Estado");

                    JSONObject objectAux;
                    JSONObject objectAux2;

                    objectAux = array3.getJSONObject(0);


                    t[a] = objectAux.getString("text");
                    a++;

                    objectAux2 = array3.getJSONObject(1);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Objects.equals(objectAux2.getString("text"), "Cerrado")){
                            t[a] = getString(R.string.estado0);
                            a++;
                        }else{
                            t[a] = getString(R.string.estado1);
                            a++;
                        }
                    }else{
                        t[a] = objectAux2.getString("estado");
                    }



                }

                Log.i("FLAG","3");
                return t;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("ERROR","2");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERROR", "3");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR", "4");
            } finally {
                Log.i("FLAG","4");
                if(connection != null){
                    connection.disconnect();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "5");
                }
            }
            Log.i("FLAG","5");
            return null;

        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            ArrayAdapter<String> ad2 = new ArrayAdapter<String>(EstadoActivity.this, android.R.layout.simple_list_item_1, result);
            gridT2.setAdapter(ad2);
            //tabla1(datosTabla);
            finalizo = true;

        }
    }

    public class JSONTask3 extends AsyncTask<String,String, String[]>{

        @Override
        protected String[] doInBackground(String... params) {

            Log.i("ESTADO","Comenzando segundo");

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            String[] t;
            try {

                Log.i("FLAG","1");
                URL url2 = new URL(params[0]);
                connection = (HttpURLConnection) url2.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";

                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                Log.i("DATOS","   " + finalJson);

                JSONObject parentObjet = new JSONObject(finalJson);
                JSONObject object1 = parentObjet.getJSONObject("extractorData");
                JSONArray array1 = object1.getJSONArray("data");

                t = new String[3];
                int a = 0;

                JSONObject object2a = array1.getJSONObject(0);
                JSONArray array2a = object2a.getJSONArray("group");

                JSONObject object3a = array2a.getJSONObject(0);
                JSONArray array3a = object3a.getJSONArray("Estado");
                JSONObject object4a = array3a.getJSONObject(0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (Objects.equals(object4a.getString("text"), "Cerrado")){
                        t[0] = getString(R.string.estado0);
                    }else{
                        t[0] = getString(R.string.estado1);
                    }
                }else{
                    t[0] = object4a.getString("estado");
                }



                JSONArray array3b = object3a.getJSONArray("Riesgo");
                JSONObject object4b = array3b.getJSONObject(0);



                String s = object4b.getString("text");

                String[] separated = s.split(":");

                t[1] = getString(R.string.riesgo);
                t[2] = separated[1];

                Log.i("riezgo  ", "" + separated[1]);

                //t[1] = getString(R.string.riesgo) + ": " + separated[1];



                Log.i("FLAG","3");
                return t;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("ERROR","2");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERROR", "3");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR", "4");
            } finally {
                Log.i("FLAG","4");
                if(connection != null){
                    connection.disconnect();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "5");
                }
            }
            Log.i("FLAG","5");
            return null;

        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);


            e.setText(result[0]);
            o1.setText(result[1]);
            o2.setText(result[2]);

            Log.i("DATOS off-truck", "| "+result);

            finalizo = true;

            pDialog.dismiss();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_info:
                dialogo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dialogo(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(EstadoActivity.this);

        builder.setMessage(R.string.dialog_info)
                .setTitle(R.string.nav_help)
                .setOnCancelListener(this)
                .setOnDismissListener(this);
        builder.show();
    }

    @Override
    public void onBackPressed()
    {
        finish();
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.i("Dialogo Estado:   ","onCancel");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.i("Dialogo Estado:   ","OnDismiss");
    }

    @Override protected void onDestroy() {
        super.onDestroy();

        /*if (mInterstitialAd.isLoaded()) {
            Log.i("ANUNCIO: ","Esta cargado");
            mInterstitialAd.show();
        }*/
    }

    /*private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }*/

}

