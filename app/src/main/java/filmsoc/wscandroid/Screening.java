package filmsoc.wscandroid;

import java.security.InvalidParameterException;
import java.util.Calendar;

/**
 * This class stores a screening, as well as the tickets (in an inner class).
 *
 * Created by Richard on 18/07/2016.
 */
public class Screening {
    private int id = -1;                //This is the ID for the screening
    private int filmID = -1;            //This is the ID for the film
    private Calendar time = null;       //This stores the time and date in an appropriate class (it even keeps time zones!)
    private int[] dutyManagers = null;  //This stores the UserID for all DM's on the screening
    private int[] projectionists = null;//This stores the UserID for all Proj on the screening
    private int[] stewards = null;      //This stores the UserID for all stewards on the screenings

    /**
     * This is an inner class to store the different tickets at the screenings. It contains ID's,
     * prices and number of that type of tickets sold.
     */
    class Ticket {
        private int id = -1;        //This is the ID for the ticket
        private float price = 0;    //This is the price for the particular ticket
        private int noSold = 0;     //This is the number of this type of tickets sold at this particualr screening

        /**
         *
         * @param id
         * @param price
         */
        public Ticket (int id, float price) {
            if (id < 0) {
                throw new InvalidParameterException("Must include a valid ticket ID");
            }
            this.id = id;

            if (price < 0) {
                throw new InvalidParameterException("Must include a valid price");
            }
            this.price = price;
        }

        /**
         *
         * @return
         */
        public int getID() {
            return this.id;
        }

        /**
         *
         * @return
         */
        public float getPrice(){
            return this.price;
        }

        /**
         *
         * @return
         */
        public int getNoSold() {
            return this.noSold;
        }

        /**
         *
         * @return
         */
        public float getTotalMade() {
            return this.price*this.noSold;
        }

        /**
         *
         * @param price
         */
        public void setPrice(float price) {
            this.price = price;
        }

        /**
         *
         * @param noSold
         */
        public void setNoSold(int noSold) {
            if (noSold < 0) {
                this.noSold=0;
            }

            this.noSold = noSold;
        }

        /**
         *
         */
        public void addTicket() {
            this.noSold = this.noSold+1;
        }
    }
    private Ticket[] ticketsSold = null;

    /**
     *
     * @param id
     * @param filmID
     * @param time
     */
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
