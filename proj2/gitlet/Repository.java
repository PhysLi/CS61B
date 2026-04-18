package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
    private static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    private static final File GITLET_DIR = join(CWD, ".gitlet");
    private static final File COMMITS_DIR = join(CWD, "commits");
    private static final File BLOBS_DIR = join(CWD, "blobs");
    private static final File STAGED_DIR = join(CWD, "staged");
    private static RepoConfig config;
    /* TODO: fill in the rest of this class. */

    private static class RepoConfig implements Serializable {
        public Map<String, Commit> branchHeaders;
        public Commit head;
    }

    public static void initialize() throws IOException {
        if (GITLET_DIR.exists()) {
            throw error("A Gitlet version-control system already exists in the current directory.");
        }

        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        STAGED_DIR.mkdir();

        RepoConfig configObj = new RepoConfig();
        Commit initial = commit("initial commit.");
        Map<String, Commit> branchHeaders = new HashMap<>();
        branchHeaders.put("masterInit", initial);

        configObj.branchHeaders = branchHeaders;
        configObj.head = initial;

        File repoConfig = join(GITLET_DIR, "config");
        writeContents(repoConfig, configObj);
        repoConfig.createNewFile();
    }

    public static void loadConfig() {
        File configFile = join(GITLET_DIR, "config");
        if (configFile.exists()) {
            config = readObject(configFile, RepoConfig.class);
        }
    }

    public static void add(String fileName) throws IOException {
        File file = join(CWD, fileName);
        Blob blob = new Blob(fileName, readContentsAsString(file));
        String hash = sha1(blob);

        File stagedBlob = join(STAGED_DIR, hash);
        List<String> filesInHead = config.head.fileBlobs;

        if (stagedBlob.exists()) {
            stagedBlob.delete();
        }
        if (!filesInHead.contains(hash)) {
            stagedBlob.createNewFile();
        }

    }

    public static Commit commit(String message) {
        return new Commit();
    }

    public static void remove(String filename) {

    }
}
