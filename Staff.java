import javax.swing.*;
import java.awt.*;

public class Staff extends User {
    private JButton loginButton;
    private static String loginName;
    private LoginCallback callback;

    public Staff() {
    }

    public Staff(String userName, String password) {
        super(userName, password);
    }

    @Override
    public void login() {
        setupFrame("Staff Login", 300, 200);

        setUserNameField(new JTextField(15));
        setPasswordField(new JPasswordField(15));
        loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String userName = getUserNameField().getText();
            String password = new String(getPasswordField().getPassword());

            if (userName.equals("admin") && password.equals("admin")) {
                displayMessage("Login Successful");
                loginName = "Staff";
                getFrame().dispose();
                // Notify the callback after successful login
                if (callback != null) {
                    callback.onLogin(loginName);
                }
            } else {
                displayMessage("Invalid Username or Password");
            }
        });

        JPanel userNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userNamePanel.add(new JLabel("User Name:"));
        userNamePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        userNamePanel.add(getUserNameField());

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(new JLabel("Password:  "));
        passwordPanel.add(getPasswordField());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton.setPreferredSize(new Dimension(80, 30));
        buttonPanel.add(loginButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(userNamePanel, BorderLayout.NORTH);
        panel.add(passwordPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        getFrame().add(panel);
        getFrame().setVisible(true);
    }

    public static String getLoginName() {
        return loginName;
    }

    public void setLoginCallback(LoginCallback callback) {
        this.callback = callback;
    }

    public interface LoginCallback {
        void onLogin(String username);
    }
}
