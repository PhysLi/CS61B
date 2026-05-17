package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        Repository.loadConfig();
        String firstArg = args[0];
        try {
            switch(firstArg) {
                case "init":
                    Repository.initialize();
                    // TODO: handle the `init` command
                    break;
                case "add":
                    Repository.add(args[1]);
                    // TODO: handle the `add [filename]` command
                    break;
                case "rm":
                    Repository.remove(args[1]);
                    break;
                case "commit":
                    Repository.commit(args[1], null);
                    break;
                case "log":
                    Repository.log();
                    break;
                case "global-log":
                    Repository.globalLog();
                    break;
                case "status":
                    Repository.status();
                    break;
                case "find":
                    Repository.find(args[1]);
                    break;
                case "checkout":
                    switch (args.length) {
                        case 2:
                            Repository.checkout(args[1], 1);
                            break;
                        case 3:
                            Repository.checkout(args[2]);
                            break;
                        case 4:
                            Repository.checkout(args[1], args[3]);
                            break;
                    }
                    break;
                case "branch":
                    Repository.branch(args[1]);
                    break;
                case "rm-branch":
                    Repository.removeBranch(args[1]);
                    break;
                case "reset":
                    Repository.reset(args[1]);
                    break;
                case "merge":
                    Repository.merge(args[1]);
                    break;
                default:
                    return;

                // TODO: FILL THE REST IN
            }
        } catch (IOException e) {
            System.exit(0);
        }

    }
}
