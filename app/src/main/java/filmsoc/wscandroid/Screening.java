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
         * This creates a ticket that is valid for the show. A ticket must consist of an ID (to
         * identify the ticket) and the price (as the ticket must have a cost). The ID and the
         * ticket price cannot be less than 0, otherwise an exception is thrown.
         * @param id The ID of the ticket - must be greater than or equal to 0. Othwerise, an InvalidParameterException is thrown
         * @param price The price of the ticket (in pounds) - must be greather than or equal to 0. Otherwise, an InvalidParameterException is thrown.
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
         * This returns the ticket ID.
         * @return The ID
         */
        public int getID() {
            return this.id;
        }

        /**
         * This returns the ticket price.
         * @return The ticket price
         */
        public float getPrice(){
            return this.price;
        }

        /**
         * This returns the number of this ticket sold.
         * @return The number of tickets sold
         */
        public int getNoSold() {
            return this.noSold;
        }

        /**
         * This returns the total takings for this ticket for this screening.
         * @return The total takings (number sold * ticket price)
         */
        public float getTotalMade() {
            return this.price*this.noSold;
        }

        /**
         * This allows teh price to be modified.
         * @param price The price of a ticket (in pounds)
         */
        public void setPrice(float price) {
            this.price = price;
        }

        /**
         * This sets the number of tickets for this screening has been sold. Should a number lower
         * than 0 is given, the number of tickets sold is set to 0.
         * @param noSold Number of tickets sold
         */
        public void setNoSold(int noSold) {
            if (noSold < 0) {
                this.noSold=0;
            }

            this.noSold = noSold;
        }

        /**
         * This increments the number of tickets sold.
         */
        public void addTicket() {
            this.noSold = this.noSold+1;
        }
    }
    private Ticket[] ticketsSold = null;        //This stores an arrat of all the tickets for this screening

    /**
     * This constructs the screening object. An ID, filmID and the date & time of the screening is
     * required. Should an invalid parameter be given, an InvalidParameterException is thrown.
     * @param id This is the screening ID. If this is less than 0, then an InvalidParameterException will be thrown.
     * @param filmID This is the film ID. If this is less than 0, then an InvalidParameterException will be thrown.
     * @param time This is the date and time of the screening, stored as a Calender object. If this is null, an InvalidParameterException will be thrown.
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
