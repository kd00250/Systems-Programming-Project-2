package src;

import java.io.*;

/**
 * lib class processes input files and log messages.
 */
public class Lib {

    /**
     * Reads branch's weekly sales file
     */
    public static String processInputFiles(File[] branchFolders, File outputFile) {
        boolean errorOccurred = false;

        File logFile = new File("Project 2 Version 1/log.txt");

        for (File folder : branchFolders) {
            try {
                File inputFile = new File(folder, "branch_weekly_sales.txt");
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));

                String branchCode = "";
                String productCode = "PROD001";
                int totalQuantity = 0;

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        branchCode = parts[0].trim();
                        int quantity = Integer.parseInt(parts[2].trim());
                        totalQuantity += quantity;
                    }
                }

                reader.close();

                String summaryLine = branchCode + ", " + productCode + ", " + totalQuantity;
                writeToSummaryFile(summaryLine, outputFile, logFile);
                log("Processed folder: " + folder.getName(), logFile);

            } catch (FileNotFoundException e) {
                errorOccurred = true;
                log("File not found: " + folder.getAbsolutePath(), logFile);
            } catch (IOException e) {
                errorOccurred = true;
                log("Error reading folder " + folder.getName() + ": " + e.getMessage(), logFile);
            } catch (NumberFormatException e) {
                errorOccurred = true;
                log("Error parsing number in folder " + folder.getName() + ": " + e.getMessage(), logFile);
            }
        }

        return errorOccurred ? "ERROR" : "OK";
    }

    /**
     * adds line to summary file
     */
    private static void writeToSummaryFile(String line, File outputFile, File logFile) {
        try (FileWriter writer = new FileWriter(outputFile, true)) {
            writer.write(line + "\n");
        } catch (IOException e) {
            log("Error writing to summary file: " + e.getMessage(), logFile);
        }
    }

    /**
     * Logs a message to log.txt
     */
    public static void log(String message, File logFile) {
        try (FileWriter logWriter = new FileWriter(logFile, true)) {
            logWriter.write(message + "\n");
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }
}
