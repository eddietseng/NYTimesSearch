package com.eddietseng.nytimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eddietseng.nytimessearch.R;
import com.eddietseng.nytimessearch.adapter.ArticleArrayAdapter;
import com.eddietseng.nytimessearch.fragment.ArticleFilterFragment;
import com.eddietseng.nytimessearch.helper.EndlessRecyclerViewScrollListener;
import com.eddietseng.nytimessearch.helper.ItemClickSupport;
import com.eddietseng.nytimessearch.helper.SpacesItemDecoration;
import com.eddietseng.nytimessearch.model.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity
        implements ArticleFilterFragment.ArticleFilterDialogListener{
    RecyclerView rvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    String query;
    HashMap<String,String> filterParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupView();
    }

    public void setupView() {
        rvResults = (RecyclerView)findViewById(R.id.rvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        rvResults.setAdapter(adapter);

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);

        rvResults.setLayoutManager(gridLayoutManager);

        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        rvResults.addItemDecoration(decoration);

        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        if(!isNetworkAvailable()) {
                            Toast.makeText(getApplicationContext(),"Please Check Network...",Toast.LENGTH_LONG).show();
                            return;
                        }

                        // create an intent to display the article
                        Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                        // get the article to display
                        Article article = articles.get(position);
                        // pass in the article into intent
                        intent.putExtra("article", article);
                        //launch the activity
                        startActivity(intent);
                    }
                }
        );

        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });
    }

    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int page) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        //  --> Deserialize API response and then construct new objects to append to the adapter
        //  --> Notify the adapter of the changes
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", "1a7b80d61a344a0aacb114e1f4b4ac02");
        params.put("page", page);
        params.put("q", query);

        if(filterParams != null) {
            for(Map.Entry<String,String> s : filterParams.entrySet()) {
                params.put(s.getKey(),s.getValue());
            }
        }

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try{
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJsonArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());

                } catch(JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // Customize searchView text and hint colors
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setLines(1);
        et.setSingleLine(true);
        et.setTextColor(Color.GREEN);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(),"Please Check Network...",Toast.LENGTH_LONG).show();
                    return true;
                }

                setQuery(query);
                // perform query here
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

                RequestParams params = new RequestParams();
                params.put("api-key", "1a7b80d61a344a0aacb114e1f4b4ac02");
                params.put("page", 0);
                params.put("q", query);

                if(filterParams != null) {
                    for(Map.Entry<String,String> s : filterParams.entrySet()) {
                        params.put(s.getKey(),s.getValue());
                    }
                }

                client.get(url, params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        JSONArray articleJsonResults = null;

                        try{
                            // Remember to CLEAR OUT old items before appending in the new ones
                            adapter.clear();

                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            articles.addAll(Article.fromJsonArray(articleJsonResults));
                            adapter.notifyDataSetChanged();
                            Log.d("DEBUG", articles.toString());

                        } catch(JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                // workaround to avoid issues with some emulators and keyboard devices
                // firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showEditDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ArticleFilterFragment editNameDialogFragment = ArticleFilterFragment.newInstance("Search Filter");
        editNameDialogFragment.show(fm, "fragment_article_filter");
    }

    @Override
    public void onFinishEditDialog(HashMap<String,String> params) {
        filterParams = params;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
