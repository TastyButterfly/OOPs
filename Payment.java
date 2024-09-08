import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Payment extends JPanel {
    private final Order order;
    private final JTextArea textArea;
    private final JButton confirmButton;
    private final JComboBox<String> paymentMethodComboBox;

    public Payment(Order order) {
        this.order = order;

        // Setup JPanel
        setLayout(new BorderLayout());

        // Text Area to display order details
        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Payment Method ComboBox
        paymentMethodComboBox = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "PayPal", "Cash"});
        paymentMethodComboBox.setVisible(false); // Initially hidden

        // Confirm Button
        confirmButton = new JButton("Confirm Payment");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show payment method selection after displaying order details
                if (!paymentMethodComboBox.isVisible()) {
                    showPaymentMethodSelection();
                } else {
                    // Handle payment confirmation
                    saveOrder();
                    JOptionPane.showMessageDialog(Payment.this, "Payment Confirmed!");
                }
            }
        });
        add(confirmButton, BorderLayout.SOUTH);

        // Display order details
        displayOrderDetails();
    }

    private void displayOrderDetails() {
        DecimalFormat df = new DecimalFormat("#.00"); // Format to 2 decimal places
        StringBuilder sb = new StringBuilder();

        sb.append("Order Details:\n");
        sb.append("--------------------\n");

        // Loop through each product in the order
        for (int i = 0; i < order.getProdList().size(); i++) {
            Product product = order.getProdList().get(i);
            int quantity = order.getQty().get(i);
            double subtotal = order.getSubtotals().get(i);

            // Append product details
            sb.append(product.getName()).append(" - Quantity: ").append(quantity)
                .append(" - Price: $").append(df.format(product.getPrice()))
                .append(" - Subtotal: $").append(df.format(subtotal)).append("\n");
        }

        // Append totals
        sb.append("--------------------\n");
        sb.append("Total: $").append(df.format(order.getTotal())).append("\n");
        sb.append("Tax (6%): $").append(df.format(order.getTax())).append("\n");
        sb.append("Grand Total: $").append(df.format(order.getPayAmt())).append("\n");

        // Display in text area
        textArea.setText(sb.toString());
    }

    private void showPaymentMethodSelection() {
        // Show payment method selection
        JOptionPane.showMessageDialog(Payment.this, paymentMethodComboBox, "Select Payment Method", JOptionPane.QUESTION_MESSAGE);
        paymentMethodComboBox.setVisible(true);
        confirmButton.setText("Confirm Payment and Save");

        // Add action listener to confirm button for payment method
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();
                JOptionPane.showMessageDialog(Payment.this, "Payment Method: " + selectedMethod);
                // Handle payment confirmation and save order
                saveOrder(selectedMethod);
                JOptionPane.showMessageDialog(Payment.this, "Payment Confirmed!");
            }
        });
    }

    private void saveOrder(String paymentMethod) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Order.ORDER_FILE, true))) {
            writer.write("Order ID: " + order.getOrderID() + "\n");
            writer.write("Date Ordered: " + order.getDateOrdered() + "\n");

            for (int i = 0; i < order.getProdList().size(); i++) {
                Product product = order.getProdList().get(i);
                int quantity = order.getQty().get(i);
                writer.write(product.getName() + " - Quantity: " + quantity + "\n");
            }

            writer.write("Grand Total: $" + order.getPayAmt() + "\n");
            writer.write("Payment Method: " + paymentMethod + "\n");
            writer.write("--------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
