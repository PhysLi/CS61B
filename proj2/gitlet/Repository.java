package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static gitlet.Utils.*;

//

/** Represents a gitlet repository.
 *
 *  does at a high level.
 *
 *  @author
 */
public class Repository {
    /**
     *
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

    /*  */

    private static class RepoConfig implements Serializable {
        private Map<String, Commit> branchHeaders;
        private Commit head;
        private String currentBranch;
        private Map<String, String> stagedForAdd;
        private Set<String> stagedForRM;

        RepoConfig() {
            head = null;
            currentBranch = "master";
            stagedForAdd = new HashMap<>();
            stagedForRM = new HashSet<>();
            branchHeaders = new HashMap<>();
        }
        
        public Map<String, Commit> getBranchHeaders() {
            return branchHeaders;
        }
        public Commit getHead() {
            return head;
        }
        public void setHead(Commit head) {
            this.head = head;
        }
        public String getCurrentBranch() {
            return currentBranch;
        }
        public void setCurrentBranch(String branch) {
            this.currentBranch = branch;
        }
        public Map<String, String> getStagedForAdd() {
            return stagedForAdd;
        }
        public Set<String> getStagedForRM() {
            return stagedForRM;
        }
    }
    
    public static File getGitletDir() {
        return GITLET_DIR;
    }

    public static void initialize() throws IOException {
        if (GITLET_DIR.exists()) {
            message("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        GITLET_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BLOBS_DIR.mkdir();
        STAGED_DIR.mkdir();

        config = new RepoConfig();
        Commit initCommit = commit("initial commit", null);
        config.getBranchHeaders().put("master", initCommit);
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
            message("File does not exist.");
            System.exit(0);
        }

        Blob blob = new Blob(fileName, readContentsAsString(file));
        String hash = sha1((Object) serialize(blob));

        File stagedBlob = join(STAGED_DIR, hash);
        writeObject(stagedBlob, blob);

        if (config.getStagedForAdd().containsValue(hash)) {
            stagedBlob.delete();
            config.getStagedForAdd().remove(fileName);
        }
        if (!config.getHead().getFileBlobs().containsValue(hash)) {
            stagedBlob.createNewFile();
            config.getStagedForAdd().put(fileName, hash);
        }

        saveConfig();
    }

    public static void remove(String filename) throws IOException {
        int flag = 0;

        String blobHash = config.getStagedForAdd().get(filename);
        if (blobHash != null) {
            File stagedBlob = join(STAGED_DIR, blobHash);
            stagedBlob.delete();
            config.getStagedForAdd().remove(filename);
            flag++;
        }
        if (config.getHead().getFileBlobs().containsKey(filename)) {
            config.getStagedForRM().add(filename);
            File fileCWD = join(CWD, filename);
            if (fileCWD.exists()) {
                fileCWD.delete();
            }
            flag++;
        }
        if (flag == 0) {
            message("No reason to remove the file.");
            System.exit(0);
        }
        saveConfig();
    }

    public static Commit commit(String message, Commit mergeGiven) throws IOException {
        if (config.getStagedForAdd().isEmpty() && config.getStagedForRM().isEmpty()) {
            message("No changes added to the commit.");
            System.exit(0);
        } else if (message == "") {
            message("Please enter a commit message.");
            System.exit(0);
        }
        Commit newCommit;
        String headHash = sha1((Object) serialize(config.getHead()));
        long currentTime = System.currentTimeMillis();
        if (config.getHead() == null) {
            newCommit = new Commit(message, 0, new HashMap<>());
        } else {
            if (mergeGiven == null) {
                newCommit = new Commit(message, currentTime, config.getHead().getFileBlobs(), headHash);
            } else {
                newCommit = new Commit(message, currentTime, config.getHead().getFileBlobs(), headHash, sha1((Object) serialize(mergeGiven)));
            }

            for (String filename : config.getStagedForRM()) {
                newCommit.getFileBlobs().remove(filename);
            }

            for (String filename : config.getStagedForAdd().keySet()) {
                String hash = config.getStagedForAdd().get(filename);
                newCommit.getFileBlobs().put(filename, hash);
                File fileBlob = join(BLOBS_DIR, hash);
                writeObject(fileBlob, readObject(join(STAGED_DIR, hash), Blob.class));
                fileBlob.createNewFile();
            }
        }

        String hashCommit = sha1((Object) serialize(newCommit));
        File commitFile = join(COMMITS_DIR, hashCommit);
        writeObject(commitFile, newCommit);

        config.setHead(newCommit);
        config.getBranchHeaders().put(config.getCurrentBranch(), newCommit);
        clearStaged();
        return newCommit;
    }

    public static void log() {
        Commit current = config.getHead();
        String[] parents = current.printCommit();
        while (parents.length != 0) {
            current = readObject(join(COMMITS_DIR, parents[0]), Commit.class);
            parents = current.printCommit();
        }
    }

    private static Commit[] listAllCommits() {
        List<String> allCommitsHash = plainFilenamesIn(COMMITS_DIR);
        Commit[] allCommits = new Commit[allCommitsHash.toArray().length];
        int i = 0;
        for (String hash : allCommitsHash) {
            allCommits[i] = readObject(join(COMMITS_DIR, hash), Commit.class);
            i++;
        }
        return allCommits;
    }
    public static void globalLog() {
        Commit[] allCommits = listAllCommits();
        for (int i = 0; i < allCommits.length; i++) {
            allCommits[i].printCommit();
        }
    }

    public static void status() {
        System.out.println("=== Branches ===");
        for (String branchName : config.getBranchHeaders().keySet()) {
            if (config.getCurrentBranch().equals(branchName)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }

        System.out.println("\n=== Staged Files ===");
        for (String stagedAdd : config.getStagedForAdd().keySet()) {
            System.out.println(stagedAdd);
        }

        System.out.println("\n=== Removed Files ===");
        for (String stagedRM : config.getStagedForRM()) {
            System.out.println(stagedRM);
        }

        System.out.println("\n=== Modifications Not Staged For Commit ===");
        System.out.println("\n=== Untracked Files ===\n");
    }

    public static void find(String message) {
        Commit[] allCommits = listAllCommits();
        int found = 0;
        for (int i = 0; i < allCommits.length; i++) {
            if (allCommits[i].getMessage().equals(message)) {
                System.out.println(sha1((Object) serialize(allCommits[i])));
                found++;
            }
        }
        if (found == 0) {
            message("Found no commit with that message.");
            System.exit(0);
        }
    }


    public static void checkout(String commitHash, String filename) throws IOException {
        if (!Objects.requireNonNull(plainFilenamesIn(COMMITS_DIR)).contains(commitHash)) {
            message("No commit with that id exists.");
            System.exit(0);
        }
        Commit targetCommit = readObject(join(COMMITS_DIR, commitHash), Commit.class);
        String blobHash = targetCommit.getFileBlobs().get(filename);
        if (blobHash == null) {
            message("File does not exist in that commit.");
            System.exit(0);
        }

        File fileCWD = join(CWD, filename);
        Blob blob = readObject(join(BLOBS_DIR, blobHash), Blob.class);
        fileCWD.createNewFile();
        writeContents(fileCWD, blob.getContent());

        String stagedHash = config.getStagedForAdd().get(filename);
        if (stagedHash != null) {
            join(STAGED_DIR, stagedHash).delete();
            config.getStagedForAdd().remove(filename);
        }
        config.getStagedForRM().remove(filename);
        saveConfig();
    }

    public static void checkout(String filename) throws IOException {
        checkout(sha1((Object) serialize(config.getHead())), filename);
    }

    public static void checkout(String branchName, int flag) throws IOException {
        if (!config.getBranchHeaders().containsKey(branchName)) {
            message("No such branch exists.");
            System.exit(0);
        } else if (config.getCurrentBranch().equals(branchName)) {
            message("No need to checkout the current branch.");
            System.exit(0);
        }
        config.setCurrentBranch(branchName);
        Commit targetCommit = config.getBranchHeaders().get(branchName);
        reset(sha1((Object) serialize(targetCommit)));
    }

    private static void clearStaged() throws IOException {
        config.getStagedForAdd().clear();
        config.getStagedForRM().clear();
        for (String hash : Objects.requireNonNull(plainFilenamesIn(STAGED_DIR))) {
            join(STAGED_DIR, hash).delete();
        }
        saveConfig();
    }

    public static void reset(String commitHash) throws IOException {
        if (!plainFilenamesIn(COMMITS_DIR).contains(commitHash)) {
            message("No commit with that id exists.");
            System.exit(0);
        }

        Commit targetCommit = readObject(join(COMMITS_DIR, commitHash), Commit.class);
        config.setHead(targetCommit);

        String targetHash =  sha1((Object) serialize(targetCommit));
        for (String fileCWD : plainFilenamesIn(CWD)) {
            if (!targetCommit.getFileBlobs().containsKey(fileCWD)) {
                join(CWD, fileCWD).delete();
            } else {
                checkout(targetHash, fileCWD);
            }
        }
        clearStaged();
    }

    public static void branch(String branchName) throws IOException {
        if (config.getBranchHeaders().containsKey(branchName)) {
            message("A branch with that name already exists.");
            System.exit(0);
        }
        config.getBranchHeaders().put(branchName, config.getHead());
        saveConfig();
    }

    public static void removeBranch(String branchName) throws IOException {
        if (!config.getBranchHeaders().containsKey(branchName)) {
            message("A branch with that name does not exists.");
            System.exit(0);
        } else if (config.getCurrentBranch().equals(branchName)) {
            message("Cannot remove the current branch.");
            System.exit(0);
        }
        config.getBranchHeaders().remove(branchName);
        saveConfig();
    }

    private static Commit findSplit(Commit head1, Commit head2) {
        Commit hold, split, parent;
        String[] parentCommits;
        if (head1.getTimeStamp() == head2.getTimeStamp()) {
            return head1;
        } else if (head1.getParentCommits().length * head2.getParentCommits().length == 0) {
            return null;
        }

        if (head1.getTimeStamp() > head2.getTimeStamp()) {
            parentCommits = head1.getParentCommits();
            hold = head2;
        } else {
            parentCommits = head2.getParentCommits();
            hold = head1;
        }

        for (String parentCommit : parentCommits) {
            parent = readObject(join(COMMITS_DIR, parentCommit), Commit.class);
            split = findSplit(parent, hold);
            if (split != null) {
                return split;
            }
        }
        return null;
    }

    public static void merge(String branchName) throws IOException {
        if (!config.getStagedForAdd().isEmpty() || !config.getStagedForRM().isEmpty()) {
            message("You have uncommitted changes.");
            System.exit(0);
        } else if (!config.getBranchHeaders().containsKey(branchName)) {
            message("A branch with that name does not exist.");
            System.exit(0);
        } else if (branchName.equals(config.getCurrentBranch())) {
            message("Cannot merge a branch with itself.");
            System.exit(0);
        }
        Commit head1 = config.getBranchHeaders().get(config.getCurrentBranch());
        Commit head2 = config.getBranchHeaders().get(branchName);
        Commit split = findSplit(head1, head2);
        if (split.equals(head2)) {
            message("Given branch is an ancestor of the current branch.");
            System.exit(0);
        } else if (split.equals(head1)) {
            checkout(branchName, 0);
            message("Current branch fast-forwarded.");
            System.exit(0);
        }

        String curHash, givHash, splitHash;
        Set<String> fullFiles = head1.getFileBlobs().keySet();
        Set<String> givenFiles = head2.getFileBlobs().keySet();
        Set<String> splitFiles = split.getFileBlobs().keySet();
        fullFiles.addAll(givenFiles);
        fullFiles.addAll(splitFiles);

        int confFlag = 0;
        for (String filename : fullFiles) {
            curHash = head1.getFileBlobs().get(filename);
            givHash = head2.getFileBlobs().get(filename);
            splitHash = split.getFileBlobs().get(filename);
            fullFiles.remove(filename);
            boolean splitCur = Objects.equals(splitHash, curHash);
            boolean splitGiv = Objects.equals(splitHash, givHash);
            boolean curGiv = Objects.equals(curHash, givHash);

            if (curHash != null && givHash == null && splitHash == null) { //case4
                continue;
            } else if (givHash != null && curHash == null && splitHash == null) { //case5
                checkout(sha1((Object) serialize(head2)), filename);
                add(filename);
            } else if (splitCur && givHash == null) { //case6
                remove(filename);
            } else if (splitGiv && curHash == null) { //case7
                continue;
            } else  if (splitCur && !splitGiv) { //case1
                checkout(sha1((Object) serialize(head2)), filename);
                add(filename);
            } else if (!splitCur && splitGiv) { //case2
                continue;
            } else if (curGiv) {//case3
                continue;
            } else {
                String curContent = readObject(join(BLOBS_DIR, curHash), Blob.class).getContent();
                String givContent = readObject(join(BLOBS_DIR, givHash), Blob.class).getContent();
                String confContent = "<<<<<<< HEAD\n" + curContent + "=======" + givContent + ">>>>>>>";

                File fileCWD = join(CWD, filename);
                fileCWD.createNewFile();
                writeContents(fileCWD, confContent);
                add(filename);
                confFlag = 1;
            }
            commit("Merge " + branchName + " into " + config.getCurrentBranch() + ".", head2);
            if (confFlag == 1) {
                System.out.println("Encountered a merge conflict.");
            }
        }
    }
}
