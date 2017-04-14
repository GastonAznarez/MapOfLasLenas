package com.app.mapll.mapoflaslenas;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends Activity implements View.OnClickListener {

    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_main);

        boton = (Button) findViewById(R.id.button);
        boton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == boton){

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));

            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.about2)});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sugerenciaSolo));
            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sugerencia));
            emailIntent.setType("message/plain");
            startActivity(Intent.createChooser(emailIntent, "Email "));
        }

    }
}
