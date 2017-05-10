package filmsoc.wscandroid;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Richard on 09/06/2016.
 */
public class ScheduleManager {

    private static Film[] schedule;

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

    public static boolean isSchedule() {
        return (schedule != null);
    }

    public static Film[] getSchedule() {
        return schedule;
    }

    public static String getNextFilmName() {
        Film result = null;
        if (schedule == null) {
            return null;
        } else {
            result = getNextFilm();
        }
        return result.getName();
    }

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

    //TODO: build getFilmsBetween function
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

    public static int getScheduleSize() {
        if (schedule == null) {
            return -1;
        } else {
            return schedule.length;
        }
    }
}
