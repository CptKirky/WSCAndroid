package filmsoc.wscandroid;

import java.security.InvalidParameterException;
import java.util.Calendar;

/**
 * Created by Richard on 18/07/2016.
 */
public class Screening {
    private int id = -1;
    private int filmID = -1;
    private Calendar time = null;
    private int[] dutyManagers = null;
    private int[] projectionists = null;
    private int[] stewards = null;
    class Ticket {
        private int id = -1;
        private float price = 0;
        private int noSold = 0;

        public Ticket (int id) {
            if (id < 0) {
                throw new InvalidParameterException("Must include a valid ticket ID");
            }
            this.id = id;
        }

        public int getID() {
            return this.id;
        }

        public float getPrice(){
            return this.price;
        }

        public int getNoSold() {
            return this.noSold;
        }

        public float getTotalMade() {
            return this.price*this.noSold;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public void setNoSold(int noSold) {
            this.noSold = noSold;
        }

        public void addTicket() {
            this.noSold = this.noSold+1;
        }
    }
    private Ticket[] ticketsSold = null;

    public Screening(int id, int filmID, Calendar time) {
        if (id < 0 || filmID < 0 || time == null ) {
            throw new InvalidParameterException("Must include a valid screening ID, film ID and a time");
        }
        this.id = id;
        this.filmID = filmID;
        this.time = time;
    }
    //TODO: finish class off
}
