package com.odownard.symptomlogger;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.odownard.symptomlogger.SymptomManager.SymptomManager;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements QuickSymptomsFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener, HistoryFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    //private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;


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
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close){
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
                .replace(R.id.container, QuickSymptomsFragment.newInstance(0))
                .addToBackStack("Home")
                .commit();
        getSupportActionBar().setTitle(R.string.title_section1);

        //SymptomManager.getInstance().addSymptom(getContentResolver(), "Knee Dislocation", "Knee has been dislocated");
        //SymptomManager.getInstance().addSymptom(getContentResolver(), "Wrist Dislocation", "Wrist has been dislocated");
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
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
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
    public void onEpisodeLog(int id, CharSequence name) {
        long datetime = Calendar.getInstance().getTimeInMillis();

        Bundle data = new Bundle();
        data.putInt("ID", id + 1); //The +1 is to make the Ids line up correctly to those in the DB
        data.putLong("Datetime", datetime);
        data.putCharSequence("Name", name);
        SymptomDialogFragment dialogFragment = new SymptomDialogFragment();
        dialogFragment.setArguments(data);
        dialogFragment.show(getSupportFragmentManager(), "New Episode");
        //SymptomManager.getInstance().addEpisode(getContentResolver(), datetime, id);
        //Date d = new Date();
        //d.setTime(datetime);
        //Log.v(TAG,"Logging Episode with Time: " + d.toString() );
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
                        .replace(R.id.container, QuickSymptomsFragment.newInstance(0))
                        .addToBackStack("Home")
                        .commit();
                mToolbar.setTitle(R.string.title_section1);
                break;
            case R.id.History:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HistoryFragment.newInstance())
                        .addToBackStack("History")
                        .commit();
                mToolbar.setTitle(R.string.title_section2);
                break;
            case R.id.Options:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(1))
                        .addToBackStack("Options")
                        .commit();
                mToolbar.setTitle(R.string.title_section3);
                break;

        }

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.v(TAG,"DING DONG");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
