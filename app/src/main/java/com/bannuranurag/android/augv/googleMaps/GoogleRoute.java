package com.bannuranurag.android.augv.googleMaps;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class GoogleRoute {
    JSONObject object;
    Set<Double> latlng;

    private static final String TAG = "GoogleRoute";

    public void initiateJSON(JSONObject jsonObject){object=jsonObject;}  //Initiate the JSON for further use

    public String getStatusCode(){
        String getStatus="";
        try {
             getStatus=object.getString("status");
        } catch (JSONException e) {
            Log.v(TAG,"GETSTATUS: "+e);
        }
        return getStatus;
    }

    public double getTotalDistance(){
        Double totalDistance=0.000;
        JSONArray routes = null;
        try {
            routes = object.getJSONArray("routes");
            Log.v(TAG,"Total Routes length"+routes.length());
            JSONObject route = routes.getJSONObject(0);
            JSONArray legs = route.getJSONArray("legs");
            int totalLegs=legs.length();
            for(int i=0;i<totalLegs;i++){
                JSONObject leg= legs.getJSONObject(i);
                JSONObject distance = leg.getJSONObject("distance");
                totalDistance= Double.parseDouble(distance.getString("value"));
                Log.v(TAG,"Total Dist"+totalDistance);
            }
        } catch (JSONException e) {
            Log.v(TAG,"GetTotalDistance: "+e);
        }
        return totalDistance;
    }

    public ArrayList<String> getInstructions(){
        int totalSteps=0;
        JSONArray routes = null;
        ArrayList<String> instructions = new ArrayList<>();
        try {
            routes = object.getJSONArray("routes");
            Log.v(TAG,"Total Routes length"+routes.length());
            JSONObject route = routes.getJSONObject(0);
            JSONArray legs = route.getJSONArray("legs");
            int totalLegs=legs.length();
            for(int i=0;i<totalLegs;i++){
                JSONObject leg= legs.getJSONObject(i);
                JSONArray steps = leg.getJSONArray("steps");
                totalSteps= steps.length();
                for(int k=0;k<totalSteps;k++){
                    JSONObject step_object = steps.getJSONObject(k);
                    String instruction = step_object.getString("html_instructions");
                    instructions.add(instruction);
                }
            }

        }catch (Exception e){
            Log.v(TAG,"GetTotalNumberOfSteps: "+e);
        }
        return instructions;
    }

    public int getNumberOfLegs(){
        int numberOfLegs=0;
        JSONArray routes = null;
        try {
            routes = object.getJSONArray("routes");
            Log.v(TAG,"Total Routes length"+routes.length());
            JSONObject route = routes.getJSONObject(0);
            JSONArray legs = route.getJSONArray("legs");
            numberOfLegs=legs.length();
        } catch (JSONException e) {
            Log.v(TAG,"GetTotalDistance: "+e);
        }
        return numberOfLegs;
    }

    public String getPolyline(){

        String poly="";
        JSONArray routes = null;
        try{
            routes=object.getJSONArray("routes");
            //Log.v(TAG,"Total Routes length"+ routes.length());
            JSONObject route = routes.getJSONObject(0);
            JSONObject overView_polylines = route.getJSONObject("overview_polyline");
            poly= overView_polylines.getString("points");
        }
        catch (Exception e){
            Log.v(TAG,"Polyline "+e);
        }
        return poly;
    }

    public ArrayList<String> getOverViewPolylines(){
        ArrayList<String> overViewPolylines = new ArrayList<>();
                int totalSteps=0;
        JSONArray routes = null;
        try {

            routes = object.getJSONArray("routes");
            Log.v(TAG,"Total Routes length"+routes.length());
            JSONObject route = routes.getJSONObject(0);
            JSONArray legs = route.getJSONArray("legs");
            int totalLegs=legs.length();

            for(int i=0;i<totalLegs;i++){
                JSONObject leg= legs.getJSONObject(i);
                JSONArray steps = leg.getJSONArray("steps");
                totalSteps= steps.length();
                ArrayList<Double> step_latlng = new ArrayList<>();
                for(int k=0;k<totalSteps;k++){
                    JSONObject step_object = steps.getJSONObject(k);
                   JSONObject overView_polylin=step_object.getJSONObject("polyline");
                   String overView_polyline=overView_polylin.getString("points");
                   overViewPolylines.add(overView_polyline);
                }

            }
            //Par
//            routes = object.getJSONArray("routes");
//
        }catch (Exception e){
            Log.v(TAG,"GetOverViewPolyLine: "+e);
        }

        return overViewPolylines;
        //return overView_polyline;
    }

    public int getTotalNumberOfSteps(){
        int totalSteps=0;
        JSONArray routes = null;
        try {
            routes = object.getJSONArray("routes");
            Log.v(TAG,"Total Routes length"+routes.length());
            JSONObject route = routes.getJSONObject(0);
            JSONArray legs = route.getJSONArray("legs");
            int totalLegs=legs.length();
            for(int i=0;i<totalLegs;i++){
                JSONObject leg= legs.getJSONObject(i);
                JSONArray steps = leg.getJSONArray("steps");
                totalSteps= steps.length();
            }
            //Parse total steps
        }catch (Exception e){
            Log.v(TAG,"GetTotalNumberOfSteps: "+e);
        }


        return totalSteps;
    }

    public ArrayList<Double> getLatitudeLongitude(){            //The array List is [Starting latitude, StartingLong,DestLat,DestLong,StartingLat, Startlong..]
        int totalSteps=0;
        JSONArray routes = null;
        ArrayList<Double> latLngs = new ArrayList<>();
        Set<Double> s = null;
        try {
            routes = object.getJSONArray("routes");
            Log.v(TAG,"Total Routes length"+routes.length());
            JSONObject route = routes.getJSONObject(0);
            JSONArray legs = route.getJSONArray("legs");
            int totalLegs=legs.length();

            for(int i=0;i<totalLegs;i++){
                JSONObject leg= legs.getJSONObject(i);
                JSONArray steps = leg.getJSONArray("steps");
                totalSteps= steps.length();
                ArrayList<Double> step_latlng = new ArrayList<>();
                for(int k=0;k<totalSteps;k++){
                    JSONObject step_object = steps.getJSONObject(k);
                    JSONObject startLocation = step_object.getJSONObject("start_location");
                    JSONObject endLocation = step_object.getJSONObject("end_location");
                    double start_latitude= startLocation.getDouble("lat");
                    step_latlng.add(start_latitude);
                    double start_longitude = startLocation.getDouble("lng");
                    step_latlng.add(start_longitude);
                    double end_latitude= endLocation.getDouble("lat");
                    step_latlng.add(end_latitude);
                    double end_longitude = endLocation.getDouble("lng");
                    step_latlng.add(end_longitude);
                    Log.v(TAG,"All the latlngs are: "+step_latlng);
                }
                latLngs=step_latlng;
                s = new LinkedHashSet<>(latLngs);
                latlng=s;
                Log.v(TAG,"No repeat LatLngs are: "+s);
            }
            //Parse total steps
        }catch (Exception e){
            Log.v(TAG,"GetTotalNumberOfSteps: "+e);
        }
        ArrayList<Double> ltln= new ArrayList<>();
        ltln.addAll(s);
        return ltln;
    }

    public ArrayList<Double> getBearings(){
        ArrayList<Double> bearings = new ArrayList<>();
        ArrayList<Double> route_bearings = new ArrayList<>();
        bearings.addAll(latlng);
       // int size
        Log.v(TAG,"LATLNGS"+bearings);
        for(int i=0;i<bearings.size()-2;i+=2){
            LatLng source = new LatLng(bearings.get(i),bearings.get(i+1));
            LatLng destination = new LatLng(bearings.get(i+2),bearings.get(i+3));
            double bearing= SphericalUtil.computeHeading(source,destination);
            Log.v(TAG,"The bearings for: Source:"+source+"and destination"+destination+"are"+bearing);
            route_bearings.add(bearing);
        }
        //Log.v(TAG,"Bearings are:"+route_bearings);
       // double bearings= SphericalUtil.computeHeading(src,dest);  //Latlng parameters

    return  route_bearings;
    }

    public ArrayList<Double> getIndividualStepDistance() {
        int totalSteps = 0;
        JSONArray routes = null;
        ArrayList<Double> stepDistances = new ArrayList<>();
        Set<Double> s = null;
        try {
            routes = object.getJSONArray("routes");
            Log.v(TAG, "Total Routes length" + routes.length());
            JSONObject route = routes.getJSONObject(0);
            JSONArray legs = route.getJSONArray("legs");
            int totalLegs = legs.length();
            for(int i=0;i<totalLegs;i++){
                JSONObject leg= legs.getJSONObject(i);
                JSONArray steps = leg.getJSONArray("steps");
                totalSteps= steps.length();
                //ArrayList<Double> step_latlng = new ArrayList<>();
                for(int k=0;k<totalSteps;k++){
                    JSONObject step_object = steps.getJSONObject(k);
                    JSONObject step_distance = step_object.getJSONObject("distance");
                    double distance = step_distance.getDouble("value");
                    stepDistances.add(distance);
                }

                Log.v(TAG,"No repeat LatLngs are: "+s);
            }

        }catch (Exception e){
            Log.v(TAG,"GetIndiStepDistance"+e.getMessage());
        }
        return stepDistances;
    }


}
