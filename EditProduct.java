public class EditProduct {
    private ProductDatabase productDatabase;

    // No-argument constructor
    public EditProduct() {
        // Initialize productDatabase with a default instance
        this.productDatabase = new ProductDatabase();
    }

    public EditProduct(ProductDatabase productDatabase) {
        this.productDatabase = productDatabase;
    }

    // Method to update a product
    public void updateProduct(String prodID, String newName, double newPrice, int newQtyS, int newQtyM, int newQtyL, int newQtyXL) {
        Product product = productDatabase.getProducts().get(prodID);
        if (product != null) {
            // Calculate old quantities and total quantity
            int[] oldQtySizes = product.getQtySizes();
            int oldTotalQty = product.getTotalQty();
            
            // New product information
            Product updatedProduct = new Product(prodID, newName, newPrice, newQtyS + newQtyM + newQtyL + newQtyXL, newQtyS, newQtyM, newQtyL, newQtyXL);
            
            // Calculate quantity changes
            int[] qtyChange = new int[]{newQtyS - oldQtySizes[0], newQtyM - oldQtySizes[1], newQtyL - oldQtySizes[2], newQtyXL - oldQtySizes[3]};
            int totalQuantityChange = (newQtyS + newQtyM + newQtyL + newQtyXL) - oldTotalQty;
    
            // Update ProductDatabase
            productDatabase.updateProduct(prodID, updatedProduct, qtyChange, totalQuantityChange);
    
            System.out.println("Product updated successfully.");
        } else {
            System.err.println("Error: Product not found.");
        }
    }    

    // Method to add a new product
    public void addProduct(String prodID, String prodName, double price, int qtyS, int qtyM, int qtyL, int qtyXL) {
        if (!productDatabase.getProducts().containsKey(prodID)) {
            int totalQty = qtyS + qtyM + qtyL + qtyXL;
            Product newProduct = new Product(prodID, prodName, price, totalQty, qtyS, qtyM, qtyL, qtyXL);
            productDatabase.getProducts().put(prodID, newProduct);
            
            // Save changes to Product.txt
            productDatabase.saveProducts("Product.txt", productDatabase.getProducts());
    
            // Update StaffProduct.txt if necessary
            // Check if the product should be added to StaffProduct.txt
            if (!productDatabase.getStaffProducts().containsKey(prodID)) {
                productDatabase.getStaffProducts().put(prodID, new Product(prodID, prodName, price, totalQty, qtyS, qtyM, qtyL, qtyXL));
            } else {
                // Optionally update existing staff product if needed
                Product staffProduct = productDatabase.getStaffProducts().get(prodID);
                staffProduct.setQtySizes(new int[]{qtyS, qtyM, qtyL, qtyXL});
                staffProduct.setTotalQty(totalQty);
            }
            productDatabase.saveProducts("StaffProduct.txt", productDatabase.getStaffProducts());
    
            System.out.println("Product added successfully.");
        } else {
            System.err.println("Error: Product ID already exists.");
        }
    }

    // Method to delete a product
    public void deleteProduct(String prodID) {
        if (productDatabase.getProducts().remove(prodID) != null) {
            // Save changes to Product.txt
            productDatabase.saveProducts("Product.txt", productDatabase.getProducts());
    
            // Remove from StaffProduct.txt if it exists
            productDatabase.getStaffProducts().remove(prodID);
            productDatabase.saveProducts("StaffProduct.txt", productDatabase.getStaffProducts());
    
            System.out.println("Product deleted successfully.");
        } else {
            System.err.println("Error: Product not found.");
        }
    }
    
}
