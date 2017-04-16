package edu.iit.xwu64.hw5_know_your_government;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<Official> officialList = new ArrayList<>();
    private OfficialAdapter officialAdapter;
    private static TextView locationTextView;
    private Locator locator;
    private static MainActivity mainActivity;
    private TextView warning;
    private ConnectivityManager cm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.myrecycler);
        officialAdapter = new OfficialAdapter(officialList, this);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainActivity = this;
        warning = (TextView) findViewById(R.id.ma_warning);

        if (networkCheck()){
            locator = new Locator(this);
        } else {
            warningShow("Cannot connect to network");
        }

        if (locationTextView.getText().toString().equals("No Data For Location")) warningShow("close app and reopen!");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menuInfo:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuSearch:
                searchButton();



            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildAdapterPosition(v);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("location", locationTextView.getText().toString());
        intent.putExtra("official", officialList.get(pos));
        startActivityForResult(intent, 1);
    }



    public void searchButton(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter a City, State or Zip code");
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText inputTextView = (EditText) view.findViewById(R.id.inputTextView);
                String input = inputTextView.getText().toString();

                AsyncDataLoader asyncDataLoader = new AsyncDataLoader(mainActivity);
                asyncDataLoader.execute(input);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setLocation(double lat, double lon){

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat,lon, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses == null){
            Log.d("addresses", "Null");

        }else{
            locationTextView.setText(addresses.get(0).getAddressLine(1).toString());
        }

        AsyncDataLoader adl = new AsyncDataLoader(this);

        adl.execute(locationTextView.getText().toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void addOfficial(Official official){
        officialList.add(official);
        officialAdapter.notifyDataSetChanged();
    }

    public void clearOfficial(){
        officialList.clear();
    }

    public void setLocationText(String location){
        locationTextView.setText(location);
    }

    public void test(String s){
        Log.d("test",s);
        locationTextView.setText(s);
    }

    public Boolean networkCheck(){
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo !=null && netInfo.isConnectedOrConnecting())return true;
        else return false;
    }

    public void warningShow(String update){
        warning.setVisibility(View.VISIBLE);
        warning.setText(update);
    }

    public void warningClose(){
        warning.setVisibility(View.INVISIBLE);
    }
}
//curl "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyCoRMp-XScdHeWT_XtyZKiaPB4Rme3QZlI&address=1263%20Pacific%20Ave.%20Kansas%20City%20KS"
