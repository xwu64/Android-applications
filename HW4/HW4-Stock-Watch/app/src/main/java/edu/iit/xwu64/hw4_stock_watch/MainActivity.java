package edu.iit.xwu64.hw4_stock_watch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static String urlPrefix="http://www.marketwatch.com/investing/stock/";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swiper;
    private List<Stock> stockList = new ArrayList<>();
    private StockAdapter stockAdapter;
    private DatabaseHandler databaseHandler;
    private ConnectivityManager cm;
    private static MainActivity ma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ma = this;

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        stockAdapter = new StockAdapter(this, stockList);
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stockAdapter.notifyDataSetChanged();

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (networkCheck() == false || stockList.size() == 0) {
                    swiper.setRefreshing(false);
                    return;
                }

                for (Stock stock : stockList){
                    AsyncUpdateLoader aul = new AsyncUpdateLoader(ma);
                    aul.execute(stock);
                }
                swiper.setRefreshing(false);
            }
        });

        databaseHandler = new DatabaseHandler(this);
        if (networkCheck() == false) {
            return;
        }
        ArrayList<String[]> stocks = databaseHandler.loadStocks();

        for(String[] tmp: stocks){
            Stock s = new Stock(tmp[0], tmp[1]);
            AsyncDataLoader adl = new AsyncDataLoader(this);
            adl.execute(s);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.addStock:

                addButton();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addButton(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter stock");
        builder.setTitle("Add Stock");
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText inputTextView = (EditText) view.findViewById(R.id.inputTextView);
                String input = inputTextView.getText().toString();

                if (networkCheck()){
                    AsyncSymbolLoader asl = new AsyncSymbolLoader(ma);
                    asl.execute(input);
                }
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

    @Override
    public void onClick(View v) {
        final int pos = recyclerView.getChildAdapterPosition(v);
        Stock stock = stockList.get(pos);
        String url = urlPrefix+stock.getSymbol();
        Intent i = new Intent((Intent.ACTION_VIEW));
        i.setData(Uri.parse(url));
        startActivity(i);

    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildAdapterPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this stock?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("position: ", ""+pos);

                databaseHandler.deleteStock(stockList.get(pos).getSymbol());
                stockList.remove(pos);
                stockAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return false;
    }

    public Boolean networkCheck(){
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo !=null && netInfo.isConnectedOrConnecting())return true;
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Network Error");
            builder.setMessage("Cannot connect to internet");
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
    }

    public void addData(Stock stock){
        for(Stock each: stockList){
            if(each.getSymbol().equals(stock.getSymbol())) return;
        }
        stockList.add(stock);
        Collections.sort(stockList, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return o1.getSymbol().compareTo(o2.getSymbol());
            }
        });
        databaseHandler.addStock(stock);
        stockAdapter.notifyDataSetChanged();
    }

    public void updeData(Stock stock) {
        int i=0;
        for(Stock each: stockList){
            if (each.getSymbol().equals(stock.getSymbol())) break;
            i++;
        }
        stockList.remove(i);
        stockList.add(i,stock);
        stockAdapter.notifyDataSetChanged();
    }
}
