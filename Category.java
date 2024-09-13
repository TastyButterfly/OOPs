public class Category {
    private String categoryId;
    private String categoryName;

    // No-argument constructor
    public Category() {
        // Initialize fields with default values
        this.categoryId = ""; // Default empty string for categoryId
        this.categoryName = ""; // Default empty string for categoryName
    }

    public Category(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    
}
