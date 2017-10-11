package filmsoc.wscandroid;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Calendar;

/**
 * This class stores all the data for a film. This includes reviews, ratings, showings and any other
 * details about the film. To identify the film, the ID field should be unique compared to all other
 * Film objects stored.
 *
 * Created by Richard on 10/06/2016.
 */

public class Film {
    private int id;         //This is the ID of the film
    private String name;    //This is the name of the film
    private int year = -1;  //This is the year the film was produced (useful for when there are 2 films with the same name)
    private int runTime = -1;   //The length of the film (minutes)
    private float aspectRatio = -1.0f;  //The aspect ratio of the film
    private String director;    //The directors of the film
    private String staring;     //Key actors of the film
    private String review;      //The review of the film
    private Calendar[] shown;   //All screenings of the film
    private Rating rating;      //The UK rating for the film
    private String image;       //The URL of the image
    private int subtitles = -1; //Stores if there are subtitles
    private Projection howProjected = Projection.DIGITAL;   //This stores how the film is projected

    /**
     * This enum stores all the valid ratings available in the UK (BBFC as of 2017).
     */
    public enum Rating{
        U_C, U, PG, TWELVE, TWELVE_A, FIFTEEN, EIGHTEEN, R_EIGHTEEN
    }

    /**
     * This enum stores how the film is projected
     */
    public enum Projection {
        DIGITAL, THIRTY_FIVE, SEVENTY
    }

    /**
     * This constructs the film object. Every film must have an ID and name. Should the ID be less
     * than 0, or the string be NULL or empty, an exception is returned. Otherwise, the data is
     * stored.
     * @param id The ID of the film. This must be greater than 0, and should be unique.
     * @param name The name of the film. This must not be NULL or empty.
     * @throws InvalidParameterException Thrown when an invalid ID or name is given.
     */
    public Film(int id, String name) throws InvalidParameterException {
        if (id < 0 || name == null || name.equals("")) {
            throw new InvalidParameterException("Invalid inputs to film class function \"Film\"");
        }
        this.id = id;
        this.name = name;
    }

    /**
     * This returns the ID of the film
     * @return The ID of the film
     */
    public int getId() {
        return this.id;
    }

    /**
     * This returns the name of the film
     * @return The name of the film
     */
    public String getName() {
        return this.name;
    }

    /**
     * This returns the year of the film
     * @return The year of the film
     */
    public int getYear() {
        return this.year;
    }

    /**
     * This returns the length of the film in minutes
     * @return The length of the film in minutes
     */
    public int getRunTime() {
        return this.runTime;
    }

    /**
     * This returns the aspect ratio of the film
     * @return The aspect ratio of the film
     */
    public float getAspectRatio() {
        return this.aspectRatio;
    }

    /**
     * This returns the directors of the film
     * @return The directors of the film
     */
    public String getDirector() {
        return this.director;
    }

    /**
     * This returns the staring actors
     * @return The staring actors
     */
    public String getStaring() {
        return this.staring;
    }

    /**
     *  This returns the review for the film
     * @return The review for the film
     */
    public String getReview() {
        return this.review;
    }

    /**
     * This gets a list of all screenings of the film before a given date.
     * @param beforeDate The date by which the list of screenings should be checked against.
     * @return An array of calendar objects of screenings before the given date.
     */
    public Calendar[] getShownBefore(Calendar beforeDate) {
        if (shown == null) {
            return null;
        }

        int counter = 0;
        for (int i = 0; i < shown.length; i++) {
            if (shown[i].getTimeInMillis() < beforeDate.getTimeInMillis()) {
                counter++;
            }
        }

        if (counter==0) {
            return null;
        }

        Calendar[] result = new Calendar[counter];
        counter = 0;
        for (int i = 0; i < shown.length; i++) {
            if (shown[i].getTimeInMillis() < beforeDate.getTimeInMillis()) {
                result[counter] = shown[i];
                counter++;
            }
        }

        return result;
    }

    /**
     * This gets a list of all screenings of the film after a given date.
     * @param afterDate The date by which the list of screenings should be checked against.
     * @return An array of calendar objects of screenings after the given date.
     */
    public Calendar[] getShownAfter(Calendar afterDate) {
        if (shown == null) {
            return null;
        }

        int counter = 0;
        for (int i = 0; i < shown.length; i++) {
            if (shown[i].getTimeInMillis() > afterDate.getTimeInMillis()) {
                counter++;
            }
        }

        if (counter==0) {
            return null;
        }

        Calendar[] result = new Calendar[counter];
        counter = 0;
        for (int i = 0; i < shown.length; i++) {
            if (shown[i].getTimeInMillis() > afterDate.getTimeInMillis()) {
                result[counter] = shown[i];
                counter++;
            }
        }

        return result;
    }

    /**
     * This gets the next available screening of the film
     * @return A calender object for the next screening of the film. Should there be no future screening, NULL is returned.
     */
    public Calendar getNextShowing() {
        Calendar result = null;

        if (shown == null) {
            return null;
        }

        for (int i = 0; i < shown.length; i++) {
            if (result==null && shown[i].getTimeInMillis() > System.currentTimeMillis()) {
                result = shown[i];
            } else if (shown[i].getTimeInMillis() > System.currentTimeMillis() && shown[i].getTimeInMillis() < result.getTimeInMillis()) {
                result = shown[i];
            }
        }
        return result;
    }

    /**
     * This returns the first screening of the film.
     * @return A calendar object of the first screening
     */
    public Calendar getFirstShowing() {
        if (shown == null) {
            return null;
        }

        orderShowings();

        return shown[0];
    }

    /**
     * This returns all the screenings stored for the film.
     * @return An array of all screenings
     */
    public Calendar[] getShown() {
        return this.shown;
    }

    /**
     * This returns the rating of the film
     * @return The rating for the film
     */
    public Rating getRating() {
        return this.rating;
    }

    /**
     * This returns the URL for the image of the film
     * @return The URL of the image of the film
     */
    public String getImage() {
        return this.image;
    }

    /**
     * This returns the level of subtitles
     * @return The level of subtitles
     */
    public int getSubtitles() {
        return this.subtitles;
    }

    /**
     * This allows the name of the film to be set
     * @param name The new name of the film
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This allows the year of the film to be set
     * @param year The new year of the film
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * This allows the runtime of the film to be set (minutes)
     * @param runTime The number of minutes in the film
     */
    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    /**
     * This allows the aspect ratio of the film to be set
     * @param aspectRatio The aspect ratio of the film
     */
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    /**
     * This allows the director(s) of the film to be set
     * @param director The director(s) of the film
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * This allows the actors and actresses staring in the film to be set
     * @param staring The people stating in the film
     */
    public void setStaring(String staring) {
        this.staring = staring;
    }

    /**
     * This allows for the review for the film to be set
     * @param review The review for the film to be set
     */
    public void setReview(String review) {
        this.review = review;
    }

    /**
     * This function adds a screening to the film
     * @param shown A calander object of the date and time of the screening
     */
    public void addShown(Calendar shown) {
        if (this.shown == null) {
            this.shown = new Calendar[1];
            this.shown[0] = shown;
            return;
        }
        Calendar[] tmpShown = new Calendar[this.shown.length+1];
        for (int i = 0; i < this.shown.length; i++) {
            tmpShown[i] = this.shown[i];
        }
        tmpShown[tmpShown.length-1] = shown;
        this.shown = tmpShown;
        orderShowings();
    }

    /**
     * This function allows for multiple screenings to be added to the film
     * @param shown An array of calendar objects of screenings
     */
    public void addShown(Calendar[] shown) {
        if (this.shown == null) {
            this.shown = shown;
            return;
        }
        Calendar[] tmpShown = new Calendar[this.shown.length+shown.length];
        for (int i = 0; i < this.shown.length; i++) {
            tmpShown[i] = this.shown[i];
        }
        for (int i = this.shown.length; i < (this.shown.length + shown.length); i++) {
            tmpShown[i] = shown[i-this.shown.length];
        }
        this.shown = tmpShown;
        orderShowings();
    }

    /**
     * This sets the list of screenings of the film, overriding any screenings already stored
     * @param shown An array of calendar objects storing all the objects
     */
    public void setShown(Calendar[] shown) {
        this.shown = shown;
        orderShowings();
    }

    /**
     * This sets the rating of the film
     * @param rating The rating of the film
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    /**
     * This sets the URL of the image of the film
     * @param image The URL of the image of the film
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * This sets the subtitle level of the film
     * @param subtitles The subtitle level of the film
     */
    public void setSubtitles(int subtitles) {
        this.subtitles = subtitles;
    }

    /**
     * This reorders the list of screenings in the film into date order
     * (it uses bubble sort, bite me ;P )
     */
    public void orderShowings() {
        boolean flag = true;
        while(flag) {
            flag = false;
            for (int i = 0; i < this.shown.length-1; i++) {
                if (this.shown[i].getTimeInMillis() > this.shown[i+1].getTimeInMillis()) {
                    Calendar tmpCal = this.shown[i];
                    this.shown[i] = this.shown[i+1];
                    this.shown[i+1] = tmpCal;
                    flag = true;
                }
            }
        }
    }

    /**
     * This duplicates a film object, returning a replica.
     * @param film The film object to be replicated
     * @return The replicated film object
     */
    public static Film duplicate(Film film) {
        Film resultFilm = new Film(film.getId(), film.getName());
        resultFilm.setYear(film.getYear());
        resultFilm.setRunTime(film.getRunTime());
        resultFilm.setAspectRatio(film.getAspectRatio());
        resultFilm.setDirector(film.getDirector());
        resultFilm.setStaring(film.getStaring());
        resultFilm.setReview(film.getReview());
        resultFilm.setShown(film.getShown());
        resultFilm.setRating(film.getRating());
        resultFilm.setImage(film.getImage());
        resultFilm.setSubtitles(film.getSubtitles());
        return resultFilm;
    }
}
