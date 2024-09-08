import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Order {
    private static final String ORDER_FILE = "Order.txt";
    private static int orderCount = 0; // Default value, will be updated
    private String orderID;
    private Date dateOrdered;
    private double payAmt;
    private List<Product> prodList;
    private List<Integer> qty;
    private List<Double> subtotals;
    private String status;
    private double tax;
    private double total;

    private static final DecimalFormat df = new DecimalFormat("0.00"); // Decimal format for 2 decimal places
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Date format for output

    public Order() {
        this.orderID = generateOrderID();
        this.dateOrdered = new Date(); // Use java.util.Date
        this.prodList = new ArrayList<>();
        this.qty = new ArrayList<>();
        this.subtotals = new ArrayList<>();
        this.status = "pending";
        this.tax = 0.0;
        this.total = 0.0;
    }

    private String generateOrderID() {
        int lastOrderID = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID: ")) {
                    String idString = line.substring(10);
                    try {
                        int id = Integer.parseInt(idString.replaceAll("\\D", ""));
                        if (id > lastOrderID) {
                            lastOrderID = id;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing order ID: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading order file: " + e.getMessage());
        }

        // Increment lastOrderID and format it
        orderCount = lastOrderID + 1;
        return String.format("ORD%04d", orderCount);
    }

    public void addProduct(Product product, int quantity) {
        prodList.add(product);
        qty.add(quantity);
        updateCalculations();
    }

    public void removeProduct(int index) {
        if (index >= 0 && index < prodList.size()) {
            prodList.remove(index);
            qty.remove(index);
            subtotals.remove(index);
            updateCalculations();
        }
    }

    private void updateCalculations() {
        total = 0.0;
        subtotals.clear(); // Clear previous subtotals
        for (int i = 0; i < prodList.size(); i++) {
            double itemSubtotal = prodList.get(i).getPrice() * qty.get(i);
            subtotals.add(itemSubtotal);
            total += itemSubtotal;
        }
        double taxRate = 0.06; // 6% tax
        tax = total * taxRate;
        payAmt = total + tax;

        // Format values to 2 decimal places
        tax = Double.parseDouble(df.format(tax));
        total = Double.parseDouble(df.format(total));
        payAmt = Double.parseDouble(df.format(payAmt));
    }

    public String getOrderID() {
        return orderID;
    }

    public String getDateOrdered() {
        return sdf.format(dateOrdered); // Format date as a string
    }

    public double getPayAmt() {
        return payAmt;
    }

    public List<Product> getProdList() {
        return prodList;
    }

    public List<Integer> getQty() {
        return qty;
    }

    public String getStatus() {
        return status;
    }

    public double getTax() {
        return tax;
    }

    public double getTotal() {
        return total;
    }

    public List<Double> getSubtotals() {
        return subtotals;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter for ORDER_FILE
    public static String getOrderFile() {
        return ORDER_FILE;
    }
}
