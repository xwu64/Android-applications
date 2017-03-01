package edu.iit.xwu64.hw4_stock_watch;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by xiaoliangwu on 2017/3/1.
 */

public class AsyncSymbolLoader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private final String APIKEY = "a66767efffb6d56dc60221ea7addbd79d4a14ef4";
    private final String prefix = "http://stocksearchapi.com/api/?api_key="+APIKEY+"&search_text=";

    public AsyncSymbolLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... params) {

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(prefix+params[0]);


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        Log.d("json file: ", sb.toString());
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        final ArrayList<Stock> candidateList = parseJson(s);
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        final Stock[] stock = new Stock[1];

        if (candidateList == null) {
            builder.setTitle("Symbol Not Found");
            builder.setMessage("Not found stock");
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }
        else if (candidateList.size() == 1){
            stock[0] = candidateList.get(0);
            AsyncDataLoader adl = new AsyncDataLoader(mainActivity);
            adl.execute(stock[0]);
        }else{
            CharSequence[] stocks = new CharSequence[candidateList.size()];
            int i =0;
            for (Stock each : candidateList){
                CharSequence tmp = each.getSymbol()+" - "+each.getName();
                stocks[i] = tmp;
                i++;
            }

            builder.setTitle("Make a selection");
            builder.setItems(stocks, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stock[0] = candidateList.get(which);
                    AsyncDataLoader adl = new AsyncDataLoader(mainActivity);
                    adl.execute(stock[0]);
                }
            });
            builder.setNegativeButton("Nevrmind", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }


    }

    private ArrayList<Stock> parseJson(String s){
        ArrayList<Stock> candidateList = new ArrayList<>();

        try {
            JSONArray jObjMain = new JSONArray(s);

            for (int i =0; i<jObjMain.length(); i++){
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                Stock stock = new Stock(jStock.getString("company_symbol"), jStock.getString("company_name"));
                candidateList.add(stock);
            }
            return candidateList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}
