package edu.iit.xwu64.hw6_news_gateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

/**
 * Created by xiaoliangwu on 2017/4/19.
 */

public class ArticleFragment extends Fragment implements Serializable{
    public static final ArticleFragment newInstance(Article article){
        ArticleFragment f = new ArticleFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("article", article);
        f.setArguments(bdl);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final Article article;
        if (savedInstanceState == null) article = (Article) getArguments().getSerializable("article");
        else article = (Article) savedInstanceState.getSerializable("article");
        View v = inflater.inflate(R.layout.fragment_article, container, false);

        TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);
        TextView authorTexView = (TextView) v.findViewById(R.id.authorTextView);
        TextView dateTextView = (TextView) v.findViewById(R.id.dateTextView);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.descript);
        TextView indexTextView = (TextView) v.findViewById(R.id.indexTextView);
        final ImageButton imageButton = (ImageButton) v.findViewById(R.id.image);

        titleTextView.setText(article.getTitle());
        authorTexView.setText(article.getAuthor());
        dateTextView.setText(article.getPublishedAt().split("T")[0]);
        descriptionTextView.setText(article.getDescription());
        indexTextView.setText(""+article.getIndex()+" of "+article.getTotal());

        if (article.getUrlToImage() != null){
            Picasso picasso = new Picasso.Builder(v.getContext()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
                    final String changedUrl = article.getUrlToImage().replace("http:", "https:");
                    picasso.load(changedUrl) .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder) .into(imageButton);
                }
            }).build();

            picasso.load(article.getUrlToImage()) .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder) .into(imageButton);
        } else {
            Picasso.with(v.getContext()).load(article.getUrlToImage()) .error(R.drawable.brokenimage).placeholder(R.drawable.missingimage);
        }

        final Intent i = new Intent((Intent.ACTION_VIEW));
        i.setData(Uri.parse(article.getUrl()));
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        descriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("article", getArguments().getSerializable("article"));
    }


}
