package com.app.mapll.mapoflaslenas;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener, OnClickListener  {

    private static final int REQUEST_LOCATION = 1;
    GoogleMap mapa;

    boolean onB = false;

    ImageView divider;

    LatLng llubicacion = new LatLng(-35.1480031, -70.0819219);

    FloatingActionButton fab;
    com.github.clans.fab.FloatingActionMenu fabMenu;

    android.support.design.widget.CoordinatorLayout coor;

    int info = 0;

    com.github.clans.fab.FloatingActionButton fm1;
    com.github.clans.fab.FloatingActionButton fm2;
    com.github.clans.fab.FloatingActionButton fm3;
    com.github.clans.fab.FloatingActionButton fm4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        divider = (ImageView) findViewById(R.id.divider);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        fabMenu = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.fabMenu);
        fabMenu.setOnClickListener(this);
        fabMenu.setMenuButtonColorNormal(getResources().getColor(R.color.fabColor));
        fabMenu.setMenuButtonColorRipple(getResources().getColor(R.color.fabColor));
        fabMenu.setMenuButtonColorPressed(getResources().getColor(R.color.colorPrimary));
        fabMenu.setClosedOnTouchOutside(true);


        fm1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab1);
        fm2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab2);
        fm3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab3);
        fm4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab4);

        fm1.setOnClickListener(this);
        fm2.setOnClickListener(this);
        fm3.setOnClickListener(this);
        fm4.setOnClickListener(this);

        coor = (android.support.design.widget.CoordinatorLayout) findViewById(R.id.coor);

        /*AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mapa = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            // permission has been granted, continue as usual
            mapa.setMyLocationEnabled(true);
        }


        //mapa.setOnPolylineClickListener(this);

        mapa.getUiSettings().setMyLocationButtonEnabled(false);
        mapa.setOnMarkerClickListener(this);

        ubicar();

    }


    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                mapa.setMyLocationEnabled(true);

            } else {

                // Permission was denied or request was cancelled
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);

                builder.setMessage("La aplicación necesita el permiso ACCES_FINE_LOCATION para poder funcionar, de lo contrario se cerrara.")
                        .setTitle("Confirmacion")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Dialogos", "Confirmacion Aceptada.");
                                dialog.cancel();

                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    // Check Permissions Now
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            REQUEST_LOCATION);
                                } else {
                                    // permission has been granted, continue as usual
                                    mapa.setMyLocationEnabled(true);
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Dialogos", "Confirmacion Cancelada.");
                                dialog.cancel();
                                finish();
                            }
                        });
                builder.create();
            }
        }
    }

    public void mostrarLugares() {


        info = 2;

        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1474499, -70.0832067))
                .title("UFO Point")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ufo_map)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1490248, -70.0840921))
                .title("Hotel Pisis")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1479033, -70.0844304))
                .title("Hotel Escorpio")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1485388, -70.0819722))
                .title("Corinto")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1480132, -70.0823658))
                .title("Atenas")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1476651, -70.0835014))
                .title("Innsbruck")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1472867, -70.0828912))
                .title("Hotel Acuario")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1476226, -70.0817264))
                .title("Esparta")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1481887, -70.0811350))
                .title("Tebas")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1474356, -70.0806569))
                .title("Geminis")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1459793, -70.0817452))
                .title("Hotel Virgo")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1480376, -70.0804963))
                .title("Delphos")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1480354, -70.0800376))
                .title("Ligüen")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1476327, -70.0799126))
                .title("Playén")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1475820, -70.0795253))
                .title(getString(R.string.supermercado))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_mall)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1473767, -70.0790734))
                .title("Milla")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1479343, -70.0792722))
                .title("Laquir")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1474438, -70.0784692))
                .title("Apartur")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1469501, -70.0776119))
                .title("Cirrus")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.restauran_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1471521, -70.0831477))
                .title("Brasero")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_bar)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1467110, -70.0816718))
                .title(getString(R.string.kiosco))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_mall)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.146732, -70.081392))
                .title(getString(R.string.baño))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.banos_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1468750, -70.0809798))
                .title(getString(R.string.ingreso))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_info)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1468750, -70.0820493))
                .title("Sky Rental")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.tools_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1465548, -70.0803166))
                .title(getString(R.string.autoE))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_car)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1469547, -70.0797718))
                .title(getString(R.string.coleE))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_car)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1469931, -70.0817569))
                .title(getString(R.string.CentroM))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.medico_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1451199, -70.0833223))
                .title("Parador Eros/" + getString(R.string.baño))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.restauran_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1450782, -70.1125665))
                .title("Parador Neptuno/" + getString(R.string.baño))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.restauran_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1425831, -70.0827373))
                .title(getString(R.string.cafe) + "Minerva")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.restauran_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1424051, -70.0828271))
                .title(getString(R.string.baño))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.banos_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1390444, -70.0952739))
                .title("Parador Minerva/" + getString(R.string.baño))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.restauran_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1440534, -70.0971139))
                .title(getString(R.string.servPista))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.tools_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1298497, -70.0990223))
                .title(getString(R.string.servPista))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.tools_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1378805, -70.0786415))
                .title("Hotel Aries")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1397523, -70.0818109))
                .title("Villa Capricornio")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.1404152, -70.0829830))
                .title("Club de la Nieve")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.hotel_mapa)));
        mapa.addMarker(new MarkerOptions().position(new LatLng(-35.148163, -70.083586))
                .title("Pirammide")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_mall)));
    }

    public void mostrarMedios() {

        info = 1;

        final PolylineOptions minerva2 = new PolylineOptions()
                .add(new LatLng(-35.1423613, -70.0833304))
                .add(new LatLng(-35.1386929, -70.0956407));
        minerva2.width(6);
        minerva2.color(Color.GREEN);
        final PolylineOptions TKeros2 = new PolylineOptions()
                .add(new LatLng(-35.1465734, -70.0825908))
                .add(new LatLng(-35.1461367, -70.0828449))
                .add(new LatLng(-35.1456846, -70.0831513))
                .add(new LatLng(-35.1452016, -70.0833891))
                .add(new LatLng(-35.1447530, -70.0836881));
        TKeros2.width(6);
        TKeros2.color(Color.BLACK);
        final PolylineOptions TKeros1 = new PolylineOptions()
                .add(new LatLng(-35.1468284, -70.0834109))
                .add(new LatLng(-35.1465369, -70.0836217))
                .add(new LatLng(-35.1460440, -70.0839097))
                .add(new LatLng(-35.1456292, -70.0842551))
                .add(new LatLng(-35.1452397, -70.0845156))
                .add(new LatLng(-35.1447127, -70.0849102));
        TKeros1.width(6);
        TKeros1.color(Color.BLACK);
        final PolylineOptions TKminerva1 = new PolylineOptions()
                .add(new LatLng(-35.1425995, -70.0832208))
                .add(new LatLng(-35.1426294, -70.0843329))
                .add(new LatLng(-35.1427103, -70.0853772))
                .add(new LatLng(-35.1427986, -70.0867532))
                .add(new LatLng(-35.1428967, -70.0880719))
                .add(new LatLng(-35.1429576, -70.0891970))
                .add(new LatLng(-35.1424786, -70.0903537))
                .add(new LatLng(-35.1414974, -70.0911018))
                .add(new LatLng(-35.1407774, -70.0916241))
                .add(new LatLng(-35.1401589, -70.0920747))
                .add(new LatLng(-35.1391453, -70.0928197));
        TKminerva1.width(6);
        TKminerva1.color(Color.BLACK);
        final PolylineOptions TScaris = new PolylineOptions()
                .add(new LatLng(-35.1426801, -70.0831986))
                .add(new LatLng(-35.1427544, -70.0837304))
                .add(new LatLng(-35.1428342, -70.0842698))
                .add(new LatLng(-35.1429417, -70.0854041))
                .add(new LatLng(-35.1430130, -70.0862302))
                .add(new LatLng(-35.1431637, -70.0875358))
                .add(new LatLng(-35.1432918, -70.0886784))
                .add(new LatLng(-35.1434417, -70.0901717))
                .add(new LatLng(-35.1435149, -70.0910987))
                .add(new LatLng(-35.1435887, -70.0917733))
                .add(new LatLng(-35.1437110, -70.0930303))
                .add(new LatLng(-35.1438039, -70.0938691))
                .add(new LatLng(-35.1438782, -70.0946711))
                .add(new LatLng(-35.1439912, -70.0958637))
                .add(new LatLng(-35.1440641, -70.0969151));
        TScaris.width(6);
        TScaris.color(Color.BLUE);
        final PolylineOptions TKurano = new PolylineOptions()
                .add(new LatLng(-35.1361737, -70.0797520))
                .add(new LatLng(-35.1360229, -70.0806512))
                .add(new LatLng(-35.1359058, -70.0814300))
                .add(new LatLng(-35.1357690, -70.0822354))
                .add(new LatLng(-35.1356396, -70.0830997))
                .add(new LatLng(-35.1354644, -70.0841552))
                .add(new LatLng(-35.1353125, -70.0851372))
                .add(new LatLng(-35.1350786, -70.0865272))
                .add(new LatLng(-35.1349166, -70.0874898));
        TKurano.width(6);
        TKurano.color(Color.BLACK);
        final PolylineOptions TSvulcano = new PolylineOptions()
                .add(new LatLng(-35.138157, -70.084886))
                .add(new LatLng(-35.137977, -70.085259))
                .add(new LatLng(-35.137549, -70.086010))
                .add(new LatLng(-35.137231, -70.086538))
                .add(new LatLng(-35.136554, -70.087684))
                .add(new LatLng(-35.135977, -70.088689))
                .add(new LatLng(-35.135353, -70.089732))
                .add(new LatLng(-35.134945, -70.090402))
                .add(new LatLng(-35.134332, -70.091417))
                .add(new LatLng(-35.133667, -70.092495))
                .add(new LatLng(-35.132860, -70.093864))
                .add(new LatLng(-35.131880, -70.095582))
                .add(new LatLng(-35.131661, -70.095910))
                .add(new LatLng(-35.130897, -70.097350))
                .add(new LatLng(-35.130541, -70.097927))
                .add(new LatLng(-35.130131, -70.098632))
                .add(new LatLng(-35.129837, -70.099014));
        TSvulcano.width(6);
        TSvulcano.color(Color.BLUE);

        final PolylineOptions TsVenusI = new PolylineOptions()
                .add(new LatLng(-35.14696543815408, -70.08455380797386))
                .add(new LatLng(-35.147096480768546, -70.085389316082))
                .add(new LatLng(-35.147265629605364, -70.08674181997776))
                .add(new LatLng(-35.14740352539859, -70.0878244265914))
                .add(new LatLng(-35.14755375752763, -70.08901733905077))
                .add(new LatLng(-35.14769987719732, -70.09028099477291))
                .add(new LatLng(-35.147821049407334, -70.09118221700191))
                .add(new LatLng(-35.14795181649943, -70.09220916777849))
                .add(new LatLng(-35.14810149932804, -70.09328473359346))
                .add(new LatLng(-35.14826406663041, -70.09454805403948))
                .add(new LatLng(-35.1483156056062, -70.09498290717602))
                .add(new LatLng(-35.148335618072025, -70.09523537009954));
        TsVenusI.width(6);
        TsVenusI.color(Color.BLUE);

        final PolylineOptions TsNeptuno = new PolylineOptions()
                .add(new LatLng(-35.14901549070021, -70.09471368044615))
                .add(new LatLng(-35.14889240127853, -70.09541004896164))
                .add(new LatLng(-35.14863059576475, -70.09649567306042))
                .add(new LatLng(-35.14844280804501, -70.09731005877256))
                .add(new LatLng(-35.14814700723969, -70.09838595986366))
                .add(new LatLng(-35.148009935139584, -70.0991614535451))
                .add(new LatLng(-35.14773962828158, -70.10030072182417))
                .add(new LatLng(-35.147518666836184, -70.10121334344149))
                .add(new LatLng(-35.14706796942269, -70.10297019034624))
                .add(new LatLng(-35.146310496066825, -70.10618280619383))
                .add(new LatLng(-35.14606540545833, -70.10709576308727))
                .add(new LatLng(-35.14580989634239, -70.10804492980242))
                .add(new LatLng(-35.14552121394047, -70.10935217142105))
                .add(new LatLng(-35.14514534956355, -70.11088773608208))
                .add(new LatLng(-35.14484898846257, -70.11211786419153))
                .add(new LatLng(-35.14474316453322, -70.1124980673194))
                .add(new LatLng(-35.144677093046425, -70.11270828545094));
        TsNeptuno.width(6);
        TsNeptuno.color(Color.BLUE);

        final PolylineOptions TsMarte = new PolylineOptions()
                .add(new LatLng(-35.14385188033191, -70.11115562170744))
                .add(new LatLng(-35.14373371795943, -70.11122100055218))
                .add(new LatLng(-35.143340847399465, -70.11132996529341))
                .add(new LatLng(-35.14290164163942, -70.11144764721394))
                .add(new LatLng(-35.14262857618933, -70.1115445420146))
                .add(new LatLng(-35.14237607208141, -70.11160153895617))
                .add(new LatLng(-35.141428283658875, -70.11177722364664))
                .add(new LatLng(-35.14056931516501, -70.11226639151573))
                .add(new LatLng(-35.13895416665011, -70.11250745505095))
                .add(new LatLng(-35.138113541961225, -70.11280953884125))
                .add(new LatLng(-35.13667491851439, -70.11307943612337))
                .add(new LatLng(-35.13606321481208, -70.11325445026159))
                .add(new LatLng(-35.13505695085214, -70.1135266944766))
                .add(new LatLng(-35.13361827339326, -70.11394713073969))
                .add(new LatLng(-35.13235478268664, -70.11427201330662))
                .add(new LatLng(-35.131346827690834, -70.11453319340944))
                .add(new LatLng(-35.13119875925738, -70.11461399495602));
        TsMarte.width(6);
        TsMarte.color(Color.BLUE);

        final PolylineOptions TkIris = new PolylineOptions()
                .add(new LatLng(-35.130579885131255, -70.11145334690809))
                .add(new LatLng(-35.13044031561545, -70.11157538741827))
                .add(new LatLng(-35.13033118250716, -70.11178325861692))
                .add(new LatLng(-35.12982225834222, -70.11281255632639))
                .add(new LatLng(-35.12939696421099, -70.1135977730155))
                .add(new LatLng(-35.12895000538023, -70.11437393724918))
                .add(new LatLng(-35.12860505020048, -70.11492010205984));
        TkIris.width(6);
        TkIris.color(Color.BLACK);

        /*mapa.addPolyline(minerva2);
        mapa.addPolyline(TScaris);
        mapa.addPolyline(TKeros1);
        mapa.addPolyline(TKeros2);
        mapa.addPolyline(TKminerva1);
        mapa.addPolyline(TKurano);
        mapa.addPolyline(TSvulcano);
        mapa.addPolyline(TkIris);
        //mapa.addPolyline(TsMarte);
        mapa.addPolyline(TsNeptuno);
        mapa.addPolyline(TsVenusI);*/

        final Polyline Ts_MinervaII = mapa.addPolyline(minerva2);
        final Polyline Ts_Caris = mapa.addPolyline(TScaris);
        final Polyline Tk_ErosI = mapa.addPolyline(TKeros1);
        final Polyline Tk_ErosII = mapa.addPolyline(TKeros2);
        final Polyline Tk_MinervaI = mapa.addPolyline(TKminerva1);
        final Polyline Tk_Urano = mapa.addPolyline(TKurano);
        final Polyline Ts_Vulcano = mapa.addPolyline(TSvulcano);
        final Polyline Tk_Iris = mapa.addPolyline(TkIris);
        final Polyline Ts_Marte = mapa.addPolyline(TsMarte);
        final Polyline Ts_Neptuno = mapa.addPolyline(TsNeptuno);
        final Polyline Ts_VenusI = mapa.addPolyline(TsVenusI);

        Ts_MinervaII.setClickable(true);
        Ts_Caris.setClickable(true);
        Tk_ErosI.setClickable(true);
        Tk_ErosII.setClickable(true);
        Tk_MinervaI.setClickable(true);
        Tk_Urano.setClickable(true);
        Ts_Vulcano.setClickable(true);
        Tk_Iris.setClickable(true);
        Ts_Marte.setClickable(true);
        Ts_Neptuno.setClickable(true);
        Ts_VenusI.setClickable(true);


        mapa.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onPolylineClick(final Polyline polyline) {

                Log.i("Polilyne Clicked ==", " " + polyline.getId());


                final int color = polyline.getColor();

                polyline.setColor(Color.YELLOW);
                polyline.setWidth(15);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // acciones que se ejecutan tras los milisegundos
                        polyline.setColor(color);
                        polyline.setWidth(6);
                    }
                }, 2000);


                if (Objects.equals(polyline.getId(), Ts_MinervaII.getId())) {

                    Snackbar.make(fab, "Ts Minerva II", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Ts_Caris.getId())) {

                    Snackbar.make(fab, "Ts Caris", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Tk_ErosI.getId())) {

                    Snackbar.make(fab, "Tk Eros I", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Tk_ErosII.getId())) {

                    Snackbar.make(fab, "Tk Eros II", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Tk_MinervaI.getId())) {

                    Snackbar.make(fab, "Tk Minerva I", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Tk_Urano.getId())) {

                    Snackbar.make(fab, "Tk Urano", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Ts_Vulcano.getId())) {

                    Snackbar.make(fab, "Ts Vulcano", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Tk_Iris.getId())) {

                    Snackbar.make(fab, "Tk Iris", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Ts_Marte.getId())) {

                    Snackbar.make(fab, "Ts Marte", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Ts_Neptuno.getId())) {

                    Snackbar.make(fab, "Ts Neptuno", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Ts_VenusI.getId())) {

                    Snackbar.make(fab, "Ts Venus I", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }


            }
        });


    }

    public void mostrarPistas() {

        info = 0;


        PolylineOptions caris = new PolylineOptions()
                .add(new LatLng(-35.144242, -70.097025))
                .add(new LatLng(-35.144942, -70.091498))
                .add(new LatLng(-35.14468147953683, -70.09087979793549))
                .add(new LatLng(-35.144594297995546, -70.09044997394085))
                .add(new LatLng(-35.14452109832743, -70.09002987295389))
                .add(new LatLng(-35.144449817688475, -70.08958261460066))
                .add(new LatLng(-35.14438100439703, -70.0891725718975))
                .add(new LatLng(-35.14430698206637, -70.08880008012056))
                .add(new LatLng(-35.14423953943983, -70.08855532854795));
        caris.width(6);
        caris.color(Color.RED);

        PolylineOptions talìa = new PolylineOptions()
                .add(new LatLng(-35.144942, -70.091498))
                .add(new LatLng(-35.145108, -70.090674))
                .add(new LatLng(-35.145152, -70.090122))
                .add(new LatLng(-35.145241, -70.089665))
                .add(new LatLng(-35.145284, -70.088714))
                .add(new LatLng(-35.145335, -70.087599))
                .add(new LatLng(-35.145224, -70.086794))
                .add(new LatLng(-35.145031, -70.085945))
                .add(new LatLng(-35.144907, -70.084836));
        talìa.width(6);
        talìa.color(Color.RED);

        PolylineOptions eros1 = new PolylineOptions()
                .add(new LatLng(-35.144907, -70.084836))
                .add(new LatLng(-35.146309, -70.084043));
        eros1.width(6);
        eros1.color(Color.BLUE);


        PolylineOptions VulcanoI = new PolylineOptions()
                .add(new LatLng(-35.129931666337875, -70.0992938876152))
                .add(new LatLng(-35.130071236725485, -70.09927678853273))
                .add(new LatLng(-35.13023767889855, -70.09910043329))
                .add(new LatLng(-35.13034187646142, -70.09891636669636))
                .add(new LatLng(-35.13045704203369, -70.09866792708635))
                .add(new LatLng(-35.13056069090948, -70.0984389334917))
                .add(new LatLng(-35.13068134022836, -70.09819149971008))
                .add(new LatLng(-35.13084339391855, -70.09794808924198))
                .add(new LatLng(-35.130957187772225, -70.09775832295418))
                .add(new LatLng(-35.13109538539515, -70.0975377112627))
                .add(new LatLng(-35.13124263141387, -70.09732279926538))
                .add(new LatLng(-35.131386312560956, -70.09712062776089))
                .add(new LatLng(-35.13149023445399, -70.09698282927275))
                .add(new LatLng(-35.13164817354143, -70.09678266942501))
                .add(new LatLng(-35.13180090254124, -70.09657848626375))
                .add(new LatLng(-35.13196076041551, -70.0963632389903))
                .add(new LatLng(-35.13215187653601, -70.09612385183573))
                .add(new LatLng(-35.132397009038215, -70.09584691375494))
                .add(new LatLng(-35.132531913858834, -70.09569972753525))
                .add(new LatLng(-35.13271068982147, -70.09552538394928))
                .add(new LatLng(-35.1328359972791, -70.09538557380438))
                .add(new LatLng(-35.1330967568189, -70.09512908756733))
                .add(new LatLng(-35.13330624127015, -70.09494569152594))
                .add(new LatLng(-35.13349735423263, -70.0947954878211))
                .add(new LatLng(-35.133655837812505, -70.09467478841543))
                .add(new LatLng(-35.13378525670082, -70.09459733963013))
                .add(new LatLng(-35.133903159322564, -70.09453397244215))
                .add(new LatLng(-35.1339983038719, -70.09451285004616))
                .add(new LatLng(-35.13411483522898, -70.09435091167688))
                .add(new LatLng(-35.13418557653615, -70.09417656809092))
                .add(new LatLng(-35.13423959214405, -70.09397070854902))
                .add(new LatLng(-35.13429141419104, -70.09372863918543))
                .add(new LatLng(-35.13437065524673, -70.09339068084955))
                .add(new LatLng(-35.1344353641485, -70.09311340749264))
                .add(new LatLng(-35.134516250203404, -70.09288977831602))
                .add(new LatLng(-35.13463799041928, -70.09264267981052))
                .add(new LatLng(-35.1347783753071, -70.09233187884092))
                .add(new LatLng(-35.13500979049225, -70.09196944534779))
                .add(new LatLng(-35.13509890161445, -70.09166166186333))
                .add(new LatLng(-35.135168545315885, -70.09135857224464))
                .add(new LatLng(-35.13527301075636, -70.09102061390877))
                .add(new LatLng(-35.13534210593466, -70.09076211601496))
                .add(new LatLng(-35.13544355509816, -70.09046070277691))
                .add(new LatLng(-35.135582019292976, -70.09020254015923))
                .add(new LatLng(-35.135744885787076, -70.08986458182335))
                .add(new LatLng(-35.135886639692664, -70.08942067623138))
                .add(new LatLng(-35.13621703226704, -70.08887350559235))
                .add(new LatLng(-35.136507941133296, -70.08842457085848))
                .add(new LatLng(-35.136837782822184, -70.08807554841042))
                .add(new LatLng(-35.13706562779698, -70.08774228394032))
                .add(new LatLng(-35.137315954934245, -70.0872715562582))
                .add(new LatLng(-35.13755531411672, -70.08685547858477))
                .add(new LatLng(-35.13782126793851, -70.08639849722385))
                .add(new LatLng(-35.13796932432881, -70.08598510175943))
                .add(new LatLng(-35.13804170735496, -70.08558042347431))
                .add(new LatLng(-35.138174683579976, -70.0851821154356))
                .add(new LatLng(-35.1383433026045, -70.08478548377752));
        VulcanoI.width(6);
        VulcanoI.color(Color.RED);


        PolylineOptions Vulcanito = new PolylineOptions()
                .add(new LatLng(-35.13045704203369, -70.09866792708635))
                .add(new LatLng(-35.130610047470626, -70.09859215468168))
                .add(new LatLng(-35.13078965026009, -70.09850062429905))
                .add(new LatLng(-35.130943203475894, -70.09847313165665))
                .add(new LatLng(-35.13108578834559, -70.09843993932009))
                .add(new LatLng(-35.13121603392182, -70.09851034730673))
                .add(new LatLng(-35.1313558763086, -70.09858913719654))
                .add(new LatLng(-35.13152642885525, -70.09868938475847))
                .add(new LatLng(-35.13178116020907, -70.09871117770672))
                .add(new LatLng(-35.132035068171334, -70.09856667369604))
                .add(new LatLng(-35.13223824881059, -70.0982977822423))
                .add(new LatLng(-35.13244910645478, -70.09800307452679))
                .add(new LatLng(-35.132745238505194, -70.09751558303833))
                .add(new LatLng(-35.132942385078636, -70.09724367409945))
                .add(new LatLng(-35.13316695062177, -70.09689196944237))
                .add(new LatLng(-35.1334030316788, -70.09646147489548))
                .add(new LatLng(-35.133659402318536, -70.09606819599867))
                .add(new LatLng(-35.13381349850715, -70.09573325514793))
                .add(new LatLng(-35.13402352949746, -70.09537450969219))
                .add(new LatLng(-35.13426810798351, -70.09506706148386))
                .add(new LatLng(-35.13442384816127, -70.09482633322477))
                .add(new LatLng(-35.13459055562708, -70.09466506540775))
                .add(new LatLng(-35.134727924515914, -70.09448871016502))
                .add(new LatLng(-35.13480579420225, -70.0942275300622))
                .add(new LatLng(-35.1350078711747, -70.09387481957674))
                .add(new LatLng(-35.135187190076245, -70.09360559284687))
                .add(new LatLng(-35.135282607312405, -70.09332496672869))
                .add(new LatLng(-35.13545040976682, -70.09283110499382))
                .add(new LatLng(-35.13560642186924, -70.09211998432875))
                .add(new LatLng(-35.135693613037034, -70.09165160357952))
                .add(new LatLng(-35.1357662722722, -70.09116545319557))
                .add(new LatLng(-35.13594641205858, -70.09071417152882))
                .add(new LatLng(-35.136167679104595, -70.09024109691381))
                .add(new LatLng(-35.136424315222584, -70.08984178304672))
                .add(new LatLng(-35.13664366169078, -70.08943509310484))
                .add(new LatLng(-35.13678541403148, -70.08902035653591))
                .add(new LatLng(-35.137049999461674, -70.08857242763042))
                .add(new LatLng(-35.137366678302016, -70.08813489228487))
                .add(new LatLng(-35.137602198822606, -70.08780933916569))
                .add(new LatLng(-35.13781030078777, -70.08750222623348))
                .add(new LatLng(-35.137885425740706, -70.08708648383617))
                .add(new LatLng(-35.137897763773786, -70.08673578500748))
                .add(new LatLng(-35.13782126793851, -70.08639849722385));
        Vulcanito.color(Color.RED);
        Vulcanito.width(6);


        PolylineOptions Minerva = new PolylineOptions()
                .add(new LatLng(-35.140002330675976, -70.09393651038408))
                .add(new LatLng(-35.1398287799922, -70.09399686008692))
                .add(new LatLng(-35.13968483957092, -70.09407330304384))
                .add(new LatLng(-35.1394929186133, -70.09420741349459))
                .add(new LatLng(-35.13935829950016, -70.09433783590794))
                .add(new LatLng(-35.1390339515169, -70.09468752890825))
                .add(new LatLng(-35.13875511540443, -70.09505297988653))
                .add(new LatLng(-35.13867533026435, -70.09520787745714))
                .add(new LatLng(-35.13860541538659, -70.09534902870655))
                .add(new LatLng(-35.138595270869935, -70.09558338671923))
                .add(new LatLng(-35.13857059501333, -70.0957191735506))
                .add(new LatLng(-35.13859088405153, -70.0957778468728))
                .add(new LatLng(-35.13863694563302, -70.09580198675394))
                .add(new LatLng(-35.13863694563302, -70.09580198675394))
                .add(new LatLng(-35.13879706426114, -70.09581573307514))
                .add(new LatLng(-35.13901229171543, -70.09569335728884))
                .add(new LatLng(-35.13920229458752, -70.09553108364344))
                .add(new LatLng(-35.139413682543115, -70.09521525353193))
                .add(new LatLng(-35.1395825731741, -70.09495206177235))
                .add(new LatLng(-35.13972569125921, -70.09462516754866))
                .add(new LatLng(-35.13983700515144, -70.0943499058485))
                .add(new LatLng(-35.140002330675976, -70.09393651038408))
                .add(new LatLng(-35.14016957506065, -70.0935985520482))
                .add(new LatLng(-35.14036670365648, -70.09332697838545))
                .add(new LatLng(-35.14061674647373, -70.09305272251368))
                .add(new LatLng(-35.14092546055914, -70.09274192154408))
                .add(new LatLng(-35.14120703091097, -70.09243950247765))
                .add(new LatLng(-35.14152616107669, -70.09190306067467))
                .add(new LatLng(-35.1418107451691, -70.0914953649044))
                .add(new LatLng(-35.14209724741702, -70.09110778570175))
                .add(new LatLng(-35.14248080243623, -70.09058441966772))
                .add(new LatLng(-35.142816925649, -70.09006507694721))
                .add(new LatLng(-35.14307902402631, -70.08961714804173))
                .add(new LatLng(-35.143290401914534, -70.08921414613724))
                .add(new LatLng(-35.14341487060879, -70.0888916105032))
                .add(new LatLng(-35.14351384227607, -70.08852515369654))
                .add(new LatLng(-35.143556062839906, -70.08819188922644))
                .add(new LatLng(-35.14360294408966, -70.08783247321844))
                .add(new LatLng(-35.1436651782216, -70.08750658482313))
                .add(new LatLng(-35.14364735788031, -70.08728127926588))
                .add(new LatLng(-35.14367285467511, -70.08686687797308))
                .add(new LatLng(-35.143694787395425, -70.08653998374939))
                .add(new LatLng(-35.14369917393878, -70.08631065487862))
                .add(new LatLng(-35.14369040085183, -70.08609842509031))
                .add(new LatLng(-35.14366627485786, -70.08573465049267))
                .add(new LatLng(-35.14354235486703, -70.08552610874176))
                .add(new LatLng(-35.1434557204251, -70.08523341268301))
                .add(new LatLng(-35.14340280757185, -70.0849749147892))
                .add(new LatLng(-35.14329588512093, -70.08467350155115))
                .add(new LatLng(-35.14320020311627, -70.08441634476185))
                .add(new LatLng(-35.14310205355051, -70.08402541279793))
                .add(new LatLng(-35.14306476765092, -70.08373506367207))
                .add(new LatLng(-35.14300582299541, -70.08341185748577))
                .add(new LatLng(-35.14294605581276, -70.0832100212574))
                .add(new LatLng(-35.14287395130921, -70.08306819945574));
        Minerva.width(6);
        Minerva.color(Color.BLUE);


        PolylineOptions MinervaB = new PolylineOptions();
        MinervaB.width(6);
        MinervaB.color(Color.BLUE);


        PolylineOptions CarisMinerva = new PolylineOptions()
                .add(new LatLng(-35.143963736897696, -70.09691141545773))
                .add(new LatLng(-35.143852976965654, -70.0969124212861))
                .add(new LatLng(-35.14367340299318, -70.0968748703599))
                .add(new LatLng(-35.14353577503923, -70.09679038077593))
                .add(new LatLng(-35.143316995462115, -70.09679239243269))
                .add(new LatLng(-35.143152224991375, -70.09675584733486))
                .add(new LatLng(-35.142940572582795, -70.0966827571392))
                .add(new LatLng(-35.14269903593633, -70.09653825312853))
                .add(new LatLng(-35.14244379041524, -70.09643197059631))
                .add(new LatLng(-35.14223652278667, -70.09633339941502))
                .add(new LatLng(-35.14211260061906, -70.09618017822504))
                .add(new LatLng(-35.14200814395514, -70.09602930396795))
                .add(new LatLng(-35.141837065034906, -70.09589552879333))
                .add(new LatLng(-35.14151300109458, -70.09587038308382))
                .add(new LatLng(-35.14108338134379, -70.0959424674511))
                .add(new LatLng(-35.140910655469874, -70.0959213450551))
                .add(new LatLng(-35.14058658784133, -70.09603399783373))
                .add(new LatLng(-35.14019754048161, -70.0960836187005))
                .add(new LatLng(-35.139813426359396, -70.09617548435926))
                .add(new LatLng(-35.13954172141399, -70.09611513465643))
                .add(new LatLng(-35.13927961164663, -70.0960386916995))
                .add(new LatLng(-35.13906794916833, -70.09593442082405))
                .add(new LatLng(-35.13891413709985, -70.09591430425644))
                .add(new LatLng(-35.13879706426114, -70.09581573307514));
        CarisMinerva.color(Color.BLUE);
        CarisMinerva.width(6);


        PolylineOptions MinervaEros = new PolylineOptions()
                .add(new LatLng(-35.14209724741702, -70.09110778570175))
                .add(new LatLng(-35.14235797729442, -70.09100183844566))
                .add(new LatLng(-35.14260472404326, -70.09083285927773))
                .add(new LatLng(-35.142873128824014, -70.09074836969376))
                .add(new LatLng(-35.143105343482, -70.09062599390745))
                .add(new LatLng(-35.14334112155962, -70.09048014879227))
                .add(new LatLng(-35.14353988743167, -70.09023874998093))
                .add(new LatLng(-35.14374112024766, -70.08989777415991))
                .add(new LatLng(-35.14398155716972, -70.08942738175392))
                .add(new LatLng(-35.14414934169341, -70.08888490498066))
                .add(new LatLng(-35.14429437084776, -70.08836757391691))
                .add(new LatLng(-35.144410064997324, -70.08787203580141)) //
                .add(new LatLng(-35.144603345140965, -70.08733995258808))
                .add(new LatLng(-35.144737133113416, -70.08705832064152))
                .add(new LatLng(-35.14479635067035, -70.08670192211866))
                .add(new LatLng(-35.14476482280975, -70.08632741868496))
                .add(new LatLng(-35.144697380562604, -70.08612759411335))
                .add(new LatLng(-35.1446458392954, -70.08594587445259))
                .add(new LatLng(-35.144603345140965, -70.08565116673708))
                .add(new LatLng(-35.14461540799996, -70.08540742099285))
                .add(new LatLng(-35.14468449524886, -70.08516233414412))
                .add(new LatLng(-35.144907, -70.084836));
        MinervaEros.color(Color.BLUE);
        MinervaEros.width(6);


        PolylineOptions ErosMinerva = new PolylineOptions()
                .add(new LatLng(-35.144907, -70.084836))
                .add(new LatLng(-35.14477058007216, -70.08484415709972))
                .add(new LatLng(-35.14463021241538, -70.08491355925798))
                .add(new LatLng(-35.144477507482556, -70.08492093533278))
                .add(new LatLng(-35.14435962017509, -70.08489042520523))
                .add(new LatLng(-35.14428258209818, -70.08475966751575))
                .add(new LatLng(-35.14422418663898, -70.08459974080324))
                .add(new LatLng(-35.14415838888829, -70.08445288985968))
                .add(new LatLng(-35.14408793040461, -70.08430067449808))
                .add(new LatLng(-35.143971139165004, -70.08420545607805))
                .add(new LatLng(-35.14385708934206, -70.0840974971652))
                .add(new LatLng(-35.143725767352805, -70.08400328457355))
                .add(new LatLng(-35.14364324549331, -70.08384570479393))
                .add(new LatLng(-35.14351959962697, -70.0837628915906))
                .add(new LatLng(-35.14340829077068, -70.0836331397295))
                .add(new LatLng(-35.143282451264604, -70.08351914584637))
                .add(new LatLng(-35.14314948338337, -70.08342660963535))
                .add(new LatLng(-35.14309794113595, -70.08324757218361))
                .add(new LatLng(-35.143040367310334, -70.08311111479998))
                .add(new LatLng(-35.142946878297224, -70.08304473012686));
        ErosMinerva.color(Color.BLUE);
        ErosMinerva.width(6);


        PolylineOptions ErosII = new PolylineOptions()
                .add(new LatLng(-35.146175065777086, -70.08293308317661))
                .add(new LatLng(-35.145998512591326, -70.08307658135891))
                .add(new LatLng(-35.14574382572159, -70.08324891328812))
                .add(new LatLng(-35.14549187958401, -70.08343432098627))
                .add(new LatLng(-35.14521800036255, -70.0836056470871))
                .add(new LatLng(-35.14497701878382, -70.08376389741898));
        ErosII.width(6);
        ErosII.color(Color.BLUE);


        PolylineOptions ErosIIMinerva = new PolylineOptions()
                .add(new LatLng(-35.14497701878382, -70.08376389741898))
                .add(new LatLng(-35.144757420614624, -70.08385878056288))
                .add(new LatLng(-35.144660095393846, -70.0838490575552))
                .add(new LatLng(-35.144629938259556, -70.08376590907574))
                .add(new LatLng(-35.14459539461929, -70.08363548666239))
                .add(new LatLng(-35.1445611251204, -70.08349366486073))
                .add(new LatLng(-35.1445367252284, -70.08332971483469))
                .add(new LatLng(-35.14451232532908, -70.08318454027176))
                .add(new LatLng(-35.14445694575517, -70.08304741233587))
                .add(new LatLng(-35.144356056137546, -70.08299343287945))
                .add(new LatLng(-35.14420965630695, -70.0829253718257))
                .add(new LatLng(-35.14411315290383, -70.08282914757729))
                .add(new LatLng(-35.14398950775134, -70.08274164050817))
                .add(new LatLng(-35.14388450517948, -70.08265748620033))
                .add(new LatLng(-35.1437762125679, -70.08260417729616))
                .add(new LatLng(-35.143651196107996, -70.08258741348982))
                .add(new LatLng(-35.143502327573046, -70.08260484784842))
                .add(new LatLng(-35.143330155152555, -70.0826521217823))
                .add(new LatLng(-35.143152224991375, -70.0827382877469));
        ErosIIMinerva.color(Color.BLUE);
        ErosIIMinerva.width(6);


        PolylineOptions CarisII = new PolylineOptions()
                .add(new LatLng(-35.143073540805304, -70.09062130004168))
                .add(new LatLng(-35.143104246838185, -70.0909036025405))
                .add(new LatLng(-35.143161272297085, -70.09125094860792))
                .add(new LatLng(-35.14320513800752, -70.09164221584797))
                .add(new LatLng(-35.1432248775695, -70.09214144200087))
                .add(new LatLng(-35.14324955201525, -70.09289279580116))
                .add(new LatLng(-35.14330054584608, -70.09385269135237))
                .add(new LatLng(-35.14332439778824, -70.09498357772827))
                .add(new LatLng(-35.143350991324716, -70.09559445083141))
                .add(new LatLng(-35.1433803264528, -70.096151009202))
                .add(new LatLng(-35.14344886643051, -70.09678736329079));
        CarisII.color(Color.RED);
        CarisII.width(6);


        PolylineOptions CarisIII = new PolylineOptions()
                .add(new LatLng(-35.14177428117431, -70.09152855724096))
                .add(new LatLng(-35.141790182768034, -70.09198721498251))
                .add(new LatLng(-35.141805810193325, -70.09269196540117))
                .add(new LatLng(-35.14183158173023, -70.09346008300781))
                .add(new LatLng(-35.14185625659835, -70.0941839441657))
                .add(new LatLng(-35.14188888224582, -70.09510863572359))
                .add(new LatLng(-35.141924249529566, -70.09562227874994))
                .add(new LatLng(-35.142009240613724, -70.09605880826712));
        CarisIII.color(Color.RED);
        CarisIII.width(6);


        PolylineOptions Pluton = new PolylineOptions()
                .add(new LatLng(-35.14451561520358, -70.09496916085482))
                .add(new LatLng(-35.14462006864955, -70.09479448199272))
                .add(new LatLng(-35.14474618024297, -70.09462617337704))
                .add(new LatLng(-35.14489970713641, -70.09446490556002))
                .add(new LatLng(-35.14504802480665, -70.09427648037672))
                .add(new LatLng(-35.145241851742796, -70.09405620396137))
                .add(new LatLng(-35.14548228423146, -70.09377758949995))
                .add(new LatLng(-35.145773708291166, -70.09336184710264))
                .add(new LatLng(-35.14599878674253, -70.09294878691435))
                .add(new LatLng(-35.14627567898957, -70.09258434176445))
                .add(new LatLng(-35.146515560125145, -70.09227555245161))
                .add(new LatLng(-35.14680094921802, -70.09206399321556))
                .add(new LatLng(-35.147055632779555, -70.09190171957016))
                .add(new LatLng(-35.14726508131149, -70.0915765017271))
                .add(new LatLng(-35.14740736344913, -70.09108934551477));
        Pluton.color(Color.BLACK);
        Pluton.width(8);


        PolylineOptions MinervaIII = new PolylineOptions()
                .add(new LatLng(-35.138885074537335, -70.0948528200388))
                .add(new LatLng(-35.138877945982685, -70.09452056139708))
                .add(new LatLng(-35.138829416959396, -70.09430900216103))
                .add(new LatLng(-35.13874332585257, -70.09408839046955))
                .add(new LatLng(-35.138653944543414, -70.09393952786922))
                .add(new LatLng(-35.13861226978903, -70.093740709126))
                .add(new LatLng(-35.13858649723289, -70.09351640939713))
                .add(new LatLng(-35.13860185109711, -70.09327199310064))
                .add(new LatLng(-35.138610350556405, -70.09310971945524))
                .add(new LatLng(-35.13862104342331, -70.09284149855375))
                .add(new LatLng(-35.13862707529633, -70.09255986660719))
                .add(new LatLng(-35.13862296265568, -70.09229030460119))
                .add(new LatLng(-35.13862789782443, -70.09200096130371))
                .add(new LatLng(-35.138635300576986, -70.08955210447311))
                .add(new LatLng(-35.138544822443976, -70.08941598236561))
                .add(new LatLng(-35.138517404808056, -70.08922755718231))
                .add(new LatLng(-35.13851137292692, -70.08813891559839))
                .add(new LatLng(-35.138475729983774, -70.08737951517105))
                .add(new LatLng(-35.13844118373169, -70.08720383048058))
                .add(new LatLng(-35.13838250990488, -70.08707407861948))
                .add(new LatLng(-35.13829340237672, -70.08696544915438))
                .add(new LatLng(-35.13822759983178, -70.08688531816006));
        MinervaIII.color(Color.RED);
        MinervaIII.width(6);


        PolylineOptions Selene = new PolylineOptions()
                .add(new LatLng(-35.13822759983178, -70.08688531816006))
                .add(new LatLng(-35.13815658785902, -70.0867361202836))
                .add(new LatLng(-35.138117106272105, -70.08652556687593))
                .add(new LatLng(-35.13812889591464, -70.08629623800516))
                .add(new LatLng(-35.1381357503572, -70.08601427078247))
                .add(new LatLng(-35.1381702967389, -70.08568100631237))
                .add(new LatLng(-35.138282161112386, -70.08508387953043))
                .add(new LatLng(-35.1383433026045, -70.08478548377752))
                .add(new LatLng(-35.138339738303536, -70.08466076105833))
                .add(new LatLng(-35.13832822286856, -70.0845430791378))
                .add(new LatLng(-35.138307111233544, -70.08437175303698))
                .add(new LatLng(-35.138264065415335, -70.08423931896687))
                .add(new LatLng(-35.138032111124126, -70.08372534066439))
                .add(new LatLng(-35.13786020131168, -70.08330523967743))
                .add(new LatLng(-35.137612891821924, -70.08271247148514))
                .add(new LatLng(-35.13673962558478, -70.08060861378908))
                .add(new LatLng(-35.136287771747696, -70.07955484092236));
        Selene.color(Color.BLUE);
        Selene.width(6);


        PolylineOptions VulcanoMinerva = new PolylineOptions()
                .add(new LatLng(-35.1381702967389, -70.08568100631237))
                .add(new LatLng(-35.138330416284866, -70.08548185229301))
                .add(new LatLng(-35.138584852175825, -70.08523106575012))
                .add(new LatLng(-35.13884860923195, -70.08496150374413))
                .add(new LatLng(-35.13916857116641, -70.08473418653011))
                .add(new LatLng(-35.13984879454491, -70.08435029536486))
                .add(new LatLng(-35.1409578124115, -70.08369248360395))
                .add(new LatLng(-35.142032544605165, -70.08305311203003));
        VulcanoMinerva.color(Color.BLUE);
        VulcanoMinerva.width(6);


        PolylineOptions VestaUrano = new PolylineOptions()
                .add(new LatLng(-35.13984879454491, -70.08435029536486))
                .add(new LatLng(-35.139951060607274, -70.08405961096287))
                .add(new LatLng(-35.140054149054855, -70.08371192961931))
                .add(new LatLng(-35.14006347087613, -70.08330624550581))
                .add(new LatLng(-35.139998218104814, -70.0831288844347))
                .add(new LatLng(-35.13978573497861, -70.08283283561468))
                .add(new LatLng(-35.13901640433642, -70.08187729865313))
                .add(new LatLng(-35.1380151120839, -70.08079536259174))
                .add(new LatLng(-35.137453319224676, -70.08011642843485))
                .add(new LatLng(-35.137163236279406, -70.07987469434738))
                .add(new LatLng(-35.13687616830184, -70.07977411150932))
                .add(new LatLng(-35.13662666236061, -70.0795990973711))
                .add(new LatLng(-35.136287771747696, -70.07955484092236));
        VestaUrano.color(Color.BLUE);
        VestaUrano.width(6);

        PolylineOptions VulcanoMinervaII = new PolylineOptions()
                .add(new LatLng(-35.13468569937315, -70.09466305375099))
                .add(new LatLng(-35.13485597075647, -70.09463489055634))
                .add(new LatLng(-35.13502870947711, -70.09467277675867))
                .add(new LatLng(-35.135190754515236, -70.0947130098939))
                .add(new LatLng(-35.135453974194284, -70.09474854916334))
                .add(new LatLng(-35.13570211279997, -70.09477704763412))
                .add(new LatLng(-35.13599631372495, -70.09476765990257))
                .add(new LatLng(-35.13633164116219, -70.09476263076067))
                .add(new LatLng(-35.136688901826325, -70.0947941467166))
                .add(new LatLng(-35.137099626270434, -70.09485214948654))
                .add(new LatLng(-35.13747772124004, -70.09482197463512))
                .add(new LatLng(-35.13773627248169, -70.09486723691225))
                .add(new LatLng(-35.1380614481691, -70.09455643594265))
                .add(new LatLng(-35.138353172975606, -70.09403977543116))
                .add(new LatLng(-35.138573336775536, -70.09356167167425))
                .add(new LatLng(-35.13876992088573, -70.09344264864922))
                .add(new LatLng(-35.1389862451111, -70.0932864099741))
                .add(new LatLng(-35.13927193477879, -70.09315095841885))
                .add(new LatLng(-35.13957078374207, -70.09310971945524))
                .add(new LatLng(-35.139861680624186, -70.09302321821451))
                .add(new LatLng(-35.14020137887198, -70.09290721267462))
                .add(new LatLng(-35.1404146834233, -70.09288340806961))
                .add(new LatLng(-35.140603038005835, -70.09287972003222))
                .add(new LatLng(-35.14079495634617, -70.0928445160389));
        VulcanoMinervaII.color(Color.BLUE);
        VulcanoMinervaII.width(6);

        PolylineOptions UranoII = new PolylineOptions()
                .add(new LatLng(-35.13508436965347, -70.08812248706818))
                .add(new LatLng(-35.134934937074235, -70.08814696222544))
                .add(new LatLng(-35.13477426247203, -70.08815165609121))
                .add(new LatLng(-35.134660748142146, -70.0879692658782))
                .add(new LatLng(-35.134622361617964, -70.08776675909758))
                .add(new LatLng(-35.13462126485987, -70.08752569556236))
                .add(new LatLng(-35.134589733058185, -70.0872903317213))
                .add(new LatLng(-35.134617700395935, -70.08686821907759))
                .add(new LatLng(-35.13457657195473, -70.08660268038511))
                .add(new LatLng(-35.13457657195473, -70.08576516062021))
                .add(new LatLng(-35.13470571519031, -70.08480794727802))
                .add(new LatLng(-35.134987855431824, -70.08324086666107))
                .add(new LatLng(-35.135181432136, -70.081900767982))
                .add(new LatLng(-35.13534813805063, -70.08067399263382))
                .add(new LatLng(-35.13543670042893, -70.08019454777241))
                .add(new LatLng(-35.13556200369126, -70.07990252226591))
                .add(new LatLng(-35.13568895187633, -70.07976975291967))
                .add(new LatLng(-35.13586305975644, -70.07967285811901))
                .add(new LatLng(-35.13600097486805, -70.07957395166159))
                .add(new LatLng(-35.13614080903691, -70.0794867798686))
                .add(new LatLng(-35.136287771747696, -70.07955484092236));
        UranoII.color(Color.BLUE);
        UranoII.width(6);

        PolylineOptions UranoI = new PolylineOptions()
                .add(new LatLng(-35.13530399391896, -70.08782275021076))
                .add(new LatLng(-35.13550579546834, -70.0874187424779))
                .add(new LatLng(-35.136115858251834, -70.08628617972136))
                .add(new LatLng(-35.136124632155145, -70.08592240512371))
                .add(new LatLng(-35.13637770402197, -70.0835982710123))
                .add(new LatLng(-35.136442959695366, -70.08215457201004))
                .add(new LatLng(-35.13646516854707, -70.08061666041613))
                .add(new LatLng(-35.13646626528033, -70.08007451891899))
                .add(new LatLng(-35.136287771747696, -70.07955484092236));
        UranoI.color(Color.RED);
        UranoI.width(6);

        PolylineOptions UranoVesta = new PolylineOptions()
                .add(new LatLng(-35.13508436965347, -70.08812248706818))
                .add(new LatLng(-35.13530399391896, -70.08782275021076))
                .add(new LatLng(-35.13566646862689, -70.08752334862947))
                .add(new LatLng(-35.136070891982506, -70.08730541914701))
                .add(new LatLng(-35.13666312866128, -70.08712202310562))
                .add(new LatLng(-35.13737051682586, -70.08686687797308))
                .add(new LatLng(-35.137814961827026, -70.08681926876307))
                .add(new LatLng(-35.13801045105611, -70.0868920236826))
                .add(new LatLng(-35.13813794377869, -70.08692622184753))
                .add(new LatLng(-35.13822759983178, -70.08688531816006));
        UranoVesta.color(Color.BLUE);
        UranoVesta.width(6);

        PolylineOptions VestaMinerva = new PolylineOptions()
                .add(new LatLng(-35.13822759983178, -70.08688531816006))
                .add(new LatLng(-35.13833288387815, -70.08678909391165))
                .add(new LatLng(-35.138613366493374, -70.08667510002851))
                .add(new LatLng(-35.13882612685508, -70.08647494018078))
                .add(new LatLng(-35.13990472559762, -70.08561059832573))
                .add(new LatLng(-35.14098111670397, -70.08451022207737))
                .add(new LatLng(-35.14155769019181, -70.08392818272114))
                .add(new LatLng(-35.1417175289093, -70.08352015167475))
                .add(new LatLng(-35.141848305808324, -70.08320063352585))
                .add(new LatLng(-35.142032544605165, -70.08305311203003));
        VestaMinerva.color(Color.BLUE);
        VestaMinerva.width(6);

        PolylineOptions noName = new PolylineOptions()
                .add(new LatLng(-35.13105919080229, -70.11450804769993))
                .add(new LatLng( - 35.131070981466884, -70.11424653232098))
                .add(new LatLng( - 35.13108661094988, -70.11338654905558))
                .add(new LatLng( - 35.13101175392517, -70.11269252747297))
                .add(new LatLng( - 35.13098981778768, -70.11235322803259))
                .add(new LatLng( - 35.13084832955888, -70.11206690222025))
                .add(new LatLng( - 35.13071616397579, -70.11187445372343))
                .add(new LatLng( - 35.13066900110168, -70.11165786534548))
                .add(new LatLng(-35.13066488805904, -70.11137455701828));
        noName.color(Color.BLUE);
        noName.width(6);

        PolylineOptions Apolo = new PolylineOptions() //agreegar on polylineClick
                .add(new LatLng(-35.12869444254154, -70.11512361466885))
                .add(new LatLng (-35.128645633238634,-70.11538278311491))
                .add(new LatLng (-35.12851867407219,-70.11570062488317))
                .add(new LatLng (-35.1282984830831,-70.11597756296396))
                .add(new LatLng (-35.12798560793887,-70.11621493846178))
                .add(new LatLng (-35.127425116945055,-70.11655692011118))
                .add(new LatLng (-35.126970195049,-70.11681441217661))
                .add(new LatLng (-35.12647989663409,-70.11693812906742))
                .add(new LatLng (-35.126120123212694,-70.11684358119965))
                .add(new LatLng (-35.125663000269704,-70.1168442517519))
                .add(new LatLng (-35.12505971471398,-70.11717114597559))
                .add(new LatLng (-35.12451154364556,-70.11770624667406))
                .add(new LatLng (-35.12395815860479,-70.11857394129038))
                .add(new LatLng (-35.12388795686409,-70.11946879327297))
                .add(new LatLng (-35.124206606466764,-70.12006424367428))
                .add(new LatLng (-35.125316934283745,-70.12063555419445))
                .add(new LatLng (-35.12646810530476,-70.12114349752665))
                .add(new LatLng (-35.12761542107117,-70.12165814638138))
                .add(new LatLng (-35.1283157583628,-70.12223683297634))
                .add(new LatLng (-35.12908327066251,-70.12197900563478))
                .add(new LatLng (-35.12974794856713,-70.1222301274538))
                .add(new LatLng (-35.1303566834727,-70.12307938188314))
                .add(new LatLng (-35.13065638777025,-70.12433666735888))
                .add(new LatLng(-35.131524783655536, -70.12528281658888))
                .add(new LatLng (-35.13305124049252,-70.1258309930563))
                .add(new LatLng (-35.134786600976625,-70.1253542304039))
                .add(new LatLng (-35.13579478757696,-70.12400642037392))
                .add(new LatLng (-35.13762139138449,-70.12268476188183))
                .add(new LatLng (-35.139532399532975,-70.12122496962547))
                .add(new LatLng (-35.140908462123086,-70.12058090418577))
                .add(new LatLng (-35.14224721517724,-70.12017354369164))
                .add(new LatLng(-35.14289643256812, -70.1191546395421))
                .add(new LatLng (-35.14289643256812,-70.11815182864666))
                .add(new LatLng(-35.14322926413817, -70.1160915568471))
                .add(new LatLng (-35.14346394521785,-70.11506896466017))
                .add(new LatLng (-35.14365667929009,-70.11409733444452))
                .add(new LatLng (-35.14382419032498,-70.11341907083988))
                .add(new LatLng (-35.144076141625106,-70.11261206120253))
                .add(new LatLng (-35.14428943602257,-70.11188853532076))
                .add(new LatLng (-35.14439388975881,-70.1113249361515));
        Apolo.color(Color.BLUE);
        Apolo.width(6);

        PolylineOptions NeptunoI = new PolylineOptions()//agreegar on polylineClick
                .add(new LatLng(-35.14439388975881, -70.1113249361515))
                .add(new LatLng(-35.144424595293586,-70.11030670255423))
                .add(new LatLng(-35.14444789859349,-70.10951142758131))
                .add(new LatLng(-35.144387035843216,-70.10870307683945))
                .add(new LatLng(-35.14437744036042,-70.10780286043882))
                .add(new LatLng(-35.14441609644137,-70.10703273117542))
                .add(new LatLng(-35.14470258951864,-70.10570503771305))
                .add(new LatLng(-35.14545240155488,-70.10434113442898))
                .add(new LatLng(-35.146044844132106,-70.10322399437428))
                .add(new LatLng(-35.14686235845971,-70.10114260017872))
                .add(new LatLng(-35.14731552433287, -70.10011062026024))
                .add(new LatLng(-35.14775964088906,-70.09869508445263))
                .add(new LatLng(-35.14808203509301,-70.09731877595186))
                .add(new LatLng(-35.1481272688715,-70.09657178074121))
                .add(new LatLng(-35.148094645724704,-70.09585294872522))
                .add(new LatLng(-35.14809190428319,-70.09498558938503))
                .add(new LatLng(-35.147918096703336,-70.09383492171764));
        NeptunoI.color(Color.BLUE);
        NeptunoI.width(6);

        PolylineOptions VenusI = new PolylineOptions()//agreegar on polylineClick
                .add(new LatLng(-35.147918096703336, -70.09383492171764))
                .add(new LatLng( - 35.14764532222939, -70.0919097661972))
                .add(new LatLng( - 35.147408460034974, -70.090521723032))
                .add(new LatLng( - 35.14713294237898, -70.0882988423109))
                .add(new LatLng( - 35.14699065976155, -70.08700635284185))
                .add(new LatLng( - 35.146884290320514, -70.0856065750122))
                .add(new LatLng( - 35.14677380851162, -70.08486527949572));
        VenusI.color(Color.GREEN);
        VenusI.width(6);


        PolylineOptions VenusII = new PolylineOptions()
                .add(new LatLng(-35.14808203509301,-70.09731877595186))
                .add(new LatLng(-35.14850586090435,-70.09688057005405))
                .add(new LatLng(-35.148854295606924, -70.09645510464907))
                .add(new LatLng(-35.14901028202021,-70.09601354598999))
                .add(new LatLng(-35.14912130907424,-70.09544726461172))
                .add(new LatLng(-35.14917421820965,-70.09503051638603))
                .add(new LatLng(-35.14927345701335,-70.09444043040276))
                .add(new LatLng(-35.14921013060839,-70.09408302605152))
                .add(new LatLng(-35.149106231335054, -70.09366996586323))
                .add(new LatLng(-35.148939005313096,-70.09272079914808))
                .add(new LatLng(-35.14875204073086, -70.09152419865131))
                .add(new LatLng(-35.14848968647987,-70.09006943553686))
                .add(new LatLng(-35.148202932590216,-70.08871257305145))
                .add(new LatLng(-35.14798827772663,-70.08744556456804))
                .add(new LatLng(-35.14781008360389,-70.0865587592125))
                .add(new LatLng(-35.14770179621562,-70.08590061217546))
                .add(new LatLng(-35.14756335263594,-70.08520055562258));
        VenusII.width(6);
        VenusII.color(Color.GREEN);

        PolylineOptions JupiterA = new PolylineOptions()
                .add(new LatLng(-35.12869444254154, -70.11512361466885))
                .add(new LatLng(-35.12913646703117, -70.11574756354094))
                .add(new LatLng(-35.1294391920964, -70.11607680469751))
                .add(new LatLng(-35.1298658570348, -70.11618744581938))
                .add(new LatLng(-35.13033419875082, -70.11645499616861))
                .add(new LatLng(-35.13078608562843, -70.11699344962835))
                .add(new LatLng(-35.13114364482727, -70.11774849146605))
                .add(new LatLng(-35.131364650724855, -70.11886194348335))
                .add(new LatLng(-35.13174167553042, -70.12016247957945));
        JupiterA.width(6);
        JupiterA.color(Color.RED);

        PolylineOptions JupiterB = new PolylineOptions()
                .add(new LatLng(- 35.13105919080229, -70.11450804769993))
                .add(new LatLng(- 35.13127498711415, -70.11508069932461))
                .add(new LatLng(- 35.131462814442315, -70.1156972721219))
                .add(new LatLng(-35.13135944091532, -70.11624544858932))
                .add(new LatLng(- 35.13138494155884, -70.1170477643609))
                .add(new LatLng(- 35.13218121569987, -70.1245354861021))
                .add(new LatLng(-35.13218121569987, -70.1245354861021))
                .add(new LatLng(-35.13252094599559, -70.12535288929939))
                .add(new LatLng(-35.13305124049252, -70.1258309930563));
        JupiterB.width(6);
        JupiterB.color(Color.RED);

        PolylineOptions anexo = new PolylineOptions()
                .add(new LatLng(-35.13105919080229,-70.11450804769993))
                .add(new LatLng(-35.131070981466884,-70.11424653232098))
                .add(new LatLng(-35.13108661094988,-70.11338654905558))
                .add(new LatLng(-35.13101175392517,-70.11269252747297))
                .add(new LatLng(-35.13098981778768,-70.11235322803259))
                .add(new LatLng(-35.13084832955888,-70.11206690222025))
                .add(new LatLng(-35.13071616397579,-70.11187445372343))
                .add(new LatLng(-35.13066900110168,-70.11165786534548))
                .add(new LatLng(-35.13066488805904,-70.11137455701828));
        anexo.width(6);
        anexo.color(Color.GREEN);


        final Polyline PjupiterB = mapa.addPolyline(JupiterB);
        final Polyline PjupiterA = mapa.addPolyline(JupiterA);
        final Polyline PvenusII = mapa.addPolyline(VenusII);
        final Polyline PvenusI = mapa.addPolyline(VenusI);
        final Polyline PneptunoI = mapa.addPolyline(NeptunoI);
        final Polyline Papolo = mapa.addPolyline(Apolo);
        final Polyline Pcaris = mapa.addPolyline(caris);
        final Polyline Ptalìa = mapa.addPolyline(talìa);
        final Polyline Peros1 = mapa.addPolyline(eros1);
        final Polyline PVulcanoI = mapa.addPolyline(VulcanoI);
        final Polyline PVulcanito = mapa.addPolyline(Vulcanito);
        final Polyline PMinerva = mapa.addPolyline(Minerva);
        final Polyline PMinervaB = mapa.addPolyline(MinervaB);
        final Polyline PCarisMinerva = mapa.addPolyline(CarisMinerva);
        final Polyline PMinervaEros = mapa.addPolyline(MinervaEros);
        final Polyline PErosMinerva = mapa.addPolyline(ErosMinerva);
        final Polyline PErosII = mapa.addPolyline(ErosII);
        final Polyline PErosIIMinerva = mapa.addPolyline(ErosIIMinerva);
        final Polyline PCarisII = mapa.addPolyline(CarisII);
        final Polyline PCarisIII = mapa.addPolyline(CarisIII);
        final Polyline PPluton = mapa.addPolyline(Pluton);
        final Polyline PMinervaIII = mapa.addPolyline(MinervaIII);
        final Polyline PSelene = mapa.addPolyline(Selene);
        final Polyline PVulcanoMinerva = mapa.addPolyline(VulcanoMinerva);
        final Polyline PVestaUrano = mapa.addPolyline(VestaUrano);
        final Polyline PVulcanoMinervaII = mapa.addPolyline(VulcanoMinervaII);
        final Polyline PUranoII = mapa.addPolyline(UranoII);
        final Polyline PUranoI = mapa.addPolyline(UranoI);
        final Polyline PVestaMinerva = mapa.addPolyline(VestaMinerva);
        final Polyline PUranoVesta = mapa.addPolyline(UranoVesta);
        final Polyline Panexo = mapa.addPolyline(anexo);

        Pcaris.setClickable(true);
        Ptalìa.setClickable(true);
        Peros1.setClickable(true);
        PVulcanoI.setClickable(true);
        PVulcanito.setClickable(true);
        PMinerva.setClickable(true);
        PMinervaB.setClickable(true);
        PCarisMinerva.setClickable(true);
        PMinervaEros.setClickable(true);
        PErosMinerva.setClickable(true);
        PErosII.setClickable(true);
        PErosIIMinerva.setClickable(true);
        PCarisII.setClickable(true);
        PCarisIII.setClickable(true);
        PPluton.setClickable(true);
        PMinervaIII.setClickable(true);
        PSelene.setClickable(true);
        PVulcanoMinerva.setClickable(true);
        PVestaUrano.setClickable(true);
        PVulcanoMinervaII.setClickable(true);
        PUranoII.setClickable(true);
        PUranoI.setClickable(true);
        PVestaMinerva.setClickable(true);
        PUranoVesta.setClickable(true);
        PjupiterB.setClickable(true);
        PjupiterA.setClickable(true);
        PvenusII.setClickable(true);
        PvenusI.setClickable(true);
        PneptunoI.setClickable(true);
        Papolo.setClickable(true);
        Panexo.setClickable(true);


                mapa.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onPolylineClick(final Polyline polyline) {


                Log.i("Polilyne Clicked ==", " " + polyline.getId());

                final int color = polyline.getColor();

                polyline.setColor(Color.YELLOW);
                polyline.setWidth(12);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // acciones que se ejecutan tras los milisegundos
                        polyline.setColor(color);
                        polyline.setWidth(6);
                    }
                }, 2000);

                if (Objects.equals(polyline.getId(), Pcaris.getId())) {
                    Snackbar.make(fab, "Caris           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), Ptalìa.getId())) {
                    Snackbar.make(fab, "Talìa           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), Peros1.getId())) {
                    Snackbar.make(fab, "Eros I           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVulcanoI.getId())) {
                    Snackbar.make(fab, "Vulcano I        ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVulcanito.getId())) {
                    Snackbar.make(fab, "Vulcanito       ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PMinerva.getId()) ||
                        Objects.equals(polyline.getId(), PMinervaB.getId())) {
                    Snackbar.make(fab, "Minerva         ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PCarisMinerva.getId())) {
                    Snackbar.make(fab, "Caris - Minerva    ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PMinervaEros.getId())) {
                    Snackbar.make(fab, "Minerva - Eros     ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PErosMinerva.getId())) {
                    Snackbar.make(fab, "Eros - Minerva     ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PErosII.getId())) {
                    Snackbar.make(fab, "Eros II          ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PErosIIMinerva.getId())) {
                    Snackbar.make(fab, "Eros II - Minerva   ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PCarisII.getId())) {
                    Snackbar.make(fab, "Caris II         ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PCarisIII.getId())) {
                    Snackbar.make(fab, "Caris III        ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PPluton.getId())) {
                    Snackbar.make(fab, "Pluton          ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PMinervaIII.getId())) {
                    Snackbar.make(fab, "Minerva III      ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PSelene.getId())) {
                    Snackbar.make(fab, "Selene          ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVulcanoMinerva.getId())) {
                    Snackbar.make(fab, "Vulcano - Minerva  ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVestaUrano.getId())) {
                    Snackbar.make(fab, "Vesta - Urano      ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVulcanoMinervaII.getId())) {
                    Snackbar.make(fab, "Vulcano - MinervaII", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PUranoII.getId())) {
                    Snackbar.make(fab, "Urano II         ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PUranoI.getId())) {
                    Snackbar.make(fab, "Urano I          ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVestaMinerva.getId())) {
                    Snackbar.make(fab, "Vesta - Minerva    ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PUranoVesta.getId())) {
                    Snackbar.make(fab, "Urano - Vesta      ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), Papolo.getId())) {
                    Snackbar.make(fab, "Apolo           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PneptunoI.getId())) {
                    Snackbar.make(fab, "Neptuno I           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PvenusI.getId())) {
                    Snackbar.make(fab, "Venus I           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PvenusII.getId())) {
                    Snackbar.make(fab, "Venus II           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PjupiterA.getId()) ||
                        Objects.equals(polyline.getId(), PjupiterB.getId())) {
                    Snackbar.make(fab, "Jupiter           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

            }
        });


    }

    public void mostrarPyM() {
        final PolylineOptions minerva2 = new PolylineOptions()
                .add(new LatLng(-35.1423613, -70.0833304))
                .add(new LatLng(-35.1386929, -70.0956407));
        minerva2.width(6);

        final PolylineOptions TKeros2 = new PolylineOptions()
                .add(new LatLng(-35.1465734, -70.0825908))
                .add(new LatLng(-35.1461367, -70.0828449))
                .add(new LatLng(-35.1456846, -70.0831513))
                .add(new LatLng(-35.1452016, -70.0833891))
                .add(new LatLng(-35.1447530, -70.0836881));
        TKeros2.width(6);

        final PolylineOptions TKeros1 = new PolylineOptions()
                .add(new LatLng(-35.1468284, -70.0834109))
                .add(new LatLng(-35.1465369, -70.0836217))
                .add(new LatLng(-35.1460440, -70.0839097))
                .add(new LatLng(-35.1456292, -70.0842551))
                .add(new LatLng(-35.1452397, -70.0845156))
                .add(new LatLng(-35.1447127, -70.0849102));
        TKeros1.width(6);

        final PolylineOptions TKminerva1 = new PolylineOptions()
                .add(new LatLng(-35.1425995, -70.0832208))
                .add(new LatLng(-35.1426294, -70.0843329))
                .add(new LatLng(-35.1427103, -70.0853772))
                .add(new LatLng(-35.1427986, -70.0867532))
                .add(new LatLng(-35.1428967, -70.0880719))
                .add(new LatLng(-35.1429576, -70.0891970))
                .add(new LatLng(-35.1424786, -70.0903537))
                .add(new LatLng(-35.1414974, -70.0911018))
                .add(new LatLng(-35.1407774, -70.0916241))
                .add(new LatLng(-35.1401589, -70.0920747))
                .add(new LatLng(-35.1391453, -70.0928197));
        TKminerva1.width(6);

        final PolylineOptions TScaris = new PolylineOptions()
                .add(new LatLng(-35.1426801, -70.0831986))
                .add(new LatLng(-35.1427544, -70.0837304))
                .add(new LatLng(-35.1428342, -70.0842698))
                .add(new LatLng(-35.1429417, -70.0854041))
                .add(new LatLng(-35.1430130, -70.0862302))
                .add(new LatLng(-35.1431637, -70.0875358))
                .add(new LatLng(-35.1432918, -70.0886784))
                .add(new LatLng(-35.1434417, -70.0901717))
                .add(new LatLng(-35.1435149, -70.0910987))
                .add(new LatLng(-35.1435887, -70.0917733))
                .add(new LatLng(-35.1437110, -70.0930303))
                .add(new LatLng(-35.1438039, -70.0938691))
                .add(new LatLng(-35.1438782, -70.0946711))
                .add(new LatLng(-35.1439912, -70.0958637))
                .add(new LatLng(-35.1440641, -70.0969151));
        TScaris.width(6);

        final PolylineOptions TKurano = new PolylineOptions()
                .add(new LatLng(-35.1361737, -70.0797520))
                .add(new LatLng(-35.1360229, -70.0806512))
                .add(new LatLng(-35.1359058, -70.0814300))
                .add(new LatLng(-35.1357690, -70.0822354))
                .add(new LatLng(-35.1356396, -70.0830997))
                .add(new LatLng(-35.1354644, -70.0841552))
                .add(new LatLng(-35.1353125, -70.0851372))
                .add(new LatLng(-35.1350786, -70.0865272))
                .add(new LatLng(-35.1349166, -70.0874898));
        TKurano.width(6);

        final PolylineOptions TSvulcano = new PolylineOptions()
                .add(new LatLng(-35.138157, -70.084886))
                .add(new LatLng(-35.137977, -70.085259))
                .add(new LatLng(-35.137549, -70.086010))
                .add(new LatLng(-35.137231, -70.086538))
                .add(new LatLng(-35.136554, -70.087684))
                .add(new LatLng(-35.135977, -70.088689))
                .add(new LatLng(-35.135353, -70.089732))
                .add(new LatLng(-35.134945, -70.090402))
                .add(new LatLng(-35.134332, -70.091417))
                .add(new LatLng(-35.133667, -70.092495))
                .add(new LatLng(-35.132860, -70.093864))
                .add(new LatLng(-35.131880, -70.095582))
                .add(new LatLng(-35.131661, -70.095910))
                .add(new LatLng(-35.130897, -70.097350))
                .add(new LatLng(-35.130541, -70.097927))
                .add(new LatLng(-35.130131, -70.098632))
                .add(new LatLng(-35.129837, -70.099014));
        TSvulcano.width(6);

        final PolylineOptions TsVenusI = new PolylineOptions()
                .add(new LatLng(-35.14696543815408, -70.08455380797386))
                .add(new LatLng(-35.147096480768546, -70.085389316082))
                .add(new LatLng(-35.147265629605364, -70.08674181997776))
                .add(new LatLng(-35.14740352539859, -70.0878244265914))
                .add(new LatLng(-35.14755375752763, -70.08901733905077))
                .add(new LatLng(-35.14769987719732, -70.09028099477291))
                .add(new LatLng(-35.147821049407334, -70.09118221700191))
                .add(new LatLng(-35.14795181649943, -70.09220916777849))
                .add(new LatLng(-35.14810149932804, -70.09328473359346))
                .add(new LatLng(-35.14826406663041, -70.09454805403948))
                .add(new LatLng(-35.1483156056062, -70.09498290717602))
                .add(new LatLng(-35.148335618072025, -70.09523537009954))
                ;
        TsVenusI.width(6);

        final PolylineOptions TsNeptuno = new PolylineOptions()
                .add(new LatLng(-35.14901549070021, -70.09471368044615))
                .add(new LatLng(-35.14889240127853, -70.09541004896164))
                .add(new LatLng(-35.14863059576475, -70.09649567306042))
                .add(new LatLng(-35.14844280804501, -70.09731005877256))
                .add(new LatLng(-35.14814700723969, -70.09838595986366))
                .add(new LatLng(-35.148009935139584, -70.0991614535451))
                .add(new LatLng(-35.14773962828158, -70.10030072182417))
                .add(new LatLng(-35.147518666836184, -70.10121334344149))
                .add(new LatLng(-35.14706796942269, -70.10297019034624))
                .add(new LatLng(-35.146310496066825, -70.10618280619383))
                .add(new LatLng(-35.14606540545833, -70.10709576308727))
                .add(new LatLng(-35.14580989634239, -70.10804492980242))
                .add(new LatLng(-35.14552121394047, -70.10935217142105))
                .add(new LatLng(-35.14514534956355, -70.11088773608208))
                .add(new LatLng(-35.14484898846257, -70.11211786419153))
                .add(new LatLng(-35.14474316453322, -70.1124980673194))
                .add(new LatLng(-35.144677093046425, -70.11270828545094));
        TsNeptuno.width(6);

        final PolylineOptions TsMarte = new PolylineOptions()
                .add(new LatLng(-35.14385188033191, -70.11115562170744))
                .add(new LatLng(-35.14373371795943, -70.11122100055218))
                .add(new LatLng(-35.143340847399465, -70.11132996529341))
                .add(new LatLng(-35.14290164163942, -70.11144764721394))
                .add(new LatLng(-35.14262857618933, -70.1115445420146))
                .add(new LatLng(-35.14237607208141, -70.11160153895617))
                .add(new LatLng(-35.141428283658875, -70.11177722364664))
                .add(new LatLng(-35.14056931516501, -70.11226639151573))
                .add(new LatLng(-35.13895416665011, -70.11250745505095))
                .add(new LatLng(-35.138113541961225, -70.11280953884125))
                .add(new LatLng(-35.13667491851439, -70.11307943612337))
                .add(new LatLng(-35.13606321481208, -70.11325445026159))
                .add(new LatLng(-35.13505695085214, -70.1135266944766))
                .add(new LatLng(-35.13361827339326, -70.11394713073969))
                .add(new LatLng(-35.13235478268664, -70.11427201330662))
                .add(new LatLng(-35.131346827690834, -70.11453319340944))
                .add(new LatLng(-35.13119875925738, -70.11461399495602));
        TsMarte.width(6);

        final PolylineOptions TkIris = new PolylineOptions()
                .add(new LatLng(-35.130579885131255, -70.11145334690809))
                .add(new LatLng(-35.13044031561545, -70.11157538741827))
                .add(new LatLng(-35.13033118250716, -70.11178325861692))
                .add(new LatLng(-35.12982225834222, -70.11281255632639))
                .add(new LatLng(-35.12939696421099, -70.1135977730155))
                .add(new LatLng(-35.12895000538023, -70.11437393724918))
                .add(new LatLng(-35.12860505020048, -70.11492010205984));
        TkIris.width(6);

        PolylineOptions caris = new PolylineOptions()
                .add(new LatLng(-35.144242, -70.097025))
                .add(new LatLng(-35.144942, -70.091498))
                .add(new LatLng(-35.14468147953683, -70.09087979793549))
                .add(new LatLng(-35.144594297995546, -70.09044997394085))
                .add(new LatLng(-35.14452109832743, -70.09002987295389))
                .add(new LatLng(-35.144449817688475, -70.08958261460066))
                .add(new LatLng(-35.14438100439703, -70.0891725718975))
                .add(new LatLng(-35.14430698206637, -70.08880008012056))
                .add(new LatLng(-35.14423953943983, -70.08855532854795));
        caris.width(6);

        PolylineOptions talìa = new PolylineOptions()
                .add(new LatLng(-35.144942, -70.091498))
                .add(new LatLng(-35.145108, -70.090674))
                .add(new LatLng(-35.145152, -70.090122))
                .add(new LatLng(-35.145241, -70.089665))
                .add(new LatLng(-35.145284, -70.088714))
                .add(new LatLng(-35.145335, -70.087599))
                .add(new LatLng(-35.145224, -70.086794))
                .add(new LatLng(-35.145031, -70.085945))
                .add(new LatLng(-35.144907, -70.084836));
        talìa.width(6);

        PolylineOptions eros1 = new PolylineOptions()
                .add(new LatLng(-35.144907, -70.084836))
                .add(new LatLng(-35.146309, -70.084043));
        eros1.width(6);

        PolylineOptions VulcanoI = new PolylineOptions()
                .add(new LatLng(-35.129931666337875, -70.0992938876152))
                .add(new LatLng(-35.130071236725485, -70.09927678853273))
                .add(new LatLng(-35.13023767889855, -70.09910043329))
                .add(new LatLng(-35.13034187646142, -70.09891636669636))
                .add(new LatLng(-35.13045704203369, -70.09866792708635))
                .add(new LatLng(-35.13056069090948, -70.0984389334917))
                .add(new LatLng(-35.13068134022836, -70.09819149971008))
                .add(new LatLng(-35.13084339391855, -70.09794808924198))
                .add(new LatLng(-35.130957187772225, -70.09775832295418))
                .add(new LatLng(-35.13109538539515, -70.0975377112627))
                .add(new LatLng(-35.13124263141387, -70.09732279926538))
                .add(new LatLng(-35.131386312560956, -70.09712062776089))
                .add(new LatLng(-35.13149023445399, -70.09698282927275))
                .add(new LatLng(-35.13164817354143, -70.09678266942501))
                .add(new LatLng(-35.13180090254124, -70.09657848626375))
                .add(new LatLng(-35.13196076041551, -70.0963632389903))
                .add(new LatLng(-35.13215187653601, -70.09612385183573))
                .add(new LatLng(-35.132397009038215, -70.09584691375494))
                .add(new LatLng(-35.132531913858834, -70.09569972753525))
                .add(new LatLng(-35.13271068982147, -70.09552538394928))
                .add(new LatLng(-35.1328359972791, -70.09538557380438))
                .add(new LatLng(-35.1330967568189, -70.09512908756733))
                .add(new LatLng(-35.13330624127015, -70.09494569152594))
                .add(new LatLng(-35.13349735423263, -70.0947954878211))
                .add(new LatLng(-35.133655837812505, -70.09467478841543))
                .add(new LatLng(-35.13378525670082, -70.09459733963013))
                .add(new LatLng(-35.133903159322564, -70.09453397244215))
                .add(new LatLng(-35.1339983038719, -70.09451285004616))
                .add(new LatLng(-35.13411483522898, -70.09435091167688))
                .add(new LatLng(-35.13418557653615, -70.09417656809092))
                .add(new LatLng(-35.13423959214405, -70.09397070854902))
                .add(new LatLng(-35.13429141419104, -70.09372863918543))
                .add(new LatLng(-35.13437065524673, -70.09339068084955))
                .add(new LatLng(-35.1344353641485, -70.09311340749264))
                .add(new LatLng(-35.134516250203404, -70.09288977831602))
                .add(new LatLng(-35.13463799041928, -70.09264267981052))
                .add(new LatLng(-35.1347783753071, -70.09233187884092))
                .add(new LatLng(-35.13500979049225, -70.09196944534779))
                .add(new LatLng(-35.13509890161445, -70.09166166186333))
                .add(new LatLng(-35.135168545315885, -70.09135857224464))
                .add(new LatLng(-35.13527301075636, -70.09102061390877))
                .add(new LatLng(-35.13534210593466, -70.09076211601496))
                .add(new LatLng(-35.13544355509816, -70.09046070277691))
                .add(new LatLng(-35.135582019292976, -70.09020254015923))
                .add(new LatLng(-35.135744885787076, -70.08986458182335))
                .add(new LatLng(-35.135886639692664, -70.08942067623138))
                .add(new LatLng(-35.13621703226704, -70.08887350559235))
                .add(new LatLng(-35.136507941133296, -70.08842457085848))
                .add(new LatLng(-35.136837782822184, -70.08807554841042))
                .add(new LatLng(-35.13706562779698, -70.08774228394032))
                .add(new LatLng(-35.137315954934245, -70.0872715562582))
                .add(new LatLng(-35.13755531411672, -70.08685547858477))
                .add(new LatLng(-35.13782126793851, -70.08639849722385))
                .add(new LatLng(-35.13796932432881, -70.08598510175943))
                .add(new LatLng(-35.13804170735496, -70.08558042347431))
                .add(new LatLng(-35.138174683579976, -70.0851821154356))
                .add(new LatLng(-35.1383433026045, -70.08478548377752));
        VulcanoI.width(6);

        PolylineOptions Vulcanito = new PolylineOptions()
                .add(new LatLng(-35.13045704203369, -70.09866792708635))
                .add(new LatLng(-35.130610047470626, -70.09859215468168))
                .add(new LatLng(-35.13078965026009, -70.09850062429905))
                .add(new LatLng(-35.130943203475894, -70.09847313165665))
                .add(new LatLng(-35.13108578834559, -70.09843993932009))
                .add(new LatLng(-35.13121603392182, -70.09851034730673))
                .add(new LatLng(-35.1313558763086, -70.09858913719654))
                .add(new LatLng(-35.13152642885525, -70.09868938475847))
                .add(new LatLng(-35.13178116020907, -70.09871117770672))
                .add(new LatLng(-35.132035068171334, -70.09856667369604))
                .add(new LatLng(-35.13223824881059, -70.0982977822423))
                .add(new LatLng(-35.13244910645478, -70.09800307452679))
                .add(new LatLng(-35.132745238505194, -70.09751558303833))
                .add(new LatLng(-35.132942385078636, -70.09724367409945))
                .add(new LatLng(-35.13316695062177, -70.09689196944237))
                .add(new LatLng(-35.1334030316788, -70.09646147489548))
                .add(new LatLng(-35.133659402318536, -70.09606819599867))
                .add(new LatLng(-35.13381349850715, -70.09573325514793))
                .add(new LatLng(-35.13402352949746, -70.09537450969219))
                .add(new LatLng(-35.13426810798351, -70.09506706148386))
                .add(new LatLng(-35.13442384816127, -70.09482633322477))
                .add(new LatLng(-35.13459055562708, -70.09466506540775))
                .add(new LatLng(-35.134727924515914, -70.09448871016502))
                .add(new LatLng(-35.13480579420225, -70.0942275300622))
                .add(new LatLng(-35.1350078711747, -70.09387481957674))
                .add(new LatLng(-35.135187190076245, -70.09360559284687))
                .add(new LatLng(-35.135282607312405, -70.09332496672869))
                .add(new LatLng(-35.13545040976682, -70.09283110499382))
                .add(new LatLng(-35.13560642186924, -70.09211998432875))
                .add(new LatLng(-35.135693613037034, -70.09165160357952))
                .add(new LatLng(-35.1357662722722, -70.09116545319557))
                .add(new LatLng(-35.13594641205858, -70.09071417152882))
                .add(new LatLng(-35.136167679104595, -70.09024109691381))
                .add(new LatLng(-35.136424315222584, -70.08984178304672))
                .add(new LatLng(-35.13664366169078, -70.08943509310484))
                .add(new LatLng(-35.13678541403148, -70.08902035653591))
                .add(new LatLng(-35.137049999461674, -70.08857242763042))
                .add(new LatLng(-35.137366678302016, -70.08813489228487))
                .add(new LatLng(-35.137602198822606, -70.08780933916569))
                .add(new LatLng(-35.13781030078777, -70.08750222623348))
                .add(new LatLng(-35.137885425740706, -70.08708648383617))
                .add(new LatLng(-35.137897763773786, -70.08673578500748))
                .add(new LatLng(-35.13782126793851, -70.08639849722385));
        Vulcanito.width(6);


        PolylineOptions Minerva = new PolylineOptions()
                .add(new LatLng(-35.13863694563302, -70.09580198675394))
                .add(new LatLng(-35.13879706426114, -70.09581573307514))
                .add(new LatLng(-35.13901229171543, -70.09569335728884))
                .add(new LatLng(-35.13920229458752, -70.09553108364344))
                .add(new LatLng(-35.139413682543115, -70.09521525353193))
                .add(new LatLng(-35.1395825731741, -70.09495206177235))
                .add(new LatLng(-35.13972569125921, -70.09462516754866))
                .add(new LatLng(-35.13983700515144, -70.0943499058485))
                .add(new LatLng(-35.140002330675976, -70.09393651038408))
                .add(new LatLng(-35.14016957506065, -70.0935985520482))
                .add(new LatLng(-35.14036670365648, -70.09332697838545))
                .add(new LatLng(-35.14061674647373, -70.09305272251368))
                .add(new LatLng(-35.14092546055914, -70.09274192154408))
                .add(new LatLng(-35.14120703091097, -70.09243950247765))
                .add(new LatLng(-35.14152616107669, -70.09190306067467))
                .add(new LatLng(-35.1418107451691, -70.0914953649044))
                .add(new LatLng(-35.14209724741702, -70.09110778570175))
                .add(new LatLng(-35.14248080243623, -70.09058441966772))
                .add(new LatLng(-35.142816925649, -70.09006507694721))
                .add(new LatLng(-35.14307902402631, -70.08961714804173))
                .add(new LatLng(-35.143290401914534, -70.08921414613724))
                .add(new LatLng(-35.14341487060879, -70.0888916105032))
                .add(new LatLng(-35.14351384227607, -70.08852515369654))
                .add(new LatLng(-35.143556062839906, -70.08819188922644))
                .add(new LatLng(-35.14360294408966, -70.08783247321844))
                .add(new LatLng(-35.1436651782216, -70.08750658482313))
                .add(new LatLng(-35.14364735788031, -70.08728127926588))
                .add(new LatLng(-35.14367285467511, -70.08686687797308))
                .add(new LatLng(-35.143694787395425, -70.08653998374939))
                .add(new LatLng(-35.14369917393878, -70.08631065487862))
                .add(new LatLng(-35.14369040085183, -70.08609842509031))
                .add(new LatLng(-35.14366627485786, -70.08573465049267))
                .add(new LatLng(-35.14354235486703, -70.08552610874176))
                .add(new LatLng(-35.1434557204251, -70.08523341268301))
                .add(new LatLng(-35.14340280757185, -70.0849749147892))
                .add(new LatLng(-35.14329588512093, -70.08467350155115))
                .add(new LatLng(-35.14320020311627, -70.08441634476185))
                .add(new LatLng(-35.14310205355051, -70.08402541279793))
                .add(new LatLng(-35.14306476765092, -70.08373506367207))
                .add(new LatLng(-35.14300582299541, -70.08341185748577))
                .add(new LatLng(-35.14294605581276, -70.0832100212574))
                .add(new LatLng(-35.14287395130921, -70.08306819945574));
        Minerva.width(6);

        PolylineOptions MinervaB = new PolylineOptions()
                .add(new LatLng(-35.13863694563302, -70.09580198675394))
                .add(new LatLng(-35.13859088405153, -70.0957778468728))
                .add(new LatLng(-35.13857059501333, -70.0957191735506))
                .add(new LatLng(-35.138595270869935, -70.09558338671923))
                .add(new LatLng(-35.13860541538659, -70.09534902870655))
                .add(new LatLng(-35.13867533026435, -70.09520787745714))
                .add(new LatLng(-35.13875511540443, -70.09505297988653))
                .add(new LatLng(-35.1390339515169, -70.09468752890825))
                .add(new LatLng(-35.13935829950016, -70.09433783590794))
                .add(new LatLng(-35.1394929186133, -70.09420741349459))
                .add(new LatLng(-35.13968483957092, -70.09407330304384))
                .add(new LatLng(-35.1398287799922, -70.09399686008692))
                .add(new LatLng(-35.140002330675976, -70.09393651038408));
        MinervaB.width(6);

        PolylineOptions CarisMinerva = new PolylineOptions()
                .add(new LatLng(-35.143963736897696, -70.09691141545773))
                .add(new LatLng(-35.143852976965654, -70.0969124212861))
                .add(new LatLng(-35.14367340299318, -70.0968748703599))
                .add(new LatLng(-35.14353577503923, -70.09679038077593))
                .add(new LatLng(-35.143316995462115, -70.09679239243269))
                .add(new LatLng(-35.143152224991375, -70.09675584733486))
                .add(new LatLng(-35.142940572582795, -70.0966827571392))
                .add(new LatLng(-35.14269903593633, -70.09653825312853))
                .add(new LatLng(-35.14244379041524, -70.09643197059631))
                .add(new LatLng(-35.14223652278667, -70.09633339941502))
                .add(new LatLng(-35.14211260061906, -70.09618017822504))
                .add(new LatLng(-35.14200814395514, -70.09602930396795))
                .add(new LatLng(-35.141837065034906, -70.09589552879333))
                .add(new LatLng(-35.14151300109458, -70.09587038308382))
                .add(new LatLng(-35.14108338134379, -70.0959424674511))
                .add(new LatLng(-35.140910655469874, -70.0959213450551))
                .add(new LatLng(-35.14058658784133, -70.09603399783373))
                .add(new LatLng(-35.14019754048161, -70.0960836187005))
                .add(new LatLng(-35.139813426359396, -70.09617548435926))
                .add(new LatLng(-35.13954172141399, -70.09611513465643))
                .add(new LatLng(-35.13927961164663, -70.0960386916995))
                .add(new LatLng(-35.13906794916833, -70.09593442082405))
                .add(new LatLng(-35.13891413709985, -70.09591430425644))
                .add(new LatLng(-35.13879706426114, -70.09581573307514));
        CarisMinerva.width(6);


        PolylineOptions MinervaEros = new PolylineOptions()
                .add(new LatLng(-35.14209724741702, -70.09110778570175))
                .add(new LatLng(-35.14235797729442, -70.09100183844566))
                .add(new LatLng(-35.14260472404326, -70.09083285927773))
                .add(new LatLng(-35.142873128824014, -70.09074836969376))
                .add(new LatLng(-35.143105343482, -70.09062599390745))
                .add(new LatLng(-35.14334112155962, -70.09048014879227))
                .add(new LatLng(-35.14353988743167, -70.09023874998093))
                .add(new LatLng(-35.14374112024766, -70.08989777415991))
                .add(new LatLng(-35.14398155716972, -70.08942738175392))
                .add(new LatLng(-35.14414934169341, -70.08888490498066))
                .add(new LatLng(-35.14429437084776, -70.08836757391691))
                .add(new LatLng(-35.144410064997324, -70.08787203580141)) //
                .add(new LatLng(-35.144603345140965, -70.08733995258808))
                .add(new LatLng(-35.144737133113416, -70.08705832064152))
                .add(new LatLng(-35.14479635067035, -70.08670192211866))
                .add(new LatLng(-35.14476482280975, -70.08632741868496))
                .add(new LatLng(-35.144697380562604, -70.08612759411335))
                .add(new LatLng(-35.1446458392954, -70.08594587445259))
                .add(new LatLng(-35.144603345140965, -70.08565116673708))
                .add(new LatLng(-35.14461540799996, -70.08540742099285))
                .add(new LatLng(-35.14468449524886, -70.08516233414412))
                .add(new LatLng(-35.144907, -70.084836));
        MinervaEros.width(6);


        PolylineOptions ErosMinerva = new PolylineOptions()
                .add(new LatLng(-35.144907, -70.084836))
                .add(new LatLng(-35.14477058007216, -70.08484415709972))
                .add(new LatLng(-35.14463021241538, -70.08491355925798))
                .add(new LatLng(-35.144477507482556, -70.08492093533278))
                .add(new LatLng(-35.14435962017509, -70.08489042520523))
                .add(new LatLng(-35.14428258209818, -70.08475966751575))
                .add(new LatLng(-35.14422418663898, -70.08459974080324))
                .add(new LatLng(-35.14415838888829, -70.08445288985968))
                .add(new LatLng(-35.14408793040461, -70.08430067449808))
                .add(new LatLng(-35.143971139165004, -70.08420545607805))
                .add(new LatLng(-35.14385708934206, -70.0840974971652))
                .add(new LatLng(-35.143725767352805, -70.08400328457355))
                .add(new LatLng(-35.14364324549331, -70.08384570479393))
                .add(new LatLng(-35.14351959962697, -70.0837628915906))
                .add(new LatLng(-35.14340829077068, -70.0836331397295))
                .add(new LatLng(-35.143282451264604, -70.08351914584637))
                .add(new LatLng(-35.14314948338337, -70.08342660963535))
                .add(new LatLng(-35.14309794113595, -70.08324757218361))
                .add(new LatLng(-35.143040367310334, -70.08311111479998))
                .add(new LatLng(-35.142946878297224, -70.08304473012686));
        ErosMinerva.width(6);


        PolylineOptions ErosII = new PolylineOptions()
                .add(new LatLng(-35.146175065777086, -70.08293308317661))
                .add(new LatLng(-35.145998512591326, -70.08307658135891))
                .add(new LatLng(-35.14574382572159, -70.08324891328812))
                .add(new LatLng(-35.14549187958401, -70.08343432098627))
                .add(new LatLng(-35.14521800036255, -70.0836056470871))
                .add(new LatLng(-35.14497701878382, -70.08376389741898));
        ErosII.width(6);

        PolylineOptions ErosIIMinerva = new PolylineOptions()
                .add(new LatLng(-35.14497701878382, -70.08376389741898))
                .add(new LatLng(-35.144757420614624, -70.08385878056288))
                .add(new LatLng(-35.144660095393846, -70.0838490575552))
                .add(new LatLng(-35.144629938259556, -70.08376590907574))
                .add(new LatLng(-35.14459539461929, -70.08363548666239))
                .add(new LatLng(-35.1445611251204, -70.08349366486073))
                .add(new LatLng(-35.1445367252284, -70.08332971483469))
                .add(new LatLng(-35.14451232532908, -70.08318454027176))
                .add(new LatLng(-35.14445694575517, -70.08304741233587))
                .add(new LatLng(-35.144356056137546, -70.08299343287945))
                .add(new LatLng(-35.14420965630695, -70.0829253718257))
                .add(new LatLng(-35.14411315290383, -70.08282914757729))
                .add(new LatLng(-35.14398950775134, -70.08274164050817))
                .add(new LatLng(-35.14388450517948, -70.08265748620033))
                .add(new LatLng(-35.1437762125679, -70.08260417729616))
                .add(new LatLng(-35.143651196107996, -70.08258741348982))
                .add(new LatLng(-35.143502327573046, -70.08260484784842))
                .add(new LatLng(-35.143330155152555, -70.0826521217823))
                .add(new LatLng(-35.143152224991375, -70.0827382877469));
        ErosIIMinerva.width(6);


        PolylineOptions CarisII = new PolylineOptions()
                .add(new LatLng(-35.143073540805304, -70.09062130004168))
                .add(new LatLng(-35.143104246838185, -70.0909036025405))
                .add(new LatLng(-35.143161272297085, -70.09125094860792))
                .add(new LatLng(-35.14320513800752, -70.09164221584797))
                .add(new LatLng(-35.1432248775695, -70.09214144200087))
                .add(new LatLng(-35.14324955201525, -70.09289279580116))
                .add(new LatLng(-35.14330054584608, -70.09385269135237))
                .add(new LatLng(-35.14332439778824, -70.09498357772827))
                .add(new LatLng(-35.143350991324716, -70.09559445083141))
                .add(new LatLng(-35.1433803264528, -70.096151009202))
                .add(new LatLng(-35.14344886643051, -70.09678736329079));
        CarisII.width(6);


        PolylineOptions CarisIII = new PolylineOptions()
                .add(new LatLng(-35.14177428117431, -70.09152855724096))
                .add(new LatLng(-35.141790182768034, -70.09198721498251))
                .add(new LatLng(-35.141805810193325, -70.09269196540117))
                .add(new LatLng(-35.14183158173023, -70.09346008300781))
                .add(new LatLng(-35.14185625659835, -70.0941839441657))
                .add(new LatLng(-35.14188888224582, -70.09510863572359))
                .add(new LatLng(-35.141924249529566, -70.09562227874994))
                .add(new LatLng(-35.142009240613724, -70.09605880826712));
        CarisIII.width(6);


        PolylineOptions Pluton = new PolylineOptions()
                .add(new LatLng(-35.14451561520358, -70.09496916085482))
                .add(new LatLng(-35.14462006864955, -70.09479448199272))
                .add(new LatLng(-35.14474618024297, -70.09462617337704))
                .add(new LatLng(-35.14489970713641, -70.09446490556002))
                .add(new LatLng(-35.14504802480665, -70.09427648037672))
                .add(new LatLng(-35.145241851742796, -70.09405620396137))
                .add(new LatLng(-35.14548228423146, -70.09377758949995))
                .add(new LatLng(-35.145773708291166, -70.09336184710264))
                .add(new LatLng(-35.14599878674253, -70.09294878691435))
                .add(new LatLng(-35.14627567898957, -70.09258434176445))
                .add(new LatLng(-35.146515560125145, -70.09227555245161))
                .add(new LatLng(-35.14680094921802, -70.09206399321556))
                .add(new LatLng(-35.147055632779555, -70.09190171957016))
                .add(new LatLng(-35.14726508131149, -70.0915765017271))
                .add(new LatLng(-35.14740736344913, -70.09108934551477));
        Pluton.width(8);


        PolylineOptions MinervaIII = new PolylineOptions()
                .add(new LatLng(-35.138885074537335, -70.0948528200388))
                .add(new LatLng(-35.138877945982685, -70.09452056139708))
                .add(new LatLng(-35.138829416959396, -70.09430900216103))
                .add(new LatLng(-35.13874332585257, -70.09408839046955))
                .add(new LatLng(-35.138653944543414, -70.09393952786922))
                .add(new LatLng(-35.13861226978903, -70.093740709126))
                .add(new LatLng(-35.13858649723289, -70.09351640939713))
                .add(new LatLng(-35.13860185109711, -70.09327199310064))
                .add(new LatLng(-35.138610350556405, -70.09310971945524))
                .add(new LatLng(-35.13862104342331, -70.09284149855375))
                .add(new LatLng(-35.13862707529633, -70.09255986660719))
                .add(new LatLng(-35.13862296265568, -70.09229030460119))
                .add(new LatLng(-35.13862789782443, -70.09200096130371))
                .add(new LatLng(-35.138635300576986, -70.08955210447311))
                .add(new LatLng(-35.138544822443976, -70.08941598236561))
                .add(new LatLng(-35.138517404808056, -70.08922755718231))
                .add(new LatLng(-35.13851137292692, -70.08813891559839))
                .add(new LatLng(-35.138475729983774, -70.08737951517105))
                .add(new LatLng(-35.13844118373169, -70.08720383048058))
                .add(new LatLng(-35.13838250990488, -70.08707407861948))
                .add(new LatLng(-35.13829340237672, -70.08696544915438))
                .add(new LatLng(-35.13822759983178, -70.08688531816006));
        MinervaIII.width(6);


        PolylineOptions Selene = new PolylineOptions()
                .add(new LatLng(-35.13822759983178, -70.08688531816006))
                .add(new LatLng(-35.13815658785902, -70.0867361202836))
                .add(new LatLng(-35.138117106272105, -70.08652556687593))
                .add(new LatLng(-35.13812889591464, -70.08629623800516))
                .add(new LatLng(-35.1381357503572, -70.08601427078247))
                .add(new LatLng(-35.1381702967389, -70.08568100631237))
                .add(new LatLng(-35.138282161112386, -70.08508387953043))
                .add(new LatLng(-35.1383433026045, -70.08478548377752))
                .add(new LatLng(-35.138339738303536, -70.08466076105833))
                .add(new LatLng(-35.13832822286856, -70.0845430791378))
                .add(new LatLng(-35.138307111233544, -70.08437175303698))
                .add(new LatLng(-35.138264065415335, -70.08423931896687))
                .add(new LatLng(-35.138032111124126, -70.08372534066439))
                .add(new LatLng(-35.13786020131168, -70.08330523967743))
                .add(new LatLng(-35.137612891821924, -70.08271247148514))
                .add(new LatLng(-35.13673962558478, -70.08060861378908))
                .add(new LatLng(-35.136287771747696, -70.07955484092236));
        Selene.width(6);


        PolylineOptions VulcanoMinerva = new PolylineOptions()
                .add(new LatLng(-35.1381702967389, -70.08568100631237))
                .add(new LatLng(-35.138330416284866, -70.08548185229301))
                .add(new LatLng(-35.138584852175825, -70.08523106575012))
                .add(new LatLng(-35.13884860923195, -70.08496150374413))
                .add(new LatLng(-35.13916857116641, -70.08473418653011))
                .add(new LatLng(-35.13984879454491, -70.08435029536486))
                .add(new LatLng(-35.1409578124115, -70.08369248360395))
                .add(new LatLng(-35.142032544605165, -70.08305311203003));
        VulcanoMinerva.width(6);


        PolylineOptions VestaUrano = new PolylineOptions()
                .add(new LatLng(-35.13984879454491, -70.08435029536486))
                .add(new LatLng(-35.139951060607274, -70.08405961096287))
                .add(new LatLng(-35.140054149054855, -70.08371192961931))
                .add(new LatLng(-35.14006347087613, -70.08330624550581))
                .add(new LatLng(-35.139998218104814, -70.0831288844347))
                .add(new LatLng(-35.13978573497861, -70.08283283561468))
                .add(new LatLng(-35.13901640433642, -70.08187729865313))
                .add(new LatLng(-35.1380151120839, -70.08079536259174))
                .add(new LatLng(-35.137453319224676, -70.08011642843485))
                .add(new LatLng(-35.137163236279406, -70.07987469434738))
                .add(new LatLng(-35.13687616830184, -70.07977411150932))
                .add(new LatLng(-35.13662666236061, -70.0795990973711))
                .add(new LatLng(-35.136287771747696, -70.07955484092236));
        VestaUrano.width(6);

        PolylineOptions VulcanoMinervaII = new PolylineOptions()
                .add(new LatLng(-35.13468569937315, -70.09466305375099))
                .add(new LatLng(-35.13485597075647, -70.09463489055634))
                .add(new LatLng(-35.13502870947711, -70.09467277675867))
                .add(new LatLng(-35.135190754515236, -70.0947130098939))
                .add(new LatLng(-35.135453974194284, -70.09474854916334))
                .add(new LatLng(-35.13570211279997, -70.09477704763412))
                .add(new LatLng(-35.13599631372495, -70.09476765990257))
                .add(new LatLng(-35.13633164116219, -70.09476263076067))
                .add(new LatLng(-35.136688901826325, -70.0947941467166))
                .add(new LatLng(-35.137099626270434, -70.09485214948654))
                .add(new LatLng(-35.13747772124004, -70.09482197463512))
                .add(new LatLng(-35.13773627248169, -70.09486723691225))
                .add(new LatLng(-35.1380614481691, -70.09455643594265))
                .add(new LatLng(-35.138353172975606, -70.09403977543116))
                .add(new LatLng(-35.138573336775536, -70.09356167167425))
                .add(new LatLng(-35.13876992088573, -70.09344264864922))
                .add(new LatLng(-35.1389862451111, -70.0932864099741))
                .add(new LatLng(-35.13927193477879, -70.09315095841885))
                .add(new LatLng(-35.13957078374207, -70.09310971945524))
                .add(new LatLng(-35.139861680624186, -70.09302321821451))
                .add(new LatLng(-35.14020137887198, -70.09290721267462))
                .add(new LatLng(-35.1404146834233, -70.09288340806961))
                .add(new LatLng(-35.140603038005835, -70.09287972003222))
                .add(new LatLng(-35.14079495634617, -70.0928445160389));
        VulcanoMinervaII.width(6);

        PolylineOptions UranoII = new PolylineOptions()
                .add(new LatLng(-35.13508436965347, -70.08812248706818))
                .add(new LatLng(-35.134934937074235, -70.08814696222544))
                .add(new LatLng(-35.13477426247203, -70.08815165609121))
                .add(new LatLng(-35.134660748142146, -70.0879692658782))
                .add(new LatLng(-35.134622361617964, -70.08776675909758))
                .add(new LatLng(-35.13462126485987, -70.08752569556236))
                .add(new LatLng(-35.134589733058185, -70.0872903317213))
                .add(new LatLng(-35.134617700395935, -70.08686821907759))
                .add(new LatLng(-35.13457657195473, -70.08660268038511))
                .add(new LatLng(-35.13457657195473, -70.08576516062021))
                .add(new LatLng(-35.13470571519031, -70.08480794727802))
                .add(new LatLng(-35.134987855431824, -70.08324086666107))
                .add(new LatLng(-35.135181432136, -70.081900767982))
                .add(new LatLng(-35.13534813805063, -70.08067399263382))
                .add(new LatLng(-35.13543670042893, -70.08019454777241))
                .add(new LatLng(-35.13556200369126, -70.07990252226591))
                .add(new LatLng(-35.13568895187633, -70.07976975291967))
                .add(new LatLng(-35.13586305975644, -70.07967285811901))
                .add(new LatLng(-35.13600097486805, -70.07957395166159))
                .add(new LatLng(-35.13614080903691, -70.0794867798686))
                .add(new LatLng(-35.136287771747696, -70.07955484092236));
        UranoII.width(6);

        PolylineOptions UranoI = new PolylineOptions()
                .add(new LatLng(-35.13530399391896, -70.08782275021076))
                .add(new LatLng(-35.13550579546834, -70.0874187424779))
                .add(new LatLng(-35.136115858251834, -70.08628617972136))
                .add(new LatLng(-35.136124632155145, -70.08592240512371))
                .add(new LatLng(-35.13637770402197, -70.0835982710123))
                .add(new LatLng(-35.136442959695366, -70.08215457201004))
                .add(new LatLng(-35.13646516854707, -70.08061666041613))
                .add(new LatLng(-35.13646626528033, -70.08007451891899))
                .add(new LatLng(-35.136287771747696, -70.07955484092236));
        UranoI.width(6);

        PolylineOptions UranoVesta = new PolylineOptions()
                .add(new LatLng(-35.13508436965347, -70.08812248706818))
                .add(new LatLng(-35.13530399391896, -70.08782275021076))
                .add(new LatLng(-35.13566646862689, -70.08752334862947))
                .add(new LatLng(-35.136070891982506, -70.08730541914701))
                .add(new LatLng(-35.13666312866128, -70.08712202310562))
                .add(new LatLng(-35.13737051682586, -70.08686687797308))
                .add(new LatLng(-35.137814961827026, -70.08681926876307))
                .add(new LatLng(-35.13801045105611, -70.0868920236826))
                .add(new LatLng(-35.13813794377869, -70.08692622184753))
                .add(new LatLng(-35.13822759983178, -70.08688531816006));
        UranoVesta.width(6);

        PolylineOptions VestaMinerva = new PolylineOptions()
                .add(new LatLng(-35.13822759983178, -70.08688531816006))
                .add(new LatLng(-35.13833288387815, -70.08678909391165))
                .add(new LatLng(-35.138613366493374, -70.08667510002851))
                .add(new LatLng(-35.13882612685508, -70.08647494018078))
                .add(new LatLng(-35.13990472559762, -70.08561059832573))
                .add(new LatLng(-35.14098111670397, -70.08451022207737))
                .add(new LatLng(-35.14155769019181, -70.08392818272114))
                .add(new LatLng(-35.1417175289093, -70.08352015167475))
                .add(new LatLng(-35.141848305808324, -70.08320063352585))
                .add(new LatLng(-35.142032544605165, -70.08305311203003));
        VestaMinerva.width(6);

        PolylineOptions Apolo = new PolylineOptions() //agreegar on polylineClick
                .add(new LatLng(-35.12869444254154, -70.11512361466885))
                .add(new LatLng (-35.128645633238634,-70.11538278311491))
                .add(new LatLng (-35.12851867407219,-70.11570062488317))
                .add(new LatLng (-35.1282984830831,-70.11597756296396))
                .add(new LatLng (-35.12798560793887,-70.11621493846178))
                .add(new LatLng (-35.127425116945055,-70.11655692011118))
                .add(new LatLng (-35.126970195049,-70.11681441217661))
                .add(new LatLng (-35.12647989663409,-70.11693812906742))
                .add(new LatLng (-35.126120123212694,-70.11684358119965))
                .add(new LatLng (-35.125663000269704,-70.1168442517519))
                .add(new LatLng (-35.12505971471398,-70.11717114597559))
                .add(new LatLng (-35.12451154364556,-70.11770624667406))
                .add(new LatLng (-35.12395815860479,-70.11857394129038))
                .add(new LatLng (-35.12388795686409,-70.11946879327297))
                .add(new LatLng (-35.124206606466764,-70.12006424367428))
                .add(new LatLng (-35.125316934283745,-70.12063555419445))
                .add(new LatLng (-35.12646810530476,-70.12114349752665))
                .add(new LatLng (-35.12761542107117,-70.12165814638138))
                .add(new LatLng (-35.1283157583628,-70.12223683297634))
                .add(new LatLng (-35.12908327066251,-70.12197900563478))
                .add(new LatLng (-35.12974794856713,-70.1222301274538))
                .add(new LatLng (-35.1303566834727,-70.12307938188314))
                .add(new LatLng (-35.13065638777025,-70.12433666735888))
                .add(new LatLng(-35.131524783655536, -70.12528281658888))
                .add(new LatLng (-35.13305124049252,-70.1258309930563))
                .add(new LatLng (-35.134786600976625,-70.1253542304039))
                .add(new LatLng (-35.13579478757696,-70.12400642037392))
                .add(new LatLng (-35.13762139138449,-70.12268476188183))
                .add(new LatLng (-35.139532399532975,-70.12122496962547))
                .add(new LatLng (-35.140908462123086,-70.12058090418577))
                .add(new LatLng(-35.14224721517724, -70.12017354369164))
                .add(new LatLng(-35.14289643256812, -70.1191546395421))
                .add(new LatLng (-35.14289643256812,-70.11815182864666))
                .add(new LatLng(-35.14322926413817, -70.1160915568471))
                .add(new LatLng (-35.14346394521785,-70.11506896466017))
                .add(new LatLng (-35.14365667929009,-70.11409733444452))
                .add(new LatLng (-35.14382419032498,-70.11341907083988))
                .add(new LatLng (-35.144076141625106,-70.11261206120253))
                .add(new LatLng (-35.14428943602257,-70.11188853532076))
                .add(new LatLng (-35.14439388975881,-70.1113249361515));
        Apolo.color(Color.RED);
        Apolo.width(6);

        PolylineOptions NeptunoI = new PolylineOptions()//agreegar on polylineClick
                .add(new LatLng(-35.14439388975881, -70.1113249361515))
                .add(new LatLng(-35.144424595293586,-70.11030670255423))
                .add(new LatLng(-35.14444789859349,-70.10951142758131))
                .add(new LatLng(-35.144387035843216,-70.10870307683945))
                .add(new LatLng(-35.14437744036042,-70.10780286043882))
                .add(new LatLng(-35.14441609644137,-70.10703273117542))
                .add(new LatLng(-35.14470258951864,-70.10570503771305))
                .add(new LatLng(-35.14545240155488,-70.10434113442898))
                .add(new LatLng(-35.146044844132106,-70.10322399437428))
                .add(new LatLng(-35.14686235845971,-70.10114260017872))
                .add(new LatLng(-35.14731552433287, -70.10011062026024))
                .add(new LatLng(-35.14775964088906,-70.09869508445263))
                .add(new LatLng(-35.14808203509301,-70.09731877595186))
                .add(new LatLng(-35.1481272688715,-70.09657178074121))
                .add(new LatLng(-35.148094645724704,-70.09585294872522))
                .add(new LatLng(-35.14809190428319,-70.09498558938503))
                .add(new LatLng(-35.147918096703336,-70.09383492171764));
        NeptunoI.color(Color.RED);
        NeptunoI.width(6);

        PolylineOptions VenusI = new PolylineOptions()//agreegar on polylineClick
                .add(new LatLng(-35.147918096703336, -70.09383492171764))
                .add(new LatLng( - 35.14764532222939, -70.0919097661972))
                .add(new LatLng( - 35.147408460034974, -70.090521723032))
                .add(new LatLng( - 35.14713294237898, -70.0882988423109))
                .add(new LatLng( - 35.14699065976155, -70.08700635284185))
                .add(new LatLng( - 35.146884290320514, -70.0856065750122))
                .add(new LatLng( - 35.14677380851162, -70.08486527949572));
        VenusI.color(Color.RED);
        VenusI.width(6);


        PolylineOptions VenusII = new PolylineOptions()
                .add(new LatLng(-35.14808203509301,-70.09731877595186))
                .add(new LatLng(-35.14850586090435,-70.09688057005405))
                .add(new LatLng(-35.148854295606924, -70.09645510464907))
                .add(new LatLng(-35.14901028202021,-70.09601354598999))
                .add(new LatLng(-35.14912130907424,-70.09544726461172))
                .add(new LatLng(-35.14917421820965,-70.09503051638603))
                .add(new LatLng(-35.14927345701335,-70.09444043040276))
                .add(new LatLng(-35.14921013060839,-70.09408302605152))
                .add(new LatLng(-35.149106231335054,-70.09366996586323))
                .add(new LatLng(-35.148939005313096,-70.09272079914808))
                .add(new LatLng(-35.14875204073086, -70.09152419865131))
                .add(new LatLng(-35.14848968647987,-70.09006943553686))
                .add(new LatLng(-35.148202932590216,-70.08871257305145))
                .add(new LatLng(-35.14798827772663,-70.08744556456804))
                .add(new LatLng(-35.14781008360389,-70.0865587592125))
                .add(new LatLng(-35.14770179621562,-70.08590061217546))
                .add(new LatLng(-35.14756335263594,-70.08520055562258));
        VenusII.width(6);
        VenusII.color(Color.RED);

        PolylineOptions JupiterA = new PolylineOptions()
                .add(new LatLng(-35.12869444254154, -70.11512361466885))
                .add(new LatLng(-35.12913646703117, -70.11574756354094))
                .add(new LatLng(-35.1294391920964, -70.11607680469751))
                .add(new LatLng(-35.1298658570348, -70.11618744581938))
                .add(new LatLng(-35.13033419875082, -70.11645499616861))
                .add(new LatLng(-35.13078608562843, -70.11699344962835))
                .add(new LatLng(-35.13114364482727, -70.11774849146605))
                .add(new LatLng(-35.131364650724855, -70.11886194348335))
                .add(new LatLng(-35.13174167553042, -70.12016247957945));
        JupiterA.width(6);
        JupiterA.color(Color.RED);

        PolylineOptions JupiterB = new PolylineOptions()
                .add(new LatLng(- 35.13105919080229, -70.11450804769993))
                .add(new LatLng(- 35.13127498711415, -70.11508069932461))
                .add(new LatLng(- 35.131462814442315, -70.1156972721219))
                .add(new LatLng(-35.13135944091532, -70.11624544858932))
                .add(new LatLng(- 35.13138494155884, -70.1170477643609))
                .add(new LatLng(- 35.13218121569987, -70.1245354861021))
                .add(new LatLng(-35.13218121569987, -70.1245354861021))
                .add(new LatLng(-35.13252094599559, -70.12535288929939))
                .add(new LatLng(-35.13305124049252, -70.1258309930563));
        JupiterB.width(6);
        JupiterB.color(Color.RED);

        PolylineOptions anexo = new PolylineOptions()
                .add(new LatLng(-35.13105919080229,-70.11450804769993))
                .add(new LatLng(-35.131070981466884,-70.11424653232098))
                .add(new LatLng(-35.13108661094988,-70.11338654905558))
                .add(new LatLng(-35.13101175392517,-70.11269252747297))
                .add(new LatLng(-35.13098981778768,-70.11235322803259))
                .add(new LatLng(-35.13084832955888,-70.11206690222025))
                .add(new LatLng(-35.13071616397579,-70.11187445372343))
                .add(new LatLng(-35.13066900110168,-70.11165786534548))
                .add(new LatLng(-35.13066488805904,-70.11137455701828));
        anexo.width(6);
        anexo.color(Color.RED);


        final Polyline Ts_MinervaII = mapa.addPolyline(minerva2);
        final Polyline Ts_Caris = mapa.addPolyline(TScaris);
        final Polyline Tk_ErosI = mapa.addPolyline(TKeros1);
        final Polyline Tk_ErosII = mapa.addPolyline(TKeros2);
        final Polyline Tk_MinervaI = mapa.addPolyline(TKminerva1);
        final Polyline Tk_Urano = mapa.addPolyline(TKurano);
        final Polyline Ts_Vulcano = mapa.addPolyline(TSvulcano);
        final Polyline Tk_Iris = mapa.addPolyline(TkIris);
        final Polyline Ts_Marte = mapa.addPolyline(TsMarte);
        final Polyline Ts_Neptuno = mapa.addPolyline(TsNeptuno);
        final Polyline Ts_VenusI = mapa.addPolyline(TsVenusI);
        final Polyline Pcaris = mapa.addPolyline(caris);
        final Polyline Ptalìa = mapa.addPolyline(talìa);
        final Polyline Peros1 = mapa.addPolyline(eros1);
        final Polyline PVulcanoI = mapa.addPolyline(VulcanoI);
        final Polyline PVulcanito = mapa.addPolyline(Vulcanito);
        final Polyline PMinerva = mapa.addPolyline(Minerva);
        final Polyline PMinervaB = mapa.addPolyline(MinervaB);
        final Polyline PCarisMinerva = mapa.addPolyline(CarisMinerva);
        final Polyline PMinervaEros = mapa.addPolyline(MinervaEros);
        final Polyline PErosMinerva = mapa.addPolyline(ErosMinerva);
        final Polyline PErosII = mapa.addPolyline(ErosII);
        final Polyline PErosIIMinerva = mapa.addPolyline(ErosIIMinerva);
        final Polyline PCarisII = mapa.addPolyline(CarisII);
        final Polyline PCarisIII = mapa.addPolyline(CarisIII);
        final Polyline PPluton = mapa.addPolyline(Pluton);
        final Polyline PMinervaIII = mapa.addPolyline(MinervaIII);
        final Polyline PSelene = mapa.addPolyline(Selene);
        final Polyline PVulcanoMinerva = mapa.addPolyline(VulcanoMinerva);
        final Polyline PVestaUrano = mapa.addPolyline(VestaUrano);
        final Polyline PVulcanoMinervaII = mapa.addPolyline(VulcanoMinervaII);
        final Polyline PUranoII = mapa.addPolyline(UranoII);
        final Polyline PUranoI = mapa.addPolyline(UranoI);
        final Polyline PVestaMinerva = mapa.addPolyline(VestaMinerva);
        final Polyline PUranoVesta = mapa.addPolyline(UranoVesta);
        final Polyline PjupiterB = mapa.addPolyline(JupiterB);
        final Polyline PjupiterA = mapa.addPolyline(JupiterA);
        final Polyline PvenusII = mapa.addPolyline(VenusII);
        final Polyline PvenusI = mapa.addPolyline(VenusI);
        final Polyline PneptunoI = mapa.addPolyline(NeptunoI);
        final Polyline Papolo = mapa.addPolyline(Apolo);
        final Polyline Panexo = mapa.addPolyline(anexo);

        Ts_MinervaII.setClickable(true);
        Ts_Caris.setClickable(true);
        Tk_ErosI.setClickable(true);
        Tk_ErosII.setClickable(true);
        Tk_MinervaI.setClickable(true);
        Tk_Urano.setClickable(true);
        Ts_Vulcano.setClickable(true);
        Tk_Iris.setClickable(true);
        Ts_Marte.setClickable(true);
        Ts_Neptuno.setClickable(true);
        Ts_VenusI.setClickable(true);
        Pcaris.setClickable(true);
        Ptalìa.setClickable(true);
        Peros1.setClickable(true);
        PVulcanoI.setClickable(true);
        PVulcanito.setClickable(true);
        PMinerva.setClickable(true);
        PMinervaB.setClickable(true);
        PCarisMinerva.setClickable(true);
        PMinervaEros.setClickable(true);
        PErosMinerva.setClickable(true);
        PErosII.setClickable(true);
        PErosIIMinerva.setClickable(true);
        PCarisII.setClickable(true);
        PCarisIII.setClickable(true);
        PPluton.setClickable(true);
        PMinervaIII.setClickable(true);
        PSelene.setClickable(true);
        PVulcanoMinerva.setClickable(true);
        PVestaUrano.setClickable(true);
        PVulcanoMinervaII.setClickable(true);
        PUranoII.setClickable(true);
        PUranoI.setClickable(true);
        PVestaMinerva.setClickable(true);
        PUranoVesta.setClickable(true);
        PjupiterB.setClickable(true);
        PjupiterA.setClickable(true);
        PvenusII.setClickable(true);
        PvenusI.setClickable(true);
        PneptunoI.setClickable(true);
        Papolo.setClickable(true);
        Panexo.setClickable(true);

        Ts_MinervaII.setColor(Color.BLUE);
        Ts_Caris.setColor(Color.BLUE);
        Tk_ErosI.setColor(Color.BLUE);
        Tk_ErosII.setColor(Color.BLUE);
        Tk_MinervaI.setColor(Color.BLUE);
        Tk_Urano.setColor(Color.BLUE);
        Ts_Vulcano.setColor(Color.BLUE);
        Tk_Iris.setColor(Color.BLUE);
        Ts_Marte.setColor(Color.BLUE);
        Ts_Neptuno.setColor(Color.BLUE);
        Ts_VenusI.setColor(Color.BLUE);

        Pcaris.setColor(Color.RED);
        Ptalìa.setColor(Color.RED);
        Peros1.setColor(Color.RED);
        PVulcanoI.setColor(Color.RED);
        PVulcanito.setColor(Color.RED);
        PMinerva.setColor(Color.RED);
        PMinervaB.setColor(Color.RED);
        PCarisMinerva.setColor(Color.RED);
        PMinervaEros.setColor(Color.RED);
        PErosMinerva.setColor(Color.RED);
        PErosII.setColor(Color.RED);
        PErosIIMinerva.setColor(Color.RED);
        PCarisII.setColor(Color.RED);
        PCarisIII.setColor(Color.RED);
        PPluton.setColor(Color.RED);
        PMinervaIII.setColor(Color.RED);
        PSelene.setColor(Color.RED);
        PVulcanoMinerva.setColor(Color.RED);
        PVestaUrano.setColor(Color.RED);
        PVulcanoMinervaII.setColor(Color.RED);
        PUranoII.setColor(Color.RED);
        PUranoI.setColor(Color.RED);
        PVestaMinerva.setColor(Color.RED);
        PUranoVesta.setColor(Color.RED);


        mapa.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onPolylineClick(final Polyline polyline) {

                Log.i("Polilyne Clicked ==", " " + polyline.getId());

                final int color = polyline.getColor();

                polyline.setColor(Color.YELLOW);
                polyline.setWidth(12);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // acciones que se ejecutan tras los milisegundos
                        polyline.setColor(color);
                        polyline.setWidth(6);
                    }
                }, 1800);

                if (Objects.equals(polyline.getId(), Ts_MinervaII.getId())) {
                    Snackbar.make(fab, "Ts Minerva II", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Ts_Caris.getId())) {
                    Snackbar.make(fab, "Ts Caris", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Tk_ErosI.getId())) {
                    Snackbar.make(fab, "Tk Eros I", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Tk_ErosII.getId())) {
                    Snackbar.make(fab, "Tk Eros II", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Tk_MinervaI.getId())) {
                    Snackbar.make(fab, "Tk Minerva I", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Tk_Urano.getId())) {
                    Snackbar.make(fab, "Tk Urano", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Ts_Vulcano.getId())) {
                    Snackbar.make(fab, "Ts Vulcano", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Tk_Iris.getId())) {
                    Snackbar.make(fab, "Tk Iris", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Ts_Marte.getId())) {
                    Snackbar.make(fab, "Ts Marte", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Ts_Neptuno.getId())) {
                    Snackbar.make(fab, "Ts Neptuno", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Ts_VenusI.getId())) {
                    Snackbar.make(fab, "Ts Venus I", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), Pcaris.getId())) {
                    Snackbar.make(fab, "Caris           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), Ptalìa.getId())) {
                    Snackbar.make(fab, "Talìa           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), Peros1.getId())) {
                    Snackbar.make(fab, "Eros I           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVulcanoI.getId())) {
                    Snackbar.make(fab, "Vulcano I        ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVulcanito.getId())) {
                    Snackbar.make(fab, "Vulcanito       ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PMinerva.getId())) {
                    Snackbar.make(fab, "Minerva         ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PMinervaB.getId())) {
                    Snackbar.make(fab, "Minerva        ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PCarisMinerva.getId())) {
                    Snackbar.make(fab, "Caris - Minerva    ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PMinervaEros.getId())) {
                    Snackbar.make(fab, "Minerva - Eros     ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PErosMinerva.getId())) {
                    Snackbar.make(fab, "Eros - Minerva     ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PErosII.getId())) {
                    Snackbar.make(fab, "Eros II          ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PErosIIMinerva.getId())) {
                    Snackbar.make(fab, "Eros II - Minerva   ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PCarisII.getId())) {
                    Snackbar.make(fab, "Caris II         ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PCarisIII.getId())) {
                    Snackbar.make(fab, "Caris III        ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PPluton.getId())) {
                    Snackbar.make(fab, "Pluton          ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PMinervaIII.getId())) {
                    Snackbar.make(fab, "Minerva III      ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PSelene.getId())) {
                    Snackbar.make(fab, "Selene          ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVulcanoMinerva.getId())) {
                    Snackbar.make(fab, "Vulcano - Minerva  ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVestaUrano.getId())) {
                    Snackbar.make(fab, "Vesta - Urano      ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVulcanoMinervaII.getId())) {
                    Snackbar.make(fab, "Vulcano - MinervaII", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PUranoII.getId())) {
                    Snackbar.make(fab, "Urano II         ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PUranoI.getId())) {
                    Snackbar.make(fab, "Urano I          ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PVestaMinerva.getId())) {
                    Snackbar.make(fab, "Vesta - Minerva    ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PUranoVesta.getId())) {
                    Snackbar.make(fab, "Urano - Vesta      ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

                if (Objects.equals(polyline.getId(), Papolo.getId())) {
                    Snackbar.make(fab, "Apolo           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PneptunoI.getId())) {
                    Snackbar.make(fab, "Neptuno I           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PvenusI.getId())) {
                    Snackbar.make(fab, "Venus I           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PvenusII.getId())) {
                    Snackbar.make(fab, "Venus II           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                if (Objects.equals(polyline.getId(), PjupiterA.getId()) ||
                        Objects.equals(polyline.getId(), PjupiterB.getId())) {
                    Snackbar.make(fab, "Jupiter           ", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

            }
        });


    }

    public void ubicar() {
        CameraPosition camPos1 = new CameraPosition.Builder()
                .target(llubicacion)
                .zoom(15)
                .bearing(-50)
                .tilt(70)
                .build();

        CameraUpdate camUpd1 = CameraUpdateFactory.newCameraPosition(camPos1);
        mapa.animateCamera(camUpd1);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        /*Toast.makeText(
                MainActivity.this,
                "Marcador pulsado:\n" +
                        marker.getTitle(),
                Toast.LENGTH_SHORT).show();*/
        return false;
    }

    public void emergencia() {
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("app.alpify");

        if (LaunchIntent != null) {

            LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(LaunchIntent);
        } else {
            try {

                LaunchIntent = new Intent(Intent.ACTION_VIEW);
                LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LaunchIntent.setData(Uri.parse("market://details?id=" + "app.alpify"));
                startActivity(LaunchIntent);

            } catch (ActivityNotFoundException anfe) {

                LaunchIntent = new Intent(Intent.ACTION_VIEW);
                LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LaunchIntent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + "app.alpify"));
                startActivity(LaunchIntent);

            }
        }
    }

    public void localizar() {
        if (mapa.getMyLocation() != null) {
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 15));
        } else {
            Snackbar.make(divider, R.string.snackbar1, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (onB) {

                super.onBackPressed();

            } else {

                Toast.makeText(MainActivity.this, getString(R.string.toatB),  Toast.LENGTH_SHORT).show();

                onB = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onB = false;
                }
            }, 2000);}

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_M) {
            mapa.clear();
            mostrarMedios();
            ubicar();

        } else if (id == R.id.nav_L) {
            mapa.clear();
            mostrarLugares();
            ubicar();

        } else if (id == R.id.nav_P) {
            mapa.clear();
            mostrarPistas();
            ubicar();

        } else if (id == R.id.nav_PM) {
            mapa.clear();
            mostrarPyM();
            ubicar();

        } else if (id == R.id.nav_estado) {
            Intent intent = new Intent(this, EstadoActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_emergencias) {
            emergencia();

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_compartir) {

            String shareBody = getResources().getString(R.string.compartir_mensaje);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share us"));

        } else if (id == R.id.nav_horarios) {
            startActivity(new Intent(MainActivity.this,
                    HorariosActivity.class));

        } else if (id == R.id.nav_help) {
            dialogo();

        }else if (id == R.id.nav_pronostico){
            startActivity(new Intent(MainActivity.this,
                    PronosticoActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == fab) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            } else {
                // permission has been granted, continue as usual
                localizar();
            }

        }else if (v == fm1){mapa.clear();ubicar();mostrarPistas();fabMenu.close(true);}
        else if (v == fm2){mapa.clear();ubicar();mostrarMedios();fabMenu.close(true);}
        else if (v == fm3){mapa.clear();ubicar();mostrarLugares();fabMenu.close(true);}
        else if (v == fm4){mapa.clear();ubicar();mostrarPyM();fabMenu.close(true);}

    }

    public void dialogo() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_lugares);
        dialog.setTitle("Ayuda");

        TabHost tabs = (TabHost) dialog.findViewById(R.id.tabhost);

        tabs.setup();

        TabHost.TabSpec tabpage1 = tabs.newTabSpec("Pistas");
        tabpage1.setContent(R.id.tab1);
        tabpage1.setIndicator(getString(R.string.nav_pistas));

        TabHost.TabSpec tabpage2 = tabs.newTabSpec("Medios");
        tabpage2.setContent(R.id.tab2);
        tabpage2.setIndicator(getString(R.string.nav_medios));

        TabHost.TabSpec tabpage3 = tabs.newTabSpec("Lugares");
        tabpage3.setContent(R.id.tab3);
        tabpage3.setIndicator(getString(R.string.nav_lugares));

        tabs.addTab(tabpage1);
        tabs.addTab(tabpage2);
        tabs.addTab(tabpage3);

        tabs.setCurrentTab(info);


        dialog.show();
    }


}
