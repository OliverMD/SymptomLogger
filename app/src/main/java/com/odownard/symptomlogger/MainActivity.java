package com.odownard.symptomlogger;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.odownard.symptomlogger.Adapters.SimpleCursorRecyclerAdapter;
import com.odownard.symptomlogger.DataManager.DataManager;
import com.odownard.symptomlogger.TopLevelViews.Home.MainFragment;
import com.odownard.symptomlogger.TopLevelViews.Home.NewEpisodeDialogFragment;
import com.odownard.symptomlogger.TopLevelViews.Symptoms.SymptomListFragment;
import com.odownard.symptomlogger.TopLevelViews.Tags.TagListFragment;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        SymptomListFragment.OnSymptomListInteractionListener,
        TagListFragment.OnTagListInteractionListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    //private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;


    private CharSequence mTitle;
    static final String TAG = "MAIN ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(0))
                .commit();
        getSupportActionBar().setTitle(R.string.title_section1);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        mToolbar.setTitle(R.string.title_section1);
        super.onPostCreate(savedInstanceState);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                mNavigationView.getMenu().getItem(1).setChecked(true);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                mNavigationView.getMenu().getItem(2).setChecked(true);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                mNavigationView.getMenu().getItem(3).setChecked(true);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    public void onEpisodeLog(long id, CharSequence name, int type) {
        long datetime = Calendar.getInstance().getTimeInMillis();

        Bundle data = new Bundle();
        data.putLong("ID", id); //The +1 is to make the Ids line up correctly to those in the DB
        data.putLong("Datetime", datetime);
        data.putCharSequence("Name", name);
        if (type == SimpleCursorRecyclerAdapter.SYMPTOM_TYPE) {
            NewEpisodeDialogFragment dialogFragment = new NewEpisodeDialogFragment();
            dialogFragment.setArguments(data);
            dialogFragment.show(getSupportFragmentManager(), "New Episode");
        } else {
            DataManager.getInstance().addTagEpisode(getContentResolver(), datetime, id);
            Toast.makeText(getApplicationContext(),"Tag Episode Added!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMainFragmentResume() {
        mNavigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mToolbar.setTitle(title);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if(menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        mDrawerLayout.closeDrawers();
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (menuItem.getItemId()){
            case R.id.Home:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance(0))
                        .addToBackStack("Home")
                        .commit();
                mToolbar.setTitle(R.string.title_section1);
                break;
            case R.id.Symptoms:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SymptomListFragment.newInstance())
                        .addToBackStack("Symptoms")
                        .commit();
                mToolbar.setTitle(R.string.title_section2);
                break;
            case R.id.Tags:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TagListFragment.newInstance())
                        .addToBackStack("Tags")
                        .commit();
                mToolbar.setTitle(R.string.title_section3);
                break;

        }

        return true;
    }

    @Override
    public void onSymptomListResume() {
        mNavigationView.getMenu().getItem(1).setChecked(true);
    }

    @Override
    public void onTagListResume() {
        mNavigationView.getMenu().getItem(2).setChecked(true);
    }
}
