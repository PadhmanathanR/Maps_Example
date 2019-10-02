package com.example.sys.maps_example;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;

import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{

    private static final int MY_REQUEST_INT =177 ;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
  // private Marker currentUserMarker;
    ArrayList<LatLng> latlngarray=new ArrayList<>();
    MarkerOptions markerOptions = new MarkerOptions();
    LocationManager locationManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {

              if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                 {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},MY_REQUEST_INT);


                 }


        }
        else {


            {


                final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                if (manager != null) {
                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();


                    }

                    else {

                        buildGoogeApiClient();
                        mMap.setMyLocationEnabled(true);

                    }
                }
            }
        }




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode )
        {
            case MY_REQUEST_INT:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED)
                    {


                        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                        if (manager != null) {
                            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                                buildAlertMessageNoGps();
                            }

                                else {

                                buildGoogeApiClient();
                                mMap.setMyLocationEnabled(true);

                                }
                        }
                    }


                }
                else
                {
                    Toast.makeText(this,"Unable to open",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
        }
    }


    protected synchronized void buildGoogeApiClient()
    {
        googleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        googleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
     //   if(currentUserMarker!=null) {
       //     currentUserMarker.remove();
        //}
     //   LatLng latLng=new LatLng(location.getLatitude(), location.getLongitude());


        latlngarray.add((new LatLng(location.getLatitude(),location.getLongitude())));









          //for(LatLng latLng1 : latlngarray) {
            //  markerOptions.position(latLng1);
              //markerOptions.title("Your Current Location");
             // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

              Polyline route = mMap.addPolyline(new PolylineOptions()
                      .width(5)
                      .color(Color.BLUE)
                      .geodesic(false)
                      .zIndex(3));
              route.setPoints(latlngarray);
             // mMap.addMarker(markerOptions);
          //}

           // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //mMap.animateCamera(CameraUpdateFactory.zoomBy(18));


          //  if (googleApiClient != null) {
            //    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);


            // }
    }






    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
    locationRequest.setInterval(1000).setFastestInterval(1000);
    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Unable to open",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Unable to open",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }
    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();

                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);

                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

}
