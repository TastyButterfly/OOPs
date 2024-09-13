import javax.swing.*;
import java.awt.*;

public class Login {

    private JButton staffButton;
    private JButton customerButton;
    private LoginCallback callback;

    public interface LoginCallback {
        void onLogin(String username);
    }

    public Login() {
        // No-argument constructor
    }

    public void setLoginCallback(LoginCallback callback) {
        this.callback = callback;
    }

    public void loginPage() {
        JFrame frame = new JFrame("Login Page");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Initialize buttons
        staffButton = createButton("Staff", "staff_icon.jpg");
        customerButton = createButton("Customer", "customer_icon.png");

        staffButton.addActionListener(e -> {
            Staff staff = new Staff();
            staff.setLoginCallback(username -> {
                if (callback != null && username != null) {
                    callback.onLogin(username);
                }
            });
            staff.login();
            frame.dispose(); // Close login frame
        });

        customerButton.addActionListener(e -> {
            Customer customer = new Customer();
            customer.setLoginCallback(username -> {
                if (callback != null && username != null) {
                    callback.onLogin(username);
                }
            });
            customer.login();
            frame.dispose(); // Close login frame
        });

        // Panel for buttons with GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
        buttonPanel.add(staffButton);
        buttonPanel.add(customerButton);

        frame.add(buttonPanel);
        frame.setVisible(true);
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 100));
        button.setIcon(new ImageIcon(iconPath));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        return button;
    }
}
