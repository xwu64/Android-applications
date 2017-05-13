package edu.iit.xwu64.hw6_news_gateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.jar.Manifest;

/**
 * Created by xiaoliangwu on 2017/4/21.
 */

public class NewsService extends Service{
    private String source;
    private ArrayList<Article> articles;
    private ServiceReceiver serviceReceiver;
    private boolean running = true;
    private NewsService newsService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public NewsService() {
        Log.d("service","generator");
        newsService = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service","on start command");
        articles = new ArrayList<Article>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("service", "thread");
                serviceReceiver = new ServiceReceiver();
                registerReceiver(serviceReceiver, new IntentFilter(MainActivity.REQUEST_ARTICLES));

                while (running){
                    if (articles.size() == 0 || articles.size() != articles.get(0).getTotal()){
                        try {
                            Thread.sleep(250);
                            continue;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Intent responseIntent = new Intent();
                        responseIntent.setAction(MainActivity.RESPONSE_ARTICLES);
                        responseIntent.putExtra("articles", articles);
                        sendBroadcast(responseIntent);
                        articles.clear();
                    }
                }
            }
        }).start();
        return START_STICKY;
    }

    public void addArticle(Article article){
        articles.add(article);
    }


    public class ServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            AsyncArticleLoader asyncArticleLoader = new AsyncArticleLoader(newsService);
            asyncArticleLoader.execute(intent.getStringExtra("source"));
        }
    }
}
