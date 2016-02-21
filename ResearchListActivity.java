package com.bucs.bupc.art.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bucs.bupc.art.R;
import com.bucs.bupc.art.adapter.BookResearchAdapter;
import com.bucs.bupc.art.constant.ApplicationConstant;
import com.bucs.bupc.art.dao.BookResearchDAO;
import com.bucs.bupc.art.entity.BookResearch;
import com.bucs.bupc.art.model.YearResearch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author John Paul Cas
 *
 * Connect <i> John Paul Cas </i> on facebook <br />
 * facebook.com/cassie.next <br />
 * facebook.com/simplePlan.cas
 */
public class ResearchListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final CharSequence SEARCH_BY = "Department | Title";
    private Toolbar mToolBar;
    private ListView mListView;
    private TextView mListEmptyText;

    private String mYearResearch;
    private List<String> mResearchList;
    private BookResearchAdapter adapter;
    private BookResearchDAO mResearchDAO;
    private List<BookResearch> mBookResearchList;
    private SearchView mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_list);

        mToolBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolBar);

        Intent data = getIntent();
        if (data != null) { mYearResearch = data.getStringExtra(ApplicationConstant.RESEARCH_YEAR_KEY); }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("" + mYearResearch);

        mListEmptyText = (TextView) findViewById(R.id.research_list_empty);
        mResearchDAO = new BookResearchDAO(this);
        mBookResearchList = new ArrayList<>();

        try {
            mBookResearchList = mResearchDAO.findAllByCopyrightYear(Integer.parseInt(mYearResearch));
        } catch (SQLException e) { /*Do nothing*/ }

        adapter = new BookResearchAdapter(this, mBookResearchList);
        mListView = (ListView) findViewById(R.id.research_list);
        mListView.setAdapter(adapter);
        mListView.setVisibility((adapter.isEmpty()) ? View.GONE : View.VISIBLE);
        mListEmptyText.setVisibility((adapter.isEmpty()) ? View.VISIBLE : View.GONE);
    }

    private List<BookResearch> getData() {
        List<BookResearch> item = new ArrayList<>();
        try {
            item =  mResearchDAO.findAllByCopyrightYear(Integer.parseInt(mYearResearch));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return item;
    }

    /**
     * filter the item in the list
     *
     * @param bookResearchList the item to be filtered
     * @param text the name of item to be search
     * @return the list of item filtered
     */
    private List<BookResearch> filter(List<BookResearch> bookResearchList, String text) {
        text = text.toLowerCase();
        final List<BookResearch> filterBookResearch = new ArrayList<>();

        for (BookResearch bookItem : bookResearchList) {
            String departmentName = bookItem.getDepartment().toLowerCase();
            String bookTitle = bookItem.getResearchTitle().toLowerCase();
            if(departmentName.contains(text) || bookTitle.contains(text) ) {
                filterBookResearch.add(bookItem);
            }
        }

        return filterBookResearch;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.research_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.search_research);
        mSearch = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearch.setQueryHint(SEARCH_BY);
        mSearch.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mBookResearchList = new ArrayList<BookResearch>();
                mBookResearchList = getData();
                adapter.setFilter(mBookResearchList);

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) { return true; }
        });

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<BookResearch> filterBookResearch = filter(getData(), newText);
        this.mBookResearchList = new ArrayList<>();
        this.mBookResearchList.addAll(filterBookResearch);
        adapter.setFilter(this.mBookResearchList);

        return true;
    }
}
