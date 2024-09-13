import javax.swing.*;
import java.awt.*;

public class OrderPanel extends JPanel {
    private JButton proceedToPaymentButton;
    private Order order;
    private JList<String> categoryList;    // JList for categories
    private JComboBox<String> productComboBox; // ComboBox for products
    private JComboBox<String> sizeComboBox;
    private JTextField quantityField;
    private JButton addButton;
    private ProductDatabase productDatabase;
    private JTable productTable; // Product table

    // No-argument constructor
    public OrderPanel() {
        this.order = new Order(); // Initialize a new Order object
        setupPanel();
    }

    // Parameterized constructor
    public OrderPanel(Order order) {
        this.order = order; // Use the provided Order object
        setupPanel();
    }

    private void setupPanel() {
        // Initialize ProductDatabase
        productDatabase = new ProductDatabase();

        setLayout(new BorderLayout());

        // Category selection area on the left
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Category list setup
        DefaultListModel<String> categoryModel = new DefaultListModel<>();
        for (String categoryName : productDatabase.getCategories().values().stream().map(Category::getCategoryName).toList()) {
            categoryModel.addElement(categoryName);
        }
        categoryList = new JList<>(categoryModel);  // JList for categories
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.setFont(new Font("Arial", Font.BOLD, 16));  // Bold the category text
        categoryList.setPreferredSize(new Dimension(150, 100)); // Set preferred size
        categoryList.addListSelectionListener(e -> refreshProductTable());  // Refresh product table when category is changed
        leftPanel.add(new JScrollPane(categoryList));

        // Add the category panel to the left side of the screen
        add(leftPanel, BorderLayout.WEST);

        // Product Table - Initially empty, will be populated dynamically based on category selection
        productTable = new JTable();
        productTable.setEnabled(false); // Make table non-editable
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 160));  // Set table to smaller height
        add(tableScrollPane, BorderLayout.NORTH);  // Add table to the top

        // Middle: Setup selection panel for products, size, and quantity
        JPanel selectionPanel = setupSelectionPanel();
        add(selectionPanel, BorderLayout.CENTER);

        // Bottom: Proceed to payment button
        proceedToPaymentButton = new JButton("Proceed to Payment");
        proceedToPaymentButton.setFont(new Font("Arial", Font.BOLD, 12));  // Bold the font for the button
        proceedToPaymentButton.addActionListener(e -> showPaymentPage(order));
        add(proceedToPaymentButton, BorderLayout.SOUTH);  // Add button at the bottom
    }

    // Method to setup the selection panel (products, size, and quantity)
    private JPanel setupSelectionPanel() {
        // Panel to hold product selection, size, and quantity
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());  // Use GridBagLayout for better control of components' layout
        panel.setPreferredSize(new Dimension(500, 50));  // Set a fixed size for the selection panel to avoid overflow

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Add padding between components
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left

        // Product combo box with minimized size
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Product:"), gbc);

        productComboBox = new JComboBox<>();
        productComboBox.setPreferredSize(new Dimension(230, 25));  // Set a smaller size for the product combo box
        gbc.gridx = 1;
        panel.add(productComboBox, gbc);

        // Size combo box
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Size:"), gbc);

        sizeComboBox = new JComboBox<>(new String[]{"S", "M", "L", "XL"});
        sizeComboBox.setPreferredSize(new Dimension(80, 25));  // Set a smaller size for the size combo box
        gbc.gridx = 1;
        panel.add(sizeComboBox, gbc);

        // Quantity field
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Quantity:"), gbc);

        quantityField = new JTextField(5);
        quantityField.setPreferredSize(new Dimension(80, 25));  // Set a smaller size for the quantity field
        gbc.gridx = 1;
        panel.add(quantityField, gbc);

        // Add to Order button
        addButton = new JButton("Add to Order");
        addButton.setPreferredSize(new Dimension(120, 25));  // Adjust button size
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(addButton, gbc);

        // Attach ActionListener to the button to call addSelectedProductToOrder()
        addButton.addActionListener(e -> addSelectedProductToOrder());

        return panel;
    }

    // Method to refresh the product table and update the product combo box when a category is selected
    private void refreshProductTable() {
        if (categoryList.getSelectedValue() != null) {
            String selectedCategoryID = productDatabase.getCategories().entrySet().stream()
                    .filter(entry -> entry.getValue().getCategoryName().equals(categoryList.getSelectedValue()))
                    .findFirst().get().getKey();  // Find the corresponding category ID
        
            // Update the product table
            productTable.setModel(productDatabase.getProductTable(selectedCategoryID).getModel());
        
            // Update product combo box with products from the selected category
            productComboBox.removeAllItems();
            for (Product product : productDatabase.getProductsByCategory(selectedCategoryID)) {
                productComboBox.addItem(product.getProdID() + " - " + product.getProdName());
            }
        }
    }

    // Method to add the selected product to the order
    private void addSelectedProductToOrder() {
        try {
            if (productComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid product.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String selectedProductID = productComboBox.getSelectedItem().toString().split(" - ")[0];
            Product selectedProduct = productDatabase.getProducts().get(selectedProductID);
            String size = (String) sizeComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());

            // Validate quantity and size
            if (quantity <= 0 || quantity > selectedProduct.getAvailableQuantity(size)) {
                JOptionPane.showMessageDialog(this, "Invalid quantity or exceeds available stock.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add product to order
            order.addProduct(selectedProduct, size, quantity);
            JOptionPane.showMessageDialog(this, "Product added to order.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPaymentPage(Order order) {
        // Create and set up the PaymentPanel without altering the frame's design
        PaymentPanel paymentPanel = new PaymentPanel(order);

        // Replace only the content of the panel
        setLayout(new BorderLayout());
        removeAll();
        add(paymentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        // Add a listener or callback in PaymentPanel for when payment is confirmed
        paymentPanel.addPaymentConfirmedListener(() -> {
            // Deduct the stock and save to Product.txt only when payment is confirmed
            for (int i = 0; i < order.getProdList().size(); i++) {
                Product product = order.getProdList().get(i);
                String size = order.getSizes().get(i);  // Assuming 'order' tracks the size for each product
                int quantity = order.getQty().get(i);

                // Deduct the stock
                product.updateProductQuantities(size, quantity);
            }

            // Save the updated product quantities to Product.txt
            productDatabase.saveProducts("Product.txt", productDatabase.getProducts());
        });
    }
}