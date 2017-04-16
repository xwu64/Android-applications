package edu.iit.xwu64.hw5_know_your_government;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by xiaoliangwu on 2017/4/14.
 */

public class AsyncDataLoader extends AsyncTask<String, Void, String> {
    private MainActivity mainActivity;
    private String prefix = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyCoRMp-XScdHeWT_XtyZKiaPB4Rme3QZlI&address=";

    public AsyncDataLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("async", params[0]);
        String location = params[0];

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(prefix+location);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        parseJson(s);
    }

    private void parseJson(String s) {


        try {
            JSONObject jObjMain = new JSONObject(s);

            JSONObject jNormalInput = jObjMain.getJSONObject("normalizedInput");

            String locationText = jNormalInput.getString("city")+", "+jNormalInput.getString("state")+" "+jNormalInput.getString("zip");
            mainActivity.setLocationText(locationText);
            JSONArray jArrayOffices = jObjMain.getJSONArray("offices");
            JSONArray jArrayOfficials = jObjMain.getJSONArray("officials");

            int length = jArrayOffices.length();
            mainActivity.clearOfficial();

            for (int i = 0; i<length; i++){
                JSONObject jObj = jArrayOffices.getJSONObject(i);
                String officeName = jObj.getString("name");

                JSONArray indicesStr = jObj.getJSONArray("officialIndices");
                ArrayList<Integer> indices = new ArrayList<>();

                for (int j = 0; j<indicesStr.length(); j++){
                    int pos = Integer.parseInt(indicesStr.getString(j));
                    Official official = new Official(officeName);
                    JSONObject jOfficial = jArrayOfficials.getJSONObject(pos);

                    official.setName(jOfficial.getString("name"));

                    JSONArray jAddresses = jOfficial.getJSONArray("address");
                    JSONObject jAddress = jAddresses.getJSONObject(0);

                    String address="";

                    if (jAddress.has("line1")) address+=jAddress.getString("line1")+'\n';
                    if (jAddress.has("line2")) address+=jAddress.getString("line2")+'\n';
                    if (jAddress.has("line3")) address+=jAddress.getString("line3")+'\n';
                    if (jAddress.has("city")) address+=jAddress.getString("city")+", ";
                    if (jAddress.has("state")) address+=jAddress.getString("state")+' ';
                    if (jAddress.has("zip")) address+=jAddress.getString("zip");

                    official.setAddress(address);

                    if (jOfficial.has("party")) official.setParty(jOfficial.getString("party"));
                    if (jOfficial.has("phones")) official.setPhone(jOfficial.getJSONArray("phones").getString(0));
                    if (jOfficial.has("urls")) official.setUrl(jOfficial.getJSONArray("urls").getString(0));
                    if (jOfficial.has("emails")) official.setEmail(jOfficial.getJSONArray("emails").getString(0));

                    if (jOfficial.has("channels")){
                        Channel channel = new Channel();

                        JSONArray jChannels = jOfficial.getJSONArray("channels");
                        for (int k = 0; k<jChannels.length(); k++){
                            JSONObject jChannel = jChannels.getJSONObject(k);
                            if (jChannel.getString("type").equals("GooglePlus")) channel.setGooglePlusId(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("Facebook")) channel.setFacebookId(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("Twitter")) channel.setTwitterId(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("YouTube")) channel.setYoutubeId(jChannel.getString("id"));
                        }
                        official.setChannel(channel);
                    }

                    if (jOfficial.has("photoUrl")) official.setPhotoUrl(jOfficial.getString("photoUrl"));
                    mainActivity.addOfficial(official);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            return;
        }
    }
}
