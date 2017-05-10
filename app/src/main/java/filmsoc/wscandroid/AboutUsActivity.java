package filmsoc.wscandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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
        navigationView.setCheckedItem(R.id.nav_aboutUs);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "About Us",
                        "Prices",
                        "Advertise With Us",
                        "The Executive",
                        "The Consitution",
                        "Technical",
                        "FAQs",
                        "Contact Us",
                        "Terms and Conditions"
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
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
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
                case 1: rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
                    WindowManager wm = (WindowManager) rootView.getContext().getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = (int) (size.x*0.9);
                    ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) rootView.findViewById(R.id.aboutUs_SULogo).getLayoutParams();
                    layoutParams.width = width/2;
                    rootView.findViewById(R.id.aboutUs_SULogo).setLayoutParams(layoutParams);
                    layoutParams = (ViewGroup.LayoutParams) rootView.findViewById(R.id.aboutUs_CinemaForAllLogo).getLayoutParams();
                    layoutParams.width = width/2;
                    rootView.findViewById(R.id.aboutUs_CinemaForAllLogo).setLayoutParams(layoutParams);
                    break;
                case 2: rootView = inflater.inflate(R.layout.fragment_about_us_price, container, false);
                    break;
                case 3: rootView = inflater.inflate(R.layout.fragment_about_us_advertise, container, false);
                    break;
                case 4: rootView = inflater.inflate(R.layout.fragment_about_us_exec, container, false);
                    TextView presText = (TextView) rootView.findViewById(R.id.aboutUs_president_text);
                    TextView vicePresText = (TextView) rootView.findViewById(R.id.aboutUs_vicePres_text);
                    TextView tresText = (TextView) rootView.findViewById(R.id.aboutUs_treasurer_text);
                    TextView filmsOffText = (TextView) rootView.findViewById(R.id.aboutUs_filmsOfficer_text);
                    TextView cpText = (TextView) rootView.findViewById(R.id.aboutUs_cp_text);
                    TextView cdmText = (TextView) rootView.findViewById(R.id.aboutUs_cdm_text);
                    TextView pubText = (TextView) rootView.findViewById(R.id.aboutUs_publicity_text);
                    TextView marketingText = (TextView) rootView.findViewById(R.id.aboutUs_marketing_text);
                    TextView itOfficerText = (TextView) rootView.findViewById(R.id.aboutUs_itOfficer_text);
                    TextView techOfficerText = (TextView) rootView.findViewById(R.id.aboutUs_techOfficer_text);
                    TextView eventsOfficerText = (TextView) rootView.findViewById(R.id.aboutUs_eventsCoordinator_text);
                    TextView chiefEditorText = (TextView) rootView.findViewById(R.id.aboutUs_chiefEditor_text);
                    presText.setText(Html.fromHtml("<b>Jonatan Benarroch</b> is the figurehead of WSC and is therefore responsible for ensuring the smooth day-to-day running of its activities. The President normally chairs meetings and carries out general administration work.<br><br>president@warwick.film"));
                    vicePresText.setText(Html.fromHtml("<b>Anjana Radhakrishnan</b> minutes meetings, books rooms and replies to emails.<br><br>vice-president@warwick.film"));
                    tresText.setText(Html.fromHtml("<b>Alexander Holmes</b> manages the finances of the Society. With the help of the Students' Union, the Treasurer budgets and ensures that we are not spending more money than we have!<br><br>treasurer@warwick.film"));
                    filmsOffText.setText(Html.fromHtml("<b>Jacek Kopanski</b> is our contact with our booking agent and film distributors, ordering the films we show and ensuring their timely arrival and departure.<br><br>films-officer@warwick.film"));
                    cpText.setText(Html.fromHtml("<b>Lakshmi Ajay</b> is responsible for making sure there is a projectionist for each show as well as organising the training and administration of projectionists along with the rest of the Projection Committee.<br><br>chiefproj@warwick.film"));
                    cdmText.setText(Html.fromHtml("<b>Matthew Williams</b> is the person responsible for making sure there are enough 'Front of House' staff for each WSC event, and for the administration, qualification and training of Duty Managers and Stewards.<br><br>chiefdm@warwick.film"));
                    pubText.setText(Html.fromHtml("<b>Reece Goodall</b> is responsible for overseeing the production of all WSC publicity material. The Publicity Officer is also in charge of putting film posters up in the cabinets around the Students' Union.<br><br>publicityofficer@warwick.film"));
                    marketingText.setText(Html.fromHtml("<b>Hannah Freeman</b> accepts advertising and maintains the presentations on our LCD screens and before shows. They also update both our Facebook and Twitter profiles.<br><br>marketingofficer@warwick.film"));
                    itOfficerText.setText(Html.fromHtml("<b>Joel Speed</b> is responsible for our website - making sure it is in working order and looking good. It is the IT officers job to ensure that all relevant WSC information is available there. The IT officer is also responsible for any other IT provision that the Society may make, such as use of the Students' Union and IT Services networks.<br><br>itofficer@warwick.film"));
                    techOfficerText.setText(Html.fromHtml("<b>Alex Pointon</b> keeps all of our equipment in good working order. The Technical Officer ensures that any broken equipment is fixed promptly and is responsible for planning upgrades to our facilities before they actually happen.<br><br>techofficer@warwick.film"));
                    eventsOfficerText.setText(Html.fromHtml("<b>Philip Beckett</b> organises and administer regular socials, quiz writer and co-ordinates events. (aka Overlord of Fun!)<br><br>eventsandsocialcoordinator@warwick.film"));
                    chiefEditorText.setText(Html.fromHtml("<b>Dominic Lam</b> is responsible for the design and production of the termly booklet of reviews, along with managing the team of editors who put the pages together.<br><br>chiefeditor@warwick.film"));
                    break;
                case 5: rootView = inflater.inflate(R.layout.fragement_about_us_constitution, container, false);
                    final Spinner constitutionSpinner = (Spinner) rootView.findViewById(R.id.aboutUs_constitution_spinner);
                    constitutionSpinner.setAdapter(new MyAdapter(rootView.getContext(), new String[] {
                            "Main Constitution",
                            "Executive Roles and Responsibilities",
                            "Committees",
                            "Key Allowances and Agreement"
                    }));
                    constitutionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//Set up new fragement here...
                        TextView constitutionText = (TextView) parent.getRootView().findViewById(R.id.aboutUs_constitution_text);
                        switch(position) {
                            case 0: constitutionText.setText(Html.fromHtml("<p><b>Ratified April 2012 </b></p>" +
                                    "<ol><li><h3>1.) Name</h3></li></ol><p>The name of the organisation will be the Warwick Students' Union Film Society, hereinafter referred to as WSC or Warwick Student Cinema.</p>" +
                                    "<ol><li><h3>2.) Mission Statement</h3></li></ol>The objectives of WSC shall be to:\n" +
                                    "<ol><li><br>\t\ta.) Encourage interest in film as an art and a form of entertainment.</li>\n" +
                                    "<li><br>\t\tb.) Provide education and information in the methods of running a cinema, including publicity, marketing, front of house (hereafter referred to as FoH), projection and IT.</li>\n" +
                                    "<li><br>\t\tc.) WSC shall have no stance on any political or religious issues and shall not show films for any other reason than cinematic value.</li>\n" +
                                    "<li><br>\t\td.) Run as a non-profit organization by volunteers.</li>\n" +
                                    "<li><br>\t\te.) Support the national and international Film Society movement.</li></ol>\n" +
                                    "<ol><li><h3>3.) Accompanying Documents</h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) The following documents shall be distributed with the Constitution, and shall be considered to have the equivalent authority of the Constitution:</li>\n" +
                                    "<ol><li><br>\t\t\t\ti.) Executive Roles and Responsibilities</li>\n" +
                                    "<li><br>\t\t\t\tii.) Committees</li>\n" +
                                    "<li><br>\t\t\t\tiii.) Key Allowances and Agreement</li></ol>\n" +
                                    "<li><br>\t\tb.) The Accompanying Documents may be amended by the Executive Committee at their discretion.</li></ol>\n" +
                                    "<ol><li><h3>4.) Membership</h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) The following will be entitled to be members of WSC:</li>\n" +
                                    "<ol><li><br>\t\t\t\ti.) Full members: Current Warwick University Students, who are also members of the Students' Union and the Students' Union Societies Federation.</li>\n" +
                                    "<li><br>\t\t\t\tii.) Associate members: Those persons who have associate membership of both the societies federation and WSC.</li>\n" +
                                    "<li><br>\t\t\t\tiii.) Exec members: Elected officials who are full members of WSC.</li></ol>\n" +
                                    "<li><br>\t\tb.) Membership prices will be set by the Exec before the summer vacation for the following academic year.</li>\n" +
                                    "<li><br>\t\tc.) The rights of a full member of WSC shall include:</li>\n" +
                                    "<ol><li><br>\t\t\t\ti.) Standing for a position on the Exec.</li>\n" +
                                    "<li><br>\t\t\t\tii.) Voting in WSC elections and at all WSC General Meetings, where they will be identified by their student ID number.</li>\n" +
                                    "<li><br>\t\t\t\tiii.)Participating in the running and organising of WSC activities.</li>\n" +
                                    "<li><br>\t\t\t\tiv.) Voting at all WSC meetings where they are a member of the appropriate committee.</li></ol>\n" +
                                    "<li><br>\t\td.) The rights of a WSC associate member shall include:</li>\n" +
                                    "<ol><li><br>\t\t\t\ti.) To participate at a non-decision making level in the activities of WSC.</li>\n" +
                                    "<li><br>\t\t\t\tii.) To bring a guest to any WSC screening.</li>\n" +
                                    "<li><br>\t\t\t\tiii.) Involvement in discussions on WSC business at the discretion of the meeting Chair or Returning Officer.</li>\n" +
                                    "<li><br>\t\t\t\tiv.) Associate members are not entitled to vote at general meetings or elections, however may vote at committee meetings with the consent of the Chair.</li></ol></ol>\n" +
                                    "<ol><li><h3>5.) Exec Members</h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) The day-to-day business of WSC shall be managed by the Exec, elected at the Annual General Meeting, or any subsequent General Meeting called as an election.</li>\n" +
                                    "<li><br>\t\tb.) The Exec positions within WSC shall at all times consist of at least:</li>\n" +
                                    "<ol><li><br>\t\t\t\ti.) President</li>\n" +
                                    "<li><br>\t\t\t\tii.) Treasurer</li></ol>\n" +
                                    "<li><br>\t\tc.) At the discretion of the Executive committee, other Executive roles will be defined, in any number and combination, in the document <i>&ldquo;Executive Roles and Responsibilities&rdquo;</i>.</li>\n" +
                                    "<li><br>\t\td.) The Exec members as defined here and in the above document, must fulfil the appropriate criteria and will carry out their designated job descriptions as detailed in <i>&ldquo;Executive Roles and Responsibilities&rdquo;</i>.</li>\n" +
                                    "<li><br>\t\te.) The Exec may decide upon any matter which has not yet been decided upon by a General Meeting. The Exec shall further be responsible for:</li>\n" +
                                    "<ol><li><br>\t\t\t\ti.) Organising the activities of WSC in such a way as to include the greatest possible number of WSC members.</li>\n" +
                                    "<li><br>\t\t\t\tii.) Directing the expenditure of WSC&rsquo;s funds in a responsible fashion and in line with the aims, objectives and planned activities of WSC.</li>\n" +
                                    "<li><br>\t\t\t\tiii.) Formulating and submitting any additional bids for funds from the standing committees, with detailed justification of the figures contained within the bid.</li>\n" +
                                    "<li><br>\t\t\t\tiv.) Upholding the constitution of WSC and ensuring that its aims and objectives reflect WSC activities.</li>\n" +
                                    "<li><br>\t\t\t\tv.) Effective publicity distribution.</li></ol>\n" +
                                    "<li><br>\t\tf.) If any Exec position is not filled at the Annual General Meeting, or if an Exec member resigns during their Exec term, the Exec are responsible for filling the position either by election or co-option, ensuring all interested parties are given sufficient notice.</li>\n" +
                                    "<li><br>\t\tg.) Any member may have their privileges removed by the Exec. Members may appeal such decisions as outlined in section 11.</li></ol>\n" +
                                    "<ol><li><h3>6.) Committees</h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) The committees of WSC shall be defined in the document <i>&ldquo;Committees&rdquo;</i>, wherein the roles and remits of the various committees shall be enumerated.</span></li>\n" +
                                    "<li><br>\t\tb.) Committees may only be formed by agreement of the Exec and subsequent amendment of <i>&ldquo;Committees&rdquo;</i>.</li>\n" +
                                    "<li><br>\t\tc.) Each committee shall fulfil their approved job descriptions in <i>&ldquo;Committees&rdquo;</i>.</li>\n" +
                                    "<li><br>\t\td.) Committees are able to make day to day decisions on the appropriate areas of WSC, but all decisions may be overturned by the Exec.</li>\n" +
                                    "<li><br>\t\te.) The Exec may set up any temporary committees at their discretion.</li></ol>\n" +
                                    "<ol><li><h3>7.) Committee Meetings</span></h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) The President will chair Exec meetings. If the President will be absent, they must notify the Exec and appoint a temporary Chair. Either the President or the temporary Chair will have the casting vote. Only the President can call an Exec meeting.</li>\n" +
                                    "<li><br>\t\tb.) All other committees will be chaired by the Exec officer responsible or, in their absence, a temporary chair appointed by the Exec officer responsible. Committee meetings may be called at any time; &nbsp;the committee Chair is responsible for ensuring all interested parties are given appropriate notification of meetings, the length of which shall be defined in <i>&ldquo;Committees&rdquo;</i>, but shall not be less than 48 hours.</li>\n" +
                                    "<li><br>\t\tc.) The quorum for an Exec meeting shall be two thirds (rounded up) of its membership. Quorum for any other committee meeting shall be one third (rounded up) of its membership.</li>\n" +
                                    "<li><br>\t\td.) Committee meetings are open to all members of WSC who wish to attend.</li>\n" +
                                    "<li><br>\t\te.) Exec meetings may go into closed session at the discretion of the Chair. All other WSC meetings should be entirely open.</li>\n" +
                                    "<li><br>\t\tf.) The Chair of each meeting has the casting vote.</li>\n" +
                                    "<li><br>\t\tg.) Minutes will be distributed to all committee members within one week. Once they are approved by the committee as accurate, they will be made publicly available on the website, bar Exec closed session.</li></ol>\n" +
                                    "<ol><li><h3>8.) General Meetings</h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) The Exec shall call an Annual General Meeting (AGM) for the purpose of electing the new Exec and outlining plans for the future. The Exec shall give at least fourteen days&rsquo; notice of this meeting, and such notice shall include details of the elections to be held.</li>\n" +
                                    "<li><br>\t\tb.) At the AGM, the President will give a report outlining the activities of WSC over the past year, and the Treasurer will give a report on the financial state of WSC.</li>\n" +
                                    "<li><br>\t\tc.) The quorum for both the AGM and other General Meetings shall be 20 full members, or 1% of the total number of full members, whichever is greater, rounded up.</li>\n" +
                                    "<li><br>\t\td.) The President will chair the General Meetings and has the casting vote. Agenda items must be submitted to the President no less than 24 hours before the meeting and an agenda must be circulated 24 hours before the meeting.</li></ol>\n" +
                                    "<ol><li><h3>9.) Voting Procedure</h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) Voting at any WSC meeting shall be done by a show of hands, unless a secret ballot is requested.</li>\n" +
                                    "<li><br>\t\tb.) The motion shall be carried by a simple majority except where provisions in the constitution or accompanying documents specify otherwise. If abstentions are the majority, the vote is void.</li>\n" +
                                    "<li><br>\t\tc.) The Chair of the meeting shall preside over the counting of the votes, with the exception of elections, where the Returning Officer shall preside.</li></ol>\n" +
                                    "<ol><li><h3>10.) Elections Procedure</h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) Only full members of WSC may vote or stand in elections.</li>\n" +
                                    "<li><br>\t\tb.) Any Exec election will be conducted as a secret ballot by single transferable vote, and all results shall be announced at the end. It is possible for the same candidate to run for more than one Exec position, although they may only hold one Exec position at a time. Anyone standing for more than one position must give a clear preference in their candidacy to the Returning Officer. This will allow the runner up to be announced as the successful candidate where appropriate.</li>\n" +
                                    "<li><br>\t\tc.) An appropriate Exec member (as specified in <i>&ldquo;Executive Roles and Responsibilities&rdquo;</i>) shall act as Returning Officer in all elections unless they are standing, in which case the Exec will nominate a temporary Returning Officer. Any temporary Returning Officer must be a full member of WSC.</li>\n" +
                                    "<li><br>\t\td.) Notice of elections shall be given by the Exec at least one week before the proposed election. Notification of the elections should be e-mailed to all WSC members and posted on the WSC website.</li>\n" +
                                    "<li><br>\t\te.) Nominations should be emailed to the Returning Officer by the close of nominations. Full details, including closing date for nominations, shall be included in the notification of elections.</li>\n" +
                                    "<li><br>\t\tf.) Candidates will be given the option of a five minute hustings speech at the General Meeting and will be required to answer questions from the floor.</li>\n" +
                                    "<li><br>\t\tg.) The outgoing Exec member is obliged to be available for consultation with the officer-elect until the handover of the position is completed. The handover date shall be decided prior to the elections.</li>\n" +
                                    "<li><br>\t\th.) In the event that no nominations are received for an Exec post, then the current holder shall remain in office until the handover and the incoming Exec shall decide on the appropriate course of action.</li></ol>\n" +
                                    "<ol><li><h3>11.) Appeals Process</h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) If any member concerned wishes a decision to be reconsidered, they may appeal against rulings by asking the Exec for a reconsideration of the decision, which shall be done by simple majority.</li>\n" +
                                    "<li><br>\t\tb.) If the member does not agree with the Exec decision, they may appeal to the Societies Officer at the Students&rsquo; Union who will launch an investigation in line with the Union Equal Opportunities Policy.</li></ol>\n" +
                                    "<ol><li><h3>12.) Keys</h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) The issuing of keys shall be co-ordinated by the President in accordance with the document <i>&ldquo;Key Allowances and Agreement&rdquo;</i></li>\n" +
                                    "<li><br>\t\tb.) All members issued with keys must sign the WSC key agreement set out in the document <i>&ldquo;Key Allowances and Agreement&rdquo;</i>.</li>\n" +
                                    "<li><br>\t\tc.) Keys will be administered by the WSC President.</li></ol>\n" +
                                    "<ol><li><h3>13.) The Students&rsquo; Union</h3></li></ol>\n" +
                                    "<ol><li>\t\ta.) WSC, its officers, its funds and all its activities shall be subject to the provisions of the Constitution, Regulations and policy of the University of Warwick Students&rsquo; Union.</li>\n" +
                                    "<li><br>\t\tb.) WSC shall abide by the Union&rsquo;s current Equal Opportunities Policy and Environmental Policy statement, which is included in the Students&rsquo; Union constitution.</li>\n" +
                                    "<li><br>\t\tc.) WSC shall not be wound up except by a resolution of a majority of those present at a Special General Meeting called for that purpose, or by the Students Union. In the event of WSC winding up, it is the wish of WSC that the remaining WSC funds and assets shall be devoted, in consultation with the British Film Institute, to aiding those with similar objectives to WSC.</li></ol>"));
                                break;
                            case 1: constitutionText.setText(Html.fromHtml("<p><b>Last updated: 18th February 2014</b></p>\n" +
                                    "In addition to the President and Treasurer roles mandated in the Main Constitution, the following named roles shall also be members of the Executive Committee:\n" +
                                    "<ol>\n" +
                                    "<li><br>\t\t1.) Vice-President</li>\n" +
                                    "<li><br>\t\t2.) Films Officer</li>\n" +
                                    "<li><br>\t\t3.) Chief Projectionist</li>\n" +
                                    "<li><br>\t\t4.) Chief Duty Manager</li>\n" +
                                    "<li><br>\t\t5.) Publicity Officer</li>\n" +
                                    "<li><br>\t\t6.) Technical Officer</li>\n" +
                                    "<li><br>\t\t7.) IT Officer</li>\n" +
                                    "<li><br>\t\t8.) Marketing Officer</li>\n" +
                                    "<li><br>\t\t9.) Events and Socials Co-ordinator</li>\n" +
                                    "<li><br>\t\t10.) Chief Editor</li>\n" +
                                    "</ol>\n" +
                                    "<br><br>These roles shall have the following responsibilities:\n" +
                                    "<ol>\n" +
                                    "<li><br>\t\t1.) <b>President</b>" +
                                    "<ol><li><br>\t\t\t\t1.) Figurehead of WSC.\n" +
                                    "<li><br>\t\t\t\t2.) Ultimately responsible for coordinating all activities of WSC.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Responsible, along with the Treasurer, for the finance of WSC.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Chairs Exec Meetings, General Meetings, and the Annual General Meeting.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Maintains good contact with Union Sabbatical officers and works to resolve any difficulties which may arise.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Responsible for hosting any special events.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Ensuring good relationships with sponsors and working to attract new ones.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Ensures that WSC as a whole is working effectively.</li>\n" +
                                    "<li><br>\t\t\t\t9.) Responsible for sending out appropriate e-mails to the membership on a weekly basis.</li>\n" +
                                    "<li><br>\t\t\t\t10.) Works with the Vice President to prepare meeting agendas.</li>\n" +
                                    "<li><br>\t\t\t\t11.) Works with the Vice President to moderate and maintain the Exec and Alumni mailing lists.</li>\n" +
                                    "<li><br>\t\t\t\t12.) Responsible for overall Health and Safety requirements.</li>\n" +
                                    "<li><br>\t\t\t\t13.) First port of call for any equal opportunities matters and complaints and should attend the relevant course.</li>\n" +
                                    "<li><br>\t\t\t\t14.) Responsible for co-ordinating key issuing in accordance with the .Key Allowances and Agreements. document.</li>\n" +
                                    "<li><br>\t\t\t\t15.) Works with the Vice President to organise and undertake all other roles and responsibilities.</li>\n" +
                                    "<li><br>\t\t\t\t16.) Shall have knowledge of the code to WSC's safe, and may only distribute the safe code to members that require it.</li>\n" +
                                    "<li><br>\t\t\t\t17.) Responsible, along with the Treasurer and Chief Duty Manager, for ensuring the on-going security of this code, which may include regularly changing the code.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>\t\t2.) <b>Vice-President</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Responsible for taking Exec and general meeting minutes and ensuring they are published.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Responsible for booking all rooms for WSC screenings and meetings.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Second port of call for all equal opportunities matters and should attend the relevant course.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Coordinates the setting up and staffing of all stalls in the Students' Union and other locations.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Works with the President to maintain good relationships with the current sponsors and to attract new ones.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Works to attract appropriate external advertising for the LCD screens, and liases with the Marketing Officer to implement this.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Responsible for notifying the necessary people of all meetings and preparing the agenda with the President.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Responsible for sending out &ldquo;Crew Update&rdquo; emails where necessary.</li>\n" +
                                    "<li><br>\t\t\t\t9.) Works with the President to moderate and maintain the Exec and Alumni mailing lists.</li>\n" +
                                    "<li><br>\t\t\t\t10.) Responsible for answering all e-mails regarding general enquiries from external people or companies.</li>\n" +
                                    "<li><br>\t\t\t\t11.) Answers e-mails regarding getting involved in WSC and provides the appropriate information.</li>\n" +
                                    "<li><br>\t\t\t\t12.) First port of call for other societies to contact WSC.</li>\n" +
                                    "<li><br>\t\t\t\t13.) Shall be the Returning Officer for any elections unless they are standing for the Exec again.</li>\n" +
                                    "<li><br>\t\t\t\t14.) Works with the President to organise and undertake all other roles and responsibilities.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>\t\t3.) <b>Treasurer</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Has a Students. Union photocopying card.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Responsible for all WSC finances on a day to day level and plans finances with the President.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Pays all invoices.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Handles all money requests, purchase orders and internal transfers.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Arranges sales invoices for sponsorship money.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Organises invoices to other societies or external parties.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Organises any clothing orders.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Arranges transport hire.</li>\n" +
                                    "<li><br>\t\t\t\t9.) Maintains photocopies of all documentation.</li>\n" +
                                    "<li><br>\t\t\t\t10.) Consults with the Union over any financial matters arising.</li>\n" +
                                    "<li><br>\t\t\t\t11.) Reports all audience figures to the Booking Agent.</li>\n" +
                                    "<li><br>\t\t\t\t12.) Completes the Students Union's course for Treasurers.</li>\n" +
                                    "<li><br>\t\t\t\t13.) Shall have knowledge of the code to WSC's safe, and may only distribute the safe code to members that require it.</li>\n" +
                                    "<li><br>\t\t\t\t14.) Responsible, along with the President and Chief Duty Manager, for ensuring the on-going security of this code, which may include regularly changing the code.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>\t\t4.) <b>Films Officer</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Responsible for organising and chairing the Programming Meeting (PM).</li>\n" +
                                    "<li><br>\t\t\t\t2.) Responsible for programming upcoming schedules.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Responsible for ensuring all films are booked in good time.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Responsible for chasing up any films which have not arrived or have not been collected, with the film transport company and the booking agent.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Responsible for co-ordinating all replacement shows.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Responsible for ordering film trailers and posters.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Responsible for co-ordinating collaborations, including handling contracts.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>\t\t5.) <b>Chief Projectionist</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Must be a qualified Projectionist.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Ensures that all shows are covered by a projectionist.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Shall be the chair of the Projection Committee.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Responsible for the administration of the Projection Training Scheme, including the training of 35mm/70mm and Digital.</li>\n" +
                                    "<li><br>\t\t\t\t5.) After proper consultation with the Projection Committee; responsible for freshers and all other recruitment to the training scheme and the proper maintenance of a waiting list if any such thing should exist.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Maintains all playlists on the digital projector server.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Coordinates all changes to projection procedure and ensures all projectionists are kept up to date.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Ultimately ensures that projection standards are maintained.</li>\n" +
                                    "<li><br>\t\t\t\t9.) Ensures that all films are returned to the film dump properly.</li>\n" +
                                    "<li><br>\t\t\t\t10.) First point of contact for those willing to get involved in projection.</li>\n" +
                                    "<li><br>\t\t\t\t11.) Moderates and maintains all projection mailing lists.</li>\n" +
                                    "<li><br>\t\t\t\t12.) Monitors the free films allowance of projectionists.</li>\n" +
                                    "<li><br>\t\t\t\t13.) Co-ordinates with the Technical Officer to ensure all equipment remains in an acceptable working order and that all projection related consumables are kept in good stock.</li>\n" +
                                    "<li><br>\t\t\t\t14.) Responsible for dealing with Pearl and Dean to ensure we have adverts at the appropriate time.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>\t\t6.) <b>Chief Duty Manager</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Must be a WSC Qualified Duty Manager.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Ensures that all shows are covered by a qualified DM.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Ensures that all shows are covered by a sufficient number of stewards.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Ensures that FoH has sufficient amount of:</li>\n" +
                                    "<li><br>\t\t\t\t\t\t5.) Spare change</li>\n" +
                                    "<li><br>\t\t\t\t\t\t6.) Till rolls</li>\n" +
                                    "<li><br>\t\t\t\t\t\t7.) Banking bags</li>\n" +
                                    "<li><br>\t\t\t\t\t\t8.) Spare change bags</li>\n" +
                                    "<li><br>\t\t\t\t9.) Ensures FoH standards are maintained.</li>\n" +
                                    "<li><br>\t\t\t\t10.) Monitors the free films allowance of stewards.</li>\n" +
                                    "<li><br>\t\t\t\t11.) Moderates and maintains the DM mailing lists.</li>\n" +
                                    "<li><br>\t\t\t\t12.) Oversees the training of DMs and stewards.</li>\n" +
                                    "<li><br>\t\t\t\t13.) Ensures that trainee DMs are signing up for an acceptable number of shows.</li>\n" +
                                    "<li><br>\t\t\t\t14.) Organises DM qualifications.</li>\n" +
                                    "<li><br>\t\t\t\t15.) Responsible for coordinating the development and upkeep of FoH systems in consultation with the IT Officer.</li>\n" +
                                    "<li><br>\t\t\t\t16.) First point of contact for those willing to get involved in FoH.</li>\n" +
                                    "<li><br>\t\t\t\t17.) Deals with any general issues in FoH that may arise.</li>\n" +
                                    "<li><br>\t\t\t\t18.) Calls and chairs regular FoH committee meetings.</li>\n" +
                                    "<li><br>\t\t\t\t19.) Updates the FoH risk assessments.</li>\n" +
                                    "<li><br>\t\t\t\t20.) Takes lost property to the Library Bridge Porter or the Gatehouse.</li>\n" +
                                    "<li><br>\t\t\t\t21.) Shall have knowledge of the code to WSC's safe, and may only distribute the safe code to members that require it.</li>\n" +
                                    "<li><br>\t\t\t\t22.) Responsible, along with the President and Treasurer, for ensuring the on-going security of this code, which may include regularly changing the code.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>\t\t7.) <b>Publicity Officer</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Co-ordinates the functions of the Publicity Committee.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Works with the Chief Editor to ensure that all publicity materials are produced to a high quality and arrive prior to the start of term or event.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Organises the distribution of publicity, ensuring that Exec are aware of their responsibilities regarding distribution and that Crew are aware of Publicity Runs.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Moderates and maintains publicity related mailing lists.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Maintains the publicity archive.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Responsible for ensuring film and ticket images and reviews are uploaded to the website.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Monitors the free film allowance of members of the publicity committee.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>8.) <b>Technical Officer</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Must be a WSC Qualified 35mm Projectionist.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Ensures the upkeep of all non-IT related equipment.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Coordinates and directs the Technical Committee.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Responsible for maintaining all equipment in safe working order.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Responsible for routine cleaning of projection equipment.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Plans and coordinates upgrades and technical projects.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Maintains the inventory.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Updates the risk assessments for the projection box and all technical work.</li>\n" +
                                    "<li><br>\t\t\t\t9.) First point of call for projectionists in the event of a technical problem.</li>\n" +
                                    "<li><br>\t\t\t\t10.) Moderates and maintains the technical mailing list.</li>\n" +
                                    "<li><br>\t\t\t\t11.) Encourages projectionists to join the Technical Committee.</li>\n" +
                                    "<li><br>\t\t\t\t12.) Oversees technical training.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>\t\t9.) <b>IT Officer</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Has overall responsibility for the IT needs of WSC.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Oversees maintenance and development of the website.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Maintains and moderates all mailing lists not done by other Exec members.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Ensures that all FoH systems are maintained and all tickets are ready.</li>\n" +
                                    "<li><br>\t\t\t\t5.) First point of contact in light of an IT related problem.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Liaises with the Students. Union.s IT department.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Allocates e-mail addresses and sorts out all aliases.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Monitors the free film allowance of members of the IT committee.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>\t\t10.) <b>Marketing Officer</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Has overall responsibility for all the electronic publicity and advertising of WSC.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Oversees the content on the LCD screens and pre-film slideshow.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Responsible for communication regarding, and implementation of, non-external advertising requests.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Responsible for the WSC social network presence, including Facebook and Twitter.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Responsible for WSC presence on the Warwick SU site, including news and calendar.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Submits material for Sponsored Events and The AllNighter to Warwick SU publications such as the Bubble.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Maintains the WSC electronic photograph library.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Organises the creation of termly \"What's On\" videos and any other WSC videos.</li>\n" +
                                    "<li><br>\t\t\t\t9.) Ensures that digital trailers are created to promote the films we are showing.</li>\n" +
                                    "<li><br>\t\t\t\t10.) Contacts other organisations to promote WSC, including Insite and the Union.</li>\n" +
                                    "<li><br>\t\t\t\t11.) Works in conjunction with the Publicity and IT teams.</li>\n" +
                                    "<li><br>\t\t\t\t12.) Co-ordinates the functions of the Marketing Team</li>\n" +
                                    "<li><br>\t\t\t\t13.) Monitors the free film allowance of members of the marketing committee.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>\t\t11.) <b>Events and Socials Co-ordinator</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Responsible for organising socials for Crew and Members, and making Crew aware of when socials are happening.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Overall responsibility for the non-Technical/FoH aspects of &ldquo;Special Events&rdquo; such as AllNighters, Movie Pub Quizzes, Outdoor Screenings </li>etc.\n" +
                                    "<li><br>\t\t\t\t3.) Co-ordinates the Exec to ensure that these events are well staffed.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Works with the President to organise any Sponsored Events.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Liases with the Students Union to ensure that food/drink provision and prizes are appropriate so that money requests can be signed off.</li>\n" +
                                    "</ol></li>\n" +
                                    "<li><br><br>\t\t12.) <b>Chief Editor</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Works with the Publicity Officer to ensure that all publicity materials are produced to a high quality and arrive prior to the start of term or event.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Responsible for ensuring that the booklet is designed and produced to a high standard by the publicity deadline.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Responsible for chairing meetings concerning the design of the booklet.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Oversees the training of editors.</li>\n" +
                                    "</ol></li>\n" +
                                    "</ol>"));
                                break;
                            case 2: constitutionText.setText(Html.fromHtml("<b>Last updated: 28th July 2012</b>\n" +
                                    "<p>The society shall have the following committees, in addition to the Executive Committee:</p>\n" +
                                    "<ol><li>\t\t1.) Front of House Committee (Duty Managers Committee)</li>\n" +
                                    "<li><br>\t\t2.) Projection Committee</li>\n" +
                                    "<li><br>\t\t3.) Publicity Committee</li>\n" +
                                    "<li><br>\t\t4.) Technical Committee</li>\n" +
                                    "<li><br>\t\t5.) IT Committee</li>\n" +
                                    "<li><br>\t\t6.) Marketing Committee</li></ol>\n" +
                                    "<br><br>Appropriate notification for the meeting of any committee shall be no less than 48 hours. Individual committees may define their own timescales if they consider this timescale to be impractically short.\n" +
                                    "<br><br>The committees shall have the following responsibilities and remits:\n" +
                                    "<ol><li><br>\t\t1.) <b>Front of House Committee</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) All qualified DMs who are full members of WSC.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Are essentially the student decision making body for FoH, led by the Chief Duty Manager who retains overall responsibility.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Shall help the Chief Duty Manager ensure all shows are covered by sufficient DMs and stewards.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Matters of importance as deemed by the Chief Duty Manager or Exec as a whole shall be voted on by the committee.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Shall discuss progress of trainees.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Shall vote to qualify new DMs (a minimum of three full members must be present).</li>\n" +
                                    "<li><br>\t\t\t\t7.) Can vote to propose a de-qualification to the Exec. However, the Chief Duty Manager can decide that it is not appropriate to consult the committee in this case.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Shall be key in the training of all stewards and DMs.</li>\n" +
                                    "<li><br>\t\t\t\t9.) Shall help uphold FoH standards.</li>\n" +
                                    "<li><br>\t\t\t\t10.) Shall generally assist the Chief Duty Manager when requested.</li>\n" +
                                    "<li><br>\t\t\t\t11.) Can propose to the Exec to remove a trainee DM from the training scheme, given appropriate cause and warning.</li>\n" +
                                    "<li><br>\t\t\t\t12.) When it is required for the proper execution of their duty, members shall be granted knowledge of the code to WSC's safe, on the understanding that they're not to share or change it.</li></ol></li>\n" +
                                    "<li><br><br>\t\t2.) <b>Projection Committee</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) All qualified projectionists who are full members of WSC.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Shall be the decision making body for the projectionists on any issues within their remit, chaired by the Chief Projectionist.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Shall help the Chief Projectionist ensure all shows are covered.</li>\n" +
                                    "<li><br>\t\t\t\t4.) The Chief Projectionist shall be responsible for the creation and dissemination of the meeting agenda.</li>\n" +
                                    "<li><br>\t\t\t\t5.) A minimum of three full members must be present to form a Qualification Committee., which itself has the power to qualify projectionists at Qualification Shows.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Can vote to propose a de-qualification to the Exec. However, the Chief Projectionist can decide that it is not appropriate to consult the committee in this case.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Shall be key in the proper coordination of the Projection Training Scheme and shall decide upon the maximum number of trainees.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Shall help uphold projection standards.</li>\n" +
                                    "<li><br>\t\t\t\t9.) Shall work with each other to improve knowledge of projection.</li>\n" +
                                    "<li><br>\t\t\t\t10.) Shall generally assist the Chief Projectionist when requested.</li>\n" +
                                    "<li><br>\t\t\t\t11.) Can propose to the Exec to remove a trainee projectionist from the training scheme, given appropriate cause and warning.</li></ol></li>\n" +
                                    "<li><br><br>\t\t3.) <b>Publicity Committee</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) The Publicity Officer may add any full member of WSC to the committee.</li>\n" +
                                    "<li><br>\t\t\t\t2.) The Publicity Officer has the power to appoint any full member of the committee to any of the committee's specific roles.</li>\n" +
                                    "<li><br>\t\t\t\t3.) New roles can only be created with agreement from Exec.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Shall assist the Publicity Officer at all times.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Shall work to produce all WSC publicity.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Shall work on new publicity ideas.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Shall encourage new people to become involved.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Shall help train each other.</li>\n" +
                                    "<li><br>\t\t\t\t9.) Shall help compile the publicity budget.</li></ol></li>\n" +
                                    "<li><br><br>\t\t4.) <b>Technical Committee</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) Any qualified projectionist who is a full member of WSC and wishes to join.</li>\n" +
                                    "<li><br>\t\t\t\t2.) Shall assist the Technical Officer at all times.</li>\n" +
                                    "<li><br>\t\t\t\t3.) Shall carry out all technical work.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Shall vote on large purchases or project ideas.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Shall train each other in all areas of technical work needed.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Shall help encourage others to become involved in the Technical Committee.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Shall discuss the technical budget.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Shall write all proposals for technical grants.</li></ol></li>\n" +
                                    "<li><br><br>\t\t5.) <b>IT Committee</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) The IT Officer may add any full member of WSC to the committee.</li>\n" +
                                    "<li><br>\t\t\t\t2.) The IT Officer has the power to appoint any full member of the committee to any of the committee's specific roles.</li>\n" +
                                    "<li><br>\t\t\t\t3.) New roles can only be created with agreement from Exec.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Shall assist the IT Officer at all times.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Shall carry out all IT work.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Shall vote on all large purchases or ideas.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Shall help encourage others to become involved in the IT committee.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Shall train each other in all areas of the IT systems.</li></ol></li>\n" +
                                    "<li><br><br>\t\t6.) <b>Marketing Committee</b><ol>\n" +
                                    "<li><br>\t\t\t\t1.) The Marketing Officer may add any full member of WSC to the committee.</li>\n" +
                                    "<li><br>\t\t\t\t2.) The Marketing Officer has the power to appoint any full member of the committee to any of the committee's specific roles.</li>\n" +
                                    "<li><br>\t\t\t\t3.) New roles can only be created with agreement from Exec.</li>\n" +
                                    "<li><br>\t\t\t\t4.) Shall assist the Marketing Officer at all times.</li>\n" +
                                    "<li><br>\t\t\t\t5.) Shall work to produce all WSC marketing.</li>\n" +
                                    "<li><br>\t\t\t\t6.) Shall work on new marketing ideas.</li>\n" +
                                    "<li><br>\t\t\t\t7.) Shall encourage new people to become involved.</li>\n" +
                                    "<li><br>\t\t\t\t8.) Shall help train each other.</li></ol></li></ol>"));
                                break;
                            case 3: constitutionText.setText(Html.fromHtml("<p>The following keys shall be issued to the appropriate members at the discretion of their respective committee head and President. All key holders must be members of WSC. All keys are subject to recall by the Executive Committee should it be deemed necessary, in spite of any qualifications held by the person in question.</p>\n" +
                                    "<br><b>President</b>: Corridor, Dual, Corridor Key Box, Technical Key Box, DM Locker"+
                                    "<br><b>Vice-President</b>: Corridor, Office" +
                                    "<br><b>Treasurer</b>: Corridor, Office, Corridor Key Box, DM Locker" +
                                    "<br><b>Films Officer</b>: Corridor, Dual" +
                                    "<br><b>Chief Projectionist</b>: Corridor, Dual" +
                                    "<br><b>Chief DM</b>: Corridor, Office, Corridor Key Box, DM Locker" +
                                    "<br><b>Publicity Officer</b>: Corridor, Office" +
                                    "<br><b>Technical Officer</b>: Corridor, Dual, Technical Key Box" +
                                    "<br><b>IT Officer</b>: Corridor, Dual, DM Locker" +
                                    "<br><b>Marketing Officer</b>: Corridor, Office" +
                                    "<br><b>Duty Managers</b>: Corridor, Corridor Key Box" +
                                    "<br><b>Projectionists</b>: Corridor, Dual, Technical Key Box" +
                                    "<h3>Key Agreement</h3>" +
                                    "<p>I understand that the keys I have been issued with shall remain the property of the University of Warwick Students&rsquo; Union and must be returned immediately upon the request of the WSC Exec, any Students Union Sabbatical Officer or the Societies Administrator. Keys must be returned to the WSC President, or their nominated representative, who must be an officer of WSC or the Students Union.</p>" +
                                    "<p>I agree to use these keys only to carry out official WSC business and understand that unofficial use, or non-return of keys, is a serious breach of conduct, and could result in disciplinary action in conjunction with the Students&rsquo; Union, the University or both.</p>" +
                                    "<p>This key agreement expires at noon on Thursday Week 10, Term 3, and so all keys loaned by this agreement must be returned before then, ahead of the summer vacation, unless you are a member of the WSC Exec at the time, in which case this agreement will roll over to the end of your term on the WSC Executive. I understand this and will return my keys before this deadline.</p>" +
                                    "<p>Keyholders are reminded that the University has Security Policies regarding keys to University premises. These policies apply to all keyholders, and you consent to abide by them by accepting custody of university keys. Important points include automatically returning keys when requested, not loaning your keys to others and leaving the area secure. Full documentation can be found on the University Security website: go.warwick.ac.uk/security</p>" +
                                    "<p>If at any time I become aware that these keys have been lost or stolen, then I shall notify the WSC President immediately, to allow them to act accordingly.</p>"));
                                break;
                            default: constitutionText.setText("Position of Spinner: " + position);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                    break;
                case 6: rootView = inflater.inflate(R.layout.fragment_about_us_technical, container, false);
                    break;
                case 7: rootView = inflater.inflate(R.layout.fragment_about_us_faq, container, false);
                    break;
                case 8: rootView = inflater.inflate(R.layout.fragment_about_us_contact, container, false);
                    break;
                case 9: rootView = inflater.inflate(R.layout.fragement_about_us_terms_conditions, container, false);
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
