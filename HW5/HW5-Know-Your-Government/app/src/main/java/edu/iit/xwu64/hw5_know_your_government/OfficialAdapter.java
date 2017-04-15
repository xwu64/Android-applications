package edu.iit.xwu64.hw5_know_your_government;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xiaoliangwu on 2017/4/5.
 */
public class OfficialAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private List<Official>  officialList;
    private MainActivity mainAct;

    public OfficialAdapter(List<Official> officialList, MainActivity mainAct) {
        this.officialList = officialList;
        this.mainAct = mainAct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent, false);
        view.setOnClickListener(mainAct);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Official official = officialList.get(position);
        if (official.getParty() == null) holder.row_name.setText(official.getName());
        else holder.row_name.setText(official.getName()+'('+official.getParty()+')');
        holder.row_office.setText(official.getOfficeName());
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
