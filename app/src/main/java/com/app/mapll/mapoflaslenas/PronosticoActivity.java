package com.app.mapll.mapoflaslenas;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.pwittchen.weathericonview.WeatherIconView;
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
import java.text.DecimalFormat;
import java.util.ArrayList;


public class PronosticoActivity extends AppCompatActivity{

    String[]  vcondition, vday;
    String  currentCondition;
    int humedity, visibility, currentTemp, currentCode;
    int [] vwind, code;
    int [][] temp;

    WeatherIconView weatherIconView, img1,img2,img3,img4,img5,img6,img7;

    TextView Ttemp0, Tmax0, Tmin0, Ttoday, Twind, Thum, Tvis,
            day1, day2,day3,day4,day5,day6,day7,
            max1,max2,max3,max4,max5,max6,max7,
            min1,min2,min3,min4,min5,min6,min7;

    LineChart chart;

    ProgressDialog pDialog;

    InterstitialAd mInterstitialAd;

    String ur = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22las-le%C3%B1as%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.nav_pronos);


        weatherIconView = (WeatherIconView) findViewById(R.id.imageWeather);
        img1 = (WeatherIconView) findViewById(R.id.img1);
        img2 = (WeatherIconView) findViewById(R.id.img2);
        img3 = (WeatherIconView) findViewById(R.id.img3);
        img4 = (WeatherIconView) findViewById(R.id.img4);
        img5 = (WeatherIconView) findViewById(R.id.img5);
        img6 = (WeatherIconView) findViewById(R.id.img6);
        img7 = (WeatherIconView) findViewById(R.id.img7);

        vwind = new int[2];
        vcondition = new String[8];
        vday = new String[8];
        temp = new int[2][7];
        code = new int[8];

        Ttemp0 = (TextView) findViewById(R.id.textTemp);
        Tmax0 = (TextView) findViewById(R.id.textTmax);
        Tmin0 = (TextView) findViewById(R.id.textTmin);
        Ttoday = (TextView) findViewById(R.id.textToday);
        Twind = (TextView) findViewById(R.id.textWind);
        Thum = (TextView) findViewById(R.id.textHum);
        Tvis = (TextView) findViewById(R.id.textVis);

        day1 = (TextView) findViewById(R.id.day1);
        day2 = (TextView) findViewById(R.id.day2);
        day3 = (TextView) findViewById(R.id.day3);
        day4 = (TextView) findViewById(R.id.day4);
        day5 = (TextView) findViewById(R.id.day5);
        day6 = (TextView) findViewById(R.id.day6);
        day7 = (TextView) findViewById(R.id.day7);


        max1 = (TextView) findViewById(R.id.max1);
        max2 = (TextView) findViewById(R.id.max2);
        max3 = (TextView) findViewById(R.id.max3);
        max4 = (TextView) findViewById(R.id.max4);
        max5 = (TextView) findViewById(R.id.max5);
        max6 = (TextView) findViewById(R.id.max6);
        max7 = (TextView) findViewById(R.id.max7);

        min1 = (TextView) findViewById(R.id.min1);
        min2 = (TextView) findViewById(R.id.min2);
        min3 = (TextView) findViewById(R.id.min3);
        min4 = (TextView) findViewById(R.id.min4);
        min5 = (TextView) findViewById(R.id.min5);
        min6 = (TextView) findViewById(R.id.min6);
        min7 = (TextView) findViewById(R.id.min7);

        chart = (LineChart) findViewById(R.id.chart);

        if (conexion()) {

            pDialog = new ProgressDialog(PronosticoActivity.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(getString(R.string.dialog_sppiner));
            pDialog.setCancelable(true);
            pDialog.show();

            new JSONTask().execute(ur);

        }else{
            Snackbar.make(findViewById(R.id.linerL), R.string.snackbar2, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        /*mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9503849625074367/7434724136");
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });*/


    }

    public class JSONTask extends AsyncTask<String,String, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            Log.i("ESTADO","Comenzando");

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                Log.i("FLAG","1");
                URL url2 = new URL(params[0]);
                connection = (HttpURLConnection) url2.openConnection();
                connection.connect();
                Log.i("FLAG","2");

                InputStream stream = connection.getInputStream();
                Log.i("FLAG","3");

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                Log.i("FLAG","3.2");
                String line = "";

                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                Log.i("FLAG","3.3");
                String finalJson = buffer.toString();
                Log.i("FLAG","3.4");
                JSONObject parentObjet = new JSONObject(finalJson);
                JSONObject query = parentObjet.getJSONObject("query");
                JSONObject results = query.getJSONObject("results");
                JSONObject channel = results.getJSONObject("channel");
                Log.i("FLAG","3.5");
                JSONObject wind = channel.getJSONObject("wind");
                vwind[0] = (int)(wind.getInt("speed")*1.6);
                vwind[1] = wind.getInt("direction");
                Log.i("FLAG","3.6");

                JSONObject atm = channel.getJSONObject("atmosphere");
                humedity = atm.getInt("humidity");
                visibility = (int) (atm.getInt("visibility")*1.6);
                Log.i("FLAG","3.7");

                JSONObject item = channel.getJSONObject("item");

                JSONObject condition = item.getJSONObject("condition");
                currentCode = condition.getInt("code");
                int u = condition.getInt("temp");
                currentTemp = (int) ((u-32)/1.8);
                currentCondition = condition.getString("text");
                Log.i("FLAG","3.8");
                JSONArray forecast = item.getJSONArray("forecast");


                for (int i = 0; i < 7; i++) {

                    Log.i("FLAG", "3.9." + i + ".1");

                    JSONObject o1 = forecast.getJSONObject(i);

                    vcondition[i] = o1.getString("text");
                    vday[i] = o1.getString("day");

                    int p;

                    Log.i("FLAG", "3.9." + i + ".2");

                    p = o1.getInt("high");
                    Log.i("FLAG", "3.9.A" + "||" + p);
                    temp[0][i] = (int) ((p - 32) / 1.8);

                    Log.i("FLAG", "3.9.B");
                    p = o1.getInt("low");
                    temp[1][i] = (int) ((p - 32) / 1.8);

                    code[i] = o1.getInt("code");


                }

                if(temp[0][0] < -10){
                    temp[0][0] = 3;
                }


                return null;

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
            Twind.setText(vwind[0] + " Km/h");
            Tvis.setText(visibility + " M");
            Thum.setText(humedity + " %");
            Ttemp0.setText(currentTemp + "°");
            Tmax0.setText(temp[0][0] + "°");
            Tmin0.setText(temp[1][0] + "°");




            day1.setText(setDay(vday[0]));
            day2.setText(setDay(vday[1]));
            day3.setText(setDay(vday[2]));
            day4.setText(setDay(vday[3]));
            day5.setText(setDay(vday[4]));
            day6.setText(setDay(vday[5]));
            day7.setText(setDay(vday[6]));


            max1.setText(temp[0][0] + "°");
            max2.setText(temp[0][1] + "°");
            max3.setText(temp[0][2] + "°");
            max4.setText(temp[0][3] + "°");
            max5.setText(temp[0][4] + "°");
            max6.setText(temp[0][5] + "°");
            max7.setText(temp[0][6] + "°");

            min1.setText(temp[1][0] + "°");
            min2.setText(temp[1][1] + "°");
            min3.setText(temp[1][2] + "°");
            min4.setText(temp[1][3] + "°");
            min5.setText(temp[1][4] + "°");
            min6.setText(temp[1][5] + "°");
            min7.setText(temp[1][6] + "°");

            img1.setIconResource(setImagen(code[0]));
            img2.setIconResource(setImagen(code[1]));
            img3.setIconResource(setImagen(code[2]));
            img4.setIconResource(setImagen(code[3]));
            img5.setIconResource(setImagen(code[4]));
            img6.setIconResource(setImagen(code[5]));
            img7.setIconResource(setImagen(code[6]));




            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<Entry> entries1 = new ArrayList<>();



            Entry e1 = new Entry(temp[0][0], 0);
            entries.add(e1);
            Entry e2 = new Entry(temp[0][1], 1);
            entries.add(e2);
            Entry e3 = new Entry(temp[0][2], 2);
            entries.add(e3);
            Entry e4 = new Entry(temp[0][3], 3);
            entries.add(e4);
            Entry e5 = new Entry(temp[0][4], 4);
            entries.add(e5);
            Entry e6 = new Entry(temp[0][5], 5);
            entries.add(e6);
            Entry e7 = new Entry(temp[0][6], 6);
            entries.add(e7);


            Entry e8 = new Entry(temp[1][0], 0);
            entries1.add(e8);
            Entry e9 = new Entry(temp[1][1], 1);
            entries1.add(e9);
            Entry e10 = new Entry(temp[1][2], 2);
            entries1.add(e10);
            Entry e11 = new Entry(temp[1][3], 3);
            entries1.add(e11);
            Entry e12 = new Entry(temp[1][4], 4);
            entries1.add(e12);
            Entry e13 = new Entry(temp[1][5], 5);
            entries1.add(e13);
            Entry e14 = new Entry(temp[1][6], 6);
            entries1.add(e14);

//            entries.add(new Entry(temp[0][7], 7));

            LineDataSet setComp1 = new LineDataSet(entries, "MAX T°");
            setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
            setComp1.setLineWidth(2.5f);
            setComp1.setCircleRadius(4.5f);
            setComp1.setHighLightColor(Color.rgb(244, 117, 117));
            setComp1.setValueFormatter(new MyValueFormatter());

            //setComp1.setDrawValues(false);

            LineDataSet setComp2 = new LineDataSet(entries1, "MIN T°");
            setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
            setComp2.setLineWidth(2.5f);
            setComp2.setCircleRadius(4.5f);
            setComp2.setHighLightColor(Color.rgb(244, 117, 117));
            setComp2.setValueFormatter(new MyValueFormatter());


            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            setComp2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            dataSets.add(setComp1);
            setComp2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            dataSets.add(setComp2);



            //setComp2.setDrawValues(false);
            //LineDataSet dataset = new LineDataSet(entries, "MAX T°");



            // creating labels
            ArrayList<String> labels = new ArrayList<String>();
            labels.add(vday[0]);
            labels.add(vday[1]);
            labels.add(vday[2]);
            labels.add(vday[3]);
            labels.add(vday[4]);
            labels.add(vday[5]);
            labels.add(vday[6]);
            //labels.add(vday[7]);


            LineData data = new LineData(labels, dataSets);
            chart.setData(data);
            chart.invalidate();
            chart.setTouchEnabled(false);
            chart.getAxisRight().setEnabled(false);
            chart.getAxisLeft().setEnabled(false);
            chart.getXAxis().setDrawGridLines(false);
            chart.setDescription("");
            data.setValueTextSize(10f);




            weatherIconView.setIconResource(setImagen(currentCode));

            pDialog.dismiss();

        }
    }


    private boolean conexion() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String setImagen(int entrada){

        String con;

        switch (entrada) {
            case 0:  con = getString( R.string.wi_tornado);
                break;
            case 1:  con = getString( R.string.wi_day_storm_showers);
                break;
            case 2:  con = getString( R.string.wi_hurricane);
                break;
            case 3:  con = getString( R.string.wi_thunderstorm);
                break;
            case 4:  con = getString( R.string.wi_thunderstorm);
                break;
            case 5:  con = getString( R.string.wi_rain_mix);
                break;
            case 6:  con = getString( R.string.wi_rain_mix);
                break;
            case 7:  con = getString( R.string.wi_rain_mix);
                break;
            case 8:  con = getString( R.string.wi_hail);
                break;
            case 9:  con = getString( R.string.wi_showers);
                break;
            case 10: con = getString( R.string.wi_hail);
                break;
            case 11: con = getString( R.string.wi_showers);
                break;
            case 12: con = getString( R.string.wi_showers);
                break;
            case 13: con = getString( R.string.wi_snow);
                break;
            case 14: con = getString( R.string.wi_day_snow);
                break;
            case 15: con = getString( R.string.wi_snow_wind);
                break;
            case 16: con = getString( R.string.wi_snow);
                break;
            case 17: con = getString( R.string.wi_hail);
                break;
            case 18: con = getString( R.string.wi_rain_mix);
                break;
            case 19: con = getString( R.string.wi_dust);
                break;
            case 20: con = getString( R.string.wi_fog);
                break;
            case 21: con = getString( R.string.wi_windy);
                break;
            case 22: con = getString( R.string.wi_smoke);
                break;
            case 23: con = getString( R.string.wi_strong_wind);
                break;
            case 24: con = getString( R.string.wi_strong_wind);
                break;
            case 25: con = getString( R.string.wi_snowflake_cold);
                break;
            case 26: con = getString( R.string.wi_cloudy);
                break;
            case 27: con = getString( R.string.wi_night_cloudy);
                break;
            case 28: con = getString( R.string.wi_day_cloudy);
                break;
            case 29: con = getString( R.string.wi_night_cloudy);
                break;
            case 30: con = getString( R.string.wi_day_cloudy);
                break;
            case 31: con = getString( R.string.wi_night_clear);
                break;
            case 32: con = getString( R.string.wi_day_sunny);
                break;
            case 33: con = getString( R.string.wi_night_partly_cloudy);
                break;
            case 34: con = getString( R.string.wi_day_sunny_overcast);
                break;
            case 35: con = getString( R.string.wi_rain_mix);
                break;
            case 36: con = getString( R.string.wi_hot);
                break;
            case 37: con = getString( R.string.wi_day_storm_showers);
                break;
            case 38: con = getString( R.string.wi_day_storm_showers);
                break;
            case 39: con = getString( R.string.wi_day_storm_showers);
                break;
            case 40: con = getString( R.string.wi_showers);
                break;
            case 41: con = getString( R.string.wi_snow_wind);
                break;
            case 42: con = getString( R.string.wi_snow);
                break;
            case 43: con = getString( R.string.wi_snow_wind);
                break;
            case 44: con = getString( R.string.wi_day_sunny_overcast);
                break;
            case 45: con = getString( R.string.wi_day_storm_showers);
                break;
            case 46: con = getString( R.string.wi_snow);
                break;
            case 47: con = getString( R.string.wi_day_storm_showers);
                break;
            default: con = getString( R.string.wi_stars);
                break;
        }

        return con;
    }

    public String setDay(String entrada){

        String salida = null;

        switch (entrada) {
            case "Mon":
                salida = getString(R.string.lunes);
                break;
            case "Tue":
                salida = getString(R.string.martes);
                break;
            case "Wed":
                salida = getString(R.string.miercoles);
                break;
            case "Thu":
                salida = getString(R.string.jueves);
                break;
            case "Fri":
                salida = getString(R.string.viernes);
                break;
            case "Sat":
                salida = getString(R.string.sabado);
                break;
            case "Sun":
                salida = getString(R.string.domingo);
                break;
        }

        return salida;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + " °"; // e.g. append a dollar-sign
        }
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