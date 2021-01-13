package com.example.uploadimage.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uploadimage.R;
import com.example.uploadimage.modual.LocationProvider;
import com.example.uploadimage.sign.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatsPepole extends AppCompatActivity implements OnMapReadyCallback , View.OnClickListener {


    Marker mark ;
    MapView mapM ;
    LocationProvider geterLoc = null ;
    GoogleMap realMap = null ;
    final int REQUEST_PERMISSION = 100 ;
    Location locationHere ;
    FloatingActionButton febPlus , febmin ;
    int zoom = 13 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_pepole);
        InitView();
        mapM.onCreate(savedInstanceState);

        mapM.getMapAsync(this);
        MackPermation();
        febPlus.setOnClickListener(this);
        febmin.setOnClickListener(this);


    }
    protected  void  InitView()
    {
        geterLoc  = new LocationProvider(ChatsPepole.this);
        mapM = findViewById(R.id.my_map);
        febmin = findViewById(R.id.feb_min);
        febPlus = findViewById(R.id.feb_plus);

    }
    protected void MackPermation()
    {
        if (requestPermissionIsAllowed())
        {
            locationHere = geterLoc.getLocationCurrently(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationHere = location ;

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });


        }
        else
        {
            AskForPermission();

        }
    }
    protected boolean requestPermissionIsAllowed()
    {
        if (ContextCompat.
                checkSelfPermission(ChatsPepole.this ,
                        Manifest.permission.ACCESS_FINE_LOCATION ) ==
                PackageManager.PERMISSION_GRANTED)
            return true ;
        return false ;
    }
    protected void AskForPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ChatsPepole.this , Manifest.permission.ACCESS_FINE_LOCATION ))
        {
            MainActivity.BuildPopUpContext("WE USE YOUR LOCATION TO GET YOU YOUR ADVIRTESMENT", ChatsPepole.this);
        }
        else
        {
            ActivityCompat.requestPermissions(ChatsPepole.this ,
                    new  String[] {Manifest.permission.ACCESS_FINE_LOCATION ,
                            Manifest.permission.ACCESS_COARSE_LOCATION} ,
                    REQUEST_PERMISSION );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_PERMISSION :
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    locationHere = geterLoc.getLocationCurrently(new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            locationHere = location ;
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(this, "  THANKS FOR YOUR HELP  ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapM.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapM.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapM.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapM.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapM.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapM.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        realMap = googleMap ;
        addMarker();
    }


    protected void addMarker()
    {
        if (locationHere==null || realMap == null)
        {
            Toast.makeText(this, "SOMETHING GO WRONG", Toast.LENGTH_SHORT).show();
            return;
        }
        mark = realMap.addMarker(new MarkerOptions().
                position(new LatLng(locationHere.getLatitude() ,
                locationHere.getLongitude())).
                title("you are here"));
        realMap.animateCamera(CameraUpdateFactory.
                newLatLngZoom(new LatLng(locationHere.getLatitude() ,
                        locationHere.getLongitude()),
                13));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case  R.id.feb_min :
            {
                zoom = zoom-1 ;
                realMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationHere.getLatitude(), locationHere.getLongitude()),zoom));
                break;
            }
            case R.id.feb_plus :
            {
                zoom = zoom+1 ;
                realMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationHere.getLatitude() , locationHere.getLongitude()) , zoom));
                break;
            }
        }
    }
}
