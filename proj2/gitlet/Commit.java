package gitlet;



import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.serialize;
import static gitlet.Utils.sha1;

/** Represents a gitlet commit object.
 *
 *  does at a high level.
 *
 *  @author
 */
public class Commit implements Serializable {
    /**
     *
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private long timeStamp;
    private Map<String, String> fileBlobs;
    private String[] parentCommits;
    /*  */
    Commit(String message, long timeStamp, Map<String, String> fileBlobs, String... parentCommits) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.fileBlobs = fileBlobs;
        this.parentCommits = parentCommits;
    }

    public String[] printCommit() {
        System.out.println("===");
        System.out.println("commit " + sha1((Object) serialize(this)));

        if (parentCommits.length == 2) {
            String part1 = parentCommits[0].substring(0, 8);
            String part2 = parentCommits[1].substring(0, 8);
            System.out.println("Merge: " + part1 + " " + part2);
        }

        System.out.print("Date: ");
//        Formatter fmt = new Formatter(System.out);
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//        fmt.format(Locale.US,"%1$ta %1$tb %1$td %1$tT %1$tY %1$tz%n", timeStamp);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(timeStamp);
        Formatter fmt = new Formatter(System.out);
        fmt.format(Locale.US, "%1$ta %1$tb %1$td %1$tT %1$tY %1$tz%n", cal);

        System.out.println(message);
        System.out.println();

        return parentCommits;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Map<String, String> getFileBlobs() {
        return fileBlobs;
    }

    public String[] getParentCommits() {
        return parentCommits;
    }
}
