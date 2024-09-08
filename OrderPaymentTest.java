import javax.swing.*;
import java.awt.*;

public class OrderPaymentTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Order Payment Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            // Create dummy products
            Product product1 = new Product("cat1", "prod1", 10.0);
            Product product2 = new Product("cat2", "prod2", 20.0);

            // Create an order
            Order order = new Order();
            order.addProduct(product1, 2);
            order.addProduct(product2, 1);

            // Create payment panel
            PaymentPanel paymentPanel = new PaymentPanel(order);
            frame.add(paymentPanel);

            frame.setVisible(true);
        });
    }
}
