package com.bannuranurag.android.augv.SharingDataLocation;

import android.os.AsyncTask;
import android.util.Log;

import com.bannuranurag.android.augv.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class JSONTASk extends AsyncTask<String, Void, String> {
    private static final String TAG = "JSONTASk";
    //public AsyncResponse delegate = null;
    @Override
    protected String doInBackground(String... strings) {
        HttpsURLConnection connection=null;
        BufferedReader reader=null;
        String line="";

        try{
            URL url= new URL(strings[0]);
            connection= (HttpsURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = new BufferedInputStream(connection.getInputStream());
            reader=new BufferedReader(new InputStreamReader(stream));

            StringBuilder builder = new StringBuilder();


            while ((line = reader.readLine()) != null) {
                builder.append(line+"\n");
                Log.v("Response: ", "> " + line);   //here u ll get whole response...... :-) //only partial json
                return line;

            }
            connection.disconnect();
            return builder.toString();



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(isCancelled()){
            Log.v(TAG,"Cancelled");
        }
        else{
            Log.v(TAG,"NOT CANCELLED");
        }
        return line;

    }

    @Override
    protected void onPostExecute(String s) {
        MainActivity.callNecessaryMethodsFromHere(s);
        //delegate.processFinish(s);
    }
}
