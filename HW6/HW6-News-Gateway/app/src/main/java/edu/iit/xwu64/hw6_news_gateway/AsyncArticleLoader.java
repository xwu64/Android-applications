package edu.iit.xwu64.hw6_news_gateway;

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

/**
 * Created by xiaoliangwu on 2017/4/19.
 */

public class AsyncArticleLoader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private String prefix="https://newsapi.org/v1/articles?apiKey=6ea83d10d8e4414c82259877303b1a13&source=";

    public AsyncArticleLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... params) {
        String source = params[0];
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(prefix+source);
            Log.d("url", url.toString());
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
        Log.d("parese", s);
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray jArrSources = jObjMain.getJSONArray("articles");
            mainActivity.clearArticles();

            for (int i =0; i<jArrSources.length(); i++){
                JSONObject jObjSource = jArrSources.getJSONObject(i);
                String author = jObjSource.getString("author");
                String title = jObjSource.getString("title");
                String description = jObjSource.getString("description");
                String url = jObjSource.getString("url");
                String urlToImage = jObjSource.getString("urlToImage");
                String publishedAt = jObjSource.getString("publishedAt");

                Article article = new Article(author, title, description, url, urlToImage, publishedAt, jArrSources.length(), i);
                mainActivity.addArticle(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
