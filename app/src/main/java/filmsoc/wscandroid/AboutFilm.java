package filmsoc.wscandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class AboutFilm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_film);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        Bundle extras = getIntent().getExtras();
        long filmID = extras.getLong("filmID");
        Film thisFilm = ScheduleManager.getFilm((int)filmID);

        ImageView filmImage = (ImageView) findViewById(R.id.filmImage);
        TextView filmTitleContents = (TextView) findViewById(R.id.filmTitle);
        ImageView filmRating = (ImageView) findViewById(R.id.filmRating);
        TextView filmReviewContent = (TextView) findViewById(R.id.filmReviewContent);
        TableLayout filmScreenings = (TableLayout) findViewById(R.id.filmScreeningsContext);
        TableLayout filmPreviousScreenings = (TableLayout) findViewById(R.id.filmPreviousScreeningContent);
        TableLayout filmDetails = (TableLayout) findViewById(R.id.filmDetailsContent);

        if (thisFilm.getImage() == null || !isConnected) {
            filmImage.setImageResource(R.drawable.banner);
            FrameLayout filmTitleFrame = (FrameLayout) findViewById(R.id.filmTitleFrame);
            filmTitleFrame.setBackgroundColor(0xff000000);
            filmImage.setBackgroundColor(0xff000000);
            filmTitleContents.setTextColor(0xffdddddd);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.rgb(0,0,0));
            }
        } else {
            try {
                Drawable image = new RecieveImage().execute(thisFilm.getImage()).get();
                filmImage.setImageDrawable(image);
                Bitmap filmImageBitmap = ((BitmapDrawable) filmImage.getDrawable()).getBitmap();
                float backColourRed = 0;
                float backColourGreen = 0;
                float backColourBlue = 0;
                int counter = 0;
                for (int i = 0; i < filmImageBitmap.getWidth(); i=i+4) {
                    for (int j = 0; j < filmImageBitmap.getHeight(); j=j+4) {
                        if (counter == 0) {
                            int backColour1 = filmImageBitmap.getPixel(i, j);
                            backColourRed = Color.red(backColour1);
                            backColourGreen = Color.green(backColour1);
                            backColourBlue = Color.blue(backColour1);
                            counter++;
                        } else {
                            int backColour1 = filmImageBitmap.getPixel(i, j);
                            backColourRed = (float)Color.red(backColour1) * (1.0f/((float)counter+1.0f)) + backColourRed * ((float)counter/((float)counter+1.0f));
                            backColourGreen = (float)Color.green(backColour1) * (1.0f/((float)counter+1.0f)) + backColourGreen * ((float)counter/((float)counter+1.0f));
                            backColourBlue = (float)Color.blue(backColour1) * (1.0f/((float)counter+1.0f)) + backColourBlue * ((float)counter/((float)counter+1.0f));
                            counter++;
                        }
                    }
                }

                FrameLayout filmTitleFrame = (FrameLayout) findViewById(R.id.filmTitleFrame);
                filmTitleFrame.setBackgroundColor(Color.rgb((int)backColourRed, (int)backColourGreen, (int)backColourBlue));
                filmImage.setBackgroundColor(Color.rgb((int)backColourRed, (int)backColourGreen, (int)backColourBlue));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(Color.rgb((int)backColourRed, (int)backColourGreen, (int)backColourBlue));
                }
                if ((backColourRed+backColourGreen+backColourBlue/3) < 130) {
                    filmTitleContents.setTextColor(0xffdddddd);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        filmTitleContents.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        if (thisFilm != null) {
            filmTitleContents.setText(thisFilm.getName());
            if (thisFilm.getReview() != null) {
                filmReviewContent.setText(Html.fromHtml(thisFilm.getReview()));
            } else {
                filmReviewContent.setText("Review coming soon!");
            }
            if (thisFilm.getRating() != null) {
                switch(thisFilm.getRating()) {
                    case U_C: filmRating.setImageResource(R.drawable.rating_u_c);
                        break;
                    case U: filmRating.setImageResource(R.drawable.rating_u);
                        break;
                    case PG: filmRating.setImageResource(R.drawable.rating_pg);
                        break;
                    case TWELVE: filmRating.setImageResource(R.drawable.rating_twelve);
                        break;
                    case TWELVE_A: filmRating.setImageResource(R.drawable.rating_twelve_a);
                        break;
                    case FIFTEEN: filmRating.setImageResource(R.drawable.rating_fifteen);
                        break;
                    case EIGHTEEN: filmRating.setImageResource(R.drawable.rating_eighteen);
                        break;
                    case R_EIGHTEEN: filmRating.setImageResource(R.drawable.rating_r_eighteen);
                        break;
                    default: filmRating.setVisibility(View.INVISIBLE);
                }
            } else {
                filmRating.setVisibility(View.INVISIBLE);
            }

            Calendar[] filmShowings = thisFilm.getShownAfter(Calendar.getInstance());
            if (filmShowings == null || filmShowings.length == 0) {
                ((RelativeLayout) findViewById(R.id.filmScreeningFrame)).setVisibility(View.GONE);
            } else {
                for (int i = 0; i < filmShowings.length; i++) {
                    TableRow screeningRow = new TableRow(this);
                    TextView screeningRowDate = new TextView(this);
                    screeningRowDate.setGravity(0x03);
                    TextView screeningRowTime = new TextView(this);
                    screeningRowTime.setGravity(0x05);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    screeningRowDate.setText(dateFormat.format(filmShowings[i].getTime()));
                    String screeningRowTimeString = filmShowings[i].get(Calendar.HOUR) + ":";
                    if (filmShowings[i].get(Calendar.MINUTE) < 10) {
                        screeningRowTimeString = screeningRowTimeString + "0" + filmShowings[i].get(Calendar.MINUTE);
                    } else {
                        screeningRowTimeString = screeningRowTimeString + filmShowings[i].get(Calendar.MINUTE);
                    }
                    if (filmShowings[i].get(Calendar.AM_PM) == Calendar.AM) {
                        screeningRowTimeString = screeningRowTimeString + "am";
                    } else {
                        screeningRowTimeString = screeningRowTimeString + "pm";
                    }
                    screeningRowTime.setText(screeningRowTimeString);
                    screeningRow.addView(screeningRowDate);
                    screeningRow.addView(screeningRowTime);
                    filmScreenings.addView(screeningRow);
                }
            }

            Calendar[] filmPreviousShowings = thisFilm.getShownBefore(Calendar.getInstance());
            if (filmPreviousShowings == null || filmPreviousShowings.length == 0) {
                ((RelativeLayout) findViewById(R.id.filmPreviousScreeningFrame)).setVisibility(View.GONE);
            } else {
                for (int i = 0; i < filmPreviousShowings.length; i++) {
                    TableRow screeningRow = new TableRow(this);
                    TextView screeningRowDate = new TextView(this);
                    screeningRowDate.setGravity(0x03);
                    TextView screeningRowTime = new TextView(this);
                    screeningRowTime.setGravity(0x05);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    screeningRowDate.setText(dateFormat.format(filmPreviousShowings[i].getTime()));
                    String screeningRowTimeString = filmPreviousShowings[i].get(Calendar.HOUR) + ":";
                    if (filmPreviousShowings[i].get(Calendar.MINUTE) < 10) {
                        screeningRowTimeString = screeningRowTimeString + "0" + filmPreviousShowings[i].get(Calendar.MINUTE);
                    } else {
                        screeningRowTimeString = screeningRowTimeString + filmPreviousShowings[i].get(Calendar.MINUTE);
                    }
                    if (filmPreviousShowings[i].get(Calendar.AM_PM) == Calendar.AM) {
                        screeningRowTimeString = screeningRowTimeString + "am";
                    } else {
                        screeningRowTimeString = screeningRowTimeString + "pm";
                    }
                    screeningRowTime.setText(screeningRowTimeString);
                    screeningRow.addView(screeningRowDate);
                    screeningRow.addView(screeningRowTime);
                    filmPreviousScreenings.addView(screeningRow);
                }
            }

            if (thisFilm.getYear() != -1) {
                filmDetails = addTableRow("Year", thisFilm.getYear()+"", filmDetails);
            } else {
                filmDetails = addTableRow("Year", "Unknown", filmDetails);
            }

            if (thisFilm.getRunTime() != -1) {
                filmDetails = addTableRow("Length", thisFilm.getRunTime()+" minutes", filmDetails);
            } else {
                filmDetails = addTableRow("Length", "Unknown", filmDetails);
            }

            if (thisFilm.getAspectRatio() != -1.0f) {
                filmDetails = addTableRow("Aspect Ratio", thisFilm.getAspectRatio()+"", filmDetails);
            } else {
                filmDetails = addTableRow("Aspect Ratio", "Unknown", filmDetails);
            }

            if (thisFilm.getDirector() != null) {
                String directors = thisFilm.getDirector();
                directors = directors.replaceAll(", ", "\n");
                directors = directors.replaceAll(" & ", "\n");
                filmDetails = addTableRow("Director(s)", directors, filmDetails);
            } else {
                filmDetails = addTableRow("Director(s)", "Unknown", filmDetails);
            }

            if (thisFilm.getStaring() != null) {
                String staring = thisFilm.getStaring();
                staring = staring.replaceAll(", ", "\n");
                staring = staring.replaceAll(" & ", "\n");
                filmDetails = addTableRow("Staring", staring, filmDetails);
            } else {
                filmDetails = addTableRow("Staring", "Unknown", filmDetails);
            }

            if (thisFilm.getSubtitles() == -1) {
                filmDetails = addTableRow("Subtitles", "Unknown", filmDetails);
            } else if (thisFilm.getSubtitles() == 1) {
                filmDetails = addTableRow("Staring", "Yes", filmDetails);
            } else {
                filmDetails = addTableRow("Staring", "No", filmDetails);
            }

        } else {
            filmTitleContents.setText("UNKNOWN FILM");
            filmRating.setVisibility(View.INVISIBLE);
            filmReviewContent.setText("UNKNOWN FILM");

            TableRow screeningRow = new TableRow(this);
            TextView screeningRowText = new TextView(this);
            screeningRowText.setText("UNKNOWN FILM");
            screeningRow.addView(screeningRowText);
            filmScreenings.addView(screeningRow);

            TableRow detailsRow = new TableRow(this);
            TextView detailsRowText = new TextView(this);
            detailsRowText.setText("UNKNOWN FILM");
            detailsRow.addView(detailsRowText);
            filmDetails.addView(detailsRow);
        }
    }

    private TableLayout addTableRow(String detailNameString, String detailValueString, TableLayout tableLayoutView) {
        TableRow tableRow = new TableRow(this);
        TextView detailNameView = new TextView(this);
        detailNameView.setGravity(0x03);
        detailNameView.setText(detailNameString);
        TextView detailValueView = new TextView(this);
        detailValueView.setGravity(0x05);
        detailValueView.setText(detailValueString);

        tableRow.addView(detailNameView);
        tableRow.addView(detailValueView);

        tableLayoutView.addView(tableRow);

        return tableLayoutView;
    }

    private class RecieveImage extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                InputStream content = (InputStream)url.getContent();
                Drawable drawable = Drawable.createFromStream(content,"src");
                return drawable;
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                return null;
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}
