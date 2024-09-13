import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DeliveryFormPanel extends JPanel {

    private final JLabel nameErrorLabel;
    private final JLabel phoneErrorLabel;
    private final JLabel emailErrorLabel;
    private final JLabel postalCodeErrorLabel;
    private final JLabel cityErrorLabel;
    private final JTextField nameField;
    private final JTextField phoneField;
    private final JTextField emailField;
    private final JTextField addressField;
    private final JTextField postalCodeField;
    private final JTextField cityField;
    private final JComboBox<String> stateComboBox;
    private String orderID;
    private JFrame parentFrame;
    private DeliveryCompleteListener deliveryCompleteListener;

    // Nested listener interface for completion
    public interface DeliveryCompleteListener {
        void onDeliveryComplete();
    }

    // No-arg constructor
    public DeliveryFormPanel() {
        // Initialize final fields with basic default values
        this.nameErrorLabel = new JLabel("");
        this.phoneErrorLabel = new JLabel("");
        this.emailErrorLabel = new JLabel("");
        this.postalCodeErrorLabel = new JLabel("");
        this.cityErrorLabel = new JLabel("");
        
        this.nameField = new JTextField();
        this.phoneField = new JTextField();
        this.emailField = new JTextField();
        this.addressField = new JTextField();
        this.postalCodeField = new JTextField();
        this.cityField = new JTextField();
        
        this.stateComboBox = new JComboBox<>(new String[]{});
    }
    

    public DeliveryFormPanel(String orderID, JFrame parentFrame, DeliveryCompleteListener listener) {
        this.orderID = orderID;
        this.parentFrame = parentFrame;
        this.deliveryCompleteListener = listener;
        this.nameErrorLabel = new JLabel();
        this.phoneErrorLabel = new JLabel();
        this.emailErrorLabel = new JLabel();
        this.postalCodeErrorLabel = new JLabel();
        this.cityErrorLabel = new JLabel();
        this.nameField = new JTextField(20);
        this.phoneField = new JTextField(15);
        this.emailField = new JTextField(15);
        this.addressField = new JTextField(30);
        this.postalCodeField = new JTextField(10);
        this.cityField = new JTextField(15);
        this.stateComboBox = new JComboBox<>(new String[]{"Johor", "Kedah", "Kelantan", "Malacca", "Negeri Sembilan", "Pahang", "Penang", "Perak", "Perlis", "Sabah", "Sarawak", "Selangor", "Terengganu"});
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Icon and Title 
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(244, 240, 236));

        // Icon
        ImageIcon icon = new ImageIcon("delForm_icon.png");
        JLabel iconLabel = new JLabel(icon);
        titlePanel.add(iconLabel);

        // Title
        JLabel titleLabel = new JLabel(" Delivery Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Blue Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(750, 500));
        formPanel.setBackground(new Color(176, 196, 222));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        // Name 
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nameField, gbc);

        
        gbc.gridy = 1; gbc.gridx = 1; gbc.gridwidth = 3;
        nameErrorLabel.setForeground(Color.RED);
        formPanel.add(nameErrorLabel, gbc);

        // Phone and Email
        gbc.gridy = 2; gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(phoneField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 3;
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(emailField, gbc);

        gbc.gridy = 3; gbc.gridx = 1;
        phoneErrorLabel.setForeground(Color.RED);
        formPanel.add(phoneErrorLabel, gbc);

        gbc.gridx = 3;
        emailErrorLabel.setForeground(Color.RED);
        formPanel.add(emailErrorLabel, gbc);

        // Address 
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 4;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridy = 5;
        addressField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(addressField, gbc);

        // Postal Code and City
        gbc.gridy = 6; gbc.gridwidth = 1; gbc.gridx = 0;
        formPanel.add(new JLabel("Postal Code:"), gbc);
        gbc.gridx = 1;
        postalCodeField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(postalCodeField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("City:"), gbc);
        gbc.gridx = 3;
        cityField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cityField, gbc);

        gbc.gridy = 7; gbc.gridx = 1;
        postalCodeErrorLabel.setForeground(Color.RED);
        formPanel.add(postalCodeErrorLabel, gbc);

        gbc.gridx = 3;
        cityErrorLabel.setForeground(Color.RED);
        formPanel.add(cityErrorLabel, gbc);

        // State Dropdown List
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 1;
        formPanel.add(new JLabel("State:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        stateComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(stateComboBox, gbc);

        // Proceed Button
        JButton proceedButton = new JButton("Proceed");
        proceedButton.setFont(new Font("Arial", Font.BOLD, 14));
        proceedButton.addActionListener((ActionEvent e) -> {
            handleSubmit();
        });
        gbc.gridy = 9; gbc.gridx = 0; gbc.gridwidth = 4;
        formPanel.add(proceedButton, gbc);

        // Add panels to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        // Real-time validation
        nameField.getDocument().addDocumentListener(createDocumentListener(nameField, nameErrorLabel, Validation::validateName));
        phoneField.getDocument().addDocumentListener(createDocumentListener(phoneField, phoneErrorLabel, Validation::validatePhoneNumber));
        emailField.getDocument().addDocumentListener(createDocumentListener(emailField, emailErrorLabel, Validation::validateEmail));
        postalCodeField.getDocument().addDocumentListener(createDocumentListener(postalCodeField, postalCodeErrorLabel, Validation::validatePostalCode));
        cityField.getDocument().addDocumentListener(createDocumentListener(cityField, cityErrorLabel, Validation::validateCity));
    }

    private DocumentListener createDocumentListener(JTextField field, JLabel errorLabel, ValidationFunction validationFunction) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateField();
            }

            private void validateField() {
                String error = validationFunction.validate(field.getText().trim());
                if (error.isEmpty()) {
                    errorLabel.setText("");
                } else {
                    errorLabel.setText(error);
                }
            }
        };
    }

    @FunctionalInterface
    private interface ValidationFunction {
        String validate(String text);
    }

    private boolean validateForm() {
        boolean valid = true;

        // Validate Name
        String nameError = Validation.validateName(nameField.getText().trim());
        if (!nameError.isEmpty()) {
            nameErrorLabel.setText(nameError);
            valid = false;
        } else {
            nameErrorLabel.setText("");
        }

        // Validate Phone
        String phoneError = Validation.validatePhoneNumber(phoneField.getText().trim());
        if (!phoneError.isEmpty()) {
            phoneErrorLabel.setText(phoneError);
            valid = false;
        } else {
            phoneErrorLabel.setText("");
        }

        // Validate Email
        String emailError = Validation.validateEmail(emailField.getText().trim());
        if (!emailError.isEmpty()) {
            emailErrorLabel.setText(emailError);
            valid = false;
        } else {
            emailErrorLabel.setText("");
        
           // Validate Postal Code
           String postalCodeError = Validation.validatePostalCode(postalCodeField.getText().trim());
           if (!postalCodeError.isEmpty()) {
               postalCodeErrorLabel.setText(postalCodeError);
               valid = false;
           } else {
               postalCodeErrorLabel.setText("");
           }
    
           // Validate City
           String cityError = Validation.validateCity(cityField.getText().trim());
           if (!cityError.isEmpty()) {
               cityErrorLabel.setText(cityError);
               valid = false;
           } else {
               cityErrorLabel.setText("");
           }
        }
        return valid;
        
    }
    private void handleSubmit() {
        if (validateForm()) {
            // Collect form data
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String postalCode = postalCodeField.getText().trim();
            String city = cityField.getText().trim();
            String state = (String) stateComboBox.getSelectedItem();
    
            // Show Delivery Summary and pass the listener to the next panel
            showDeliverySumPanel(orderID, name, phone, email, address, postalCode, city, state, deliveryCompleteListener);
    
            // Close the current JFrame (DeliveryForm window)
            if (parentFrame != null) {
                parentFrame.dispose();  // Closes the current frame
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please ensure all fields are correctly entered.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showDeliverySumPanel(String orderID, String name, String phone, String email, String address,
                                      String postalCode, String city, String state, DeliveryCompleteListener listener) {
        // Create a new JFrame to display the delivery summary
        JFrame deliverySummaryFrame = new JFrame("Delivery Summary");
        deliverySummaryFrame.setSize(800, 600);
        deliverySummaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        // Create a new DeliverySumPanel instance and pass the listener
        DeliverySumPanel deliverySumPanel = new DeliverySumPanel(orderID, name, phone, email, address, postalCode, city, state, deliverySummaryFrame, listener);
    
        // Add the DeliverySumPanel to the frame
        deliverySummaryFrame.add(deliverySumPanel);
    
        // Make the frame visible
        deliverySummaryFrame.setVisible(true);
    }
    
}