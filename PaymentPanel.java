import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PaymentPanel extends JPanel {
    private final Order order;
    private final JTextArea textArea;
    private final JButton confirmButton;
    private final JComboBox<String> paymentMethodComboBox;

    public PaymentPanel(Order order) {
        this.order = order;

        // Setup JPanel
        setLayout(new BorderLayout());

        // Text Area to display order details
        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Payment Method ComboBox
        paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Touch 'n Go e-Wallet", "Bank Transfer"});
        paymentMethodComboBox.setVisible(false); // Initially hidden

        // Confirm Button
        confirmButton = new JButton("Confirm Payment");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!paymentMethodComboBox.isVisible()) {
                    handlePaymentConfirmation();
                } else {
                    // Handle payment confirmation
                    String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();
                    if (selectedMethod != null) {
                        saveOrder(selectedMethod);
                        JOptionPane.showMessageDialog(PaymentPanel.this, "Payment Confirmed!");
                    }
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
            sb.append("ProductID: ").append(product.getProdID())
              .append(" Quantity: ").append(quantity)
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

    private void handlePaymentConfirmation() {
        int response = JOptionPane.showConfirmDialog(this, "Do you want to proceed with the payment?", "Confirm Payment", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            handleDeliveryMethod();
        }
    }

    private void handleDeliveryMethod() {
        String[] options = {"Take Immediately", "Delivery"};
        int choice = JOptionPane.showOptionDialog(this, "How would you like to receive the products?", "Delivery Method", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            // Placeholder for delivery page
            JOptionPane.showMessageDialog(this, "Proceeding to delivery page...");
        }
        showPaymentMethodSelection();
    }

    private void showPaymentMethodSelection() {
        // Show payment method selection
        paymentMethodComboBox.setVisible(true);
        JOptionPane.showMessageDialog(PaymentPanel.this, paymentMethodComboBox, "Select Payment Method", JOptionPane.QUESTION_MESSAGE);
        confirmButton.setText("Confirm Payment and Save");
    }

    private void saveOrder(String paymentMethod) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Order.txt", true))) {
            writer.write("Order ID: " + order.getOrderID() + "\n");
            writer.write("Date Ordered: " + order.getDateOrdered() + "\n");

            for (int i = 0; i < order.getProdList().size(); i++) {
                Product product = order.getProdList().get(i);
                int quantity = order.getQty().get(i);
                writer.write("ProductID: " + product.getProdID() + " Quantity: " + quantity + "\n");
            }

            writer.write("Grand Total: $" + order.getPayAmt() + "\n");
            writer.write("Payment Method: " + paymentMethod + "\n");
            writer.write("--------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
