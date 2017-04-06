package edu.iit.xwu64.hw5_know_your_government;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<Official> officialList = new ArrayList<>();
    private OfficialAdapter officialAdapter;
    private TextView locationTextView;
    private Locator locator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        officialList.add(new Official("president", "name 1"));
        officialList.add(new Official("vice president", "name 2"));

        recyclerView = (RecyclerView) findViewById(R.id.myrecycler);
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationTextView = (TextView) findViewById(R.id.locationTextView);
        locator = new Locator(this);

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
        Context context = getApplicationContext();
        CharSequence text = officialList.get(pos).getName();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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
                Context context = getApplicationContext();
                CharSequence text = input;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
    }

    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }
}
//curl "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyCoRMp-XScdHeWT_XtyZKiaPB4Rme3QZlI&address=1263%20Pacific%20Ave.%20Kansas%20City%20KS"
