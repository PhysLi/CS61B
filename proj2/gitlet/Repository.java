package gitlet;

import net.sf.saxon.trans.SymbolicName;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

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
    private static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    private static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    private static final File STAGED_DIR = join(GITLET_DIR, "staged");
    private static final File CONFIG_FILE = join(GITLET_DIR, "config");
    private static RepoConfig config;

    /* TODO: fill in the rest of this class. */

    private static class RepoConfig implements Serializable {
        public Map<String, Commit> branchHeaders;
        public Commit head;
        public String currentBranch;
        private Map<String, String> stagedForAdd;
        private Set<String> stagedForRM;

        public RepoConfig() {
            head = null;
            currentBranch = "master";
            stagedForAdd = new HashMap<>();
            stagedForRM = new HashSet<>();
            branchHeaders = new HashMap<>();
        }
    }

    public static void initialize() throws IOException {
        if (GITLET_DIR.exists()) {
            throw error("A Gitlet version-control system already exists in the current directory.");
        }

        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        STAGED_DIR.mkdir();

        config = new RepoConfig();
        commit("initial commit");
        saveConfig();
    }

    public static void saveConfig() throws IOException {
        writeObject(CONFIG_FILE, config);
        CONFIG_FILE.createNewFile();
    }

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            config = readObject(CONFIG_FILE, RepoConfig.class);
        }
    }

    public static void add(String fileName) throws IOException {
        File file = join(CWD, fileName);

        if (!file.exists()) {
            throw error("File does not exist.");
        }

        Blob blob = new Blob(fileName, readContentsAsString(file));
        String hash = sha1((Object) serialize(blob));

        File stagedBlob = join(STAGED_DIR, hash);
        writeObject(stagedBlob, blob);

        if (config.stagedForAdd.containsValue(hash)) {
            stagedBlob.delete();
            config.stagedForAdd.remove(fileName);
        }
        if (!config.head.fileBlobs.containsValue(hash)) {
            stagedBlob.createNewFile();
            config.stagedForAdd.put(fileName, hash);
        }

        saveConfig();
    }

    public static void remove(String filename) throws IOException {
        int flag = 0;

        String blobHash = config.stagedForAdd.get(filename);
        if (blobHash != null) {
            File stagedBlob = join(STAGED_DIR, blobHash);
            stagedBlob.delete();
            config.stagedForAdd.remove(filename);
            flag++;
        }
        if (config.head.fileBlobs.containsKey(filename)) {
            config.stagedForRM.add(filename);
            File fileCWD = join(CWD, filename);
            if (fileCWD.exists()) {
                fileCWD.delete();
            }
            flag++;
        }
        if (flag == 0) {
            throw error("No reason to remove the file.");
        }
        saveConfig();
    }

    public static Commit commit(String message) throws IOException {
        Commit newCommit;
        if (config.head == null) {
            newCommit = new Commit(message, 0, new HashMap<>());
        } else {
            newCommit = new Commit(message, System.currentTimeMillis(), config.head.fileBlobs, sha1((Object) serialize(config.head)));
            for (String filename : config.stagedForRM) {
                newCommit.fileBlobs.remove(filename);
            }
            config.stagedForRM.clear();

            for (String filename : config.stagedForAdd.keySet()) {
                String hash = config.stagedForAdd.get(filename);
                newCommit.fileBlobs.put(filename, hash);
                File fileBlob = join(BLOBS_DIR, hash);
                File fileStaged = join(STAGED_DIR, hash);
                writeObject(fileBlob, readObject(fileStaged, Blob.class));
                fileBlob.createNewFile();
                fileStaged.delete();
            }
        }

        String hashCommit = sha1((Object) serialize(newCommit));
        File commitFile = join(COMMITS_DIR, hashCommit);
        writeObject(commitFile, newCommit);

        config.head = newCommit;
        saveConfig();
        return newCommit;
    }

    public static void log() {
        Commit current = config.head;
        String[] parents = current.printCommit();
        while (parents.length != 0) {
            current = readObject(join(COMMITS_DIR, parents[0]), Commit.class);
            parents = current.printCommit();
        }
    }

    public static void globalLog() {
        List<String> allCommits = plainFilenamesIn(COMMITS_DIR);
        if (allCommits != null) {
            for (String hash : allCommits) {
                Commit current = readObject(join(COMMITS_DIR, hash), Commit.class);
                current.printCommit();
            }
        }
    }

    public static void status() {
        System.out.println("=== Branches ===");
        for (String branchName : config.branchHeaders.keySet()) {
            if (config.currentBranch.equals(branchName)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }

        System.out.println("\n=== Staged Files ===");
        for (String stagedAdd : config.stagedForAdd.keySet()) {
            System.out.println(stagedAdd);
        }

        System.out.println("\n===Removed Files===");
        for (String stagedRM : config.stagedForRM) {
            System.out.println(stagedRM);
        }

        System.out.println("\n=== Modifications Not Staged For Commit ===");
        System.out.println("\n=== Untracked Files ===");
    }


}
