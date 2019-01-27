package com.bannuranurag.android.augv.SharingDataLocation;

import android.util.Log;


public class DataParsing {
    private static final String TAG = "DatsParsing";

    public String buildURL(Double originCoordinatesLatitude,Double originCoordinatesLongitude,Double destinationCoordinateLatitude ,Double destinationCoordinatesLongitude){
        String requestURLString="https://api.mapbox.com/directions/v5/mapbox/driving/"+originCoordinatesLongitude.toString()+","+originCoordinatesLatitude.toString()+";"+destinationCoordinatesLongitude.toString()+","+destinationCoordinateLatitude.toString()+"?geometries=geojson&access_token=pk.eyJ1IjoiYW51cmFnYmFubnVyIiwiYSI6ImNqcXA5M2xsbzAxZGo0MXQxdTY1MWtxbWkifQ.CC_5wINWsae-9hwHIQ304g&steps=true";
        Log.v(TAG,"ExampleURL: "+requestURLString);
        return requestURLString;

    }

}
