package com.example.makrandpawar.medimap;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import static com.example.makrandpawar.medimap.MainActivity.medicine;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final int REQUEST_LOCATION = 0;
    int REQUEST_ACCESS_FINE_LOCATION;
    LocationRequest mLocationRequest;
    Location currentLocation;
    GoogleApiClient client;
    static String medicinename="medicinename";
    double user_latitude,user_longitude;
    LatLng user_coordinates,Shop1, Shop2,Shop3,Shop4,Shop5,Shop6;
    DatabaseReference databaseReference;
    String value,address,name,image;
    final Context context =this;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        final String med = intent.getStringExtra(medicine);

        Button button = (Button) findViewById(R.id.allshops);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this,completelist.class);
                intent.putExtra(medicinename,med);
                startActivity(intent);
            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();
        mLocationRequest=new LocationRequest();
       // alert= (ImageView) findViewById(R.id.abc);
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medimap-fbbb5.firebaseio.com/"+med);




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
        Shop1=new LatLng(12.827763, 80.048680);
        Shop2=new LatLng(12.901904, 80.093735);
        Shop3=new LatLng(12.828850, 80.093735);
        Shop4=new LatLng(12.794870, 80.022326);
        Shop5=new LatLng(12.856988, 80.069576);
        Shop6=new LatLng(12.822542, 80.048650);

        mMap.addMarker(new MarkerOptions().position(Shop1).title("store1").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        mMap.addMarker(new MarkerOptions().position(Shop2).title("store2").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        mMap.addMarker(new MarkerOptions().position(Shop3).title("store3").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        mMap.addMarker(new MarkerOptions().position(Shop4).title("store4").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        mMap.addMarker(new MarkerOptions().position(Shop5).title("store5").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        mMap.addMarker(new MarkerOptions().position(Shop6).title("store6").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        mMap.setOnMarkerClickListener(this);


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



            //mMap.addMarker(new MarkerOptions().position(user_coordinates).title("Marker at current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(user_coordinates));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_coordinates,15));



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
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.cardrow);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    value=dataSnapshot.child("store1").child("value").getValue().toString();
                    address=dataSnapshot.child("store1").child("address").getValue().toString();
                    name=dataSnapshot.child("store1").child("name").getValue().toString();
                    image=dataSnapshot.child("store1").child("image").getValue().toString();


                    dialog.setTitle(name);
                    TextView shopname = (TextView) dialog.findViewById(R.id.shopnametxt);
                    shopname.setText(name);
                    TextView shopaddress = (TextView) dialog.findViewById(R.id.shopaddress);
                    shopaddress.setText(address);
                    TextView quantity = (TextView) dialog.findViewById(R.id.medicinequantity);
                    quantity.setText("Available Quantitiy: "+value);
                    ImageView shopimage = (ImageView) dialog.findViewById(R.id.shopimage);
                    Picasso.with(context).load(image).resize(640,640).into(shopimage);
                    dialog.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        else if (marker.getTitle().equals("store2"))
        {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.cardrow);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    value=dataSnapshot.child("store2").child("value").getValue().toString();
                    address=dataSnapshot.child("store2").child("address").getValue().toString();
                    name=dataSnapshot.child("store2").child("name").getValue().toString();
                    image=dataSnapshot.child("store2").child("image").getValue().toString();


                    dialog.setTitle(name);
                    TextView shopname = (TextView) dialog.findViewById(R.id.shopnametxt);
                    shopname.setText(name);
                    TextView shopaddress = (TextView) dialog.findViewById(R.id.shopaddress);
                    shopaddress.setText(address);
                    TextView quantity = (TextView) dialog.findViewById(R.id.medicinequantity);
                    quantity.setText("Available Quantitiy: "+value);
                    ImageView shopimage = (ImageView) dialog.findViewById(R.id.shopimage);
                    Picasso.with(context).load(image).resize(640,640).into(shopimage);
                    dialog.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else if (marker.getTitle().equals("store3"))
        {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.cardrow);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    value=dataSnapshot.child("store3").child("value").getValue().toString();
                    address=dataSnapshot.child("store3").child("address").getValue().toString();
                    name=dataSnapshot.child("store3").child("name").getValue().toString();
                    image=dataSnapshot.child("store3").child("image").getValue().toString();


                    dialog.setTitle(name);
                    TextView shopname = (TextView) dialog.findViewById(R.id.shopnametxt);
                    shopname.setText(name);
                    TextView shopaddress = (TextView) dialog.findViewById(R.id.shopaddress);
                    shopaddress.setText(address);
                    TextView quantity = (TextView) dialog.findViewById(R.id.medicinequantity);
                    quantity.setText("Available Quantitiy: "+value);
                    ImageView shopimage = (ImageView) dialog.findViewById(R.id.shopimage);
                    Picasso.with(context).load(image).resize(640,640).into(shopimage);
                    dialog.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else if (marker.getTitle().equals("store4"))
        {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.cardrow);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    value=dataSnapshot.child("store4").child("value").getValue().toString();
                    address=dataSnapshot.child("store4").child("address").getValue().toString();
                    name=dataSnapshot.child("store4").child("name").getValue().toString();
                    image=dataSnapshot.child("store4").child("image").getValue().toString();


                    dialog.setTitle(name);
                    TextView shopname = (TextView) dialog.findViewById(R.id.shopnametxt);
                    shopname.setText(name);
                    TextView shopaddress = (TextView) dialog.findViewById(R.id.shopaddress);
                    shopaddress.setText(address);
                    TextView quantity = (TextView) dialog.findViewById(R.id.medicinequantity);
                    quantity.setText("Available Quantitiy: "+value);
                    ImageView shopimage = (ImageView) dialog.findViewById(R.id.shopimage);
                    Picasso.with(context).load(image).resize(640,640).into(shopimage);
                    dialog.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else if (marker.getTitle().equals("store5"))
        {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.cardrow);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    value=dataSnapshot.child("store5").child("value").getValue().toString();
                    address=dataSnapshot.child("store5").child("address").getValue().toString();
                    name=dataSnapshot.child("store5").child("name").getValue().toString();
                    image=dataSnapshot.child("store5").child("image").getValue().toString();


                    dialog.setTitle(name);
                    TextView shopname = (TextView) dialog.findViewById(R.id.shopnametxt);
                    shopname.setText(name);
                    TextView shopaddress = (TextView) dialog.findViewById(R.id.shopaddress);
                    shopaddress.setText(address);
                    TextView quantity = (TextView) dialog.findViewById(R.id.medicinequantity);
                    quantity.setText("Available Quantitiy: "+value);
                    ImageView shopimage = (ImageView) dialog.findViewById(R.id.shopimage);
                    Picasso.with(context).load(image).resize(640,640).into(shopimage);
                    dialog.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else if (marker.getTitle().equals("store6"))
        {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.cardrow);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    value=dataSnapshot.child("store6").child("value").getValue().toString();
                    address=dataSnapshot.child("store6").child("address").getValue().toString();
                    name=dataSnapshot.child("store6").child("name").getValue().toString();
                    image=dataSnapshot.child("store6").child("image").getValue().toString();


                    dialog.setTitle(name);
                    TextView shopname = (TextView) dialog.findViewById(R.id.shopnametxt);
                    shopname.setText(name);
                    TextView shopaddress = (TextView) dialog.findViewById(R.id.shopaddress);
                    shopaddress.setText(address);
                    TextView quantity = (TextView) dialog.findViewById(R.id.medicinequantity);
                    quantity.setText("Available Quantitiy: "+value);
                    ImageView shopimage = (ImageView) dialog.findViewById(R.id.shopimage);
                    Picasso.with(context).load(image).resize(640,640).into(shopimage);
                    dialog.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

}
