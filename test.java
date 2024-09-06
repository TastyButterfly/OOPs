import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class test {

    public static void main(String[] args) {
        // Path to the text file
        String filePath = "test.txt";
        
        // Use BufferedReader to read the file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read each line from the file
            while ((line = br.readLine()) != null) {
                // Print the line to the console
                System.out.println(line);
            }
        } catch (IOException e) {
            // Handle potential IOException
            e.printStackTrace();
        }
    }
}
