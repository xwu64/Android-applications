package edu.iit.xwu64.hw5_know_your_government;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by xiaoliangwu on 2017/4/5.
 */
public class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView row_name;
    public TextView row_office;

    public MyViewHolder(View itemView) {
        super(itemView);
        row_name = (TextView) itemView.findViewById(R.id.row_nameText);
        row_office = (TextView) itemView.findViewById(R.id.row_officeText);
    }
}
