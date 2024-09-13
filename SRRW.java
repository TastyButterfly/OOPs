import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class SRRW {
    private List<StockRequest> SRList= new ArrayList<StockRequest>();
    @SuppressWarnings("unused")
    private static int index = 0;
    private List<Product> prod;
    private ProductDatabase pd;

    public SRRW() {
        pd=new ProductDatabase();
        prod=new ArrayList<Product>(pd.getProducts().values());
        readStaffProduct();
        if(!readFromFile()) return;
    }
    public void writeToFile(){
        try (FileWriter fw = new FileWriter("StockRequest.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw)) {

       // Optionally, write the header line if needed
       pw.println("Stock Request ID,Status,Product ID,Quantity,Outstanding,Size,Day,Month,Year,Hour,Minute,Second");

       for (StockRequest sr : SRList) {
           if (sr != null) {
               pw.println(sr.getSRID() + "," +
                           sr.getStatus() + "," +
                           sr.getProduct().getProdID() + "," +
                           sr.getQty() + "," +
                           sr.getOutstanding() + "," +
                           sr.getSize() + "," +
                           sr.getDate().getDay() + "," +
                           sr.getDate().getMonthValue() + "," +
                           sr.getDate().getYear() + "," +
                           sr.getDate().getHour() + "," +
                           sr.getDate().getMinute() + "," +
                           sr.getDate().getSecond());
           }
       }
    }catch (Exception e) {
            if(e instanceof IOException){
                JOptionPane.showMessageDialog(null, "Error writing to file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public boolean readFromFile(){
        Path path = Paths.get("StockRequest.csv");
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            SRList.clear(); // Clear the existing list to avoid duplicates

            // Skip the header line if present
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 12) {
                    StockRequest sr = new StockRequest(fields[0], fields[1], searchProduct(fields[2]), Integer.parseInt(fields[3]), Integer.parseInt(fields[4]), fields[5], Integer.parseInt(fields[6]), Integer.parseInt(fields[7]), Integer.parseInt(fields[8]), Integer.parseInt(fields[9]), Integer.parseInt(fields[10]), Integer.parseInt(fields[11]));
                    SRList.add(sr);
                    index++;
                }
            }
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading from file. File might not be found.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public Product searchProduct(String prodID){
        try{
            for (Product p : prod) {
                if (p.getProdID().equals(prodID)) {
                    return p;
                }
            }
            JOptionPane.showMessageDialog(null, "Product ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "Unexpected error occured.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
     public void readStaffProduct(){
        try (BufferedReader br = new BufferedReader(new FileReader("StaffProduct.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                for (int i = 4; i < parts.length; i++) {
                    searchProduct(parts[0]).getStaffQty()[i-4] = Integer.parseInt(parts[i]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading from file.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public List<StockRequest> getSRList(){
        return SRList;
    }
    public List<Product> getProd(){
        return prod;
    }
}
