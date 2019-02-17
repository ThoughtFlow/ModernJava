package chap11;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static lab.util.Util.sleep;

public class ProcessHandler {

    private static void start(String programName) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        Process process = builder.command(programName).redirectOutput(ProcessBuilder.Redirect.to(new File("/tmp/log.out"))).start();

        process.descendants().forEach(System.out::println);
    }

    private static Optional<ProcessHandle> find(String programName) {
        return ProcessHandle.allProcesses().filter(p -> p.info().commandLine().filter(s -> s.contains(programName)).isPresent()).findFirst();

    }
    private static void kill(String programName) {

        Optional<ProcessHandle> handle = find(programName);
        if (handle.isPresent()) {
            System.out.println("Found pid: " + handle.get().pid());
            ProcessHandle.of(handle.get().pid()).ifPresent(ProcessHandle::destroy);
            System.out.println("Destroyed: " + handle.get().pid());
        }
    }

    private static void stats(String program) {
        Optional<ProcessHandle> handle = find(program);
        if (handle.isPresent()) {
            System.out.println("User is: " + handle.get().info().user().orElseGet(() ->"Unknown"));
            System.out.println("CPU usage: " + handle.get().info().totalCpuDuration().orElseGet(() -> Duration.ZERO).getSeconds());
        }

    }

    public static void main(String... args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage " + ProcessHandler.class + " programName");
        }
        else {
            start(args[0]);

            stats(args[0]);
            
            sleep(5000);
            kill(args[0]);
        }
    }
}
