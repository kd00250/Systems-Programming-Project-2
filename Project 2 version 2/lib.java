import java.io.*;
import java.util.concurrent.BlockingQueue;

public class lib {

    public static void process_input_file(File[] branchFolders, BlockingQueue<String> queue) {
        boolean errorOccurred = false;
        
        for (File folder : branchFolders) {
            try {
                File inputFile = new File(folder, "branch_weekly_sales.txt");
                
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                String branchCode = "";
                String productCode = "PROD001";
                int total = 0;
                String line;
                
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        branchCode = parts[0].trim();
                        int quantity = Integer.parseInt(parts[2].trim());
                        total += quantity;
                    }
                }
                reader.close();
                String message = branchCode + "," + productCode + "," + total;
                queue.put(message);
                log("Processed folder: " + folder.getName());

            } catch (FileNotFoundException e) {
                errorOccurred = true;
                log("File not found" + e.getMessage());
                try {
                    queue.put("ERROR," + folder.getName());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } catch (IOException e) {
                errorOccurred = true;
                log("Error reading folder " + folder.getName() + ": " + e.getMessage());
                try {
                    queue.put("ERROR," + folder.getName());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } catch (NumberFormatException e) {
                errorOccurred = true;
                log("Error parsing number in folder " + folder.getName() + ": " + e.getMessage());
                try {
                    queue.put("ERROR," + folder.getName());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                errorOccurred = true;
                Thread.currentThread().interrupt();
                log("Thread interrupted while processing folder: " + folder.getName());
            }
        }

        try {
            if (errorOccurred) {
                queue.put("ERROR");
                log("ERROR");
            } else {
                queue.put("OK");
                log("OK");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void log(String text) {
        try (FileWriter logWriter = new FileWriter("log.txt", true)) {
            logWriter.write(text + "\n");
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }
}