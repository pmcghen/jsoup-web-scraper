public class Job {
    public static final String PURPLE = "\u001B[35m";
    public static final String RESET = "\u001B[0m";
    private String jobTitle;
    private String companyName;
    private String datePosted;
    private String link;

    public Job() {
        this.jobTitle = "";
        this.companyName ="";
        this.datePosted = "";
        this.link = "";
    }

    public Job(String jobTitle, String companyName, String datePosted, String link) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.datePosted = datePosted;
        this.link = link;
    }

    public void setJobTitle(String title) {
        this.jobTitle = title;
    }

    public void setCompanyName(String name) {
        this.companyName = name;
    }

    public void setDatePosted(String date) {
        this.datePosted = date;
    }

    public void setLink(String url) {
        this.link = url;
    }

    public String toString() {
        return "\uD83E\uDEAA" + PURPLE + " Title: " + RESET + jobTitle + "\n"
        + "\uD83C\uDFEC" + PURPLE + " Company: " + RESET + companyName + "\n"
        + "\uD83D\uDDD3\uFE0F" + PURPLE + " Date Posted: " + RESET + datePosted + "\n"
        + "\uD83C\uDF10" + PURPLE + " Link: " + RESET + link + "\n----------";
    }
}
