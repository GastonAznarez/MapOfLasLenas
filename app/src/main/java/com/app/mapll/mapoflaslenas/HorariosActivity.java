package com.app.mapll.mapoflaslenas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class HorariosActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horarios_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.horarios_name);

        String[] datos = new String[39];
        GridView grdOpciones;

        datos[0] = "Tk Eros I";
        datos[1] = "08:30";
        datos[2] = "17:15";
        datos[3] = "Tk Eros II";
        datos[4] = "08:30";
        datos[5] = "17:15";
        datos[6] = "Tk Urano";
        datos[7] = "08:45";
        datos[8] = "16:15";
        datos[9] = "Ts Caris";
        datos[10] = "09:00";
        datos[11] = "17:00";
        datos[12] = "Ts Neptuno";
        datos[13] = "09:00";
        datos[14] = "16:15";
        datos[15] = "Tk Minerva";
        datos[16] = "09:00";
        datos[17] = "17:00";
        datos[18] = "Ts Minerva";
        datos[19] = "09:00";
        datos[20] = "17:00";
        datos[21] = "Ts Venus";
        datos[22] = "09:00";
        datos[23] = "17:00";
        datos[24] = "Tk Venus II";
        datos[25] = "09:00";
        datos[26] = "17:00";
        datos[27] = "Ts Vesta";
        datos[28] = "09:00";
        datos[29] = "16:45";
        datos[30] = "Ts Vulcano";
        datos[31] = "09:00";
        datos[32] = "16:30";
        datos[33] = "Ts Marte";
        datos[34] = "09:30";
        datos[35] = "16:00";
        datos[36] = "Tk Iris";
        datos[37] = "09:45";
        datos[38] = "16:15";



        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);

        grdOpciones = (GridView)findViewById(R.id.tabla);

        grdOpciones.setAdapter(adaptador);
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

    }
