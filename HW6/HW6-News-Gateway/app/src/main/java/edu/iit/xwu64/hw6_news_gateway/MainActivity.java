package edu.iit.xwu64.hw6_news_gateway;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    static final String ACTION_NEWS_SOURCE = "ACTION_NEWS_SOURCE";
    static final String REQUEST_ARTICLES = "REQUEST_ARTICLES";
    static final String RESPONSE_ARTICLES = "RESPONSE_ARTICLES";

    private NewsReceiver newsReceiver;

    private ArrayList<NewsSource> sourceList = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private List<Fragment> fragments;
    private MyPageAdapter pageAdapter;
    private ViewPager pager;
    private AsyncArticleLoader aal;

    private ArrayAdapter<NewsSource> newsSourceArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create service
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);

        newsReceiver = new NewsReceiver();

        registerReceiver(newsReceiver, new IntentFilter(RESPONSE_ARTICLES));
        registerReceiver(newsReceiver, new IntentFilter(REQUEST_ARTICLES));




        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open, R.string.drawer_close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        AsyncSourceLoader asl = new AsyncSourceLoader(this);
        asl.execute("");

        newsSourceArrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_list_item, sourceList);
        mDrawerList.setAdapter(newsSourceArrayAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectSource(position);
            }
        });

        fragments = new ArrayList<Fragment>();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

//        Article test = new Article("a", "b", "c", "google.com", "https://developer.android.com/images/fragment_lifecycle.png?hl=zh-cn", "aT", 3,1);
//        fragments.add( ArticleFragment.newInstance(test));

        if (savedInstanceState != null){
            fragments = (List<Fragment>) savedInstanceState.getSerializable("fragments");
            setTitle(savedInstanceState.getString("title"));
            pageAdapter.notifyDataSetChanged();
            for (int i = 0; i< pageAdapter.getCount(); i++) pageAdapter.notifyChangeInPosition(i);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(newsReceiver);
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        stopService(intent);
        super.onDestroy();
    }

    //TODO: avoid down
    private void selectSource(int position) {
        Toast.makeText(this, sourceList.get(position).getName(), Toast.LENGTH_SHORT).show();
        setTitle(sourceList.get(position).getName());
//        if (aal != null) aal.cancel(true);
//
//        aal = new AsyncArticleLoader(this);
//        aal.execute(sourceList.get(position).getId());


        Intent requestIntent = new Intent();
        requestIntent.setAction(MainActivity.REQUEST_ARTICLES);
        requestIntent.putExtra("source", sourceList.get(position).getId());
        sendBroadcast(requestIntent);

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) return true;

        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
//        testList.add("2");
//        newsSourceArrayAdapter.notifyDataSetChanged();

        AsyncSourceLoader asl = new AsyncSourceLoader(this);

        if (item.toString().equals("all")) asl.execute("");
        else asl.execute(item.toString());

        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void addSource(NewsSource newsSource){
        sourceList.add(newsSource);
        newsSourceArrayAdapter.notifyDataSetChanged();
    }

    public void clearSource(){
        sourceList.clear();
    }



    private class MyPageAdapter extends FragmentPagerAdapter{
        private long baseId = 0;
        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            return baseId+position;
        }

        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }
    }

    //TODO:broadcast
    public class NewsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case RESPONSE_ARTICLES:
                    ArrayList<Article> articles = (ArrayList<Article>) intent.getSerializableExtra("articles");
                    fragments.clear();
                    for (int i = 0; i<articles.size(); i++){
                        fragments.add(ArticleFragment.newInstance(articles.get(i)));
                        pageAdapter.notifyChangeInPosition(i);
                    }
                    pageAdapter.notifyDataSetChanged();
                    pager.setCurrentItem(0);
//                case REQUEST_ARTICLES:
//                    String s = intent.getStringExtra("test");
//                    Toast.makeText(MainActivity.this, "Broadcast Message req Received: " + s, Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("fragments", (Serializable) fragments);
        outState.putString("title",getTitle().toString());
        super.onSaveInstanceState(outState);
    }

}
