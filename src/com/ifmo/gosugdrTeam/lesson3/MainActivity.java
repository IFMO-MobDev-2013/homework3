package com.ifmo.gosugdrTeam.lesson3;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import com.ifmo.gosugdrTeam.lesson3.background.BackgroundPictureFinder;
import com.ifmo.gosugdrTeam.lesson3.background.BackgroundWordTranslator;

public class MainActivity extends ActionBarActivity implements
        ActionBar.TabListener, SearchView.OnQueryTextListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Request request = new DemoRequest("Find");
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
                getSupportFragmentManager());

        final ActionBar actionBar = getActionBar();

        actionBar.setHomeButtonEnabled(false);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                    .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onQueryTextChange(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String arg0) {
        new BackgroundPictureFinder() {
            protected void onPostExecute(final String answer) {
                new Request() {
                    void set() {
                        translation = answer;
                    }
                }.set();
                mAppSectionsPagerAdapter.updateData();
            }
        }.execute(arg0);

        new BackgroundWordTranslator() {
            protected void onPostExecute(final String[] answer) {
                new Request() {
                    void set() {
                        images = answer;
                    }
                }.set();
                mAppSectionsPagerAdapter.updateData();
            }
        }.execute(arg0);
        Request.query = arg0;
        return false;
    }

}
