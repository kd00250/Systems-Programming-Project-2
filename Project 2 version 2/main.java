import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class main {
    public static void main(String[] args) {
        File outputDir = new File("data/weekly_summary");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File dataDir = new File("data");
        File[] branchDir = dataDir.listFiles(file ->
                file.isDirectory() && !file.getName().equals("weekly_summary"));

        File[][] groups = {
            Arrays.copyOfRange(branchDir, 0, 10),
            Arrays.copyOfRange(branchDir, 10, 20), 
            Arrays.copyOfRange(branchDir, 20, 30),
            Arrays.copyOfRange(branchDir, 30, 40)
        };
       
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        long startTime = System.nanoTime();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < groups.length; i++) {
            final File[] group = groups[i];
            Thread thread = new Thread(() -> lib.process_input_file(group, queue));
            threads.add(thread);
            thread.start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrupted: " + e.getMessage());
            }
        }

        List<String> messages = new ArrayList<>();
        queue.drainTo(messages);

        for (String message : messages) {
            if (message.equals("OK") || message.equals("ERROR") || message.startsWith("ERROR,")) {
                System.out.println("Thread status: " + message);
            } else {
                System.out.println(message);
                write_to_summary_file(message);
            }
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        double timeInMs = totalTime / 1000000.0;
        System.out.println("\nTotal Execution Time: " + totalTime + " ns (" + timeInMs + " ms)");
        System.out.println("Phew! I am done.");
    }

    public static void write_to_summary_file(String message) {
        try (FileWriter writer = new FileWriter("data/weekly_summary/weekly_sales_summary.txt", true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            lib.log("Error writing to summary file: " + e.getMessage());
        }
    }
}
