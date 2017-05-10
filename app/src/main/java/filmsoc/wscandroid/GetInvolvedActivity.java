package filmsoc.wscandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class GetInvolvedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SharedPreferences preferences;
    private NavigationView navigationView;
    private View navigationHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_nav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationHeader = navigationView.getHeaderView(0);
        //TODO: remove this refresh button
        navigationHeader.findViewById(R.id.refreshImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getAlpha() == 1.0f) {
                    v.setAlpha(0.25f);
                } else {
                    v.setAlpha(1.0f);
                }
            }
        });
        updateNavigationView();
        navigationView.setCheckedItem(R.id.nav_getInvolved);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Getting Involved",
                        "Front Of House",
                        "Projection",
                        "Publicity",
                        "Marketing",
                        "IT"
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_about_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
/*        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    public void updateNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_getInvolved);

        if (preferences.getBoolean("loggedIn", false)) {
            if (navigationView.getHeaderCount() < 1) {
                navigationView.addHeaderView(navigationHeader);
            }

            if (navigationView.getMenu().findItem(R.id.nav_login).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            }
            if (!navigationView.getMenu().findItem(R.id.nav_rota).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_rota).setVisible(true);
            }
            if (!navigationView.getMenu().findItem(R.id.nav_showNum).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_showNum).setVisible(true);
            }
            if (!navigationView.getMenu().findItem(R.id.nav_memEditors).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_memEditors).setVisible(true);
            }
            if (!navigationView.getMenu().findItem(R.id.nav_showStatus).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_showStatus).setVisible(true);
            }
            if (!navigationView.getMenu().findItem(R.id.nav_social).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_social).setVisible(true);
            }
            if (!navigationView.getMenu().findItem(R.id.nav_contacts).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_contacts).setVisible(true);
            }
            if (!navigationView.getMenu().findItem(R.id.nav_logout).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            }
        } else {
            if (navigationView.getHeaderCount() > 0) {
                navigationView.removeHeaderView(navigationHeader);
            }

            if (!navigationView.getMenu().findItem(R.id.nav_login).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            }
            if (navigationView.getMenu().findItem(R.id.nav_rota).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_rota).setVisible(false);
            }
            if (navigationView.getMenu().findItem(R.id.nav_showNum).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_showNum).setVisible(false);
            }
            if (navigationView.getMenu().findItem(R.id.nav_memEditors).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_memEditors).setVisible(false);
            }
            if (navigationView.getMenu().findItem(R.id.nav_showStatus).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_showStatus).setVisible(false);
            }
            if (navigationView.getMenu().findItem(R.id.nav_social).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_social).setVisible(false);
            }
            if (navigationView.getMenu().findItem(R.id.nav_contacts).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_contacts).setVisible(false);
            }
            if (navigationView.getMenu().findItem(R.id.nav_logout).isVisible()) {
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            }
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_aboutUs) {
            startActivity(new Intent(this, AboutUsActivity.class));
        } else if (id == R.id.nav_schedule) {
            startActivity(new Intent(this, ScheduleActivity.class));
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_login) {
            //Swap nav bar for logged in version and update settings class
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("loggedIn", true);
            drawer.closeDrawer(GravityCompat.START);
            editor.commit();
            updateNavigationView();
        } else if (id == R.id.nav_logout) {
            //Swap nav bar for standard version and update settings class
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("loggedIn", false);
            drawer.closeDrawer(GravityCompat.START);
            editor.commit();
            updateNavigationView();
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
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

        public PlaceholderFragment() {
        }

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            switch(getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1: rootView = inflater.inflate(R.layout.fragment_gettinginvolved_overall, container, false);
                    break;
                case 2: rootView = inflater.inflate(R.layout.fragment_gettinginvolved_foh, container, false);
                    break;
                case 3: rootView = inflater.inflate(R.layout.fragment_gettinginvolved_proj, container, false);
                    break;
                case 4: rootView = inflater.inflate(R.layout.fragment_gettinginvolved_pub, container, false);
                    break;
                case 5: rootView = inflater.inflate(R.layout.fragment_gettinginvolved_marketing, container, false);
                    break;
                case 6: rootView = inflater.inflate(R.layout.fragment_gettinginvolved_it, container, false);
                    break;
                default: rootView = inflater.inflate(R.layout.fragment_about_us_default, container, false);
                    TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                    break;
            }
            return rootView;
        }
    }
}
