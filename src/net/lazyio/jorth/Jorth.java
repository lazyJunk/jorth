package net.lazyio.jorth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class Jorth {

    public static final String NAME = "jorth";
    public static final String VERSION = "0.0.0.1";
    public static final List<String> EXTENSIONS = Collections.singletonList("porth");

    private static String testDivColor = "\u001B[32m";
    private static String finishColor = "\033[0;36m";
    private static String resetColor = "\033[0m";

    public static void main(String[] args) throws IOException {
        boolean verbose = args.length > 1 && args[1].equals("-v");
        var start = Instant.now();
        if (args[0].equals("--alltests")) {
            Files.list(Paths.get("test/")).forEach(path -> {
                try {
                    System.out.printf(testDivColor + "===> Running test: %s <===%s\n", path.getFileName(), resetColor);
                    List<String> porthLines = Files.readAllLines(path);
                    Porth.sim(porthLines, verbose, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                var filePath = Paths.get(args[0]);
                System.out.printf(testDivColor + "===> Running test: %s <===%s\n", filePath.getFileName(), resetColor);
                List<String> porthLines = Files.readAllLines(filePath);
                Porth.sim(porthLines, verbose, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        var end = Instant.now();
        var duration = Duration.between(start, end);
        System.out.printf(finishColor + "===> Overall time: %sm:%ss:%sms <===\n", duration.toMinutes(), duration.toSeconds(), duration.toMillis());
    }
}
