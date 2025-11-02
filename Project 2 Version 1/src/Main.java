package src;

import java.io.File;

/**
 * Main class to run 
 */
public class Main {
    public static void main(String[] args) {

        File projectRoot = new File("Project 2 Version 1"); // adjust if needed
        File dataDir = new File(projectRoot, "data");

        // Create output folder 
        File outputDir = new File(dataDir, "weekly_summary");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Output summary file
        File summaryFile = new File(outputDir, "weekly_sales_summary.txt");

        // List branch folders
        File[] branchFolders = dataDir.listFiles(file ->
                file.isDirectory() && !file.getName().equals("weekly_summary"));

        if (branchFolders == null || branchFolders.length == 0) {
            System.out.println("No branch folders found in " + dataDir.getAbsolutePath());
            return;
        }

        System.out.println("Starting processing of " + branchFolders.length + " branches...");
        for (File f : branchFolders) {
            System.out.println("Found branch folder: " + f.getAbsolutePath());
        }

        long startTime = System.nanoTime();

        // Call library function
        String result = Lib.processInputFiles(branchFolders, summaryFile);
        System.out.println("Processing result: " + result);

        long endTime = System.nanoTime();
        double elapsedMs = (endTime - startTime) / 1_000_000.0;
        System.out.println("Total execution time: " + elapsedMs + " ms");
        System.out.println("Phew! I am done.");
    }
}
