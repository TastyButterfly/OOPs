public class Validation {

    public Validation(){ }

    // Validate recipient name (only alpha and spaces, max 30 characters)
    public static String validateName(String name) {
        if (name.isEmpty()) {
            return "Name cannot be empty.";
        }
        if (!name.matches("[a-zA-Z\\s]+") || name.length() > 30) {
            return "Please enter a valid name. Only letters and spaces are allowed, max 30 characters.";
        }
        return "";
    }

    // Validate phone number (only digits, 9 to 10 characters)
    public static String validatePhoneNumber(String phone) {
        if (phone.isEmpty()) {
            return "Phone number cannot be empty.";
        }
        if (!phone.matches("\\d{9,10}")) {
            return "Phone number should be 9 to 10 digits.";
        }
        return "";
    }

    // Validate postal code (only 5 digits)
    public static String validatePostalCode(String postalCode) {
        if (postalCode.isEmpty()) {
            return "Postal code cannot be empty.";
        }
        if (!postalCode.matches("\\d{5}")) {
            return "Please enter a valid 5 digits postal code.";
        }
        return "";
    }

    // Validate email (must contain '@')
    public static String validateEmail(String email) {
        if (email.isEmpty()) {
            return "Email cannot be empty.";
        }
        if (!email.contains("@")) {
            return "Please enter a valid email that contains '@'.";
        }
        return "";
    }

    // Validate city (only alpha and spaces)
    public static String validateCity(String city) {
        if (city.isEmpty()) {
            return "City cannot be empty.";
        }
        if (!city.matches("[a-zA-Z\\s]+")) {
            return "Please enter a valid city. Only letters and spaces are allowed.";
        }
        return "";
    }
}
