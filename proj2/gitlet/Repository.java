package gitlet;

import java.io.File;
import java.util.Map;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMITS_DIR = join(CWD, "commits");
    public static final File BLOBS_DIR = join(CWD, "blobs");
    public static final File STAGED_DIR = join(CWD, "staged");

    private static Map<String, String> branchHeaders;
    private static String head;
    /* TODO: fill in the rest of this class. */

    public void initialize() {
        if (CWD.exists()) {
            throw error("A Gitlet version-control system already exists in the current directory.");
        }
        CWD.mkdir();
        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        STAGED_DIR.mkdir();

        Commit masterInit = new Commit();
        branchHeaders.put("masterInit", masterInit.sha1);
        head = masterInit.sha1;
    }
}
