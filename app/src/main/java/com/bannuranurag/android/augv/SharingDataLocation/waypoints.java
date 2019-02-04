package com.bannuranurag.android.augv.SharingDataLocation;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class waypoints {

    JSONObject jsonResponse;

    private static final String TAG = "waypoints";

    public void getJSON(JSONObject json){
        jsonResponse=json;
    }

    public void test(){
        Log.v(TAG,"JSON IS"+jsonResponse);
    }

    public int getTotalWayPoints()  {
        int numberOfWayPoints=0;
        try {
            JSONArray wayPoints = jsonResponse.getJSONArray("waypoints");
             numberOfWayPoints=wayPoints.length();
            return numberOfWayPoints;
        } catch (JSONException e) {
            Log.v(TAG,"WayPoints "+e);
        }
            return numberOfWayPoints;
    }

    public ArrayList<Double> getWayPointDistances(waypoints waypoints){
        int numberOfWayPoints = waypoints.getTotalWayPoints();
        ArrayList<Double> distances= new ArrayList<Double>();
        JSONArray wayPoints = null;
        try {
            wayPoints = jsonResponse.getJSONArray("waypoints");
            for(int i=0;i<numberOfWayPoints;i++){
                JSONObject waypointObject =  wayPoints.getJSONObject(i);
                Double distance =  Double.parseDouble(waypointObject.getString("distance"));
                distances.add(distance);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return distances;
    }


    public ArrayList<Double> getWayPointLongitude(waypoints waypoints){
        int numberOfWayPoints = waypoints.getTotalWayPoints();
        ArrayList<Double> longitudes= new ArrayList<Double>();
        JSONArray wayPoints = null;
        try {
            wayPoints = jsonResponse.getJSONArray("waypoints");
            for(int i=0;i<numberOfWayPoints;i++){
                JSONObject waypointObject =  wayPoints.getJSONObject(i);
                JSONArray locationArray=waypointObject.getJSONArray("location");
                Double longitude = locationArray.getDouble(0);
                longitudes.add(longitude);
                Log.v(TAG,"Longitude method executed");
            }
        } catch (JSONException e) {
            Log.v(TAG,"LONGITUDE Exception"+e);
        }
        return longitudes;
    }

    public ArrayList<Double> getWayPointLatitude(waypoints waypoints){
        int numberOfWayPoints = waypoints.getTotalWayPoints();
        ArrayList<Double> latitudes= new ArrayList<Double>();
        JSONArray wayPoints = null;
        try {
            wayPoints = jsonResponse.getJSONArray("waypoints");
            for(int i=0;i<numberOfWayPoints;i++){
                JSONObject waypointObject =  wayPoints.getJSONObject(i);
                JSONArray locationArray=waypointObject.getJSONArray("location");
                Double latitude = locationArray.getDouble(1);
                latitudes.add(latitude);
                Log.v(TAG,"Latitude method executed");
            }
        } catch (JSONException e) {
            Log.v(TAG,"Latitude Exception"+e);
        }
        return latitudes;
    }

    public ArrayList<String> getWayPointNames(waypoints waypoints){
        int numberOfWayPoints = waypoints.getTotalWayPoints();
        ArrayList<String> names= new ArrayList<String>();
        JSONArray wayPoints = null;
        try {
            wayPoints = jsonResponse.getJSONArray("waypoints");
            for(int i=0;i<numberOfWayPoints;i++){
                JSONObject waypointObject =  wayPoints.getJSONObject(i);
                String wayPointName =  waypointObject.getString("name");
                names.add(wayPointName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return names;
    }

    public String getCode(waypoints waypoints){
        String code= null;
        try {
            code = jsonResponse.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    public String getUUID(waypoints waypoints){
        String uuid= null;
        try {
            uuid = jsonResponse.getString("uuid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return uuid;
    }


}
