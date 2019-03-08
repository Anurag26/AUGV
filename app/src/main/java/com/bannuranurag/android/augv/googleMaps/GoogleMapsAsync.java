package com.bannuranurag.android.augv.googleMaps;

import android.os.AsyncTask;
import android.util.Log;

import com.bannuranurag.android.augv.MapsActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GoogleMapsAsync extends AsyncTask<String, Void, String> {
    private static final String TAG = "GoogleMapsAsync";
    @Override
    protected String doInBackground(String... strings) {
        String data = "";
        InputStream iStream = null;
        HttpsURLConnection urlConnection = null;
        try {
            URL url = new URL(strings[0]);

            urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            try {
                iStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }
        return data;
    }


    @Override
    protected void onPostExecute(String s) {
        MapsActivity.getJsonResponse(s);
    }
}
