package filmsoc.wscandroid;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Calendar;

/**
 * Created by Richard on 10/06/2016.
 */

public class Film {
    private int id;
    private String name;
    private int year = -1;
    private int runTime = -1;
    private float aspectRatio = -1.0f;
    private String director;
    private String staring;
    private String review;
    private Calendar[] shown;
    private Rating rating;
    private String image;
    private int subtitles = -1;

    public enum Rating{
        U_C, U, PG, TWELVE, TWELVE_A, FIFTEEN, EIGHTEEN, R_EIGHTEEN
    }

    public Film(int id, String name) throws InvalidParameterException {
        if (id < 0 || name == null || name.equals("")) {
            throw new InvalidParameterException("Invalid inputs to film class function \"Film\"");
        }
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getYear() {
        return this.year;
    }

    public int getRunTime() {
        return this.runTime;
    }

    public float getAspectRatio() {
        return this.aspectRatio;
    }

    public String getDirector() {
        return this.director;
    }

    public String getStaring() {
        return this.staring;
    }

    public String getReview() {
        return this.review;
    }

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

    public Calendar getFirstShowing() {
        if (shown == null) {
            return null;
        }

        orderShowings();

        return shown[0];
    }

    public Calendar[] getShown() {
        return this.shown;
    }

    public Rating getRating() {
        return this.rating;
    }

    public String getImage() {
        return this.image;
    }

    public int getSubtitles() {
        return this.subtitles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setStaring(String staring) {
        this.staring = staring;
    }

    public void setReview(String review) {
        this.review = review;
    }

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

    public void setShown(Calendar[] shown) {
        this.shown = shown;
        orderShowings();
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSubtitles(int subtitles) {
        this.subtitles = subtitles;
    }

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
