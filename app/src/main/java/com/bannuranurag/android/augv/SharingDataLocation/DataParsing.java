package com.bannuranurag.android.augv.SharingDataLocation;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;


public class DataParsing {
    private static final String TAG = "DatsParsing";
    public void buildURL(Double originCoordinatesLatitude,Double originCoordinatesLongitude,Double destinationCoordinateLatitude ,Double destinationCoordinatesLongitude){
        String requestURLString="https://api.mapbox.com/directions/v5/mapbox/driving/"+originCoordinatesLongitude.toString()+","+originCoordinatesLatitude.toString()+";"+destinationCoordinatesLongitude.toString()+","+destinationCoordinateLatitude.toString()+"?geometries=geojson&access_token=pk.eyJ1IjoiYW51cmFnYmFubnVyIiwiYSI6ImNqcXA5M2xsbzAxZGo0MXQxdTY1MWtxbWkifQ.CC_5wINWsae-9hwHIQ304g&steps=true";
        try {
            URL requestURL=new URL(requestURLString);
            Log.v(TAG,"RequestURL "+requestURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG,"ExampleURL: https://api.mapbox.com/directions/v5/mapbox/driving/-73.9999%2C40.733%3B-72.9988%2C40.733.json?access_token=pk.eyJ1IjoiYW51cmFnYmFubnVyIiwiYSI6ImNqcXA5M2xsbzAxZGo0MXQxdTY1MWtxbWkifQ.CC_5wINWsae-9hwHIQ304g&steps=true");
    }
}
