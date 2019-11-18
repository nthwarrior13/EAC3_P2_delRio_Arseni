package arsenidelrio.ioc.cat.eac3_p2_delrio_arseni;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public static final float INITIAL_ZOOM = 13;
    private GoogleMap mMap;
    private TextView VeureText;

    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView mLocationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        VeureText =  findViewById(R.id.t_coord);
        final ImageButton button = findViewById(R.id.b_coord);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getLocation();
                VeureText.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng zona = new LatLng(41.979293,  2.821628); //Requeriment 1 Zona determinada
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); // Requeriment 2 Mapa tipus terreny
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zona, INITIAL_ZOOM)); // Requeriment 3 nivell de zoom


        mMap.getUiSettings().setZoomControlsEnabled(true); //Requeriment 6 controls de zoom


        LatLng casa = new LatLng(41.859461,  2.731725); //Requeriment 8 ubicació lloc identificat
        mMap.addMarker(new MarkerOptions().position(casa).title("Casa").icon(BitmapDescriptorFactory.defaultMarker
                (BitmapDescriptorFactory.HUE_RED)));

        enableMyLocation(); //Requeriment 5 posició actual del dispositiu, 7 posició de l'usuari amb cercle blau i 9 botó per centrar en la ubicació del dispositiu
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @SuppressLint("StringFormatMatches")
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLastLocation = location;
                                VeureText.append(
                                        getString(R.string.location_text,
                                                mLastLocation.getLatitude(),
                                                mLastLocation.getLongitude()
                                                ));
                            } else {
                                //mLocationTextView.setText(R.string.no_location);
                            }
                        }
                    });
        }
    }
}





