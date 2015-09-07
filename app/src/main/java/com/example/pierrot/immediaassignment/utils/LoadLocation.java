package com.example.pierrot.immediaassignment.utils;

/**
 * Created by Pierrot on 9/4/2015.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.pierrot.immediaassignment.model.Venue;
import com.example.pierrot.immediaassignment.model.location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;





/**
 * This class loads the data from FourSquare API, parses the venues from the fetched JSON data
 * and passes the venues on to the fragment for display.
 */
public class LoadLocation extends AsyncTask<String, Void, String> {
    private static final String TAG = LoadVenues.class.getSimpleName();
    public static ArrayList<String> listlocation = new ArrayList<String>();
    private static final String CLIENT_ID = "FJO4LA5KPC32RPU33FMNDXSDG2XYRGK4V2AZ2IZGGIP5MOQY";
    private static final String CLIENT_SECRET = "HDSOEQ34YDE23OEQYSQYRX5DKOPHNDQT2LSBVHHRGIZBHRI5";

    public interface Callback {
        void onLocationLoaded(ArrayList<location> venues);
        void onError(String errorMsg);
    }
    private Callback mCallback;

    public LoadLocation (Callback callback) {
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... params ) {
        String result = "";
        if (params.length > 0 && params[0] != null && !params[0].isEmpty()) {
            // make Call to the url
            result = readTextFromURL("https://api.foursquare.com/v2/venues/explore?locale=en&" +
                    "client_id=" + CLIENT_ID +
                    "&client_secret=" + CLIENT_SECRET +
                    "&v=20140701" +
                    "&ll=" + params[0] +
                    "&query=" +
                    "&intent=checkin" +
                    "&radius=3000");
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result ) {
        Log.d(TAG, "result=" + result);
        if (result == null || result.isEmpty()) {
            mCallback.onError("Unable to load data from FourSquare.");

        } else {
            // parse the results
            ArrayList<location> venues = parseVenues(result);
            Collections.sort(venues);
            mCallback.onLocationLoaded(venues);
        }
    }

    /**
     * Simple helper method to retrieve text from a file hosted at the given URL.
     *
     * @param urlString	The URL from which the text file is to be read.
     * @return	The actual text read from the file at the URL.
     */
    private String readTextFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            InputStream in = url.openStream();
            StringBuilder inputStringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = bufferedReader.readLine();
            while(line != null){
                inputStringBuilder.append(line);
                inputStringBuilder.append('\n');
                line = bufferedReader.readLine();
            }
            String output = inputStringBuilder.toString();
            in.close();
            bufferedReader.close();
            return output;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //parses a list of venues from a JSON structure returned by FourSquare's API
    private static ArrayList<location> parseVenues(final String root) {

        ArrayList venuesList = new ArrayList();
        try {
            // make a jsonObject in order to parse the response
            JSONObject rootObject = new JSONObject(root);
            JSONObject responseObject = rootObject.getJSONObject("response");

            if (responseObject != null) {
                JSONArray groups = responseObject.getJSONArray("groups");

                if (groups != null) {
                    for (int b = 0; b < groups.length(); b++) {
                        JSONObject groupObject = groups.getJSONObject(b);

                        JSONArray items = groupObject.getJSONArray("items");
                        if (items != null) {


                            for (int t = 0; t < items.length(); t++) {
                                JSONObject itemObject = items.getJSONObject(t);





                                if (!itemObject.has("venue")|| !itemObject.has("tips") ||
                                        !items .getJSONObject(t).getJSONObject("venue").getJSONObject("location").has("city")

                                        ) {

                                    continue;
                                }


                                String idcat = items .getJSONObject(t).getJSONObject("venue").getString("name");
                                JSONArray idtips = items .getJSONObject(t).getJSONArray("tips");
                                String idtipsid="";
                                String pic="";
                                String suffix="";
                                String prefix = "";
                                JSONArray addressLines = items .getJSONObject(t).getJSONObject("venue").getJSONObject("location").getJSONArray("formattedAddress");
                                String address = "";
                                for (int j = 0; j < addressLines.length(); j++) {
                                    address += addressLines.getString(j) + "\n";
                                }
                                for (int d = 0; d < idtips.length(); d++) {
                                    idtipsid = idtips .getJSONObject(d).getString("createdAt");
                                    prefix = idtips .getJSONObject(d).getJSONObject("user").getJSONObject("photo").getString("prefix");
                                    suffix = idtips .getJSONObject(d).getJSONObject("user").getJSONObject("photo").getString("suffix");
                                    pic = prefix +"500x500"+ suffix ;
                                }
                                String city = items .getJSONObject(t).getJSONObject("venue").getJSONObject("location").getString("city");
                                if(!isexisting(city)){
                                    listlocation.add(city);
                                venuesList.add(new location(
                                        city,
                                        address,
                                        pic,
                                        items .getJSONObject(t).getJSONObject("venue").getJSONObject("location").getInt("distance"),
                                        items .getJSONObject(t).getJSONObject("venue").getJSONObject("location").getDouble("lat"),
                                        items .getJSONObject(t).getJSONObject("venue").getJSONObject("location").getDouble("lng")
                                ));}
                            }
                        }
                    }
                }}}

        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
        return venuesList;
    }
    public static Boolean isexisting(String city){
        Boolean result = false;
        for(int i = 0; i < listlocation.size(); i++) {
            if (listlocation.get(i).toString().equals(city)){
                result = true ;
                i=listlocation.size();
            }else {
                result = false ;
            }
        }


    return result;
    }

}




