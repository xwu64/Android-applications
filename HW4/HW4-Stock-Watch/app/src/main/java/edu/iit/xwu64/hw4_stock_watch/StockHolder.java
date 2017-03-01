package edu.iit.xwu64.hw4_stock_watch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

/**
 * Created by xiaoliangwu on 2017/2/27.
 */

public class StockHolder extends RecyclerView.ViewHolder{
    public TextView name;
    public TextView symbol;
    public TextView price;
    public TextView change;

    public StockHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        symbol = (TextView) itemView.findViewById(R.id.symbol);
        change = (TextView) itemView.findViewById(R.id.change);
        price = (TextView) itemView.findViewById(R.id.price);
    }
}
