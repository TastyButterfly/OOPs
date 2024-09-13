import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class ReceiptPanel extends JPanel {
    private final Order order;
    private final JTextArea textArea;
    private final JButton backButton;

    // No-argument constructor
    public ReceiptPanel() {
        this.order = new Order(); // Initialize 

        // Initialize UI components
        this.textArea = new JTextArea();
        this.backButton = new JButton("Back");

        // Setup JPanel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Outer padding

        //  order details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBackground(new Color(176, 196, 222)); 

        // display receipt details
        textArea.setEditable(false);
        textArea.setBackground(new Color(176, 196, 222)); 
        textArea.setForeground(Color.BLACK); 
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 20)); 
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Add textArea 
        detailsPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Add detailsPanel 
        add(detailsPanel, BorderLayout.CENTER);

        // Panel for the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton); 

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public ReceiptPanel(Order order) {
        this.order = order;

        // Setup JPanel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Outer padding

        // Panel to hold the receipt details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBackground(new Color(176, 196, 222)); // Background color

        // Text Area to display receipt details
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(176, 196, 222)); // Match background color
        textArea.setForeground(Color.BLACK); // Text color
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 20)); // Font size
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Border

        // Add textArea to the detailsPanel
        detailsPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Add detailsPanel to the center of ReceiptPanel
        add(detailsPanel, BorderLayout.CENTER);

        // Back Button
        backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> returnToHomePage());

        // Add backButton to the south of ReceiptPanel
        add(backButton, BorderLayout.SOUTH);

        // Display receipt details
        displayReceiptDetails();
    }

    private void displayReceiptDetails() {
        DecimalFormat df = new DecimalFormat("#.00"); // Format to 2 decimal places
        StringBuilder sb = new StringBuilder();

        // Append order ID
        sb.append("\t\t\t\t            Receipt\n");
        sb.append("\t--------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append("\tOrder ID: ").append(order.getOrderID()).append("\n");
        sb.append("\t--------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append("\t--       Product Name       --\t                 Quantity\t     --  \tPrice\t  --  \tSubtotal\t     --\n");
        sb.append("\t--------------------------------------------------------------------------------------------------------------------------------------\n");

        // Loop through each product in the order
        for (int i = 0; i < order.getProdList().size(); i++) {
            Product product = order.getProdList().get(i);
            int quantity = order.getQty().get(i);
            double subtotal = order.getSubtotals().get(i);

            // Append product details
            sb.append("\t").append(i + 1).append(".   ").append(product.getProdName())
              .append("\t").append(quantity)
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

    private void returnToHomePage() {
        MyFrame mainFrame = (MyFrame) SwingUtilities.getWindowAncestor(this);
        if (mainFrame != null) {
            mainFrame.getContentPane().removeAll(); // Remove all components
            Home homePanel = new Home();
            mainFrame.setContentPanel(homePanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }
}
