import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;

public class SRMenu{
    private static List<StockRequest> SRList= new ArrayList<StockRequest>();
    private static List<Product> prod;
    private static ProductDatabase pd;
    private static MyFrame mainFrame;
    private static String loggedInUserName="Staff";
    JPanel panel=new JPanel();
    JButton add=new JButton("Add Stock Out");
    JButton modify=new JButton("Modify Stock Out");
    JButton back=new JButton("Back");
    JButton display=new JButton("Display Stock Out");
    JButton delete=new JButton("Delete Stock Out");
    public SRMenu(){
        pd=new ProductDatabase();
        prod=new ArrayList<Product>(pd.getProducts().values());
        readStaffProduct();
        if(!readFromFile()) return;
        panel.setLayout(new GridLayout(5,1));
        panel.add(add);
        panel.add(modify);
        panel.add(display);
        panel.add(delete);
        panel.add(back);
        mainFrame=new MyFrame(loggedInUserName);
        mainFrame.setContentPanel(panel);
        mainFrame.setSize(600,600);
        mainFrame.setVisible(true);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStockRequest();
            }
        });
        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyStockRequest();
            }
        });
        display.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStockRequest();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStockRequest();
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new Main();
            }
        });
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 24); // You can change the font size and style as needed
        add.setFont(buttonFont);
        modify.setFont(buttonFont);
        display.setFont(buttonFont);
        delete.setFont(buttonFont);
        back.setFont(buttonFont);
    }
    public void writeToProd(){
        try (FileWriter fw = new FileWriter("Product.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw)) {

            for (Product p : prod) {
                if (p != null) {
                    pw.println(p.getProdID() + "," +
                                p.getProdName() + "," +
                                String.format("%.2f",p.getPrice()) + "," +
                                p.getTotalQty() + "," +
                                p.getQtySizes()[0] + "," +
                                p.getQtySizes()[1] + "," +
                                p.getQtySizes()[2] + "," +
                                p.getQtySizes()[3]);
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
    public void writeToStaffProd(){
        try (FileWriter fw = new FileWriter("StaffProduct.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw)) {

            for (Product p : prod) {
                if (p != null) {
                    pw.println(p.getProdID() + "," +
                                p.getProdName() + "," +
                                String.format("%.2f",p.getPrice()) + "," +
                                p.getTotalQty() + "," +
                                p.getStaffQty()[0] + "," +
                                p.getStaffQty()[1] + "," +
                                p.getStaffQty()[2] + "," +
                                p.getStaffQty()[3]);
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
    public void writeToFile(){
        try (FileWriter fw = new FileWriter("StockRequest.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw)) {

       // Optionally, write the header line if needed
       pw.println("Stock Request ID,Status,Product ID,Quantity,Outstanding,Size,Day,Month,Year,Hour,Minute,Second");

       for (StockRequest sr: SRList) {
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
                    if (searchProduct(fields[1])==null) {
                        JOptionPane.showMessageDialog(null, "Product ID for "+fields[0]+" not found.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    StockRequest sr = new StockRequest(fields[0], fields[1], searchProduct(fields[2]), Integer.parseInt(fields[3]), Integer.parseInt(fields[4]), fields[5], Integer.parseInt(fields[6]), Integer.parseInt(fields[7]), Integer.parseInt(fields[8]), Integer.parseInt(fields[9]), Integer.parseInt(fields[10]), Integer.parseInt(fields[11]));
                    SRList.add(sr);
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
    public StockRequest searchSR(String SRID){
        try{
            for (StockRequest sr : SRList) {
                if (sr.getSRID().equals(SRID)) {
                    return sr;
                }
            }
            JOptionPane.showMessageDialog(null, "Stock Request ID not found!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "Unexpected error occured.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public void addStockRequest(){
        try{
            String prodID=JOptionPane.showInputDialog("Enter Product ID: ");
            if(searchProduct(prodID)==null) return;
            int qty=Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity: "));
            if(qty<=0){
                JOptionPane.showMessageDialog(null, "Invalid quantity! Please enter a number greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String size=JOptionPane.showInputDialog("Enter Size: ");
            size=size.toUpperCase();
            if(!(size.equals("S")||size.equals("M")||size.equals("L")||size.equals("XL"))){
                JOptionPane.showMessageDialog(null, "Invalid size.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(searchProduct(prodID).getStaffQty()[searchProduct(prodID).getSizeIndex(size)]>=qty){
                stockRequest.add(new stockRequest(searchProduct(prodID),qty,size));
                JOptionPane.showMessageDialog(null, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(searchProduct(prodID).getStaffQty()[searchProduct(prodID).getSizeIndex(size)]<qty){
                JOptionPane.showMessageDialog(null, "Quantity exceeds that in stock.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else{
                JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            writeToFile();
            writeToProd();
        }
        catch(Exception e){
            if(e instanceof NullPointerException){
                JOptionPane.showMessageDialog(null, "Record not added. User entered no input or cancelled.", "Operation stopped.", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(e instanceof NumberFormatException){
                JOptionPane.showMessageDialog(null, "Invalid quantity! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void modifyStockOut(){
        if(!(readFromFile())) return;
        try{
            String stockOutID=JOptionPane.showInputDialog("Enter Stock Out ID: ");
            if(searchSO(stockOutID)==null) return;
            JOptionPane.showMessageDialog(null, "Record found.", "Success", JOptionPane.INFORMATION_MESSAGE);
            JFrame frame = new JFrame("Modify Stock Out");
            frame.setLayout(new GridLayout(8, 1));
            frame.setSize(500, 500);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Font buttonFont = new Font("Segoe UI", Font.PLAIN, 18);

            // Add buttons for each attribute
            JButton prodIDButton = new JButton("Change Product ID, Size and Quantity");
            prodIDButton.setFont(buttonFont);
            JButton sizeButton = new JButton("Change Size and Quantity");
            sizeButton.setFont(buttonFont);
            JButton qtyButton = new JButton("Change Quantity");
            qtyButton.setFont(buttonFont);
            JButton stockOutIDButton = new JButton("Change Stock Out ID");
            stockOutIDButton.setFont(buttonFont);
            stockOutIDButton.setForeground(Color.WHITE);
            stockOutIDButton.setBackground(new Color(128, 0, 0)); // Maroon color
            JLabel warningLabel = new JLabel("      Warning: Changing the Stock Out ID can be dangerous. DO SO WITH CAUTION!");
            warningLabel.setForeground(Color.RED);
            JButton dateButton = new JButton("Change Date");
            dateButton.setFont(buttonFont);
            JButton timeButton = new JButton("Change Time");
            timeButton.setFont(buttonFont);

            // Add buttons to the frame
            frame.add(prodIDButton);
            frame.add(qtyButton);
            frame.add(sizeButton);
            frame.add(dateButton);
            frame.add(timeButton);
            frame.add(stockOutIDButton);
            frame.add(warningLabel);
        
            // Add action listeners for the buttons
            prodIDButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newProdID = JOptionPane.showInputDialog(frame, "Enter new product ID:", searchSO(stockOutID).getProduct().getProdID());
                    String newQty=JOptionPane.showInputDialog(frame, "Enter new quantity:", searchSO(stockOutID).getQty());
                    String newSize=JOptionPane.showInputDialog(frame, "Enter new size:", searchSO(stockOutID).getSize());
                    if (newProdID != null && !newProdID.trim().isEmpty()&& searchProduct(newProdID)!=null &&  searchSO(stockOutID).setPQS(searchProduct(newProdID), Integer.parseInt(newQty), newSize)) {
                        JOptionPane.showMessageDialog(frame, "Changes made successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        writeToFile();
                        writeToProd();
                        writeToStaffProd();
                    }
                }
            });
        
        
            qtyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newQtyStr = JOptionPane.showInputDialog(frame, "Enter new quantity:", searchSO(stockOutID).getQty());
                    try {
                        if (newQtyStr != null && !newQtyStr.trim().isEmpty() && searchSO(stockOutID).setQty(Integer.parseInt(newQtyStr))) {
                            JOptionPane.showMessageDialog(frame, "Quantity updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            writeToFile();
                            writeToStaffProd();
                            writeToProd();
                        }
                        else if(searchProduct(searchSO(stockOutID).getProduct().getProdID()).getStaffQty()[searchProduct(searchSO(stockOutID).getProduct().getProdID()).getSizeIndex(searchSO(stockOutID).getSize())]<Integer.parseInt(newQtyStr)){
                            JOptionPane.showMessageDialog(null, "Stocked Out quantity exceeds that in stock!.\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(frame, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });   

            dateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newDate = JOptionPane.showInputDialog(frame, "Enter new date (DD-MM-YYY):", searchSO(stockOutID).getDate().getDate());
                    if (newDate != null && !newDate.trim().isEmpty() && newDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
                        try {
                            String[] dateParts = newDate.split("-");
                            searchSO(stockOutID).getDate().changeDate(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                            JOptionPane.showMessageDialog(frame, "Date updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            writeToFile();
                        } catch (Exception ex) {
                            if(ex instanceof NumberFormatException){
                                JOptionPane.showMessageDialog(frame, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            else if(ex instanceof DateTimeParseException){
                                JOptionPane.showMessageDialog(frame, "Invalid date!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            else{
                                JOptionPane.showMessageDialog(frame, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            timeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newTime = JOptionPane.showInputDialog(frame, "Enter new time (HH:MM:SS):", searchSO(stockOutID).getDate().getTime());
                    if (newTime != null && !newTime.trim().isEmpty() && newTime.matches("\\d{2}:\\d{2}:\\d{4}")) {
                        try {
                            String[] timeParts = newTime.split(":");
                            searchSO(stockOutID).getDate().changeTime(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]), Integer.parseInt(timeParts[2]));
                            JOptionPane.showMessageDialog(frame, "Time updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            writeToFile();
                        } catch (Exception ex) {
                            if(ex instanceof NumberFormatException){
                                JOptionPane.showMessageDialog(frame, "Invalid time format!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            else if(ex instanceof DateTimeParseException){
                                JOptionPane.showMessageDialog(frame, "Invalid time!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            else{
                                JOptionPane.showMessageDialog(frame, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(frame, "Invalid time format!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            frame.setVisible(true);
        }
        catch(Exception e){
            if(e instanceof NullPointerException){
                JOptionPane.showMessageDialog(null, "Record not added. User entered no input or cancelled.", "Operation stopped.", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(e instanceof NumberFormatException){
                JOptionPane.showMessageDialog(null, "Invalid quantity! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void displayStockOut(){
        try{
            String stockOutID=JOptionPane.showInputDialog("Enter stockRequest ID: ");
            if(searchSO(stockOutID)==null) return;
            stockRequest stockRequest = searchSO(stockOutID);
            String[][] data = {
                {"stockRequest ID:", stockRequest.getSOID()},
                {"Product ID:", stockRequest.getProduct().getProdID()},
                {"Quantity:", String.valueOf(stockRequest.getQty())},
                {"Size:", stockRequest.getSize()},
                {"Date:", stockRequest.getDate().getDMY()},
                {"Time:", stockRequest.getDate().getTime()}
            };
            String[] columnNames = {"Attribute", "Value"};
            JTable table = new JTable(data,columnNames);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.setRowHeight(25);
            table.setShowGrid(false);

            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 12));
            header.setBackground(Color.WHITE);
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 20));
            ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT); // Left align header text

            // Create a JScrollPane to make the text area scrollable
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(400, 173));

            // Show the details in a JOptionPane
            JOptionPane.showMessageDialog(null, scrollPane, "Record Details", JOptionPane.INFORMATION_MESSAGE);

        }
        catch(Exception e){
            if(e instanceof NullPointerException){
                JOptionPane.showMessageDialog(null, "Record not found. User entered no input or cancelled.", "Operation stopped.", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void deleteStockOut(){
        try{
            String stockOutID=JOptionPane.showInputDialog("Enter stockRequest ID: ");
            if(searchSO(stockOutID)==null) return;
            int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Warning", JOptionPane.YES_NO_OPTION);
            if(choice==JOptionPane.YES_OPTION){
                stockRequest.remove(searchSO(stockOutID));
                JOptionPane.showMessageDialog(null, "Record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                writeToFile();
            }
        }
        catch(Exception e){
            if(e instanceof NullPointerException){
                JOptionPane.showMessageDialog(null, "Record not deleted. User entered no input or cancelled.", "Operation stopped.", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public static void main(String[] args){
        new StockOutMenu();
    }   
}

