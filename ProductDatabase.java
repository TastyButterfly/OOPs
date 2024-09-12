import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;


public class ProductDatabase {
    private Map<String, Category> categories = new HashMap<>();
    private Map<String, Product> products = new HashMap<>();
    private Map<String, Product> staffProducts = new HashMap<>();    // For StaffProduct.txt

    public ProductDatabase() {
        loadCategories();
        loadProducts("Product.txt", products);             // Load data for Product.txt
        loadProducts("StaffProduct.txt", staffProducts);    // Load data for StaffProduct.txt
    }

    // Method to return a JTable for displaying products based on a category ID
    public JTable getProductTable(String categoryID) {
        String[] columnNames = {"Product ID", "Product Name", "Price", "Total Quantity"};
        Vector<Vector<Object>> productData = getProductDataForTable(categoryID);

        // Create the table model
        DefaultTableModel tableModel = new DefaultTableModel(productData, new Vector<>(List.of(columnNames))) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        // Create the JTable
        JTable productTable = new JTable(tableModel);

        // Set larger font size for the table content
        Font tableFont = new Font("Arial", Font.BOLD, 14); // Adjust size as needed
        productTable.setFont(tableFont);  // Set the font for table content
        productTable.setRowHeight(30);    // Set row height to fit the larger font

        // Optionally, adjust the table header font size
        Font headerFont = new Font("Arial", Font.BOLD, 16); // Adjust size as needed
        productTable.getTableHeader().setFont(headerFont);

        return productTable;
    }

    // Returns product data in a Vector for a given category ID
    public Vector<Vector<Object>> getProductDataForTable(String categoryID) {
        Vector<Vector<Object>> productData = new Vector<>();
        List<Product> filteredProducts = getProductsByCategory(categoryID);
        for (Product product : filteredProducts) {
            Vector<Object> row = new Vector<>();
            row.add(product.getProdID());
            row.add(product.getProdName());
            row.add(product.getPrice());
            row.add(product.getTotalQty());
            productData.add(row); // Add row to product data
        }
        return productData;
    }

    // Load categories from a file
    public void loadCategories() {
        try (BufferedReader reader = new BufferedReader(new FileReader("category.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String categoryID = parts[0];
                    String categoryName = parts[1];
                    categories.put(categoryID, new Category(categoryID, categoryName));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading category file: " + e.getMessage());
        }
    }

    // Load products from the specified file into the given map
    private void loadProducts(String fileName, Map<String, Product> targetMap) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    String prodID = parts[0];
                    String prodName = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int totalQty = Integer.parseInt(parts[3]);
                    int qtyS = Integer.parseInt(parts[4]);
                    int qtyM = Integer.parseInt(parts[5]);
                    int qtyL = Integer.parseInt(parts[6]);
                    int qtyXL = Integer.parseInt(parts[7]);

                    Product product = new Product(prodID, prodName, price, totalQty, qtyS, qtyM, qtyL, qtyXL);
                    targetMap.put(prodID, product);  // Add product to the appropriate map
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + fileName + ": " + e.getMessage());
        }
    }

    // Save the product map to a specific file (Product.txt or StaffProduct.txt)
    public void saveProducts(String fileName, Map<String, Product> sourceMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Product product : sourceMap.values()) {
                writer.write(String.format("%s,%s,%.2f,%d,%d,%d,%d,%d%n",
                    product.getProdID(),
                    product.getProdName(),
                    product.getPrice(),
                    product.getTotalQty(),
                    product.getQtySizes()[0],
                    product.getQtySizes()[1],
                    product.getQtySizes()[2],
                    product.getQtySizes()[3]));
            }
        } catch (IOException e) {
            System.err.println("Error writing to " + fileName + ": " + e.getMessage());
        }
    }

    // Update only the modified product in Product.txt and StaffProduct.txt separately
    public void updateProduct(String prodID, Product updatedProduct, int[] qtyChange, int totalQuantityChange) {
        // Check if the product exists in Product.txt
        if (products.containsKey(prodID)) {
            // Update product in Product.txt
            Product existingProduct = products.get(prodID);
            existingProduct.setProdName(updatedProduct.getProdName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setQtySizes(updatedProduct.getQtySizes());
            existingProduct.setTotalQty(updatedProduct.getTotalQty());
    
            // Save updated data to Product.txt
            saveProducts("Product.txt", products);
    
            // Update StaffProduct.txt based on the calculated changes
            if (staffProducts.containsKey(prodID)) {
                Product staffProduct = staffProducts.get(prodID);
                int[] staffQtySizes = staffProduct.getQtySizes();
                for (int i = 0; i < staffQtySizes.length; i++) {
                    staffQtySizes[i] += qtyChange[i];
                }
                
                staffProduct.setProdName(updatedProduct.getProdName());
                staffProduct.setPrice(updatedProduct.getPrice());
                staffProduct.setQtySizes(staffQtySizes);
                staffProduct.setTotalQty(staffProduct.getTotalQty() + totalQuantityChange);
    
                // Save updated data to StaffProduct.txt
                saveProducts("StaffProduct.txt", staffProducts);
            } else {
                System.err.println("Product ID " + prodID + " not found in StaffProduct.txt.");
            }
        } else {
            System.err.println("Product ID " + prodID + " not found in Product.txt.");
        }
    }
    

    public Product findProductById(String prodID) {
        // First, check in the main products map
        if (products.containsKey(prodID)) {
            return products.get(prodID); // Return the product if found
        }
    
        // If not found, check in the staff products map
        if (staffProducts.containsKey(prodID)) {
            return staffProducts.get(prodID); // Return the product if found in staff products
        }
    
        // If product is not found in both maps, return null or handle accordingly
        System.err.println("Product with ID " + prodID + " not found.");
        return null;
    }
    
    

    // Get products by category ID
    public List<Product> getProductsByCategory(String categoryID) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getProdID().startsWith(categoryID)) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    // Get category by ID
    public Category getCategory(String categoryID) {
        return categories.get(categoryID);
    }

    // Get all categories
    public Map<String, Category> getCategories() {
        return categories;
    }

    // Get all products
    public Map<String, Product> getProducts() {
        return products;
    }

    // Get all products from StaffProduct.txt
    public Map<String, Product> getStaffProducts() {
        return staffProducts;
    }
}
