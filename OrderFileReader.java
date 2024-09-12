import java.io.*;
import java.util.*;

public class OrderFileReader {
    private static final String ORDER_FILE = "Order.txt";
    private List<Order> orderList;
    private ProductDatabase productDatabase;

    public OrderFileReader() {
        orderList = new ArrayList<>();
        productDatabase = new ProductDatabase();
        loadOrdersFromFile();
    }

    // Load orders from Order.txt and store them in orderList
    private void loadOrdersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            Order currentOrder = null;
    
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order ID: ")) {
                    if (currentOrder != null) {
                        orderList.add(currentOrder); // Save the previous order
                    }
                    currentOrder = new Order(); // Create a new order
                    currentOrder.setOrderID(line.substring(10)); // Set the order ID
                } else if (line.startsWith("Date Ordered: ")) {
                    String dateTimeString = line.substring(14); // Extract the date-time string
                    CDate date = parseDateTimeString(dateTimeString); // Convert it into a CDate object
                    currentOrder.setDateOrdered(date); // Set the date in the order
                } else if (line.startsWith("ProductID: ")) {
                    String[] parts = line.split(" Size: | Quantity: ");
                    String prodID = parts[0].substring(11);
                    String size = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    
                    // Use ProductDatabase to find product by ID
                    Product product = productDatabase.findProductById(prodID);
                    
                    if (product != null) {
                        currentOrder.addProduct(product, size, quantity); // Add product with size and quantity
                    } else {
                        System.err.println("Product ID " + prodID + " not found.");
                    }
                } else if (line.startsWith("Grand Total: RM")) {
                    double grandTotal = Double.parseDouble(line.substring(15));
                    currentOrder.setPayAmt(grandTotal); // Set the total amount
                } else if (line.startsWith("Payment Method: ")) {
                    currentOrder.setPaymentMethod(line.substring(16)); // Set the payment method
                } else if (line.startsWith("Order Status: ")) {
                    currentOrder.setStatus(line.substring(14)); // Set the order status
                } else if (line.equals("--------------------------------------------------")) {
                    orderList.add(currentOrder); // Add completed order to the order list
                    currentOrder = null; // Reset for next order
                }
            }
            if (currentOrder != null) {
                orderList.add(currentOrder); // Add the last order
            }
        } catch (IOException e) {
            System.err.println("Error loading orders from file: " + e.getMessage());
        }
    }
    
    // Helper method to parse the date-time string and convert it into a CDate object
    private CDate parseDateTimeString(String dateTimeString) {
        String[] dateTimeParts = dateTimeString.split(" ");
        String[] dateParts = dateTimeParts[0].split("-");
        String[] timeParts = dateTimeParts[1].split(":");
    
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
    
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        int second = Integer.parseInt(timeParts[2]);
    
        CDate date = new CDate();
        date.changeDateTime(day, month, year, hour, minute, second); // Use CDate's method to set the date and time
        return date;
    }
    
    // Method to get all orders
    public List<Order> getAllOrders() {
        return orderList;
    }

}
