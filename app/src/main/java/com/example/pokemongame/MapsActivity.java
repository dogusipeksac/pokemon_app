package com.example.pokemongame;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        CheckUserPermsions();
        LoadPokemon();
    }

    //access to permsions
    void CheckUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(
                    this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }

        runListener();// init the contact list

    }
    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    runListener();// init the contact list
                } else {
                    // Permission Denied
                    Toast.makeText( this,"cannot access it denail" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void  runListener(){
        MyLocationListener locationListener=new MyLocationListener(this);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        lm.requestLocationUpdates
                (LocationManager.GPS_PROVIDER, 3,
                        10, locationListener);
        MyThread myThread=new MyThread();
        myThread.start();
    }
    Location oldLocation;
    double myPower=0;
    class MyThread extends Thread{
        MyThread(){
            oldLocation= new Location("Start");
            oldLocation.setLatitude(0);
            oldLocation.setLongitude(0);
        }
        public void run(){
            while (true) try {
                Thread.sleep(1000);
                if (oldLocation.distanceTo(MyLocationListener.location) == 0) {
                    continue;
                }
                oldLocation = MyLocationListener.location;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.clear();
                        // Add a marker in Sydney and move the camera
                        LatLng sydney = new LatLng(MyLocationListener.location.getLatitude(), MyLocationListener.location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(sydney).title("Me").icon(
                                BitmapDescriptorFactory.fromResource(R.drawable.mario)
                        ));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 5));

                        for (int i = 0; i < pokemonArrayList.size(); i++) {
                            Pokemon pokemon = pokemonArrayList.get(i);

                            if (pokemon.isCatch == false) {
                                LatLng pockemonlocation =
                                        new LatLng(pokemon.location.getLatitude(), pokemon.location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(pockemonlocation)
                                        .title(pokemon.name)
                                        .snippet(pokemon.des + ",power:" + pokemon.power)
                                        .icon(BitmapDescriptorFactory.fromResource(pokemon.image)
                                        ));

                                // catch the pockemn
                                if (MyLocationListener.location.distanceTo(pokemon.location) < 2) {
                                    myPower = myPower + pokemon.power;
                                    Toast.makeText(MapsActivity.this, "Catch Pockemon, new power is" + myPower,
                                            Toast.LENGTH_SHORT).show();
                                    pokemon.isCatch = true;
                                    pokemonArrayList.set(i, pokemon);

                                }
                            }


                        }

                    }
                });


            } catch (Exception ex) {
            }
        }
    }
    //add listof pokemon
    ArrayList<Pokemon> pokemonArrayList=new ArrayList<>();
    void LoadPokemon(){
       pokemonArrayList.add(new Pokemon(R.drawable.charmander,"Charmander",
               "lives is adana",100,65.6950,-132.7394));
        pokemonArrayList.add(new Pokemon(R.drawable.bulbasaur,"Bulbasaur",
                "lives is karşiyaka",200,65.6955,-132.7390));
        pokemonArrayList.add(new Pokemon(R.drawable.squirtle,"Squirtle",
                "lives is karataş",500,65.6951,-132.7389));

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

    }
}

