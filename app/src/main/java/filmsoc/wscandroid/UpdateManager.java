package filmsoc.wscandroid;

import java.net.URL;
import java.util.Calendar;

/**
 * Created by Richard on 15/06/2016.
 * http://blog.mashape.com/list-of-40-tutorials-on-how-to-create-an-api/
 */
public class UpdateManager {

    public static void setData() {
        //Temporary data
        Film[] tmpFilm = new Film[4];
        tmpFilm[0] = new Film(3376, "The Jungle Book");
        tmpFilm[0].setYear(2016);
        tmpFilm[0].setRunTime(105);
        tmpFilm[0].setRating(Film.Rating.PG);
        tmpFilm[0].setDirector("Jon Favreau");
        tmpFilm[0].setStaring("Neel Sethi, Bill Murray, Ben Kingsley");
        Calendar[] showDates = new Calendar[2];
        showDates[0] = Calendar.getInstance();
        showDates[0].set(2016,5, 24, 18, 30);
        showDates[1] = Calendar.getInstance();
        showDates[1].set(2016,5, 24, 21, 30);
        tmpFilm[0].setShown(showDates);

        tmpFilm[1] = new Film(3377, "Zootropolis");
        tmpFilm[1].setReview("The film follows young Judy Hopps (Ginnifer Goodwin), the first bunny cop in the big city of Zootropolis, where mammals of all shapes and sizes live together in harmony. Raised in a family of carrot farmers, Judy has always been determined to make a difference in the world – but she’ll soon find out that this is no easy feat. As soon as she joins the force, she faces prejudice and gets demoted to a simple Meter Maid. When mammals start disappearing mysteriously and turning “savage”, Judy volunteers herself to solve the case in just 36 hours, roping in the help of sly fox Nick Wilde (Jason Bateman). Can Judy find the missing mammals and prove herself to be the best police officer there is? Or will her detective skills fail her and her dream job be lost forever? Full of laughs and beautiful animation, this film celebrates love and diversity in times of fear and hate.<P></P> <p><i>Jessica Carroll</i></p>");
        tmpFilm[1].setImage("https://warwick.film/image/wi/3404.jpeg?maxwidth=300&maxheight=300");
        tmpFilm[1].setYear(2016);
        tmpFilm[1].setRunTime(108);
        tmpFilm[1].setRating(Film.Rating.PG);
        tmpFilm[1].setDirector("Byron Howard, Rich Moore");
        tmpFilm[1].setStaring("Ginnifer Goodwin, Jason Bateman, Idris Elba");
        showDates = new Calendar[2];
        showDates[0] = Calendar.getInstance();
        showDates[0].set(2016, 5, 25, 18, 30);
        showDates[1] = Calendar.getInstance();
        showDates[1].set(2016, 5, 25, 21, 30);
        tmpFilm[1].setShown(showDates);

        tmpFilm[2] = new Film(3378, "Green Room");
        tmpFilm[2].setYear(2016);
        tmpFilm[2].setRunTime(94);
        tmpFilm[2].setRating(Film.Rating.EIGHTEEN);
        tmpFilm[2].setDirector("Jeremy Saulnier");
        tmpFilm[2].setStaring("Alia Shawkat, Imogen Poots, Anton Yelchin");
        showDates = new Calendar[3];
        showDates[0] = Calendar.getInstance();
        showDates[0].set(2016, 5, 26, 11, 00);
        showDates[1] = Calendar.getInstance();
        showDates[1].set(2016, 5, 26, 19, 30);
        showDates[2] = Calendar.getInstance();
        showDates[2].set(2016, 5, 29, 16, 30);
        tmpFilm[2].setShown(showDates);

        tmpFilm[3] = new Film(2095, "Shrek");
        tmpFilm[3].setReview("<p>On Tuesday 28th June (the last Tuesday of term) an event arrives that is so mammoth that Warwick Student Cinema can only hold it once a year: the Outdoor Screening! This year’s film is…. Shrek! Does it even need an introduction?  Undoubtedly the strongest of its trilogy, the film that started it all.  Come see all your favourite characters, and hear all the classic lines again. Rejected by a prejudiced world which views all ogres as evil and ugly, our hero Shrek (Mike Myers) is content to spend his life alone, having nothing to do with the rest of the world.  However, when the ruler of the land, Prince Farquaad (a superbly cast John Lithgow), wishes to rid the land of all mythical and magical creatures, they all seek refuge in Shrek’s swamp.  Prince Farquaad agrees to clear the swamp if Shrek will complete a small task for him.  He must climb to the highest room of the tallest tower, slaying the fire-breathing dragon, to rescue Princess Fiona so Prince Farquaad may marry her and become king.</p> <p>Shrek is not alone in his quest, however.  He is accompanied by the smooth-talking Donkey (Eddie Murphy), whose heart is as large as his mouth.  The only one so far able to look past Shrek’s exterior to see his kindness within.  Both social outcasts, a strong yet volatile friendship is formed between them. Will Princess Fiona escape her tower? Will Donkey survive the fire-breathing dragon?  And will Shrek finally get some peace and quiet?</p> <p>Full of laughs, magic, and larger-than-life characters, Shrek remains grounded by the honest emotion shown between the characters, giving it a delightfully heart-warming human centre.  Shrek is one of those rare gems which gets the balance just right between comedy for the young and the old.  The full gamut of humour is covered, from political satire, spoofs of popular culture, to even the occasional fart joke.  The perfect summer film, Shrek is definitely not to be missed! Hope to see you there… </p> <p><b>Natalie Tyldesley</b></P>");
        tmpFilm[3].setImage("https://warwick.film/image/wi/1630.jpeg?maxwidth=300&maxheight=300");
        tmpFilm[3].setYear(2001);
        tmpFilm[3].setRunTime(91);
        tmpFilm[3].setRating(Film.Rating.U);
        tmpFilm[3].setDirector("Andrew Adamson & Vicky Jenson");
        tmpFilm[3].setStaring("Mike Myers & Eddie Murphy");
        showDates = new Calendar[3];
        showDates[0] = Calendar.getInstance();
        showDates[0].set(2008, 5, 24, 22, 00);
        showDates[1] = Calendar.getInstance();
        showDates[1].set(2016, 5, 28, 22, 00);
        showDates[2] = Calendar.getInstance();
        showDates[2].set(2016, 5, 30, 10, 00);
        tmpFilm[3].setShown(showDates);

        ScheduleManager.addFilm(tmpFilm);
    }
}
