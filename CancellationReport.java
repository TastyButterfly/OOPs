import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class CancellationReport {
    private List<Cancellation> cancel= new ArrayList<Cancellation>();
    private List<Product> prod;
    private List<Order> order= new ArrayList<Order>();
    private List<Product> staffProd;
    private ProductDatabase pd;
    private OrderFileReader ofr;
    private List<Data> data;
    public CancellationReport() {
        try{
            pd = new ProductDatabase();
            ofr = new OrderFileReader();
            prod = new ArrayList<Product>(pd.getProducts().values());
            data=new ArrayList<>();
            for (Product p : prod) {
                data.add(new Data(p.getProdID()));
            }
            staffProd = new ArrayList<Product>(pd.getStaffProducts().values());
            order = ofr.getAllOrders();
            displayReport();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean readFromFile(){
        Path path = Paths.get("Cancellation.csv");
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            cancel.clear(); // Clear the existing list to avoid duplicates

            // Skip the header line if present
            br.readLine();

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
    public void displayReport() {
        readFromFile();
        count();
        rank();
        // Define column names
        String[] columnNames = {"Product ID", "Name","Quantity Cancelled","Cancellation Count","Avg."};

        // Create table model with the column names
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        List<Object[]> data = getData(); // Assuming getData() returns a list of Object arrays
        for (int i = 0; i < data.size() && i < 20; i++) {
            model.addRow(data.get(i));
        }
        // Create table with the model
        JTable table = new JTable(model);
        JTableHeader header=table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setShowGrid(false);

        // Set table contents font
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24); // Adjust row height if necessary
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {//Ensure double printed out to 3dp
            @Override
            protected void setValue(Object value) {
                if (value instanceof Double) {
                    value = String.format("%.3f", (Double) value);
                }
                super.setValue(value);
            }
        };
        table.getColumnModel().getColumn(4).setCellRenderer(renderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(130);//product id width
        table.getColumnModel().getColumn(1).setPreferredWidth(300);//product name width, much wider
        table.getColumnModel().getColumn(2).setPreferredWidth(140);//qty width
        table.getColumnModel().getColumn(3).setPreferredWidth(140);//count width
        table.getColumnModel().getColumn(4).setPreferredWidth(90);//avg width

        //Create title
        JLabel titleLabel = new JLabel("Most Cancelled Products", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 24));//set font
        titleLabel.setBorder(new EmptyBorder(10, 12, 10, 12));//add padding
        // Set up the frame and panel
        JFrame frame = new JFrame("Cancellation Report");
        frame.setPreferredSize(new Dimension(800,500));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(titleLabel, BorderLayout.NORTH);//add title to top of panel
        panel.add(new JScrollPane(table), BorderLayout.CENTER);//add table to top of panel
        //Buttons
        JButton close = new JButton("Close");
        JButton export = new JButton("Export to CSV");
        //Action button, close disposes window upon click, export exports the table to a CSV file
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToCSV(model);
            }
        });
        JPanel buttonPanel = new JPanel();//separate panel for buttons
        buttonPanel.add(close);
        buttonPanel.add(export);

        // Add button panel to the frame
        panel.add(buttonPanel, BorderLayout.SOUTH);//put buttons at bottom of panel
        // Add panel to frame
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
    public void count(){
        for(int i=0;i<cancel.size();i++){//for size in cancel
            for(int j=0;j<data.size();j++){//for size in data
                if(cancel.get(i).getProduct().getProdID().equals(data.get(j).prodID) && cancel.get(i).getStatus().equals("Approved")){//if the cancellation is approved and has product id that matches data
                    data.get(j).qty+=cancel.get(i).getQty();//add the quantity to the data object
                    data.get(j).count++;//adds cancellation count
                }
            }
        }
    }
    public void rank(){
        data.sort(new Comparator<Data>(){
            public int compare(Data d1, Data d2){
                return d2.qty-d1.qty;
            }
        });
    }
    private void exportToCSV(DefaultTableModel model) {
        // Method to export the report to a CSV file
        try (FileWriter writer = new FileWriter("CancellationReport.csv")) {
            for (int i = 0; i < model.getColumnCount(); i++) {//Write table header
                writer.write(model.getColumnName(i) + ",");
            }
            writer.write("\n");
            for (int i = 0; i < model.getRowCount(); i++) {//Write table contents
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                    if (value instanceof Double) {
                        writer.write(String.format("%.3f", (Double) value) + ",");
                    } else {
                        writer.write(value.toString() + ",");
                    }
                }
                writer.write("\n");
            }
            JOptionPane.showMessageDialog(null, "Report exported successfully.", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error exporting report: " + e.getMessage());
        }
    }
    private List<Object[]> getData() {
        return data.stream()
                   .filter(d -> d.qty != 0) // Filter out entries where qty is 0
                   .limit(20) // Limit to 20 rows
                   .map(d -> new Object[]{d.prodID, searchProduct(d.prodID).getProdName(), d.qty, d.count, (double) d.qty / d.count})
                   .toList(); // Collect to list
    }
}   
class Data{
    String prodID;
    int qty;
    int count;
    public Data(String prodID){
        this.prodID = prodID;
        qty = 0;
        count = 0;
    }
}
