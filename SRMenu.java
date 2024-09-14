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
    private static JFrame mainFrame;
    JPanel panel=new JPanel();
    JButton add=new JButton("Add Stock Request");
    JButton modify=new JButton("Modify Stock Request");
    JButton back=new JButton("Back");
    JButton display=new JButton("Display Stock Request");
    JButton delete=new JButton("Delete Stock Request");
    public SRMenu(){
        pd=new ProductDatabase();
        prod=new ArrayList<Product>(pd.getProducts().values());
        if(!readFromFile()) return;
        panel.setLayout(new GridLayout(5,1));
        panel.add(add);
        panel.add(modify);
        panel.add(display);
        panel.add(delete);
        panel.add(back);
        mainFrame=new JFrame("Stock Request Menu");
        mainFrame.add(panel);
        mainFrame.setSize(600,600);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
                    if (searchProduct(fields[2])==null) {
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
            SRList.add(new StockRequest(searchProduct(prodID),qty,size));
            JOptionPane.showMessageDialog(null, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            writeToFile();
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
    public void modifyStockRequest(){
        if(!(readFromFile())) return;
        try{
            String SRID=JOptionPane.showInputDialog("Enter Stock Request ID: ");
            if(searchSR(SRID)==null) return;
            JOptionPane.showMessageDialog(null, "Record found.", "Success", JOptionPane.INFORMATION_MESSAGE);
            JFrame frame = new JFrame("Modify Stock Request");
            frame.setLayout(new GridLayout(7, 1));
            frame.setSize(500, 500);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Font buttonFont = new Font("Segoe UI", Font.PLAIN, 18);

            // Add buttons for each attribute
            JButton prodIDButton = new JButton("Change Product ID, Size and Quantity");
            prodIDButton.setFont(buttonFont);
            JButton qtyButton = new JButton("Change Quantity");
            qtyButton.setFont(buttonFont);
            JButton osButton=new JButton("Change Outstanding Quantity");
            osButton.setFont(buttonFont);
            JButton SRIDButton = new JButton("Change Stock Request ID");
            SRIDButton.setFont(buttonFont);
            SRIDButton.setForeground(Color.WHITE);
            SRIDButton.setBackground(new Color(128, 0, 0)); // Maroon color
            JLabel warningLabel = new JLabel("      Warning: Changing the Stock Out ID can be dangerous. DO SO WITH CAUTION!");
            warningLabel.setForeground(Color.RED);
            JButton dateButton = new JButton("Change Date");
            dateButton.setFont(buttonFont);
            JButton timeButton = new JButton("Change Time");
            timeButton.setFont(buttonFont);

            // Add buttons to the frame
            frame.add(prodIDButton);
            frame.add(qtyButton);
            frame.add(osButton);
            frame.add(dateButton);
            frame.add(timeButton);
            frame.add(SRIDButton);
            frame.add(warningLabel);
        
            // Add action listeners for the buttons
            prodIDButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newProdID = JOptionPane.showInputDialog(frame, "Enter new product ID:", searchSR(SRID).getProduct().getProdID());
                    String newQty=JOptionPane.showInputDialog(frame, "Enter new quantity:", searchSR(SRID).getQty());
                    String newSize=JOptionPane.showInputDialog(frame, "Enter new size:", searchSR(SRID).getSize());
                    if (newProdID != null && !newProdID.trim().isEmpty()&& searchProduct(newProdID)!=null) {
                        searchSR(SRID).setPQS(searchProduct(newProdID), Integer.parseInt(newQty), newSize);
                        JOptionPane.showMessageDialog(frame, "Changes made successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        writeToFile();
                    }
                }
            });
        
            osButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newOutstandingStr = JOptionPane.showInputDialog(frame, "Enter new outstanding quantity:", searchSR(SRID).getOutstanding());
                    try {
                        if (newOutstandingStr != null && !newOutstandingStr.trim().isEmpty() && Integer.parseInt(newOutstandingStr)>=0) {
                            searchSR(SRID).setOutstanding(Integer.parseInt(newOutstandingStr));
                            JOptionPane.showMessageDialog(frame, "Outstanding quantity updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            writeToFile();
                        }
                        else if(Integer.parseInt(newOutstandingStr)<0){
                            JOptionPane.showMessageDialog(null, "Outstanding quantity must be more than or equal to 0!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(frame, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        
            qtyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newQtyStr = JOptionPane.showInputDialog(frame, "Enter new quantity:", searchSR(SRID).getQty());
                    try {
                        if (newQtyStr != null && !newQtyStr.trim().isEmpty() && Integer.parseInt(newQtyStr)>0) {
                            searchSR(SRID).setQty(Integer.parseInt(newQtyStr));
                            JOptionPane.showMessageDialog(frame, "Quantity updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            writeToFile();
                        }
                        else if(Integer.parseInt(newQtyStr)<=0){
                            JOptionPane.showMessageDialog(null, "Stock Request Quantity must be more than 0!", "Warning", JOptionPane.WARNING_MESSAGE);
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
                    String newDate = JOptionPane.showInputDialog(frame, "Enter new date (DD-MM-YYY):", searchSR(SRID).getDate().getDate());
                    if (newDate != null && !newDate.trim().isEmpty() && newDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
                        try {
                            String[] dateParts = newDate.split("-");
                            searchSR(SRID).getDate().changeDate(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
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

            SRIDButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newSRID = JOptionPane.showInputDialog(frame, "Enter new Stock Request ID:", searchSR(SRID).getSRID());
                    if (newSRID != null && !newSRID.trim().isEmpty() && searchSR(SRID).changeSRID(newSRID)) {
                        JOptionPane.showMessageDialog(frame, "Stock Request ID updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        writeToFile();
                    }
                    else if(newSRID==null || newSRID.trim().isEmpty()){
                        JOptionPane.showMessageDialog(frame, "Record not updated. User entered no input or cancelled.", "Operation stopped.", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            timeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newTime = JOptionPane.showInputDialog(frame, "Enter new time (HH:MM:SS):", searchSR(SRID).getDate().getTime());
                    if (newTime != null && !newTime.trim().isEmpty() && newTime.matches("\\d{2}:\\d{2}:\\d{4}")) {
                        try {
                            String[] timeParts = newTime.split(":");
                            searchSR(SRID).getDate().changeTime(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]), Integer.parseInt(timeParts[2]));
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
    public void displayStockRequest(){
        try{
            String SRID=JOptionPane.showInputDialog("Enter Stock Request ID: ");
            if(searchSR(SRID)==null) return;
            StockRequest SR = searchSR(SRID);
            String[][] data = {
                {"Stock Request ID:", SR.getSRID()},
                {"Product ID:", SR.getProduct().getProdID()},
                {"Quantity:", String.valueOf(SR.getQty())},
                {"Size:", SR.getSize()},
                {"Date:", SR.getDate().getDMY()},
                {"Time:", SR.getDate().getTime()}
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
    public void deleteStockRequest(){
        try{
            String SRID=JOptionPane.showInputDialog("Enter Stock Request ID: ");
            if(searchSR(SRID)==null) return;
            int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Warning", JOptionPane.YES_NO_OPTION);
            if(choice==JOptionPane.YES_OPTION){
                SRList.remove(searchSR(SRID));
                JOptionPane.showMessageDialog(null, "Record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                writeToFile();
            }
            else if(choice==JOptionPane.NO_OPTION){
                JOptionPane.showMessageDialog(null, "Record not deleted.", "Operation stopped.", JOptionPane.INFORMATION_MESSAGE);
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
}

