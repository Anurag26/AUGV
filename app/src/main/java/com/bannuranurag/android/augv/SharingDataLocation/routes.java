package com.bannuranurag.android.augv.SharingDataLocation;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class routes {

    JSONObject jsonResponse;
    private static final String TAG = "routes";

    public void getJSON(JSONObject json){
        jsonResponse=json;
    }

    public void test(){
        Log.v(TAG,"JSON RESPONSE IS"+jsonResponse);
    }

    public int getNumberOfLegs(){
        int totalNumberOfLegs=0;
        try {
            JSONArray routes= jsonResponse.getJSONArray("routes");
            int totalRoutes= routes.length();
            for(int i=0;i<totalRoutes;i++){
                JSONObject route=routes.getJSONObject(i);
                JSONArray leg = route.getJSONArray("legs");
                totalNumberOfLegs=leg.length();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return totalNumberOfLegs;
    }

    public int getNumberOfSteps(routes routes){
        int numberOfLegs= routes.getNumberOfLegs();
        int numberOfSteps=0;

        try {
            JSONArray mRoutes= jsonResponse.getJSONArray("routes");
            for(int i=0;i<mRoutes.length();i++){
                JSONObject route=mRoutes.getJSONObject(i);
                JSONArray leg = route.getJSONArray("legs");
                for(int k=0;k<numberOfLegs;k++) {
                    JSONObject j = leg.getJSONObject(k);
                    JSONArray steps = j.getJSONArray("steps");
                    numberOfSteps = steps.length();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return numberOfSteps;
    }

    public double getTotalRouteDistance(routes routes){
        //Get total distance of the trip
        int numberOfLegs= routes.getNumberOfLegs();
        double totalDistance=0;
        String distanceString="";
        try {
            JSONArray mRoutes= jsonResponse.getJSONArray("routes");
            for(int i=0;i<mRoutes.length();i++){
                JSONObject route=mRoutes.getJSONObject(i);
                JSONArray leg = route.getJSONArray("legs");
                for(int k=0;k<numberOfLegs;k++) {
                    JSONObject j = leg.getJSONObject(k);
                    distanceString=j.getString("distance");
                    totalDistance=Double.parseDouble(distanceString);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v(TAG,"Distance"+totalDistance);
        return totalDistance;
    }

    public ArrayList<Double> getIndividualStepDistance(routes routes){
        int numberOfLegs= routes.getNumberOfLegs();
        int numberOfSteps=routes.getNumberOfSteps(routes);
        ArrayList<Double> individualStepDistances= new ArrayList<>();
        double mIndividualStepDistance=0;
        try {
            JSONArray mRoutes= jsonResponse.getJSONArray("routes");
            for(int i=0;i<mRoutes.length();i++){
                JSONObject route=mRoutes.getJSONObject(i);
                JSONArray leg = route.getJSONArray("legs");
                for(int k=0;k<numberOfLegs;k++) {
                    JSONObject j = leg.getJSONObject(k);
                    JSONArray steps = j.getJSONArray("steps");
                    for(int l=0;l<numberOfSteps;l++){
                        JSONObject stepObject = steps.getJSONObject(l);
                        mIndividualStepDistance= Double.parseDouble(stepObject.getString("distance"));
                        individualStepDistances.add(mIndividualStepDistance);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return individualStepDistances;
    }

    public ArrayList<Double> getManeuverBearings(routes routes){
        int numberOfLegs= routes.getNumberOfLegs();
        int numberOfSteps=routes.getNumberOfSteps(routes);
        ArrayList<Double> bearings= new ArrayList<>();
        double bearingBefore=0, bearingAfter=0;
        try {
            JSONArray mRoutes= jsonResponse.getJSONArray("routes");
            for(int i=0;i<mRoutes.length();i++){
                JSONObject route=mRoutes.getJSONObject(i);
                JSONArray leg = route.getJSONArray("legs");
                for(int k=0;k<numberOfLegs;k++) {
                    JSONObject j = leg.getJSONObject(k);
                    JSONArray steps = j.getJSONArray("steps");
                    for(int l=0;l<numberOfSteps;l++){
                        JSONObject stepObject = steps.getJSONObject(l);
                        JSONObject maneuver = stepObject.getJSONObject("maneuver");
                        bearingBefore=Double.parseDouble(maneuver.getString("bearing_before"));     //Before first, After next
                        bearings.add(bearingBefore);
                        bearingAfter=Double.parseDouble(maneuver.getString("bearing_after"));
                        bearings.add(bearingAfter);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bearings;
    }

    public ArrayList<String> getEachStepInstruction(routes routes){
        int numberOfLegs= routes.getNumberOfLegs();
        int numberOfSteps=routes.getNumberOfSteps(routes);
        String instruction="";
        ArrayList<String> instructions= new ArrayList<>();
        try {
            JSONArray mRoutes= jsonResponse.getJSONArray("routes");
            for(int i=0;i<mRoutes.length();i++){
                JSONObject route=mRoutes.getJSONObject(i);
                JSONArray leg = route.getJSONArray("legs");
                for(int k=0;k<numberOfLegs;k++) {
                    JSONObject j = leg.getJSONObject(k);
                    JSONArray steps = j.getJSONArray("steps");
                    for(int l=0;l<numberOfSteps;l++){
                        JSONObject stepObject = steps.getJSONObject(l);
                        JSONObject maneuver = stepObject.getJSONObject("maneuver");
                        instruction=maneuver.getString("instruction");     //Before first, After next
                        instructions.add(instruction);

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return instructions;
    }
    public ArrayList<Double> getCoordinatesForManeuver(routes routes){  //order is [longitude,latitude]
        int numberOfLegs= routes.getNumberOfLegs();
        int numberOfSteps=routes.getNumberOfSteps(routes);
         double mLocation;
        ArrayList<Double> locationsForManeuver= new ArrayList<>();
        try {
            JSONArray mRoutes= jsonResponse.getJSONArray("routes");
            for(int i=0;i<mRoutes.length();i++){
                JSONObject route=mRoutes.getJSONObject(i);
                JSONArray leg = route.getJSONArray("legs");
                for(int k=0;k<numberOfLegs;k++) {
                    JSONObject j = leg.getJSONObject(k);
                    JSONArray steps = j.getJSONArray("steps");
                    for(int l=0;l<numberOfSteps;l++){
                        JSONObject stepObject = steps.getJSONObject(l);
                        JSONObject maneuver = stepObject.getJSONObject("maneuver");
                        JSONArray location = maneuver.getJSONArray("location");
                        for(int m=0;m<location.length();m++){
                            mLocation= Double.parseDouble(location.getString(m));
                            locationsForManeuver.add(mLocation);
                        }
                        Log.v(TAG,"MAn"+locationsForManeuver);
                    }
                }
            }
        } catch (JSONException e) {
           Log.v(TAG,"Maneuver "+e);
        }
        return locationsForManeuver;
    }


}
