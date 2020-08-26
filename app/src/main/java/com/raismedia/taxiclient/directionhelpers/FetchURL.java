package com.raismedia.taxiclient.directionhelpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//import com.projets.raismedia.uber.fragment.FragmentHome;

import com.raismedia.taxiclient.activity.MainActivity;
import com.raismedia.taxiclient.fragment.customer.FragmentHome;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vishal on 10/20/2018.
 */

public class FetchURL extends AsyncTask<String, Void, String> {
    Context mContext;
    String directionMode = "";
    String categorie = "";

    public FetchURL(Context mContext, String categorie) {
        this.mContext = mContext;
        this.categorie = categorie;
    }

    @Override
    protected String doInBackground(String... strings) {
        // For storing data from web service
        String data = "";
        directionMode = strings[1];
        try {
            // Fetching the data from web service
            data = downloadUrl(strings[0]);
            Log.d("mylog", "Background task data " + data.toString());
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        PointsParser parserTask = new PointsParser(mContext, directionMode, categorie);
        // Invokes the thread for parsing the JSON data
        parserTask.execute(s);

        try {
            if(categorie.equals("home")) {
                if (directionMode.equals("bicycling")) {
                    FragmentHome.parseRouteDistanceBicycling(new JSONObject(s));
                }else {
                    FragmentHome.parseRouteDistance(new JSONObject(s));
                }
            }else if(categorie.equals("timeout")) {
                MainActivity.parseRouteDistanceTimeOut(new JSONObject(s));
            }else{
//                FragmentHome.dismissBottomSheet();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("mylog", "Downloaded URL: " + data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("mylog", "Exception downloading URL: " + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}

