package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.adapter.DictionaryAdapter;
import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DictionaryHelper;
import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.model.ModelDictionary;
import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.pref.AppPreference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    DictionaryAdapter adapter;
    DictionaryHelper helper;
    AppPreference preference;
    String title="";
    String subtitle= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.words_container);
        helper = new DictionaryHelper(this);
        adapter = new DictionaryAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        preference = new AppPreference(getApplicationContext());
        String pref = preference.getState();
Log.d("TAG:",pref);
        if(pref.equalsIgnoreCase("en")) {
            title = "Dictionary ";
            subtitle = "English to Indonesia";
            helper.open();
            ArrayList<ModelDictionary> dictionaries = helper.getAllEnglishData();
            helper.close();
            adapter.setData(dictionaries);

        }else if(pref.equalsIgnoreCase("id")){
            title = "Kamus ";
            subtitle = "Indonesia ke Inggris";
            helper.open();
            ArrayList<ModelDictionary> dictionaries = helper.getAllEnglishData();
            helper.close();
            adapter.setData(dictionaries);

        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title+" - "+subtitle);
        }
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                AppPreference preference = new AppPreference(getApplicationContext());
                String pref = preference.getState();
                showDescription(adapter.getItemId(position),pref);
            }
        });
    }

    private void showDescription(long itemId,String pref) {
        helper.open();
        ModelDictionary model = new ModelDictionary();
        model=helper.getDetail(itemId,pref);

        Intent moveToDetail = new Intent(MainActivity.this,DescriptionActivity.class);
        moveToDetail.putExtra(DescriptionActivity.EXTRAS_DATA, model);
        startActivity(moveToDetail);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.hint_search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    AppPreference preference = new AppPreference(getApplicationContext());
                    String pref = preference.getState();

                    helper.open();
                    ArrayList<ModelDictionary> dictionaries = helper.getDataByWord(s,pref);
                    helper.close();

                    adapter.setData(dictionaries);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    AppPreference preference = new AppPreference(getApplicationContext());
                    String pref = preference.getState();

                    helper.open();
                    ArrayList<ModelDictionary> dictionaries = helper.getDataByWord(s,pref);
                    helper.close();

                    adapter.setData(dictionaries);
                    return true;
                }
            });
        }
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.english_to_indonesian) {
            AppPreference preference = new AppPreference(getApplicationContext());
            preference.setState("en");
            helper.open();
            ArrayList<ModelDictionary> dictionaries = helper.getAllEnglishData();
            helper.close();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Kamus - Inggris ke Indonesia");
            }
            adapter.setData(dictionaries);
        } else if (id == R.id.indonesian_to_english) {
            AppPreference preference = new AppPreference(getApplicationContext());
            preference.setState("id");
            helper.open();
            ArrayList<ModelDictionary> dictionaries = helper.getAllIndonesianData();
            helper.close();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Kamus - Indonesia ke Inggris");
            }
            adapter.setData(dictionaries);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
