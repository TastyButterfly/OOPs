import javax.swing.*;
import java.awt.*;

public class EditProductPanel extends JPanel {
    private JTable productTable;
    private JComboBox<String> categoryComboBox;
    private ProductDatabase productDatabase;
    private EditProduct editProductBackend;

    public EditProductPanel() {
        productDatabase = new ProductDatabase();  // Load products from both files
        editProductBackend = new EditProduct(productDatabase);    // Initialize backend process
        setupUI();
    }

    private void setupUI() {
        this.setLayout(new BorderLayout());

        // Category selection panel
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Label and ComboBox for categories
        JLabel categoryLabel = new JLabel("Select Category:");
        categoryComboBox = new JComboBox<>(getCategoryNames());
        categoryComboBox.addActionListener(e -> updateProductTable());  // Update the product table when category changes

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryComboBox);
        this.add(categoryPanel, BorderLayout.NORTH);

        // Product table
        productTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        this.add(tableScrollPane, BorderLayout.CENTER);

        // Action buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Add product button
        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> openAddProductDialog());
        buttonPanel.add(addButton);

        // Update product button
        JButton updateButton = new JButton("Update Product");
        updateButton.addActionListener(e -> openUpdateProductDialog());
        buttonPanel.add(updateButton);

        // Delete product button
        JButton deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(e -> deleteSelectedProduct());
        buttonPanel.add(deleteButton);

        this.add(buttonPanel, BorderLayout.SOUTH);

        // Initially load the product table with the first category
        updateProductTable();
    }

    // Get category names from the ProductDatabase
    private String[] getCategoryNames() {
        return productDatabase.getCategories().values().stream().map(Category::getCategoryName).toArray(String[]::new);
    }

    // Update the product table based on the selected category
    private void updateProductTable() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        if (selectedCategory != null) {
            String categoryID = productDatabase.getCategories().entrySet().stream()
                    .filter(entry -> entry.getValue().getCategoryName().equals(selectedCategory))
                    .findFirst().get().getKey();
            productTable.setModel(productDatabase.getProductTable(categoryID).getModel());  // Update the table model with products
        }
    }

    // Open a dialog for adding a new product
    private void openAddProductDialog() {
        JTextField prodIDField = new JTextField(10);
        JTextField prodNameField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JTextField qtySField = new JTextField(10);
        JTextField qtyMField = new JTextField(10);
        JTextField qtyLField = new JTextField(10);
        JTextField qtyXLField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("Product ID:"));
        panel.add(prodIDField);
        panel.add(new JLabel("Product Name:"));
        panel.add(prodNameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity S:"));
        panel.add(qtySField);
        panel.add(new JLabel("Quantity M:"));
        panel.add(qtyMField);
        panel.add(new JLabel("Quantity L:"));
        panel.add(qtyLField);
        panel.add(new JLabel("Quantity XL:"));
        panel.add(qtyXLField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String prodID = prodIDField.getText();
            String prodName = prodNameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int qtyS = Integer.parseInt(qtySField.getText());
            int qtyM = Integer.parseInt(qtyMField.getText());
            int qtyL = Integer.parseInt(qtyLField.getText());
            int qtyXL = Integer.parseInt(qtyXLField.getText());

            editProductBackend.addProduct(prodID, prodName, price, qtyS, qtyM, qtyL, qtyXL);
            updateProductTable();  // Refresh the table
        }
    }

    // Open a dialog for updating the selected product
    private void openUpdateProductDialog() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String prodID = (String) productTable.getValueAt(selectedRow, 0);  // Assuming first column is Product ID
        Product selectedProduct = productDatabase.getProducts().get(prodID);

        JTextField prodNameField = new JTextField(selectedProduct.getProdName(), 10);
        JTextField priceField = new JTextField(String.valueOf(selectedProduct.getPrice()), 10);
        JTextField qtySField = new JTextField(String.valueOf(selectedProduct.getQtySizes()[0]), 10);
        JTextField qtyMField = new JTextField(String.valueOf(selectedProduct.getQtySizes()[1]), 10);
        JTextField qtyLField = new JTextField(String.valueOf(selectedProduct.getQtySizes()[2]), 10);
        JTextField qtyXLField = new JTextField(String.valueOf(selectedProduct.getQtySizes()[3]), 10);

        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("Product Name:"));
        panel.add(prodNameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity S:"));
        panel.add(qtySField);
        panel.add(new JLabel("Quantity M:"));
        panel.add(qtyMField);
        panel.add(new JLabel("Quantity L:"));
        panel.add(qtyLField);
        panel.add(new JLabel("Quantity XL:"));
        panel.add(qtyXLField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String prodName = prodNameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int qtyS = Integer.parseInt(qtySField.getText());
            int qtyM = Integer.parseInt(qtyMField.getText());
            int qtyL = Integer.parseInt(qtyLField.getText());
            int qtyXL = Integer.parseInt(qtyXLField.getText());

            editProductBackend.updateProduct(prodID, prodName, price, qtyS, qtyM, qtyL, qtyXL);
            updateProductTable();  // Refresh the table
        }
    }

    // Delete the selected product
    private void deleteSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String prodID = (String) productTable.getValueAt(selectedRow, 0);  // Assuming first column is Product ID
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            editProductBackend.deleteProduct(prodID);
            updateProductTable();  // Refresh the table
        }
    }
}
