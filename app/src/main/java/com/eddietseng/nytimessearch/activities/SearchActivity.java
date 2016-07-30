package com.eddietseng.nytimessearch.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.eddietseng.nytimessearch.R;
import com.eddietseng.nytimessearch.adapter.ArticleArrayAdapter;
import com.eddietseng.nytimessearch.helper.ItemClickSupport;
import com.eddietseng.nytimessearch.model.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    RecyclerView rvResults;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

//1a7b80d61a344a0aacb114e1f4b4ac02
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
        rvResults.setLayoutManager(new GridLayoutManager(this, 4));

        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
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
        et.setTextColor(Color.GREEN);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

                RequestParams params = new RequestParams();
                params.put("api-key", "1a7b80d61a344a0aacb114e1f4b4ac02");
                params.put("page", 0);
                params.put("q", query);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
