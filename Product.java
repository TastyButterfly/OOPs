import java.io.*;
import java.util.*;


public class Product {
    private String prodID;
    private String prodName;
    private double price;
    private int totalQty;
    private int[] qtySizes; // Array to hold quantities for S, M, L, XL

    // No-argument constructor
    public Product() {
        // Initialize fields with default values
        this.prodID = ""; // Default empty string for prodID
        this.prodName = ""; // Default empty string for prodName
        this.price = 0.0; // Default price
        this.totalQty = 0; // Default total quantity
        this.qtySizes = new int[4]; // Array to hold quantities for S, M, L, XL, default to an array of 4 integers
    }

    // Constructor for products with sizes
    public Product(String prodID, String prodName, double price, int totalQty, int qtyS, int qtyM, int qtyL, int qtyXL) {
        this.prodID = prodID;
        this.prodName = prodName;
        this.price = price;
        this.totalQty = totalQty;
        this.qtySizes = new int[]{qtyS, qtyM, qtyL, qtyXL};
    }

    // Getters and Setters
    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public int[] getQtySizes() {
        return qtySizes;
    }

    public void setQtySizes(int[] qtySizes) {
        this.qtySizes = qtySizes;
    }

    public int getAvailableQuantity(String size) {
        int sizeIndex = getSizeIndex(size);
        if (sizeIndex != -1) {
            return qtySizes[sizeIndex];
        } else {
            return 0; // Invalid size or size not available
        }
    }

    // Method to update the quantities of a product
    public boolean updateProductQuantities(String size, int quantity) {
        int sizeIndex = getSizeIndex(size);
        if (sizeIndex != -1) {
            if (quantity <= qtySizes[sizeIndex]) {
                qtySizes[sizeIndex] -= quantity;
                totalQty -= quantity;
                return true;
            } else {
                System.err.println("Error: Quantity exceeds available stock for size.");
                return false;
            }
        } else {
            System.err.println("Error: Invalid size.");
            return false;
        }
    }

    // Helper method to map size to index in qtySizes array
    public int getSizeIndex(String size) {
        switch (size.toUpperCase()) {
            case "S":
                return 0;
            case "M":
                return 1;
            case "L":
                return 2;
            case "XL":
                return 3;
            default:
                return -1; // Invalid size
        }
    }

}