import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;


public class PaymentPanel extends JPanel {
    private final Order order;
    private final JTextArea textArea;
    private final JButton confirmButton;
    private final JComboBox<String> paymentMethodComboBox;
    private final JButton removeProductButton; 
    private PaymentMethod paymentMethod;
    private boolean isDeliveryMethodSelected = false;
    private boolean isPaymentMethodSelected = false;

    // Listener for payment confirmation
    public interface PaymentConfirmedListener {
        void onPaymentConfirmed();
    }

    private PaymentConfirmedListener paymentConfirmedListener;

    public void addPaymentConfirmedListener(PaymentConfirmedListener listener) {
        this.paymentConfirmedListener = listener;
    }

    // No-argument constructor
    public PaymentPanel() {
        this.order = new Order(); // Initialize with a default Order object

        // Initialize final attributes with default values
        this.textArea = new JTextArea();  // Default initialization
        this.confirmButton = new JButton("Confirm Payment");  // Default text
        this.paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Touch 'n Go e-Wallet", "Bank Transfer"}); // Default options
        this.removeProductButton = new JButton("Remove Product");  // Default text

    }

    public PaymentPanel(Order order) {
        this.order = order;

        // Setup JPanel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Outer padding to create blank space

        // Panel to hold the order details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBackground(new Color(176, 196, 222)); // Blue background for the details panel
        // Removed the border from detailsPanel to show the padding

        // Text Area to display order details
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(176, 196, 222)); // Match background color
        textArea.setForeground(Color.BLACK); // Black text color
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 20)); // Adjust font size
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Border inside the textArea

        // Add textArea to the detailsPanel
        detailsPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Add detailsPanel to the center of PaymentPanel
        add(detailsPanel, BorderLayout.CENTER);

        // Payment Method ComboBox
        paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Touch 'n Go e-Wallet", "Bank Transfer"});
        paymentMethodComboBox.setVisible(false); // Initially hidden

        // Remove Product Button
        removeProductButton = new JButton("Remove Product");
        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRemoveProduct();
            }
        });

        // Confirm Button
        confirmButton = new JButton("Confirm Payment");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaymentMethodSelected) {
                    handlePaymentConfirmation();
                } else if (!isDeliveryMethodSelected) {
                    handleDeliveryMethodSelection();
                } else {
                    handlePaymentMethodSelection();
                }
            }
        });
        
        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(removeProductButton);
        buttonPanel.add(confirmButton); // Add remove product button next to confirm button
        add(buttonPanel, BorderLayout.SOUTH);

        // Display order details
        displayOrderDetails();
    }

    private void displayOrderDetails() {
        DecimalFormat df = new DecimalFormat("#.00"); // Format to 2 decimal places
        StringBuilder sb = new StringBuilder();

        sb.append("\t\t\t\t            Order Details:\n");
        sb.append("\t--------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append("\t--       Product Name       --\t                 Quantity\t     --  \tPrice\t  --  \tSubtotal\t     --\n");
        sb.append("\t--------------------------------------------------------------------------------------------------------------------------------------\n");
        // Loop through each product in the order
        for (int i = 0; i < order.getProdList().size(); i++) {
            Product product = order.getProdList().get(i);
            int quantity = order.getQty().get(i);
            double subtotal = order.getSubtotals().get(i);

            // Append product details
            sb.append("\t").append(i+1).append(".   ").append(product.getProdName())
              .append("\t\t").append(quantity)
              .append("\t\tRM").append(df.format(product.getPrice()))
              .append("\t\tRM").append(df.format(subtotal)).append("\n");
        }

        // Append totals
        sb.append("\t--------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append("\t\t\t\t\tTotal\t: RM\t\t").append(df.format(order.getTotal())).append("\n");
        sb.append("\t\t\t\t\tTax (6%)\t: RM\t\t").append(df.format(order.getTax())).append("\n");
        sb.append("\t\t\t\t\tGrand Total\t: RM\t\t").append(df.format(order.getPayAmt())).append("\n");

        // Display in text area
        textArea.setText(sb.toString());
    }

    private void handlePaymentConfirmation() {
        int response = JOptionPane.showConfirmDialog(this, "Do you want to proceed with the payment?", "Confirm Payment", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(this, "Payment process cancelled.");
            returnToOrderPanel(); // Navigate back to order page
        } else if (response == JOptionPane.YES_OPTION) {
            handleDeliveryMethodSelection();
        }
    }

    private void handleDeliveryMethodSelection() {
        String[] options = {"Take Immediately", "Delivery"};
        int choice = JOptionPane.showOptionDialog(this, "How would you like to receive the products?", "Delivery Method", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    
        if (choice == JOptionPane.CLOSED_OPTION || choice == JOptionPane.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(this, "Payment process cancelled.");
            returnToOrderPanel(); // Navigate back to order page
            return;
        }
    
        if (choice == 1) {
            // Open DeliveryFormPanel in a new JFrame
            JFrame deliveryFrame = new JFrame("Delivery Form");
            deliveryFrame.setSize(800, 600); 
            deliveryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
            // Create an instance of DeliveryFormPanel and pass the required arguments
            DeliveryFormPanel deliveryFormPanel = new DeliveryFormPanel(order.getOrderID(), deliveryFrame, new DeliveryFormPanel.DeliveryCompleteListener() {
                @Override
                public void onDeliveryComplete() {
                    // Continue the process after delivery is completed
                    handlePaymentMethodSelection();  // Go back to selecting the payment method
                }
            });
    
            deliveryFrame.add(deliveryFormPanel);
            deliveryFrame.setVisible(true);
        } else {
            // If "Take Immediately" is selected, continue directly to payment method selection
            handlePaymentMethodSelection();
        }
    }
        //isDeliveryMethodSelected = true;
        //handlePaymentMethodSelection();

    private void handlePaymentMethodSelection() {
        paymentMethodComboBox.setVisible(true);
        int result = JOptionPane.showConfirmDialog(this, paymentMethodComboBox, "Select Payment Method", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.CLOSED_OPTION || result == JOptionPane.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(this, "Payment process cancelled.");
            returnToOrderPanel(); // Navigate back to order page
            return;
        }
        String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();
        if (selectedMethod != null) {
            handleSelectedPaymentMethod(selectedMethod);
        }
    }

    private void handleSelectedPaymentMethod(String method) {
        order.setPaymentMethod(method);
        switch (method) {
            case "Cash":
                paymentMethod = new Cash();
                break;
            case "Touch 'n Go e-Wallet":
                paymentMethod = new EWallet();
                break;
            case "Bank Transfer":
                paymentMethod = new BankTransfer();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid payment method selected.");
                returnToOrderPanel(); // Navigate back to order page
                break;
        }
        paymentMethod.handlePaymentMethod(this);
        order.saveOrder();

        // Notify the listener that the payment is confirmed
        if (paymentConfirmedListener != null) {
            paymentConfirmedListener.onPaymentConfirmed();
        }
    }


    public Order getOrder(){
        return order;
    }

    private void handleRemoveProduct() {
        String[] productNames = new String[order.getProdList().size()];
        for (int i = 0; i < order.getProdList().size(); i++) {
            productNames[i] = order.getProdList().get(i).getProdName();
        }
    
        String productToRemove = (String) JOptionPane.showInputDialog(
                this, "Select product to remove", "Remove Product",
                JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);
    
        if (productToRemove != null) {
            for (int i = 0; i < order.getProdList().size(); i++) {
                if (order.getProdList().get(i).getProdName().equals(productToRemove)) {
                    order.removeProduct(i); // Remove the product using its index
                    displayOrderDetails(); // Refresh the order display
                    break;
                }
            }
        }
    }
    
    public void returnToHomePage() {
        MyFrame mainFrame = (MyFrame) SwingUtilities.getWindowAncestor(this);
        if (mainFrame != null) {
            mainFrame.getContentPane().removeAll(); // Remove all components
            Home homePanel = new Home();
            mainFrame.setContentPanel(homePanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }

    public void returnToOrderPanel() {
        MyFrame mainFrame = (MyFrame) SwingUtilities.getWindowAncestor(this);
        if (mainFrame != null) {
            mainFrame.getContentPane().removeAll(); // Remove all components
            OrderPanel orderPanel = new OrderPanel(order); // Create a new instance of OrderPanel with the current order
            mainFrame.setContentPanel(orderPanel); // Set the new content panel
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }

    public void displayReceiptPanel() {
        MyFrame mainFrame = (MyFrame) SwingUtilities.getWindowAncestor(this);
        if (mainFrame != null) {
            mainFrame.getContentPane().removeAll(); // Remove all components
            ReceiptPanel receiptPanel = new ReceiptPanel(order); // Create a new instance of ReceiptPanel with the current order
            mainFrame.setContentPanel(receiptPanel); // Set the new content panel
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }
    
    
}

