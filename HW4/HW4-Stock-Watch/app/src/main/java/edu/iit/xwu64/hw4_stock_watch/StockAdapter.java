package edu.iit.xwu64.hw4_stock_watch;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xiaoliangwu on 2017/2/27.
 */

public class StockAdapter extends RecyclerView.Adapter<StockHolder> {

    private List<Stock> stockList;
    private MainActivity mainActivity;

    public StockAdapter(MainActivity mainActivity, List<Stock> stockList) {
        this.mainActivity = mainActivity;
        this.stockList = stockList;
    }

    @Override
    public StockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_row, parent, false);
        view.setOnLongClickListener(mainActivity);
        view.setOnClickListener(mainActivity);
        return new StockHolder(view);
    }

    @Override
    public void onBindViewHolder(StockHolder holder, int position) {
        Stock stock = stockList.get(position);
        holder.name.setText(stock.getName());
        holder.price.setText(String.valueOf(stock.getPrice()));
        holder.symbol.setText(stock.getSymbol());
        if (stock.getPriceChange() > 0)
        {
            holder.change.setText("▲ "+String.valueOf(stock.getPriceChange())+"("+String.valueOf(stock.getChangePercent())+"%)");
            holder.name.setTextColor(Color.GREEN);
            holder.price.setTextColor(Color.GREEN);
            holder.symbol.setTextColor(Color.GREEN);
            holder.change.setTextColor(Color.GREEN);
        }else{
            holder.change.setText("▼ "+String.valueOf(stock.getPriceChange())+"("+String.valueOf(stock.getChangePercent())+"%)");
            holder.name.setTextColor(Color.RED);
            holder.price.setTextColor(Color.RED);
            holder.symbol.setTextColor(Color.RED);
            holder.change.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
