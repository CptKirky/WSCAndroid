package filmsoc.wscandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
/**
 *  This class is the display class for the schedule activity. This holds all logic for DISPLAYING
 *  the data. Most (if not all) processing is done within the ScheduleManager class. The activity
 *  consists of a list of films, and a header allowing further films to be collected from the
 *  ScheduleManager. This class SHOULD NOT talk directly to the UpdateManager
 */
public class ScheduleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences preferences;  //Preferences set in the settings menu
    private NavigationView navigationView;  //The navigation panel view
    private View navigationHeader;          //The navigation header view
    private ListView scheduleListView;      //The view showing the list
    private ScheduleAdapter scheduleAdapter;//The object used by the schedule list view to generate the data in each segment
    private Calendar headerDateAfter;       //The date by which the data retrieved by the header starts
    private Calendar headerDateBefore;      //The date by which the list retrieved by the header ends

    /**
     *  This function creates the initial visuals. It creates the action button, draws the
     *  navigation window, and draws the list (including the header).
     * @param savedInstanceState This is the core data on the state of the program
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Generic setting up
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inherit preferences from preference manager
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Draw the navigation draw and action bar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Draw navigation view and update view depending on whether the user is logged in.
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationHeader = navigationView.getHeaderView(0);
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
        navigationView.setCheckedItem(R.id.nav_schedule);

        //Set up the list view and adapter
        scheduleListView = (ListView) findViewById(R.id.scheduleListView);
        scheduleAdapter = new ScheduleAdapter(this);
        scheduleListView.setAdapter(scheduleAdapter);
        scheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //When clicked, check the id of the schedule adapter view. If -1, then an error or black element. Otherwise, open the film activity
                if (id == -1) {
                    return;
                }
                parent.getItemAtPosition(position);
                startActivity(new Intent(parent.getContext(), AboutFilm.class).putExtra("filmID", id));
            }
        });
        //Create the header view to the list
        LinearLayout headerView = new LinearLayout(this);
        headerView.setPadding(10,10,10,10);
        headerView.setOrientation(LinearLayout.HORIZONTAL);
        ImageView headerArrowView = new ImageView(this);
        headerArrowView.setImageResource(R.drawable.ic_arrow_upward_black_48dp);
        headerView.addView(headerArrowView);
        final TextView headerTextView = new TextView(this);
        //Set the before and after dates for the header
        if (headerDateAfter == null) {
            headerDateAfter = Calendar.getInstance();
            headerDateAfter.set(headerDateAfter.get(Calendar.YEAR), headerDateAfter.get(Calendar.MONTH), headerDateAfter.get(Calendar.DAY_OF_MONTH)-14);
        }
        //Generate header string
        String headerViewTextString = "View films between ";
        if (headerDateAfter.get(Calendar.DAY_OF_MONTH) < 10) {
            headerViewTextString = headerViewTextString + "0";
        }
        headerViewTextString = headerViewTextString + headerDateAfter.get(Calendar.DAY_OF_MONTH) + "/";
        if (headerDateAfter.get(Calendar.MONTH) < 9) {
            headerViewTextString = headerViewTextString + "0";
        }
        headerViewTextString = headerViewTextString + (headerDateAfter.get(Calendar.MONTH)+1) + "/" + headerDateAfter.get(Calendar.YEAR) + " and now";
        headerTextView.setText(headerViewTextString);
        headerTextView.setPadding(10,0,0,0);
        headerView.addView(headerTextView);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update header string, header before and after dates, and add films to the list
                scheduleListView.invalidateViews();
                scheduleAdapter.addFilms(headerDateBefore, headerDateAfter);
                if (headerDateBefore == null) {
                    headerDateBefore = Calendar.getInstance();
                }
                headerDateBefore.set(headerDateAfter.get(Calendar.YEAR), headerDateAfter.get(Calendar.MONTH), headerDateAfter.get(Calendar.DAY_OF_MONTH));
                headerDateAfter.set(headerDateAfter.get(Calendar.YEAR), headerDateAfter.get(Calendar.MONTH), headerDateAfter.get(Calendar.DAY_OF_MONTH)-14);
                String headerViewTextString = "View films between ";
                if (headerDateAfter.get(Calendar.DAY_OF_MONTH) < 10) {
                    headerViewTextString = headerViewTextString + "0";
                }
                headerViewTextString = headerViewTextString + headerDateAfter.get(Calendar.DAY_OF_MONTH) + "/";
                if (headerDateAfter.get(Calendar.MONTH) < 9) {
                    headerViewTextString = headerViewTextString + "0";
                }
                headerViewTextString = headerViewTextString + (headerDateAfter.get(Calendar.MONTH)+1) + "/" + headerDateAfter.get(Calendar.YEAR) + " and ";
                if (headerDateBefore.get(Calendar.DAY_OF_MONTH) < 10) {
                    headerViewTextString = headerViewTextString + "0";
                }
                headerViewTextString = headerViewTextString + headerDateBefore.get(Calendar.DAY_OF_MONTH) + "/";
                if (headerDateBefore.get(Calendar.MONTH) < 9) {
                    headerViewTextString = headerViewTextString + "0";
                }
                headerViewTextString = headerViewTextString + (headerDateBefore.get(Calendar.MONTH)+1) + "/" + headerDateBefore.get(Calendar.YEAR);
                headerTextView.setText(headerViewTextString);
            }
        });
        //Add header view to list
        scheduleListView.addHeaderView(headerView);
    }

    /**
     *  This function is used mainly to debug, but modifies the navigation menu depending on if the
     *  user has logged in or not
     */
    public void updateNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        if (preferences.getBoolean("loggedIn", false)) {    //If the person has logged in (with the default answer being false)...
            //Add the menu header
            if (navigationView.getHeaderCount() < 1) {
                navigationView.addHeaderView(navigationHeader);
            }

            //Set the correct visibilities
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
        } else {    //If the person hasn't logged in...
            //Remove the header (if one is present)
            if (navigationView.getHeaderCount() > 0) {
                navigationView.removeHeaderView(navigationHeader);
            }

            //Set the correct visibilities
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

    /**
     *  This sets the correct back presses, depending on whether the navigation window is open
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     *  This function controls the actions when a navigation window has been pressed. For the moment,
     *  this also performs the login functions
     * @param item This is the menu item which has been clicked on
     * @return This returns true if executed correctly
     */
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

    /**
     *  This function is called when the activity is brought back from the dead. As such, a check is
     *  done to update the navigation view, and the correct item is checked (although doesn't work
     *  atm)
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationView();
        navigationView.setCheckedItem(R.id.nav_schedule);
    }

    /**
     *  This class manages the list of items in the list view. It not only manages the views and
     *  ID's, but it allows for films to be added to the "visible schedule". This schedule is a
     *  different version to the one found in the schedule manager, as each object is separated by
     *  the day, not the film ID. For example, assume a film is being shown on 12/05 and 14/05. In
     *  the schedule found in the ScheduleManager class, this would be within 1 entry, with the
     *  "shown" attrribute having 2 entries. In the visible schedule, there would be 2 entries for
     *  the film, each with one entry in the "shown" section. Another example is a film which as 2
     *  showings on 12/05 and one showing on 14/05. In this case, it would exist as one entry in
     *  the ScheduleManager class (as it is one film), and 2 entries in the visible schedule. The
     *  entry for the 12/05 would have 2 entries in the "shown" attribute, and the entry for 14/05
     *  would have 1 entry for the "shown" attribute.

     *  The list is ordered by the day, then the time. In a case where 2 films have the same date
     *  and time, then the order is detemined by there position in the ScheduleManager
     */
    private class ScheduleAdapter extends BaseAdapter {
        private Film[] visibleSchedule; //This stores all the entries shown to the user
        private Context context;        //This stores a copy of the context used in the activity

        /**
         *  This function sets up the activity by getting any upcoming screenings known to the
         *  ScheduleManager, and set the context (needed for creating views)
         * @param context This is the current context of the app
         */
        public ScheduleAdapter(Context context) {
            visibleSchedule = ScheduleManager.splitScreenings(ScheduleManager.getFilmsAfter(Calendar.getInstance()));
            this.context = context;
        }

        /**
         *  This function returns the number of items in the list. If there are no films, then 1 is
         *  returned (as the "no show" element counts :P )
         * @return This returns the number of items in teh list (1 is min, as the no show counts as 1)
         */
        @Override
        public int getCount() {
            return Math.max(visibleSchedule.length, 1);
        }

        /**
         *  This function gets the film object at a particular position. If there are no films being
         *  shown in the list, then null is returned
         * @param position This is the position ID of the item
         * @return The object at the position of the list. If the list is empty, null is returned.
         */
        @Override
        public Object getItem(int position) {
            if (visibleSchedule.length == 0) {
                return null;
            } else {
                return visibleSchedule[position];
            }
        }

        /**
         *  This function gets the ID of the selected film. If there are no films being shown, then
         *  -1 is returned
         * @param position This the position ID of the item
         * @return The ID number of the item. Should the list be emty, then -1 is returned
         */
        @Override
        public long getItemId(int position) {
            if (visibleSchedule.length == 0) {
                return -1;
            } else {
                return visibleSchedule[position].getId();
            }
        }

        /**
         *  This function returns the view to be placed in a given position. This is dependent on
         *  whether there is a view in the visible schedule, and the position of the click/tap.
         * @param position This is the position ID of the item
         * @param convertView This is not used...
         * @param parent This is the parent view for the list. It is not used...
         * @return The returns an approprate view, containf all teh film details. This includes an appropriate view if the list is empty
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //If there is no film in the visible schedule, then return the only view needed...
            if (visibleSchedule.length == 0) {
                TextView noShow = new TextView(context);
                noShow.setText(R.string.schedule_noShow);
                noShow.setPadding(10, 10, 10, 10);
                return noShow;
            }

            //Otherwise, order the visible schedule using the appropriate order function
            visibleSchedule = ScheduleManager.orderSchedule(visibleSchedule);

            //Create the view using a linear layout and a table
            LinearLayout result = new LinearLayout(context);
            result.setOrientation(LinearLayout.VERTICAL);

            TableLayout title = new TableLayout(context);   //Says the name and date
            TableLayout time = new TableLayout(context);    //Says the times for screenings of that film on that day

            TableRow titleRow = new TableRow(context);      //Row containing the title of the film and the date
            titleRow.setPadding(0, 0, 0, 5);
            TableRow timeRow = new TableRow(context);       //Row containing the times of the screenings for the film on that day

            //Set up the title row
            TextView filmNameView = new TextView(context);
            filmNameView.setText(visibleSchedule[position].getName());
            titleRow.addView(filmNameView);
            TextView filmDayView = new TextView(context);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");   //Used to format the date
            filmDayView.setText(dateFormat.format(visibleSchedule[position].getFirstShowing().getTime()));
            filmDayView.setGravity(0x05);       //Right aligned
            titleRow.addView(filmDayView);

            //For each screening...
            for (int i = 0; i < (visibleSchedule[position]).getShownAfter(headerDateAfter).length; i++) {
                //Format the time...
                TextView filmDateView = new TextView(context);
                String timeString = (visibleSchedule[position]).getShownAfter(headerDateAfter)[i].get(Calendar.HOUR) + ":" + (visibleSchedule[position]).getShownAfter(headerDateAfter)[i].get(Calendar.MINUTE);
                //... add a 0 if the screening lands on an hour...
                if ((visibleSchedule[position]).getShownAfter(headerDateAfter)[i].get(Calendar.MINUTE) == 0) {
                    timeString = timeString + 0;
                }
                //... add either an AM or PM depending on the screening time...
                if ((visibleSchedule[position]).getShownAfter(headerDateAfter)[i].get(Calendar.AM_PM) == Calendar.AM) {
                    timeString = timeString + "am";
                } else {
                    timeString = timeString + "pm";
                }

                //... and add it to the view
                filmDateView.setText(timeString);
                filmDateView.setGravity(0x01);      //Centred horizontially (I think...)
                timeRow.addView(filmDateView);
            }

            //Add the views together
            title.addView(titleRow);
            title.setStretchAllColumns(true);   //Make row take up whole width
            time.addView(timeRow);
            time.setStretchAllColumns(true);    //Make row take up whole width
            result.addView(title);
            result.addView(time);
            result.setPadding(10, 10, 10, 10);
            return result;
        }

        /**
         *  This function controls the addition to films to the visible schedule. These are usually
         *  between 2 dates. Should the dates be given in the wrong order, the function will switch
         *  it around.
         * @param beforeDate This is the date before. If "null" is passed, it is assumed to be now.
         * @param afterDate This is the date after .
         */
        public void addFilms(Calendar beforeDate, Calendar afterDate) {
            //If no before date, assume now
            if (beforeDate == null) {
                beforeDate = Calendar.getInstance();
            }
            //If dates wrong way round, swap
            if (afterDate.getTimeInMillis() > beforeDate.getTimeInMillis()) {
                Calendar tmp = beforeDate;
                beforeDate = afterDate;
                afterDate = tmp;
            }

            //Get films to be added
            Film[] additonsSchedule = ScheduleManager.getFilmsBetween(beforeDate, afterDate);
            //Create new schedule and merge additions to current
            Film[] tmpSchedule = new Film[visibleSchedule.length+additonsSchedule.length];
            for (int i = 0; i < visibleSchedule.length; i++) {
                tmpSchedule[i] = visibleSchedule[i];
            }
            for (int i = 0; i < additonsSchedule.length; i++) {
                tmpSchedule[i+visibleSchedule.length] = additonsSchedule[i];
            }
            //Split screenings (making it day dependant)
            visibleSchedule = ScheduleManager.splitScreenings(tmpSchedule);
        }
    }
}
