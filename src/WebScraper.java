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
    public static void scrape(String url) {
        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            System.err.println("Error: " + ex);
        }

        assert doc != null;

        ArrayList<Job> currentJobs = new ArrayList<>();
        Elements allJobs = doc.select(".feature");

        for (Element job :
                allJobs) {
            String postDate = job.getElementsByTag("time").attr("datetime");

            if (wasPostedWithinThreshold(postDate)) {
                Job currentJob = new Job();

                currentJob.setJobTitle(job.getElementsByClass("title").text());
                currentJob.setCompanyName(job.selectFirst(".company").text());
                currentJob.setDatePosted(postDate.substring(5, 7) + "/" + postDate.substring(8, 10));
                currentJob.setLink(url + job.getElementsByAttributeValueContaining("href", "remote-jobs").attr("href"));

                currentJobs.add(currentJob);
            }
        }

        printResults(url, currentJobs);
    }

    /**
     * Prints the jobs that were posted within a given timeframe to the console.
     *
     * @param url A string containing the url of the scraped website
     * @param currentJobs An ArrayList of Job objects that fit the timeframe
     */
    public static void printResults(String url, ArrayList<Job> currentJobs) {
        int count = 0;

        for (Job currentJob : currentJobs) {
            System.out.println(currentJob);
            count++;
        }

        System.out.println(count + " jobs were posted on " + url + " in the last 7 days");
    }

    /**
     * Takes the datetime attribute of a job post and returns whether that date occurs within 7 days of the provided
     * date.
     *
     * @param postDate a string containing the value from the datetime attribute from an HTML time element
     * @param currentMonth an int representing the month of the comparison date
     * @param currentDate an int representing the day of the comparison date
     * @return boolean
     */
    public static boolean wasPostedWithinThreshold(String postDate, int currentMonth, int currentDate) {
        if (postDate.length() > 0) {
            int prevMonth = currentMonth - 1;
            int postMonth = Integer.parseInt(postDate.substring(5, 7));
            int postDay = Integer.parseInt(postDate.substring(8, 10));

            if (postMonth == currentMonth && postDay >= currentDate - 7) {
                return true;
            } else if (postMonth == prevMonth && currentDate - 7 < 1) {
                int dayDifferential = currentDate - 7;
                int lastDayOfMonth = 0;
                int[] longerMonths = {1, 3, 5, 7, 8, 10, 12};
                int[] standardMonths = {4, 6, 9, 11};
                int longerMonthsIndex = Arrays.binarySearch(longerMonths, prevMonth);
                int standardMonthsIndex = Arrays.binarySearch(standardMonths, prevMonth);

                if (prevMonth == 2) {
                    lastDayOfMonth = 28;
                } else if (longerMonthsIndex > 0) {
                    lastDayOfMonth = 31;
                } else if (standardMonthsIndex > 0) {
                    lastDayOfMonth = 30;
                }

                return postDay >= (lastDayOfMonth + dayDifferential);
            }
        }

        return false;
    }

    /**
     * Takes the datetime attribute of a job post and returns whether that date occurs in the last 7 days.
     *
     * @param postDate a string containing the value from the datetime attribute from an HTML time element
     * @return boolean
     */
    public static boolean wasPostedWithinThreshold(String postDate) {
        if (postDate.length() > 0) {
            ZoneId zonedId = ZoneId.of("America/New_York");
            LocalDate today = LocalDate.now(zonedId);
            int thisMonth = today.getMonthValue();
            int todaysDate = today.getDayOfMonth();

            return wasPostedWithinThreshold(postDate, thisMonth, todaysDate);
        }

        return false;
    }
}
