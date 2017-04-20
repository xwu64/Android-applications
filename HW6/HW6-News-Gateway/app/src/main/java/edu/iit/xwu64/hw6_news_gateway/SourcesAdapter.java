package edu.iit.xwu64.hw6_news_gateway;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoliangwu on 2017/4/16.
 */

public class SourcesAdapter extends ArrayAdapter<NewsSource> {
    private  ViewHolder viewHolder;
    public static class ViewHolder{
        private TextView sourceTextView;
    }

    public SourcesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<NewsSource> sources) {
        super(context, resource, sources);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.drawer_list_item, parent, false );

            viewHolder = new ViewHolder();
            viewHolder.sourceTextView = (TextView) convertView.findViewById(R.id.sourceTextView);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.sourceTextView.setText(getItem(position).getName());
        return convertView;
    }
}
