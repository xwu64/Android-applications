package edu.iit.xwu64.hw3_multi_notepad;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by xiaoliangwu on 2017/2/16.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView date;
    public TextView content;

    public MyViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.cardTitle);
        date = (TextView) view.findViewById(R.id.cardDate);
        content = (TextView) view.findViewById(R.id.cardContent);
    }
}
