package gitlet;

import java.io.IOException;
import java.util.Objects;

import static gitlet.Utils.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    private static void operandError() {
        message("Incorrect operands.");
        System.exit(0);
    }

    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            operandError();
        }
    }
    public static void main(String[] args) {

        //
        if (args.length == 0) {
            message("Please enter a command.");
            System.exit(0);
        } else if (!Objects.equals(args[0], "init") && !Repository.getGitletDir().exists()) {
            message("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        Repository.loadConfig();
        String firstArg = args[0];
        try {
            switch(firstArg) {
                case "init":
                    validateNumArgs(args, 1);
                    Repository.initialize();
                    //
                    break;
                case "add":
                    validateNumArgs(args, 2);
                    Repository.add(args[1]);
                    //
                    break;
                case "rm":
                    validateNumArgs(args, 2);
                    Repository.remove(args[1]);
                    break;
                case "commit":
                    validateNumArgs(args, 2);
                    Repository.commit(args[1], null);
                    break;
                case "log":
                    validateNumArgs(args, 1);
                    Repository.log();
                    break;
                case "global-log":
                    validateNumArgs(args, 1);
                    Repository.globalLog();
                    break;
                case "status":
                    validateNumArgs(args, 1);
                    Repository.status();
                    break;
                case "find":
                    validateNumArgs(args, 2);
                    Repository.find(args[1]);
                    break;
                case "checkout":
                    switch (args.length) {
                        case 2:
                            Repository.checkout(args[1], 1);
                            break;
                        case 3:
                            if (!Objects.equals(args[1], "--")) {
                                operandError();
                            }
                            Repository.checkout(args[2]);
                            break;
                        case 4:
                            if (!Objects.equals(args[2], "--")) {
                                operandError();
                            }
                            Repository.checkout(args[1], args[3]);
                            break;
                        default:
                            operandError();
                    }
                    break;
                case "branch":
                    validateNumArgs(args, 2);
                    Repository.branch(args[1]);
                    break;
                case "rm-branch":
                    validateNumArgs(args, 2);
                    Repository.removeBranch(args[1]);
                    break;
                case "reset":
                    validateNumArgs(args, 2);
                    Repository.reset(args[1]);
                    break;
                case "merge":
                    validateNumArgs(args, 2);
                    Repository.merge(args[1]);
                    break;
                default:
                    message("No command with that name exists.");
                    System.exit(0);
                //
            }
        } catch (IOException e) {
            System.exit(0);
        }

    }
}
