package edu.iit.xwu64.hw6_news_gateway;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by xiaoliangwu on 2017/4/19.
 */

public class ArticleFragment extends Fragment {
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
        final Article article = (Article) getArguments().getSerializable("article");
        View v = inflater.inflate(R.layout.fragment_article, container, false);

        TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);
        TextView authorTexView = (TextView) v.findViewById(R.id.authorTextView);
        TextView dateTextView = (TextView) v.findViewById(R.id.dateTextView);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.descript);
        TextView indexTextView = (TextView) v.findViewById(R.id.indexTextView);
        ImageButton imageButton = (ImageButton) v.findViewById(R.id.image);

        titleTextView.setText(article.getTitle());
        authorTexView.setText(article.getAuthor());
        dateTextView.setText(article.getPublishedAt());
        descriptionTextView.setText(article.getDescription());
        indexTextView.setText(""+article.getIndex()+" of "+article.getTotal());

        if (article.getUrlToImage() != null){
            Picasso picasso = new Picasso.Builder(v.getContext()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
                    final String changedUrl = article.getUrlToImage().replace("http:", "https:");
                    picasso.load(changedUrl) .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder);
                }
            }).build();

            picasso.load(article.getUrlToImage()) .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder);
        } else {
            Picasso.with(v.getContext()).load(article.getUrlToImage()) .error(R.drawable.brokenimage).placeholder(R.drawable.missingimage);
        }

        return v;
    }


    //TODO: add click to website
    //TODO: show photo

}
