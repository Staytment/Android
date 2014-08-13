package de.trottl.staytment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.trottl.staytment.navigation_drawer.model.adapter.NavDrawerListAdapter;
import de.trottl.staytment.navigation_drawer.model.item.NavDrawerItem;

public class MainActivity extends Activity {

    // navigation drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Integer currentViewIndex = null;

    private String[] navMenuTitles;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.initNavigationDrawer(savedInstanceState);
    }

    private void initNavigationDrawer(Bundle savedInstanceState) {
        TypedArray navMenuIcons;
        ArrayList<NavDrawerItem> navDrawerItems;
        NavDrawerListAdapter adapter;

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        navDrawerItems = new ArrayList<>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayView(position);
            }
        });

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//		getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
//				getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
//				getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayView(int viewIndex) {
        if (currentViewIndex == null ||
                currentViewIndex != viewIndex) {
            // update the main content by replacing fragments
            Fragment fragment = null;
            switch (viewIndex) {
                case 0:
                    fragment = new MainFragment();
                    break;
                case 1:
                    fragment = new SettingsFragment();
                    break;
                default:
                    break;
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();

                // update selected item and title, then close the drawer
                mDrawerList.setItemChecked(viewIndex, true);
                mDrawerList.setSelection(viewIndex);
                setTitle(navMenuTitles[viewIndex]);
                mDrawerLayout.closeDrawer(mDrawerList);
                currentViewIndex = viewIndex;
            } else {
                // error in creating fragment
                Log.e("MainActivity", getString(R.string.err));
            }
        } else {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
//		getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

