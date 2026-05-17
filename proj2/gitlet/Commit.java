package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.serialize;
import static gitlet.Utils.sha1;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    public String message;
    public long timeStamp;
    public Map<String, String> fileBlobs;
    public String[] parentCommits;
    /* TODO: fill in the rest of this class. */
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
            System.out.println("Merge: " + parentCommits[0].substring(0,8) + " " + parentCommits[1].substring(0,8));
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
}
