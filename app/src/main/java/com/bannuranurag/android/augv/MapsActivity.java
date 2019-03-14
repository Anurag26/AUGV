package com.bannuranurag.android.augv;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bannuranurag.android.augv.googleMaps.GoogleMapsAsync;
import com.bannuranurag.android.augv.googleMaps.GoogleRoute;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    GeoDataClient geoDataClient;
    PlaceDetectionClient placeDetectionClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Location mLastKnownLocation;
    private static final int DEFAULT_ZOOM = 14;
    private LatLng onMapClickLatLng;
    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    ImageButton locateMe, startNav, stopNav;
    private static DatabaseReference databaseReference;
    private static final String TAG = "MapsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//
        geoDataClient = Places.getGeoDataClient(this, null);
        placeDetectionClient = Places.getPlaceDetectionClient(this, null);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toast.makeText(this,"Drop a pin to start navigation",Toast.LENGTH_SHORT).show();
        startNav=this.findViewById(R.id.start_nav);
        startNav.setEnabled(false);
        startNav.setVisibility(View.GONE);
        stopNav= this.findViewById(R.id.stop_nav);
        stopNav.setVisibility(View.GONE);
        stopNav.setEnabled(false);
        locateMe=this.findViewById(R.id.get_current_location);

        locateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation(mMap);
            }
        });

//        // Initialize Places.
//        Places
//
//        // Create a new Places client instance.
//        PlacesClient placesClient = Places.createClient(this);

        placeAutocompleteFragment=(PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        placeAutocompleteFragment.setHint("Or search destination for rover.");

        placeAutocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeAutocompleteFragment.setText("");

            }
        });
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {     //This is when the User searches for a place
                startNav.setVisibility(View.VISIBLE);
                mMap.clear();
                startNav.setEnabled(true);
                mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())).title(String.valueOf(mLastKnownLocation.getLatitude())+","+String.valueOf(mLastKnownLocation.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.addCircle(new CircleOptions().center(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())).radius(5).strokeColor(0xFFFFFFFF).fillColor(0xFF3367D6));
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(place.getLatLng().latitude,
                                place.getLatLng().longitude), DEFAULT_ZOOM));
                LatLng pl = place.getLatLng();
                onMapClickLatLng=pl;
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId()+"Latitude and Longitude"+place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Error"+status);
            }
        });


        startNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Start navigation
                String requestURl=createGoogleApiURL(String.valueOf(mLastKnownLocation.getLatitude()),String.valueOf(mLastKnownLocation.getLongitude()),String.valueOf(onMapClickLatLng.latitude),String.valueOf(onMapClickLatLng.longitude));
                                    Log.v(TAG,"GoogleMapsURL: "+requestURl);
                                    GoogleMapsAsync googleMapsAsync= new GoogleMapsAsync();
                                    googleMapsAsync.execute(requestURl);

                                    Log.v(TAG,"Destination is "+onMapClickLatLng);

                //start rover script
                String url = "https://meniscoid-bandicoot-4851.dataplicity.io/start/1/";
                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                placeAutocompleteFragment.getView().setVisibility(View.GONE);
                                startNav.setEnabled(false);
                                startNav.setVisibility(View.GONE);
                                stopNav.setVisibility(View.VISIBLE);
                                stopNav.setEnabled(true);
                                Log.v(TAG,"Volley Resp is"+response.substring(0,5));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(TAG,"Volley Error"+error);
                        Toast.makeText(MapsActivity.this,"Remote server not connected",Toast.LENGTH_SHORT).show();
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);
                //JSONObjectRequest
            }
        });



        stopNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://meniscoid-bandicoot-4851.dataplicity.io/start/0/";
                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.v(TAG,"Volley Resp is"+response.substring(0,5));
                                startNav.setVisibility(View.VISIBLE);
                                placeAutocompleteFragment.getView().setVisibility(View.VISIBLE);
                                startNav.setEnabled(true);
                                stopNav.setVisibility(View.GONE);
                                stopNav.setEnabled(false);
                                mMap.clear();
                                mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())).title(String.valueOf(mLastKnownLocation.getLatitude())+","+String.valueOf(mLastKnownLocation.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                mMap.addCircle(new CircleOptions().center(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())).radius(5).strokeColor(0xFFFFFFFF).fillColor(0xFF3367D6));
                                mMap.addMarker(new MarkerOptions().position(onMapClickLatLng));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(TAG,"Volley Error"+error);
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);
                //JSONObjectRequest

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getDeviceLocation(mMap);
    }


    private void getDeviceLocation(GoogleMap map){

        try{
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                Task locationResult=fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.v(TAG,"Location");
                            mLastKnownLocation =(Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            Log.v(TAG,"Location is Known");
                            map.addCircle(new CircleOptions().center(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())).radius(5).strokeColor(0xFFFFFFFF).fillColor(0xFF3367D6));
                            mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())).title(String.valueOf(mLastKnownLocation.getLatitude())+","+String.valueOf(mLastKnownLocation.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {     //This is for a pin dropped on a map
                                    onMapClickLatLng=latLng;
                                    //PolylineOptions options = displayDirections();
                                    map.clear();
                                    map.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())).title(String.valueOf(mLastKnownLocation.getLatitude())+","+String.valueOf(mLastKnownLocation.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                    map.addCircle(new CircleOptions().center(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())).radius(5).strokeColor(0xFFFFFFFF).fillColor(0xFF3367D6));
                                    map.addMarker(new MarkerOptions().position(latLng));
                                    startNav.setVisibility(View.VISIBLE);
                                    startNav.setEnabled(true);
                                    stopNav.setEnabled(true);

//                                    String requestURl=createGoogleApiURL(String.valueOf(mLastKnownLocation.getLatitude()),String.valueOf(mLastKnownLocation.getLongitude()),String.valueOf(latLng.latitude),String.valueOf(latLng.longitude));
//                                    Log.v(TAG,"GoogleMapsURL: "+requestURl);
//                                    GoogleMapsAsync googleMapsAsync= new GoogleMapsAsync();
//
//                                    googleMapsAsync.execute(requestURl);
                                }
                            });

                        }
                        else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        }catch (Exception e){
            Log.e(TAG,"Get DeviceLocation: "+e.getMessage());
        }
    }

    public static void getJsonResponse(String response){
        databaseReference= FirebaseDatabase.getInstance().getReference();
        try {
            JSONObject jsonObject= new JSONObject(response);
            GoogleRoute googleRoute = new GoogleRoute();
            googleRoute.initiateJSON(jsonObject);
            ArrayList<Double> getlatLong = googleRoute.getLatitudeLongitude();
            ArrayList<Double> individualStepDistance=googleRoute.getIndividualStepDistance();
            ArrayList<Double> bearings =googleRoute.getBearings();
            Double totalDistance= googleRoute.getTotalDistance();
            ArrayList<String> polylines = googleRoute.getOverViewPolylines();
            String mainPolyline = googleRoute.getPolyline();
            String status_code =googleRoute.getStatusCode();
            int total_steps = googleRoute.getTotalNumberOfSteps();
            int numberofLegs=googleRoute.getNumberOfLegs();
            ArrayList<String> instructions = googleRoute.getInstructions();
            displayDirections(polylines);


            databaseReference.child("json").child("code").setValue(status_code); //To ensure that data is sent to cloud
            databaseReference.child("json").child("routes").child("legs").child("distance").setValue(totalDistance);   //Uploading the total distance on the cloud
            databaseReference.child("json").child("polyline").setValue(mainPolyline);

            for(int i=0;i<numberofLegs;i++){
                databaseReference.child("json").child("routes").child("legs").child("TotalSteps").setValue(total_steps);
                databaseReference.child("json").child("routes").child("legs").child("ManeuverBearings").setValue(bearings);
                databaseReference.child("json").child("routes").child("legs").child("ManeuverLongLat").setValue(getlatLong);
                databaseReference.child("json").child("routes").child("legs").child("Instructions").setValue(instructions);
                databaseReference.child("json").child("routes").child("legs").child("polylines").setValue(polylines);
                databaseReference.child("json").child("routes").child("legs").child("IndividualStepDistance").setValue(individualStepDistance);
            }

        }catch (Exception e){
            Log.v(TAG,"getJsonResponse:"+e);
        }

    }


    private String createGoogleApiURL(String originLat,String originLong,String destLat,String destLong){
           StringBuilder googleDirectionURL= new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
           googleDirectionURL.append("libraries=geometry");
           googleDirectionURL.append("&origin="+originLat+","+originLong);
           googleDirectionURL.append("&destination="+destLat+","+destLong);
           googleDirectionURL.append("&mode=driving");
           googleDirectionURL.append("&key=AIzaSyBKYGPy1FmXcYF29Gu_V5zwsPijGSwR9Rw");
           LatLng destLatLng= new LatLng(Double.valueOf(destLat),Double.valueOf(destLong));
           LatLng srcLatLng= new LatLng(Double.valueOf(originLat),Double.valueOf(originLong));
           getBearings(destLatLng,srcLatLng);
           return String.valueOf(googleDirectionURL);
    }


    private void getBearings(LatLng dest, LatLng src){
        double bearings= SphericalUtil.computeHeading(dest,src);
       // Log.v(TAG,"Bearings are:"+bearings);
    }


    private static void displayDirections(ArrayList<String> options){
        databaseReference= FirebaseDatabase.getInstance().getReference();
        String  polyLineArray[] =  options.toArray(new String[options.size()]);

        try{
            for(int i=0;i<options.size();i++){
                PolylineOptions options1 = new PolylineOptions();
                options1.addAll(PolyUtil.decode(polyLineArray[i]));
                options1.color(Color.RED);
                options1.width(12);
                mMap.addPolyline(options1);
            }
            //int total_polylines=databaseReference.child("json").child("routes").child("legs").child("polylines").size
        }
        catch (Exception e){
            Log.v(TAG,"DisplayDirections"+e);
        }
    }


}
