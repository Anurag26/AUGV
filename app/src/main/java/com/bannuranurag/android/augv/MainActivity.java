package com.bannuranurag.android.augv;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bannuranurag.android.augv.SharingDataLocation.DataParsing;
import com.bannuranurag.android.augv.SharingDataLocation.JSONTASk;
import com.bannuranurag.android.augv.SharingDataLocation.routes;
import com.bannuranurag.android.augv.SharingDataLocation.waypoints;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener,PermissionsListener,MapboxMap.OnMapClickListener {
    private MapView mapView;
    // variables for adding location layer
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private Location originLocation;
    // variables for adding a marker
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;
    // variables for calculating and drawing a route
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    private Button button;
    private static DatabaseReference mDatabase;
    private static String mFinalRoute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        enableLocationComponent();
        originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
        mapboxMap.addOnMapClickListener(this);
        button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"Origin coord "+ originCoord+"Destinaion coord"+destinationCoord);
                DataParsing dataParsing= new DataParsing();
               String requestURL= dataParsing.buildURL(originCoord.getLatitude(),originCoord.getLongitude(),destinationCoord.getLatitude(),destinationCoord.getLongitude());
               Log.v(TAG,"url "+requestURL);
               JSONTASk jsontaSk= new JSONTASk();
               jsontaSk.execute(requestURL);
               NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(true)
                        .build();
// Call this method with Context from within an Activity
                NavigationLauncher.startNavigation(MainActivity.this, options);
            }
        });
    }

    @Override
    public void onMapClick(@NonNull LatLng point){
        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        destinationCoord = point;
        destinationMarker = mapboxMap.addMarker(new MarkerOptions()
                .position(destinationCoord)
        );
        destinationPosition = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
        originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
        getRoute(originPosition, destinationPosition);
        button.setEnabled(true);
        button.setBackgroundResource(R.color.mapboxBlue);
    }

    private void getRoute(Point origin, Point destination) {

        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

// Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent() {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
// Activate the MapboxMap LocationComponent to show user location
// Adding in LocationComponentOptions is also an optional parameter
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this);
            locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            originLocation = locationComponent.getLastKnownLocation();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Permissions missing", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent();
        } else {
            Toast.makeText(this,"Permissions not granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    public static void callNecessaryMethodsFromHere(String output){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        try {


            JSONObject obj = new JSONObject(output);

            waypoints wayPoints= new waypoints();       //These two lines
            routes mRoutes = new routes();                //  are important object declarations*/

            wayPoints.getJSON(obj);         //These two lines ensure
            mRoutes.getJSON(obj);           //ensure that the two classes have the JSOn response to work on

            String code= wayPoints.getCode(wayPoints);  //To ensure that the api call code is logged in JSON tree
            String uuid=wayPoints.getUUID(wayPoints);   //To ensure the UUID is logged in the JSON tree.
            mDatabase.child("json").child("code").setValue(code); //To ensure that data is sent to cloud
            mDatabase.child("json").child("uuid").setValue(uuid);

            Double totalDistance= mRoutes.getTotalRouteDistance(mRoutes);     //Getting total distance of the route
            mDatabase.child("json").child("routes").child("legs").child("distance").setValue(totalDistance);   //Uploading the total distance on the cloud

            int numberofLegs=mRoutes.getNumberOfLegs();
            int numberOfSteps=mRoutes.getNumberOfSteps(mRoutes);

            ArrayList<Double> maneBearings = new ArrayList<>();   //Get the Before After bearings of routes
            maneBearings=mRoutes.getManeuverBearings(mRoutes);

            ArrayList<Double> manLngLat= new ArrayList<>();     //Get Long and Lat of each maneuver step
            manLngLat=mRoutes.getCoordinatesForManeuver(mRoutes);

            ArrayList<String> instructions= new ArrayList<>();      //Get instruction for each step
            instructions= mRoutes.getEachStepInstruction(mRoutes);

            ArrayList<Double> distance = new ArrayList<>();
            distance=mRoutes.getIndividualStepDistance(mRoutes);


            for(int i=0;i<numberofLegs;i++){
                mDatabase.child("json").child("routes").child("legs").child("TotalSteps").setValue(numberOfSteps);
                mDatabase.child("json").child("routes").child("legs").child("ManeuverBearings").setValue(maneBearings);
                mDatabase.child("json").child("routes").child("legs").child("ManeuverLongLat").setValue(manLngLat);
                mDatabase.child("json").child("routes").child("legs").child("Instructions").setValue(instructions);
                mDatabase.child("json").child("routes").child("legs").child("IndividualStepDistance").setValue(distance);
            }




            ArrayList<Double> latitude= wayPoints.getWayPointLatitude(wayPoints);
            Log.v(TAG,"TOTALWaypoints"+latitude);



            ArrayList<Double> locations;
            locations= mRoutes.getCoordinatesForManeuver(mRoutes);
            Log.v(TAG,"Maneuver locations "+locations);         //[long,lat]
            Log.d("My App", obj.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + t + "\"");
        }
    }


}