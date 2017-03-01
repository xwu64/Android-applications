package edu.iit.xwu64.hw4_stock_watch;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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
 * Created by xiaoliangwu on 2017/3/1.
 */

public class AsyncUpdateLoader extends AsyncTask<Stock, Void, String> {
    private static final String prefix = "http://finance.google.com/finance/info?client=ig&q=";
    private MainActivity mainActivity;
    private Stock stock;

    public AsyncUpdateLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(Stock... params) {
        stock = params[0];

        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(prefix+stock.getSymbol());
            Log.d("url: ", url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            return sb.toString().substring(3);
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
        if (stock.getPrice() == 0){
            return;
        } else mainActivity.updeData(stock);
    }

    public void parseJson(String s){

        try {
            JSONArray jObjMain = new JSONArray(s);
            JSONObject jStock = (JSONObject) jObjMain.get(0);

            stock.setChangePercent(jStock.getDouble("cp"));
            stock.setPriceChange(jStock.getDouble("c"));
            stock.setPrice(jStock.getDouble("l"));
        } catch (JSONException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Download Fail");
            builder.setMessage("Not found stock: 400 Bad Request");
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
}
