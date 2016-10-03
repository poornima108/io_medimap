package com.example.mactavish.medimap;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final int REQUEST_LOCATION = 0;
    int REQUEST_ACCESS_FINE_LOCATION;
    LocationRequest mLocationRequest;
    Location currentLocation;
    GoogleApiClient client;
    double user_latitude;
    double user_longitude;
    boolean gps_enabled;
    Context context;
    LatLng user_coordinates;
    LatLng Shop1;
    LatLng Shop2;
    String value2,address2,name2;
    DatabaseReference databaseReference;
    String abc="paracetamol";
    String value,address,name;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();
        mLocationRequest=new LocationRequest();
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medimap-fbbb5.firebaseio.com/");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                value=dataSnapshot.child(abc).child("store1").child("value").getValue().toString();
                address=dataSnapshot.child(abc).child("store1").child("address").getValue().toString();
                name=dataSnapshot.child(abc).child("store1").child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                value2=dataSnapshot.child(abc).child("store2").child("value").getValue().toString();
                address2=dataSnapshot.child(abc).child("store2").child("address").getValue().toString();
                name2=dataSnapshot.child(abc).child("store2").child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(MapsActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            mMap.setMyLocationEnabled(true);
        } else {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(MapsActivity.this, "Location permission is needed", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }


    }
    @Override
    protected void onStart() {
        Log.d("FUNCTION","onStart");
        super.onStart();
        client.connect();
    }

    @Override
    protected void onStop() {
        Log.d("FUNCTION","onStop");
        client.disconnect();
        super.onStop();

    }


    @Override
    public void onConnected(Bundle bundle) {

        Log.d("FUNCTION","onConnected");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationRequest.setInterval(1000);
        currentLocation= LocationServices.FusedLocationApi.getLastLocation(client);


        if(currentLocation==null) {
            Toast.makeText(this, "Could not fetch current location", Toast.LENGTH_LONG).show();
            LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest,this);
        }
        //If the retrieved location is not null place a marker at that position
        else{

            user_latitude = currentLocation.getLatitude();
            user_longitude = currentLocation.getLongitude();



            if (currentLocation != null)
                Log.d("CURR LOCATION VAL", String.valueOf(currentLocation));
            user_coordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            Shop1=new LatLng(12.827763, 80.048680);
            Shop2=new LatLng(12.901904, 80.093735);


            mMap.addMarker(new MarkerOptions().position(user_coordinates).title("Marker at current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(user_coordinates));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_coordinates,10));

            mMap.addMarker(new MarkerOptions().position(Shop1).title("store1").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
            mMap.addMarker(new MarkerOptions().position(Shop2).title("store2").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
           mMap.setOnMarkerClickListener(this);

        }

        LocationServices.FusedLocationApi.requestLocationUpdates(client,mLocationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getTitle().equals("store1")) { // if marker source is clicked

            AlertDialog.Builder cry = new AlertDialog.Builder(this);
            cry.setMessage("Address:   "+address+System.getProperty("line.separator")+"Quantity:   "+value);
            cry.setTitle(name);
            AlertDialog alertDialog = cry.create();
            alertDialog.show();


        }
        else if (marker.getTitle().equals("store2"))
        {

            AlertDialog.Builder smile = new AlertDialog.Builder(this);
            smile.setMessage("Address:   "+address2+System.getProperty("line.separator")+"Quantity:   "+value2);
            smile.setTitle(name2);
            AlertDialog alertDialog = smile.create();
            alertDialog.show();

        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
