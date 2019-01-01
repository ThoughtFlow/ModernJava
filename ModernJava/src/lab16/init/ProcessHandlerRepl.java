package lab16.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ProcessHandlerRepl {

    /**
     * Starts the given program.
     *
     * @param programName Program to start.
     * @param output Std out
     * @throws IOException Thrown if the output file could not be created.
     */
    private static void start(String programName, String output) throws IOException {
        // Implement this method
    }

    /**
     * Prints the pid of the given program.
     *
     * @param programName Program to find.
     */
    private static void find(String programName) {
        // Implement this method
    }

    /**
     * KIlls the process with the given PID.
     *
     * @param pid The PID of the program to kill.
     */
    private static void kill(long pid) {
        // Implement this method
    }

    /**
     * Prints process stats for the given PID.
     *
     * @param pid The pid for which to provide the stats.
     */
    private static void stats(long pid) {
        // Implement this method
    }

    /**
     * Prints all of the PIDs of this host.
     */
    private static void all() {
        // Implement this method
    }

    /**
     * Parses the command to execute.
     *
     * @param input The buffered system in.
     * @return The command line tokenized by spaces
     * @throws IOException
     */
    private static List<String> getCommand(BufferedReader input) throws IOException {
        System.out.print("Process shell> ");
        String line = input.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line, " ");

        List<String> command = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            command.add(tokenizer.nextToken());
        }

        return command;
    }

    private static void runRepl() {
        final String usage =
                "Help: \n" +
                "  command [options] \n" +
                "    all: lists all processes \n" +
                "    start programName outputFile: starts the given program and sends output to given path \n" +
                "    find programName: Returns the PID for the given program mname\n" +
                "    kill pid: Kills the program with the given PID \n" +
                "    stats pid: Returns stats about the given PID \n" +
                "    exit: terminate the shell";

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));


        boolean isExit = false;
        while (!isExit) {
            try {
                List<String> command = getCommand(input);

                switch (command.get(0)) {
                    case "all": {
                        all();
                    }
                    break;

                    case "start": {
                        if (command.size() == 3) {
                            String programName = command.get(1);
                            String outputFile = command.get(2);
                            start(programName, outputFile);
                        } else {
                            System.out.println(usage);
                        }
                    }
                    break;

                    case "find": {
                        if (command.size() == 2) {
                            String programName = command.get(1);
                            find(programName);
                        } else {
                            System.out.println(usage);
                        }
                    }
                    break;

                    case "stats": {
                        if (command.size() == 2) {
                            String pid = command.get(1);
                            stats(Long.valueOf(pid));
                        } else {
                            System.out.println(usage);
                        }
                    }
                    break;

                    case "kill": {
                        if (command.size() == 2) {
                            String pid = command.get(1);
                            kill(Long.valueOf(pid));
                        } else {
                            System.out.println(usage);
                        }
                    }
                    break;

                    case "exit": {
                        isExit = true;
                        break;
                    }

                    default: {
                        System.out.println(usage);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void main(String... args) {
        runRepl();
    }
}
