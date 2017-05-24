package filmsoc.wscandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

//TODO: create splash screen (somehow...)
//TODO: create updaterManager class
//TODO: update permissions to access internet (maybe read and write storage...)
//TODO: finish home Activity
//TODO: create aboutUs Activity
//TODO: create getInvolved Activity
//TODO: finish login manager
//TODO: create login Activity
//TODO: create rota Activity - permissions = trainee steward up (not M or P)?
//TODO: create showNumbers Activity - permissions = trainee steward up (not M or P)?
//TODO: create membersEditor Activity - permissions = DM up?
//TODO: create socialStatus Activity - permissions = DM up?
//TODO: create socials Activity - permissions = all?
//TODO: create contacts Activity - permissions = all?
//TODO: create settings Activity
//TODO: create share action (somehow...)
//TODO: create help action (somehow...)

/**
 *  This class is the display class for the home screen. On this page, a film banner is at the top of
 *  the page (info updates in a class inside this class, and updated via updateManager). As well as
 *  this, next film, (and if logged in) next steward, proj and/or DM shows will appear at the bottom.
 *  If extended, favourited films will also appear here.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences preferences;
    private NavigationView navigationView;
    private View navigationHeader;
    private int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TextView nextFilmBanner;
    private TextView dayValue;
    private TextView hourValue;
    private TextView minuteValue;
    private TextView secondValue;
    private int viewPagerItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        navigationView.setCheckedItem(R.id.nav_home);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = findViewById(R.id.fragImg).getHeight();
                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) findViewById(R.id.imgGal).getLayoutParams();
                layoutParams.height = height;
                findViewById(R.id.imgGal).setLayoutParams(layoutParams);
            }
        });
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 0: ((RadioGroup) findViewById(R.id.viewPagerRadioGroup)).check(R.id.viewPageButton1); break;
                    case 1: ((RadioGroup) findViewById(R.id.viewPagerRadioGroup)).check(R.id.viewPageButton2); break;
                    case 2: ((RadioGroup) findViewById(R.id.viewPagerRadioGroup)).check(R.id.viewPageButton3); break;
                    case 3: ((RadioGroup) findViewById(R.id.viewPagerRadioGroup)).check(R.id.viewPageButton4); break;
                    case 4: ((RadioGroup) findViewById(R.id.viewPagerRadioGroup)).check(R.id.viewPageButton5); break;
                    default: ((RadioGroup) findViewById(R.id.viewPagerRadioGroup)).clearCheck();
                }
                viewPagerItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        UpdateManager.setData();

        nextFilmBanner = (TextView) findViewById(R.id.nextFilmBannerText);
        dayValue = (TextView) findViewById(R.id.dayValue);
        hourValue = (TextView) findViewById(R.id.hourValue);
        minuteValue = (TextView) findViewById(R.id.minuteValue);
        secondValue = (TextView) findViewById(R.id.secondValue);

        //TODO: place in separate, recursive method?
        if (ScheduleManager.getNextFilms().length == 0) {
            View nextFilmGroup = findViewById(R.id.nextFilmBannerGroup);
            if (nextFilmGroup != null) {
                nextFilmGroup.setVisibility(View.GONE);
            }
        } else {
        long milliseconds = ScheduleManager.getNextFilmDate() - System.currentTimeMillis();
        String filmName = ScheduleManager.getNextFilmName();
            if (filmName == null) {
                nextFilmBanner.setText("Films will be coming soon, stay tuned!");
                ((TableLayout) findViewById(R.id.dateNextFilm)).setVisibility(View.INVISIBLE);
            } else {
                nextFilmBanner.setText(Html.fromHtml("The next film, <i>" + filmName + "</i>, is being shown in:"));
                new CountDownTimer(milliseconds, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        int days = Math.round(millisUntilFinished / (1000 * 60 * 60 * 24));
                        int hours = Math.round((millisUntilFinished / (1000 * 60 * 60)) % 24);
                        int minutes = Math.round((millisUntilFinished / (1000 * 60)) % 60);
                        int seconds = Math.round((millisUntilFinished / 1000) % 60);


                        dayValue.setText("" + days);
                        hourValue.setText("" + hours);
                        minuteValue.setText("" + minutes);
                        secondValue.setText("" + seconds);
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        }
    }

    //TODO: Update the Navigation View - used to reflect login/logout
    //TODO: figure out how to highlight the correct one again...
    public void updateNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

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

    //TODO: Add refresh button to this menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
/*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_aboutUs) {
            startActivity(new Intent(this, AboutUsActivity.class));
        } else if (id == R.id.nav_schedule) {
            startActivity(new Intent(this, ScheduleActivity.class));
        } else if (id == R.id.nav_getInvolved) {
            startActivity(new Intent(this, GetInvolvedActivity.class));
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationView();
        navigationView.setCheckedItem(R.id.nav_home);
    }
}
