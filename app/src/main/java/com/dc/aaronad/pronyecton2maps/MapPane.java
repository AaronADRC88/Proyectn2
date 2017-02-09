package com.dc.aaronad.pronyecton2maps;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPane extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final int LOCATION_REQUEST_CODE = 1;
    private GoogleMap gMap;
    private FirstMapFragment mFirstMapFragment;
    private GoogleApiClient apiClient;
    Location mLastLocation;
    LatLng pizza = new LatLng(42.237007, -8.712806);
    LatLng smoke = new LatLng(42.237733, -8.715345);
    LatLng burger = new LatLng(42.237453, -8.716391);
    Marker marca, marcaFu, marcaBu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_pane);

        mFirstMapFragment = FirstMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mFirstMapFragment)
                .commit();
        mFirstMapFragment.getMapAsync(this);


        // Establecer punto de entrada para la API de ubicación
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        int radio = 20;
        CircleOptions circulo = new CircleOptions().center(pizza).radius(radio)
                .strokeColor(Color.parseColor("#0D47A1")).strokeWidth(4)
                .fillColor(Color.argb(32, 33, 150, 243));
        Circle zona = gMap.addCircle(circulo);

        CircleOptions circle = new CircleOptions().center(smoke).radius(radio)
                .strokeColor(Color.parseColor("#0D47A1")).strokeWidth(4)
                .fillColor(Color.argb(32, 33, 150, 243));
        Circle zona2 = gMap.addCircle(circle);

        CircleOptions circl = new CircleOptions().center(burger).radius(radio)
                .strokeColor(Color.parseColor("#0D47A1")).strokeWidth(4)
                .fillColor(Color.argb(32, 33, 150, 243));
        Circle zona3 = gMap.addCircle(circl);


        marca = gMap.addMarker(new MarkerOptions()
                .position(pizza)
                .title("PIZZAAAAAAA!!!"));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(pizza)
                .zoom(20)
                .build();

        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        marcaFu = gMap.addMarker(new MarkerOptions()
                .position(smoke)
                .title("fuegoooo!!!"));
        marcaBu = gMap.addMarker(new MarkerOptions()
                .position(burger)
                .title("Hamburguesaaaaa!!!"));
        marca.setVisible(false);
        marcaFu.setVisible(false);
        marcaBu.setVisible(false);
        // Controles UI
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }

        gMap.getUiSettings().setZoomControlsEnabled(true);

        // Marcadores
        // gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                gMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        apiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        if (mLastLocation != null) {
            mLastLocation.getLatitude();
            mLastLocation.getLongitude();
            updateLocationUI();

        } else {
            Toast.makeText(this, "Ubicación no encontrada", Toast.LENGTH_LONG).show();
        }
    }

    public static double distanciaCoord(double lat1, double lng1, double lat2, double lng2) {
        //double radioTierra = 3958.75;//en millas
        double radioTierra = 6371000;//en metros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2;

        return distancia;
    }

    private void updateLocationUI() {
        TextView tv = (TextView) findViewById(R.id.tv);
        if (distanciaCoord(pizza.latitude, pizza.longitude, mLastLocation.getLatitude(), mLastLocation.getLongitude()) <= 20) {
            tv.setText(String.valueOf(
                    distanciaCoord(pizza.latitude, pizza.longitude, mLastLocation.getLatitude(), mLastLocation.getLongitude()) + " m de distancia"
            ));
            marca.setVisible(true);
        }
        if (distanciaCoord(smoke.latitude, smoke.longitude, mLastLocation.getLatitude(), mLastLocation.getLongitude()) <= 20) {
            tv.setText(String.valueOf(
                    distanciaCoord(pizza.latitude, pizza.longitude, mLastLocation.getLatitude(), mLastLocation.getLongitude()) + " m de distancia"
            ));
            marcaFu.setVisible(true);
        }
        if (distanciaCoord(burger.latitude, burger.longitude, mLastLocation.getLatitude(), mLastLocation.getLongitude()) <= 20) {
            tv.setText(String.valueOf(
                    distanciaCoord(pizza.latitude, pizza.longitude, mLastLocation.getLatitude(), mLastLocation.getLongitude()) + " m de distancia"
            ));
            marcaBu.setVisible(true);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        updateLocationUI();
    }
}

