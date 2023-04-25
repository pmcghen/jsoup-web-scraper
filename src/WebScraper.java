import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;

public class WebScraper {
    public static final String PURPLE = "\u001B[35m";
    public static final String RESET = "\u001B[0m";

    public static void scrape(String url) {
        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            System.err.println("Error: " + ex);
        }

        assert doc != null;

        ArrayList<String> jobUrls = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> jobTitles = new ArrayList<>();
        ArrayList<String> companyName = new ArrayList<>();

        Elements jobs = doc.select(".feature");

        for (Element job :
                jobs) {
            dates.add(job.getElementsByTag("time").attr("datetime"));
            jobTitles.add(job.getElementsByClass("title").text());
            jobUrls.add(url + job.getElementsByAttributeValueContaining("href", "remote-jobs").attr("href"));
            companyName.add(job.selectFirst(".company").text());
        }

        int count = 0;

        for (int i = 0; i < dates.size(); i++) {
            if (wasPostedWithinThreshold(dates.get(i))) {
                System.out.println("\uD83E\uDEAA" + PURPLE + " Title: " + RESET + jobTitles.get(i));
                System.out.println("\uD83C\uDFEC" + PURPLE + " Company: " + RESET + companyName.get(i));
                System.out.println("\uD83D\uDDD3\uFE0F" + PURPLE + " Date Posted: " + RESET + dates.get(i).substring(5, 7) + "/" + dates.get(i).substring(8, 10));
                System.out.println("\uD83C\uDF10" + PURPLE + " Link: " + RESET + jobUrls.get(i));
                System.out.println("----------");
                count++;
            }
        }

        System.out.println(count + " jobs were posted on " + url + " in the last 7 days");
    }

    /**
     * Takes the datetime attribute of a job post and returns whether that date occurs in the last 7 days.
     *
     * @param datestamp a string containing the value from the datetime attribute from an HTML time element
     * @return boolean
     */
    public static boolean wasPostedWithinThreshold(String datestamp) {
        if (datestamp.length() > 0) {
            int postMonth = Integer.parseInt(datestamp.substring(5, 7));
            int postDay = Integer.parseInt(datestamp.substring(8, 10));

            ZoneId zonedId = ZoneId.of( "America/New_York" );
            LocalDate today = LocalDate.now( zonedId );
            int thisMonth = today.getMonthValue();
            int todaysDate = today.getDayOfMonth();
            int prevMonth = thisMonth - 1;

            if (postMonth == thisMonth && postDay >= todaysDate - 7) {
                return true;
            } else if (postMonth == prevMonth && todaysDate - 7 < 1) {
                int dayDifferential = todaysDate - 7;
                int lastDayOfMonth = 0;
                int[] longerMonths = {1, 3, 5, 7, 8, 10, 12};
                int[] standardMonths = {4, 6, 9, 11};
                int longerMonthsIndex = Arrays.binarySearch(longerMonths, prevMonth);
                int standardMonthsIndex = Arrays.binarySearch(standardMonths, prevMonth);

                if (prevMonth == 2) {
                    lastDayOfMonth = 28;
                }

                if (longerMonthsIndex > 0) {
                    lastDayOfMonth = 31;
                }

                if (standardMonthsIndex > 0) {
                    lastDayOfMonth = 30;
                }

                return postDay >= (lastDayOfMonth - dayDifferential);
            }
        }
        return false;
    }
}
