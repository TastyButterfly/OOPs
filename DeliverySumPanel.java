import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class DeliverySumPanel extends JPanel {

    // Generate Logistic Id
    public static AtomicInteger getLogisticIdCounter() {
        return logisticIdCounter;
    }

    public static void setLogisticIdCounter(AtomicInteger logisticIdCounter) {
        DeliverySumPanel.logisticIdCounter = logisticIdCounter;
    }

    private final JLabel nameLabel;
    private final JLabel phoneLabel;
    private final JLabel emailLabel;
    private final JLabel addressLabel;
    private final JLabel postalCodeLabel;
    private final JLabel cityLabel;
    private final JLabel stateLabel;
    private static AtomicInteger logisticIdCounter = new AtomicInteger(1);  
    private final String name, phone, email, address, postalCode, city, state;
    private JFrame parentFrame;
    private String orderID;
    private DeliveryFormPanel.DeliveryCompleteListener deliveryCompleteListener;

    // No-arg constructor
    public DeliverySumPanel() {
        // Initialize final fields with basic default values
        this.name = "";
        this.phone = "";
        this.email = "";
        this.address = "";
        this.postalCode = "";
        this.city = "";
        this.state = "";
        
        this.nameLabel = new JLabel("Name: ");
        this.phoneLabel = new JLabel("Phone: ");
        this.emailLabel = new JLabel("Email: ");
        this.addressLabel = new JLabel("Address: ");
        this.postalCodeLabel = new JLabel("Postal Code: ");
        this.cityLabel = new JLabel("City: ");
        this.stateLabel = new JLabel("State: ");
    }

    public DeliverySumPanel(String orderID, String name, String phone, String email, String address, String postalCode,
                        String city, String state, JFrame parentFrame, DeliveryFormPanel.DeliveryCompleteListener deliveryCompleteListener) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.orderID = orderID;
        this.parentFrame = parentFrame;
                            
        setLayout(new BorderLayout());

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for better control of components' layout
        detailsPanel.setPreferredSize(new Dimension(500, 50));
        detailsPanel.setBackground(new Color(176, 196, 222));
        detailsPanel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel = new JLabel("Name: " + name);
        phoneLabel = new JLabel("Phone: " + phone);
        emailLabel = new JLabel("Email: " + email);
        addressLabel = new JLabel("Address: " + address);
        postalCodeLabel = new JLabel("Postal Code: " + postalCode);
        cityLabel = new JLabel("City: " + city);
        stateLabel = new JLabel("State: " + state);
                            
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Add padding between components
        detailsPanel.add(nameLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        detailsPanel.add(phoneLabel, gbc);
        gbc.gridy = 10;
        detailsPanel.add(emailLabel, gbc);
        gbc.gridy = 15;
        detailsPanel.add(addressLabel, gbc);
        gbc.gridy = 20;
        detailsPanel.add(postalCodeLabel, gbc);
        gbc.gridy = 25;
        detailsPanel.add(cityLabel, gbc);
        gbc.gridy = 30;
        detailsPanel.add(stateLabel, gbc);

        add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.getContentPane().add(new DeliveryFormPanel(orderID, parentFrame, deliveryCompleteListener));
            parentFrame.revalidate();
            parentFrame.repaint();
        });
        buttonPanel.add(backButton);

        // Confirm Button (store data to txt file and notify listener)
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            handleConfirm(deliveryCompleteListener);
        });
        buttonPanel.add(confirmButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleConfirm(DeliveryFormPanel.DeliveryCompleteListener deliveryCompleteListener) {
        // Generate Logistic ID
        String logisticId = "LOC" + String.format("%04d", logisticIdCounter.getAndIncrement());

        // Record current date and time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String confirmationTime = dtf.format(now);

        // Save details to a file
        try (FileWriter writer = new FileWriter("delivery_details.txt", true)) {
            writer.write(String.join(",", logisticId, orderID, confirmationTime, name, phone, email, address, postalCode, city, state) + "\n");
            JOptionPane.showMessageDialog(this, "Details confirmed and saved successfully!", "Confirmation", JOptionPane.INFORMATION_MESSAGE);

            // Notify the listener that delivery is complete
            if (deliveryCompleteListener != null) {
                deliveryCompleteListener.onDeliveryComplete();
            }

            // Close the current frame
            if (parentFrame != null) {
                parentFrame.dispose();  // Closes the current frame
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving details. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    
    
}