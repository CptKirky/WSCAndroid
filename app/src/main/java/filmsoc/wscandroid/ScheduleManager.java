package filmsoc.wscandroid;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class manages the entire schedule, maintaining its ordering on addition of data. A lot of
 * functional stuff is done in this class.
 *
 * Created by Richard on 09/06/2016.
 */
public class ScheduleManager {

    private static Film[] schedule;     //This is the array storing the schedule

    /**
     * This function returns the next film that is being shown.
     * @return This returns the next film being shown (in O(n) time). Should the schedule be empty,
     * null is returned. If there is only one film, that film is returned.
     */
    private static Film getNextFilm(){
        Film result = null;
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i].getNextShowing() == null) {
                continue;
            }
            if (result == null) {
                result = schedule[i];
            } else if (result.getNextShowing().getTimeInMillis() > schedule[i].getNextShowing().getTimeInMillis()) {
                result = schedule[i];
            }
        }

        return result;
    }

    /**
     * This is used to determine if there is a film in the schedule.
     * @return True is there is a film in the schedule, false otherwise.
     */
    public static boolean isSchedule() {
        return (schedule != null);
    }

    /**
     * This returns all films in the stored state in the schedule.
     * @return An array of films
     */
    public static Film[] getSchedule() {
        return schedule;
    }

    /**
     * This is used to get the name of the next film that is being shown.
     * @return The name of the next film. Should no film be in the schedule, then null is returned.
     */
    public static String getNextFilmName() {
        Film result = null;
        if (schedule == null) {
            return null;
        } else {
            result = getNextFilm();
        }
        return result.getName();
    }

    //TODO: make the date return a Calendar object, rather than a long
    /**
     * This is used to get the date and time for the next film being shown.
     * @return The date as a long.
     */
    public static long getNextFilmDate() {
        Film result = null;
        if (schedule==null) {
          return -1;
        } else {
            result = getNextFilm();
        }
        return result.getNextShowing().getTimeInMillis();
    }

    //TODO: build orderFilmsByNextDate function - Quick sort?
    public static void orderFilmsByNextDate() {

    }

    /**
     * This orders a given list of films by the first screening for the film. This uses a bubble
     * sort...
     * @param filmList The list fo films
     * @return An array of films in first screening order
     */
    public static Film[] orderSchedule(Film[] filmList) {
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0 ; i < filmList.length-1; i++) {
                if (filmList[i].getFirstShowing().getTimeInMillis() > filmList[i+1].getFirstShowing().getTimeInMillis()) {
                    Film tmpFilm = filmList[i];
                    filmList[i] = filmList[i+1];
                    filmList[i+1] = tmpFilm;
                    flag = true;
                }
            }
        }
        return filmList;
    }

    /**
     * This gets a list of all the films, and combines films and screenings together. It then
     * orders the films. Each screening is a seperate element, rather than each film with multiple
     * screenings.
     * @return A list of films in order
     */
    public static Film[] getNextFilms() {
        Film[] tmpList = getFilmsAfter(Calendar.getInstance());
        ArrayList<Film> result = new ArrayList<Film>();
        for (int i = 0; i < tmpList.length; i++) {
            Calendar[] datesShown = tmpList[i].getShown();
            for (int j = 0; j < datesShown.length; j++) {
                boolean seenFlag = false;
                for (int k = 0; k < result.size(); k++) {
                    Film tmpFilm = result.get(k);
                    if (tmpFilm.getId() == tmpList[i].getId() &&
                            tmpFilm.getNextShowing().get(Calendar.DAY_OF_MONTH) == datesShown[j].get(Calendar.DAY_OF_MONTH) &&
                            tmpFilm.getNextShowing().get(Calendar.MONTH) == datesShown[j].get(Calendar.MONTH) &&
                            tmpFilm.getNextShowing().get(Calendar.YEAR) == datesShown[j].get(Calendar.YEAR)) {
                        seenFlag = true;
                        tmpFilm.addShown(datesShown[j]);
                        result.remove(k);
                        result.add(k, tmpFilm);
                        break;
                    }
                }
                if (!seenFlag) {
                    Film tmpFilm = new Film(tmpList[i].getId(), tmpList[i].getName());
                    tmpFilm.addShown(datesShown[j]);
                    result.add(tmpFilm);
                }
            }
        }

        return orderSchedule(result.toArray(new Film[result.size()]));
    }

    /**
     * This seperates the screenings into seperate film objects.
     * @param tmpList The list of films where the screenigns need to be split
     * @return The ordered array of screenings
     */
    public static Film[] splitScreenings (Film[] tmpList) {
        if (tmpList == null) {
            return null;
        }

        ArrayList<Film> result = new ArrayList<Film>();
        for (int i = 0; i < tmpList.length; i++) {
            Calendar[] datesShown = tmpList[i].getShown();
            for (int j = 0; j < datesShown.length; j++) {
                boolean seenFlag = false;
                for (int k = 0; k < result.size(); k++) {
                    Film tmpFilm = result.get(k);
                    if (tmpFilm.getId() == tmpList[i].getId() &&
                            tmpFilm.getFirstShowing().get(Calendar.DAY_OF_MONTH) == datesShown[j].get(Calendar.DAY_OF_MONTH) &&
                            tmpFilm.getFirstShowing().get(Calendar.MONTH) == datesShown[j].get(Calendar.MONTH) &&
                            tmpFilm.getFirstShowing().get(Calendar.YEAR) == datesShown[j].get(Calendar.YEAR)) {
                        seenFlag = true;
                        tmpFilm.addShown(datesShown[j]);
                        result.remove(k);
                        result.add(k, tmpFilm);
                        break;
                    }
                }
                if (!seenFlag) {
                    Film tmpFilm = new Film(tmpList[i].getId(), tmpList[i].getName());
                    tmpFilm.addShown(datesShown[j]);
                    result.add(tmpFilm);
                }
            }
        }

        return orderSchedule(result.toArray(new Film[result.size()]));
    }

    /**
     * This lists the films before a given date. All screenings in the film are the screenings
     * before the date.
     * @param date The date by which the screenings should be searched
     * @return An array of films before the date
     */
    public static Film[] getFilmsBefore(Calendar date) {
        ArrayList<Film> tmpList = new ArrayList<Film>();
        for (int i = 0; i < schedule.length; i++) {
            Calendar[] datesOfFilm = schedule[i].getShownBefore(date);
            if (datesOfFilm == null) {
                continue;
            }
            Film tmpFilm = Film.duplicate(schedule[i]);
            tmpFilm.setShown(datesOfFilm);
            tmpList.add(tmpFilm);
        }

        return tmpList.toArray(new Film[tmpList.size()]);
    }

    /**
     * This lists the films after a given date. All screenings in the film are the screenings
     * after the date.
     * @param date The date by which the screenings should be searched
     * @return An array of films after the date
     */
    public static Film[] getFilmsAfter(Calendar date) {
        ArrayList<Film> tmpList = new ArrayList<Film>();
        for (int i = 0; i < schedule.length; i++) {
            Calendar[] datesOfFilm = schedule[i].getShownAfter(date);
            if (datesOfFilm == null) {
                continue;
            }
            Film tmpFilm = Film.duplicate(schedule[i]);
            tmpFilm.setShown(datesOfFilm);
            tmpList.add(tmpFilm);
        }

        return tmpList.toArray(new Film[tmpList.size()]);
    }

    /**
     * This lists all the films shown between 2 dates. All screenings in the film between the 2
     * dates are given as separate films.
     * @param before The start date to be searched against
     * @param after The end date to be searched against
     * @return An array of screenings between the dates
     */
    public static Film[] getFilmsBetween(Calendar before, Calendar after) {
        ArrayList<Film> tmpList = new ArrayList<Film>();
        for (int i = 0; i < schedule.length; i++) {
            Calendar[] datesOfFilm = schedule[i].getShownAfter(after);
            if (datesOfFilm == null) {
                continue;
            }
            ArrayList<Calendar> tmpDatesOfFilm = new ArrayList<Calendar>();
            for (int j = 0; j < datesOfFilm.length; j++) {
                if (datesOfFilm[j].getTimeInMillis() < before.getTimeInMillis()) {
                    tmpDatesOfFilm.add(datesOfFilm[j]);
                }
            }
            if (tmpDatesOfFilm.size() == 0) {
                continue;
            }
            Film tmpFilm = Film.duplicate(schedule[i]);
            tmpFilm.setShown(tmpDatesOfFilm.toArray(new Calendar[tmpDatesOfFilm.size()]));
            tmpList.add(tmpFilm);
        }
        return tmpList.toArray(new Film[tmpList.size()]);
    }

    /**
     * This allows for a film to be added to the schedule. Should the film given be equal to null
     * (i.e. not a valid film), then an exception is thrown. Should the film already exist, then,
     * it will be merged with the current one.
     * @param film The film to be added to the schedule
     * @throws InvalidParameterException If the film given is null, this is thrown
     */
    public static void addFilm(Film film) throws InvalidParameterException {
        if (film == null) {
            throw new InvalidParameterException("No film given");
        }

        if (schedule==null) {
            schedule = new Film[1];
            schedule[0] = film;
        } else {
            boolean alreadyExists = false;
            Film[] tmpSchedule = new Film[schedule.length+1];
            for (int i = 0; i < schedule.length; i++) {
                tmpSchedule[i] = schedule[i];
                if (schedule[i].getId() == film.getId()) {
                    alreadyExists = true;
                }
            }
            if (!alreadyExists) {
                tmpSchedule[schedule.length] = film;
            }
            schedule = tmpSchedule;
        }
    }

    /**
     * This allows for an array of films to be added to the schedule. Should the list not exist
     * (i.e., it is null), then an exception is thrown. Should the film already exist, then,
     * it will be merged with the current one.
     * @param film The array of films to be added to the schedule
     * @throws InvalidParameterException This is thrown should the input be null
     */
    public static void addFilm(Film[] film) throws InvalidParameterException {
        if (film == null) {
            throw new InvalidParameterException("No films given");
        }

        if (schedule==null) {
            schedule = film;
        } else {
            boolean[] alreadyExists = new boolean[film.length];
            int alreadyExistsCounter = 0;
            for (int i = 0; i < schedule.length; i++) {
                for (int j = 0; j < film.length; j++) {
                    if (schedule[i].getId() == film[j].getId()) {
                        alreadyExists[j] = true;
                        alreadyExistsCounter++;
                        break;
                    }
                }
            }
            if (alreadyExistsCounter == film.length) {
                return;
            }
            Film[] tmpSchedule = new Film[schedule.length+film.length-alreadyExistsCounter];
            for (int i = 0; i < schedule.length; i++) {
                tmpSchedule[i] = schedule[i];
            }
            int counter = 0;
            for (int i = schedule.length; i < tmpSchedule.length; i++) {
                for (int j=(counter+i)-schedule.length; j < film.length; j++) {
                    if (!alreadyExists[j]) {
                        tmpSchedule[i] = film[j];
                        break;
                    } else {
                        counter++;
                    }
                }
            }
            schedule = tmpSchedule;
        }
    }

    /**
     * This gets a film details given a particular ID
     * @param filmID The film ID
     * @return The film object. Should there not be a schedule stored, or if the film is not
     * present, null object is returned.
     */
    public static Film getFilm(int filmID) {
        if (!isSchedule()) {
            return null;
        }
        for (int i = 0; i < schedule.length; i++) {
            if (filmID == schedule[i].getId()) {
                return schedule[i];
            }
        }
        return null;
    }

    /**
     * This gets the size of the currently-stored schedule.
     * @return The current size of the schedule. Should no schedule be set-up, -1 is returned
     */
    public static int getScheduleSize() {
        if (schedule == null) {
            return -1;
        } else {
            return schedule.length;
        }
    }
}
