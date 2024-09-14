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

public class StockInMenu {
    private static List<StockIn> stockInList;
    private static List<Product> prod;
    private static List<Product> staffProd;
    private static List<StockRequest> SRList;
    private static int index;
    private static JFrame mainFrame;
    private static SRRW srrw;
    private static ProductDatabase pd;
    JPanel panel=new JPanel();
    JButton add=new JButton("Add Stock In");
    JButton modify=new JButton("Modify Stock In");
    JButton back=new JButton("Back");
    JButton display=new JButton("Display Stock In");
    JButton delete=new JButton("Delete Stock In");
    public StockInMenu(){
        index=0;
        stockInList=new ArrayList<StockIn>();
        srrw=new SRRW();
        pd=new ProductDatabase();
        prod=srrw.getProd();
        staffProd=new ArrayList<Product>(pd.getStaffProducts().values());
        SRList=srrw.getSRList();
        if(!readFromFile()) return;
        panel.setLayout(new GridLayout(5,1));
        panel.add(add);
        panel.add(modify);
        panel.add(display);
        panel.add(delete);
        panel.add(back);
        mainFrame=new JFrame("Stock In Menu");
        mainFrame.add(panel);
        mainFrame.setSize(600,600);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStockIn();
            }
        });
        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyStockIn();
            }
        });
        display.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStockIn();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStockIn();
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

            for (Product p : staffProd) {
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
    public void writeToFile(){
        try (FileWriter fw = new FileWriter("StockIn.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw)) {

       // Optionally, write the header line if needed
       pw.println("Stock In ID,Stock Request ID,Product ID,Quantity,Size,Day,Month,Year,Hour,Minute,Second");
       for (StockIn si: stockInList) {
           if (si != null) {
               pw.println(si.getSIID() + "," +
                           si.getSR().getSRID() + "," +
                           si.getProduct().getProdID() + "," +
                           si.getQty() + "," +
                           si.getSize() + "," +
                           si.getDate().getDay() + "," +
                           si.getDate().getMonthValue() + "," +
                           si.getDate().getYear() + "," +
                           si.getDate().getHour() + "," +
                           si.getDate().getMinute() + "," +
                           si.getDate().getSecond());
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
        Path path = Paths.get("StockIn.csv");
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            stockInList.clear(); // Clear the existing list to avoid duplicates

            // Skip the header line if present
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 11) {
                    if (searchProduct(fields[2]) == null && searchSR(fields[1]) == null) {
                        JOptionPane.showMessageDialog(null, "Product ID or Stock Request ID for"+ fields[0] +" not found.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    StockIn si=new StockIn(fields[0],searchSR(fields[1]),searchProduct(fields[2]),Integer.parseInt(fields[3]),fields[4],Integer.parseInt(fields[5]),Integer.parseInt(fields[6]),Integer.parseInt(fields[7]),Integer.parseInt(fields[8]),Integer.parseInt(fields[9]),Integer.parseInt(fields[10]),searchStaffProduct(fields[2]));
                    stockInList.add(si);
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
    public StockRequest searchSR(String SRID){
        try{
            for (StockRequest sr: SRList) {
                if (sr.getSRID().equals(SRID)) {
                    return sr;
                }
            }
            JOptionPane.showMessageDialog(null, "Stock Request ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "Unexpected error occured.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public StockIn searchStockIn(String stockInID){
        try{
            for (StockIn si : stockInList) {
                if (si.getSIID().equals(stockInID)) {
                    return si;
                }
            }
            JOptionPane.showMessageDialog(null, "Stock In ID not found!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "Unexpected error occured.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public void addStockIn(){
        try{
            String prodID=JOptionPane.showInputDialog("Enter Product ID: ");
            if(searchProduct(prodID)==null) return;
            String SRID=JOptionPane.showInputDialog("Enter Stock Request ID: ");
            if(searchSR(SRID)==null) return;
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
            stockInList.add(new StockIn(searchProduct(prodID),qty,size,searchStaffProduct(prodID)));
            stockInList.get(index++).setSR(searchSR(SRID));
            JOptionPane.showMessageDialog(null, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            writeToFile();
            writeToProd();
            writeToStaffProd();
            srrw.writeToFile();
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
    public void modifyStockIn(){
        if(!(readFromFile())) return;
        try{
            String stockInID=JOptionPane.showInputDialog("Enter Stock In ID: ");
            if(searchStockIn(stockInID)==null) return;
            JOptionPane.showMessageDialog(null, "Record found.", "Success", JOptionPane.INFORMATION_MESSAGE);
            JFrame frame = new JFrame("Modify Stock In");
            frame.setLayout(new GridLayout(7, 1));
            frame.setSize(500, 500);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Font buttonFont = new Font("Segoe UI", Font.PLAIN, 18);

            // Add buttons for each attribute
            JButton prodIDButton = new JButton("Change Product ID, Size and Quantity");
            prodIDButton.setFont(buttonFont);
            JButton SRIDButton = new JButton("Change Stock Request ID");
            SRIDButton.setFont(buttonFont);
            JButton qtyButton = new JButton("Change Quantity");
            qtyButton.setFont(buttonFont);
            JButton stockInIDButton = new JButton("Change Stock In ID");
            stockInIDButton.setFont(buttonFont);
            stockInIDButton.setForeground(Color.WHITE);
            stockInIDButton.setBackground(new Color(128, 0, 0)); // Maroon color
            JLabel warningLabel = new JLabel("       Warning: Changing the Stock In ID can be dangerous. DO SO WITH CAUTION!");
            warningLabel.setForeground(Color.RED);
            JButton dateButton = new JButton("Change Date");
            dateButton.setFont(buttonFont);
            JButton timeButton = new JButton("Change Time");
            timeButton.setFont(buttonFont);


            frame.add(prodIDButton);
            frame.add(stockInIDButton);
            frame.add(qtyButton);
            frame.add(dateButton);
            frame.add(timeButton);
            frame.add(stockInIDButton);
            frame.add(warningLabel);
        
            // Add action listeners for the buttons
        
            prodIDButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newProdID = JOptionPane.showInputDialog(frame, "Enter new product ID:", searchStockIn(stockInID).getProduct().getProdID());
                    String newQty=JOptionPane.showInputDialog(frame, "Enter new quantity:", searchStockIn(stockInID).getQty());
                    String newSize=JOptionPane.showInputDialog(frame, "Enter new size:", searchStockIn(stockInID).getSize());
                    if (newProdID != null && !newProdID.trim().isEmpty()&& searchProduct(newProdID)!=null &&  searchStockIn(stockInID).setPQS(searchStaffProduct(newProdID),searchProduct(newProdID), Integer.parseInt(newQty), newSize)) {
                        JOptionPane.showMessageDialog(frame, "Changes made successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        writeToFile();
                        writeToProd();
                        writeToStaffProd();
                        srrw.writeToFile();
                    }
                }
            });
        
            stockInIDButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newSRID = JOptionPane.showInputDialog(frame, "Enter new Stock Request ID:", searchStockIn(stockInID).getSR().getSRID());
                    if (newSRID != null && !newSRID.trim().isEmpty() && searchSR(newSRID) != null) {
                        searchStockIn(stockInID).setSR(searchSR(newSRID));
                        JOptionPane.showMessageDialog(frame, "Order ID updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        writeToFile();
                    }   
                }
            });
        
            qtyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newQtyStr = JOptionPane.showInputDialog(frame, "Enter new quantity:", searchStockIn(stockInID).getQty());
                    try {
                        if (newQtyStr != null && !newQtyStr.trim().isEmpty() && searchStockIn(stockInID).setQty(Integer.parseInt(newQtyStr))) {
                            JOptionPane.showMessageDialog(frame, "Quantity updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            writeToFile();
                            writeToStaffProd();
                            writeToProd();
                            srrw.writeToFile();
                        } 
                    }catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });   

            dateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newDate = JOptionPane.showInputDialog(frame, "Enter new date (DD-MM-YYY):", searchStockIn(stockInID).getDate().getDate());
                    if (newDate != null && !newDate.trim().isEmpty() && newDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
                        try {
                            String[] dateParts = newDate.split("-");
                            searchStockIn(stockInID).getDate().changeDate(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
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
                    String newTime = JOptionPane.showInputDialog(frame, "Enter new time (HH:MM:SS):", searchStockIn(stockInID).getDate().getTime());
                    if (newTime != null && !newTime.trim().isEmpty() && newTime.matches("\\d{2}:\\d{2}:\\d{4}")) {
                        try {
                            String[] timeParts = newTime.split(":");
                            searchStockIn(stockInID).getDate().changeTime(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]), Integer.parseInt(timeParts[2]));
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
    public void displayStockIn(){
        try{
            String stockInID=JOptionPane.showInputDialog("Enter Stock In ID: ");
            if(searchStockIn(stockInID)==null) return;
            StockIn si = searchStockIn(stockInID);
            String[][] data = {
                {"Stock In ID:", si.getSIID()},
                {"Stock Request ID:", si.getSR().getSRID()},
                {"Product ID:", si.getProduct().getProdID()},
                {"Quantity:", String.valueOf(si.getQty())},
                {"Size:", si.getSize()},
                {"Date:", si.getDate().getDMY()},
                {"Time:", si.getDate().getTime()}
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
            scrollPane.setPreferredSize(new Dimension(400, 198));

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
    public void deleteStockIn(){
        try{
            String stockInID=JOptionPane.showInputDialog("Enter Stock In ID: ");
            if(searchStockIn(stockInID)==null) return;
            int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Warning", JOptionPane.YES_NO_OPTION);
            if(choice==JOptionPane.YES_OPTION){
                stockInList.remove(searchStockIn(stockInID));
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
    public Product searchStaffProduct(String prodID){
        try{
            for (Product p : staffProd) {
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
}

