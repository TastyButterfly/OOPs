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

public class CancelMenu {
    private static List<Cancellation> cancel;
    private static List<Product> prod;
    private static List<Product> staffProd;
    private static List<Order> order;
    private static OrderFileReader ofr;
    private static JFrame mainFrame;
    private static ProductDatabase pd;
    JPanel panel=new JPanel();
    JButton add=new JButton("Add Cancellation");
    JButton modify=new JButton("Modify Cancellation");
    JButton back=new JButton("Back");
    JButton display=new JButton("Display Cancellation");
    JButton delete=new JButton("Delete Cancellation");
    public CancelMenu(){
        cancel=new ArrayList<>();
        pd=new ProductDatabase();
        prod=new ArrayList<Product>(pd.getProducts().values());
        staffProd=new ArrayList<Product>(pd.getStaffProducts().values());
        ofr=new OrderFileReader();
        order=ofr.getAllOrders();
        if(!readFromFile()) return;
        panel.setLayout(new GridLayout(5,1));
        panel.add(add);
        panel.add(modify);
        panel.add(display);
        panel.add(delete);
        panel.add(back);
        mainFrame=new JFrame("Cancellation Menu");
        mainFrame.add(panel);
        mainFrame.setSize(600,600);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCancellation();
            }
        });
        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyCancellation();
            }
        });
        display.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCancellation();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCancellation();
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
        try (FileWriter fw = new FileWriter("Cancellation.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw)) {

       // Optionally, write the header line if needed
       pw.println("Cancellation ID,Order ID,Status,Product ID,Quantity,Size,Day,Month,Year,Hour,Minute,Second");

       for (Cancellation c : cancel) {
           if (c != null) {
               pw.println(c.getCancelID() + "," +
                           c.getOrder().getOrderID() + "," +
                           c.getStatus() + "," +
                           c.getProduct().getProdID() + "," +
                           c.getQty() + "," +
                           c.getSize() + "," +
                           c.getDate().getDay() + "," +
                           c.getDate().getMonthValue() + "," +
                           c.getDate().getYear() + "," +
                           c.getDate().getHour() + "," +
                           c.getDate().getMinute() + "," +
                           c.getDate().getSecond());
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
        Path path = Paths.get("Cancellation.csv");
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            cancel.clear(); // Clear the existing list to avoid duplicates

            // Skip the header line if present
            br.readLine();
            Cancellation.setCount(0); // Reset the count to prevent gaps in numbering
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 12) {
                    String cancelID = fields[0];
                    String orderID = fields[1];
                    String status = fields[2];
                    String prodID = fields[3];
                    int qty = Integer.parseInt(fields[4]);
                    String size = fields[5];
                    int day = Integer.parseInt(fields[6]);
                    int month = Integer.parseInt(fields[7]);
                    int year = Integer.parseInt(fields[8]);
                    int hour = Integer.parseInt(fields[9]);
                    int minute = Integer.parseInt(fields[10]);
                    int second = Integer.parseInt(fields[11]);

                    Product product = searchProduct(prodID);
                    Product staffProduct = searchStaffProduct(prodID);
                    Order order = searchOrder(orderID);
                    if (product == null && order == null) {
                        JOptionPane.showMessageDialog(null, "Product ID or Order ID for "+cancelID+" not found.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    Cancellation cancellation = new Cancellation(cancelID, status,qty,day,month,year,hour,minute,second,size,product,order,staffProduct);
                    cancel.add(cancellation);
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
    public Order searchOrder(String orderID){
        try{
            for (Order o : order) {
                if (o.getOrderID().equals(orderID)) {
                    return o;
                }
            }
            JOptionPane.showMessageDialog(null, "Order ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "Unexpected error occured.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public Cancellation searchCancel(String cancelID){
        try{
            for (Cancellation c : cancel) {
                if (c.getCancelID().equals(cancelID)) {
                    return c;
                }
            }
            JOptionPane.showMessageDialog(null, "Cancellation ID not found!", "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, "Unexpected error occured.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public void addCancellation(){
        try{
            String status=JOptionPane.showInputDialog("Enter Status: ");
            if(!(status.equals("Pending")) && !(status.equals("Approved"))&&!(status.equals("Rejected"))){
                JOptionPane.showMessageDialog(null, "Invalid Status.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String prodID=JOptionPane.showInputDialog("Enter Product ID: ");
            if(searchProduct(prodID)==null) return;
            String orderID=JOptionPane.showInputDialog("Enter Order ID: ");
            if(searchOrder(orderID)==null) return;
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
            if(validateProduct(searchProduct(prodID),searchOrder(orderID))==-1){
                return;
            }
            else if(searchOrder(orderID).getSizes().get(validateProduct(searchProduct(prodID),searchOrder(orderID))).equals(size) && searchOrder(orderID).getQty().get(validateProduct(searchProduct(prodID),searchOrder(orderID)))>=qty){
                cancel.add(new Cancellation(status, searchProduct(prodID), qty,searchOrder(orderID),size, searchStaffProduct(prodID)));
                JOptionPane.showMessageDialog(null, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(!searchOrder(orderID).getSizes().get(validateProduct(searchProduct(prodID),searchOrder(orderID))).equals(size)){
                JOptionPane.showMessageDialog(null, "Size does not match size in order.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if(searchOrder(orderID).getQty().get(validateProduct(searchProduct(prodID),searchOrder(orderID)))<qty){
                JOptionPane.showMessageDialog(null, "Quantity exceeds quantity in order.", "Error", JOptionPane.ERROR_MESSAGE);
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
    public void modifyCancellation(){
        if(!(readFromFile())) return;
        try{
            String cancelID=JOptionPane.showInputDialog("Enter Cancellation ID: ");
            if(searchCancel(cancelID)==null) return;
            JOptionPane.showMessageDialog(null, "Record found.", "Success", JOptionPane.INFORMATION_MESSAGE);
            JFrame frame = new JFrame("Modify Cancellation");
            frame.setLayout(new GridLayout(8, 1));
            frame.setSize(500, 500);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Font buttonFont = new Font("Segoe UI", Font.PLAIN, 18);

            // Add buttons for each attribute
            JButton statusButton = new JButton("Change Status");
            statusButton.setFont(buttonFont);
            JButton prodIDButton = new JButton("Change Product ID, Size and Quantity");
            prodIDButton.setFont(buttonFont);
            JButton orderIDButton = new JButton("Change Order ID");
            orderIDButton.setFont(buttonFont);
            JButton qtyButton = new JButton("Change Quantity");
            qtyButton.setFont(buttonFont);
            JButton cancelIDButton = new JButton("Change Cancellation ID");
            cancelIDButton.setFont(buttonFont);
            cancelIDButton.setForeground(Color.WHITE);
            cancelIDButton.setBackground(new Color(128, 0, 0)); // Maroon color
            JLabel warningLabel = new JLabel("    Warning: Changing the Cancellation ID can be dangerous. DO SO WITH CAUTION!");
            warningLabel.setForeground(Color.RED);
            JButton dateButton = new JButton("Change Date");
            dateButton.setFont(buttonFont);
            JButton timeButton = new JButton("Change Time");
            timeButton.setFont(buttonFont);


            frame.add(statusButton);
            frame.add(prodIDButton);
            frame.add(orderIDButton);
            frame.add(qtyButton);
            frame.add(dateButton);
            frame.add(timeButton);
            frame.add(cancelIDButton);
            frame.add(warningLabel);
        
            // Add action listeners for the buttons
            statusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newStatus = JOptionPane.showInputDialog(frame, "Enter new status:", searchCancel(cancelID).getStatus());
                    if(searchCancel(cancelID).getStatus().equals(newStatus)){
                        JOptionPane.showMessageDialog(frame, "Status unchanged.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    else if(searchCancel(cancelID).getStatus().equals("Approved") && !(newStatus.equals("Approved"))){
                        JOptionPane.showMessageDialog(frame, "Cancellation has been approved. Status cannot be changed.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    else if(searchCancel(cancelID).getStatus().equals("Rejected") && !(newStatus.equals("Rejected"))){
                        JOptionPane.showMessageDialog(frame, "Cancellation has been rejected. Status cannot be changed.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    else if (newStatus != null && !newStatus.trim().isEmpty() && searchCancel(cancelID).setStatus(newStatus)) {
                        JOptionPane.showMessageDialog(frame, "Status updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        writeToFile();
                        writeToProd();
                        writeToStaffProd();
                    }
                }
            });
        
            prodIDButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newProdID = JOptionPane.showInputDialog(frame, "Enter new product ID:", searchCancel(cancelID).getProduct().getProdID());
                    String newQty=JOptionPane.showInputDialog(frame, "Enter new quantity:", searchCancel(cancelID).getQty());
                    String newSize=JOptionPane.showInputDialog(frame, "Enter new size:", searchCancel(cancelID).getSize());
                    if(validateProduct(searchProduct(newProdID),searchCancel(cancelID).getOrder())==-1){
                        return;
                    }
                    else if (newProdID != null && !newProdID.trim().isEmpty()&& searchProduct(newProdID)!=null &&  searchCancel(cancelID).setPQS(searchCancel(cancelID).getStatus(),searchStaffProduct(newProdID),searchProduct(newProdID), Integer.parseInt(newQty), newSize)) {
                        JOptionPane.showMessageDialog(frame, "Changes made successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        writeToFile();
                        writeToProd();
                        writeToStaffProd();
                    }
                }
            });
        
            orderIDButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newOrderID = JOptionPane.showInputDialog(frame, "Enter new order ID:", searchCancel(cancelID).getOrder().getOrderID());
                    if (newOrderID != null && !newOrderID.trim().isEmpty() && searchOrder(newOrderID) != null) {
                        if(searchCancel(cancelID).setOrder(searchOrder(newOrderID)))
                        JOptionPane.showMessageDialog(frame, "Order ID updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        writeToFile();
                    } 
                }
            });
        
            qtyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newQtyStr = JOptionPane.showInputDialog(frame, "Enter new quantity:", searchCancel(cancelID).getQty());
                    try {
                        if(validateProduct(searchCancel(cancelID).getProduct(),searchCancel(cancelID).getOrder())==-1){
                            return;
                        }
                        else if (newQtyStr != null && !newQtyStr.trim().isEmpty() && searchCancel(cancelID).getOrder().getQty().get(validateProduct(searchCancel(cancelID).getProduct(),searchCancel(cancelID).getOrder()))>=Integer.parseInt(newQtyStr) && searchCancel(cancelID).setQty(searchCancel(cancelID).getStatus(),Integer.parseInt(newQtyStr))) {
                            JOptionPane.showMessageDialog(frame, "Quantity updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            writeToFile();
                            writeToStaffProd();
                            writeToProd();
                        }
                        else if(searchCancel(cancelID).getOrder().getQty().get(validateProduct(searchCancel(cancelID).getProduct(),searchCancel(cancelID).getOrder()))<Integer.parseInt(newQtyStr)){
                            JOptionPane.showMessageDialog(null, "Cancelled quantity exceeds order!.\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(frame, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });   

            cancelIDButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newCancelID = JOptionPane.showInputDialog(frame, "Enter new cancellation ID:", searchCancel(cancelID).getCancelID());
                    if (newCancelID != null && !newCancelID.trim().isEmpty() && searchCancel(newCancelID) == null) {
                        if(searchCancel(cancelID).changeCancelID(newCancelID))
                        JOptionPane.showMessageDialog(frame, "Cancellation ID updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        writeToFile();
                        frame.dispose();
                    }
                    else if (newCancelID == null || newCancelID.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Record not updated. User entered no input or cancelled.", "Operation stopped.", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else if(newCancelID.equals(cancelID)){
                        JOptionPane.showMessageDialog(frame, "Record not updated. New Cancellation ID is the same as the old one.", "Operation stopped.", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else if(Cancellation.getCancelIDSet().contains(newCancelID)){
                        JOptionPane.showMessageDialog(frame, "Record not updated. New Cancellation ID already exists.", "Operation stopped.", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "An unexpected error occurred.", "Operation stopped.", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            dateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newDate = JOptionPane.showInputDialog(frame, "Enter new date (DD-MM-YYY):", searchCancel(cancelID).getDate().getDate());
                    if (newDate != null && !newDate.trim().isEmpty() && newDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
                        try {
                            String[] dateParts = newDate.split("-");
                            searchCancel(cancelID).getDate().changeDate(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
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
                    String newTime = JOptionPane.showInputDialog(frame, "Enter new time (HH:MM:SS):", searchCancel(cancelID).getDate().getTime());
                    if (newTime != null && !newTime.trim().isEmpty() && newTime.matches("\\d{2}:\\d{2}:\\d{4}")) {
                        try {
                            String[] timeParts = newTime.split(":");
                            searchCancel(cancelID).getDate().changeTime(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]), Integer.parseInt(timeParts[2]));
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
    public void displayCancellation(){
        try{
            String cancelID=JOptionPane.showInputDialog("Enter Cancellation ID: ");
            if(searchCancel(cancelID)==null) return;
            Cancellation cancellation = searchCancel(cancelID);
            String[][] data = {
                {"Cancellation ID:", cancellation.getCancelID()},
                {"Order ID:", cancellation.getOrder().getOrderID()},
                {"Status:", cancellation.getStatus()},
                {"Product ID:", cancellation.getProduct().getProdID()},
                {"Quantity:", String.valueOf(cancellation.getQty())},
                {"Size:", cancellation.getSize()},
                {"Date:", cancellation.getDate().getDMY()},
                {"Time:", cancellation.getDate().getTime()}
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
            scrollPane.setPreferredSize(new Dimension(400, 223));

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
    public void deleteCancellation(){
        try{
            String cancelID=JOptionPane.showInputDialog("Enter Cancellation ID: ");
            if(searchCancel(cancelID)==null) return;
            int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Warning", JOptionPane.YES_NO_OPTION);
            if(choice==JOptionPane.YES_OPTION){
                cancel.remove(searchCancel(cancelID));
                Cancellation.getCancelIDSet().remove(cancelID);
                JOptionPane.showMessageDialog(null, "Record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                writeToFile();
            }
            else{
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
    public int validateProduct(Product product , Order order){
        int i;
            for(i=0;i<order.getProdList().size();i++){
                if(order.getProdList().get(i).getProdID().equals(product.getProdID())) return i;
            }
            JOptionPane.showMessageDialog(null, "Product does not match Order.\nChanges not made.", "Warning", JOptionPane.WARNING_MESSAGE);
            return -1;
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

