package com.odownard.symptomlogger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;


public class MainActivity extends AppCompatActivity implements RecordListFragment.RecordListListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);



        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return RecordFragment.newInstance();
                    case 1:
                        return TrackFragment.newInstance();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "Record";
                    case 1:
                        return "Track";
                    default:
                        return "Bugger";
                }
            }

            @Override
            public int getItemPosition(Object object) {
                Log.v("Item Position: ", object.toString());
                return super.getItemPosition(object);
            }


        };
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mViewPager.setAdapter(mFragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        mToolbar.setTitle(R.string.title_section1);
        super.onPostCreate(savedInstanceState);
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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mToolbar.setTitle(title);
    }
    @Override
    public void onBackPressed() {
        if (!returnBackStackImmediate(getSupportFragmentManager())) {
            super.onBackPressed();
        }
    }



    // HACK: propagate back button press to child fragments.
    // This might not work properly when you have multiple fragments adding multiple children to the backstack.
    // (in our case, only one child fragments adds fragments to the backstack, so we're fine with this)
    private boolean returnBackStackImmediate(FragmentManager fm) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                    if (fragment.getChildFragmentManager().popBackStackImmediate()) {
                        return true;
                    } else {
                        return returnBackStackImmediate(fragment.getChildFragmentManager());
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void OnDataChanged() {
    }
}
