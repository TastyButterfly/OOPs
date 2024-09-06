public class Product {
    private String prodID;
    private String name;
    private String size;
    private int quantity;
    private double price;

    public Product(String prodID, String name, String size, double price) {
        this.prodID = prodID;
        this.name = name;
        this.size = size;
        this.price = price;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ID: " + prodID + ", Product: " + name + ", Size: " + size + ", Quantity: " + quantity + ", Price: $" + price;
    }
}
