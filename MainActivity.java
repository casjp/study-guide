package com.bucs.bupc.art.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bucs.bupc.art.R;
import com.bucs.bupc.art.adapter.RecyclerAdapter;
import com.bucs.bupc.art.constant.ApplicationConstant;
import com.bucs.bupc.art.dao.BookResearchDAO;
import com.bucs.bupc.art.entity.BookResearch;
import com.bucs.bupc.art.model.YearResearch;
import com.bucs.bupc.art.rest.BookResearchRestClient;
import com.bucs.bupc.art.utils.NetworkDetector;
import com.bucs.bupc.art.utils.RecycleTouchListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Paul Cas
 *
 * connect to <i> John Paul Cas </i> on facebook
 * <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 */
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
{

    private Toolbar toolbar;
    private BookResearchDAO mBookResearchDao;
    private Context context;
    private NetworkDetector networkDetector = null;
    private RecyclerView mRecycleView;
    private RecyclerAdapter mRecycleAdapter;
    private SearchView mSearch;
    private List<YearResearch> mResearchYearList;
    private SwipeRefreshLayout mRefresh;

    private static final String SEARCH_TEXT = "Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAppbarComponents();
        initComponents();
    }

    private void initComponents() {
        context = this;
        mResearchYearList = new ArrayList<>();
        mResearchYearList = getData();

        mRecycleAdapter = new RecyclerAdapter(context, mResearchYearList);
        mRecycleView = (RecyclerView) findViewById(R.id.book_research_item);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh_list);
        networkDetector = new NetworkDetector(context);
        mBookResearchDao = new BookResearchDAO(context);

        mRecycleView.setAdapter(mRecycleAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(context));
        mRecycleView.addOnItemTouchListener(new RecycleTouchListener(this, mRecycleView,
                new RecycleTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                YearResearch year = mResearchYearList.get(position);
                Intent i = new Intent(context, ResearchListActivity.class);

                i.putExtra(ApplicationConstant.RESEARCH_YEAR_KEY, year.getYearNameDesc());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        if (networkDetector.isConnected()) {
            try {
                List<BookResearch> bookList = mBookResearchDao.findAll();
                if(bookList.size() <= 0) {
                /* Download data content if bookList is empty */
                    new LoadContent().execute(ApplicationConstant.RESEARCH_URL);
                }
            } catch (SQLException e) {
             /* Do nothing */
            }
        } else {
            /* No internet network */
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
        }

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new UpdateContent().execute(ApplicationConstant.RESEARCH_URL);
            }
        });
    }

    private static List<YearResearch> getData() {
        List<YearResearch> list = new ArrayList<>();
        for (int year = 2013; year <= 2040; year++) {
            YearResearch yearResearch = new YearResearch();
            yearResearch.setYearId(year);
            yearResearch.setYearNameDesc(yearToYearString(year));
            list.add(yearResearch);
        }

        return list;
    }

    private static String yearToYearString(int year) {
        return ""+year;
    }

    private void initAppbarComponents() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(ApplicationConstant.ART);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem menuItem = menu.findItem(R.id.search_book);
        mSearch = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearch.setQueryHint(SEARCH_TEXT);
        mSearch.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(menuItem,
                new MenuItemCompat.OnActionExpandListener() {

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        mResearchYearList = new ArrayList<YearResearch>();
                        mResearchYearList = getData();
                        mRecycleAdapter.setFilter(mResearchYearList);

                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        return true;
                    }
                });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.history:
                startActivity(new Intent(context, History.class));
                break;
            case R.id.about:
                startActivity(new Intent(context, AboutActivity.class));
                break;
         }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        final List<YearResearch> filterYearList = filter(getData(), newText);
        this.mResearchYearList = new ArrayList<>();
        this.mResearchYearList.addAll(filterYearList);
        mRecycleAdapter.setFilter(this.mResearchYearList);

        return true;
    }

    /**
     * Handle filtering by user input in the list
     *
     * @param researchYearList the list to be find in the list
     * @param year the year of research file item
     * @return search filtered list
     */
    private List<YearResearch> filter(List<YearResearch> researchYearList, String year) {
        year = year.toLowerCase();

        final List<YearResearch> filteredYearResearch = new ArrayList<>();
        for (YearResearch yearItem : researchYearList) {
            String text = yearItem.getYearNameDesc().toLowerCase();
            if(text.contains(year)) {
                filteredYearResearch.add(yearItem);
            }
        }

        return filteredYearResearch;
    }

    /**
     * Loading data for first time
     */
    private class LoadContent extends AsyncTask<String, Void, Void> {

        private String url;
        private ProgressDialog progressDialog;
        private BookResearchRestClient bookResearchRestClient;

        @Override
        protected void onPreExecute() {
            url = null;
            progressDialog = ProgressDialog.show(MainActivity.this, null, "Please wait...");
            bookResearchRestClient = new BookResearchRestClient(MainActivity.this);
        }

        @Override
        protected Void doInBackground(String... params) {
            url = params[0];
            try {
                bookResearchRestClient.execute(url);
            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
           if(progressDialog.isShowing()) { progressDialog.dismiss(); }
        }
    }

    /**
     * Call when swife to refresh was called
     */
    private class UpdateContent extends AsyncTask<String, Void, Void> {

        private String url;
        private BookResearchRestClient bookResearchRestClient;

        @Override
        protected void onPreExecute() {
            url = null;
            bookResearchRestClient = new BookResearchRestClient(MainActivity.this);
        }

        @Override
        protected Void doInBackground(String... params) {
            url = params[0];
            try {
                bookResearchRestClient.execute(url);
            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRefresh.setRefreshing(false);
        }
    }
}
