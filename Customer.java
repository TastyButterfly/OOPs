import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Customer extends User {
    private JButton regNewCusButton;
    private JButton loginButton;
    private JPasswordField confirmPasswordField;
    private JButton submitButton;
    private String loginName;
    private Login.LoginCallback callback;

    public Customer() {
    }

    public Customer(String userName, String password) {
        super(userName, password);
    }

    @Override
    public void login() {
        setupFrame("Customer Login", 300, 200);
        getFrame().getContentPane().removeAll();

        setUserNameField(new JTextField(15));
        setPasswordField(new JPasswordField(15));

        loginButton = new JButton("Login");
        regNewCusButton = new JButton("Register");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = getUserNameField().getText();
                String password = new String(getPasswordField().getPassword());
                if (validateCustomerLogin(userName, password)) {
                    displayMessage("Login Successful");
                    Customer.this.loginName = userName;  // Set instance variable
                    getFrame().dispose();
                    if (callback != null) {
                        callback.onLogin(userName);
                    }
                } else {
                    displayMessage("Invalid Username or Password");
                }
            }
        });

        regNewCusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        // Layout for login screen
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        loginPanel.add(new JLabel("User Name:"));
        loginPanel.add(getUserNameField());
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(getPasswordField());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(regNewCusButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getFrame().add(mainPanel);
        getFrame().revalidate();
        getFrame().repaint();
        getFrame().setVisible(true);
    }

    private boolean validateCustomerLogin(String userName, String password) {
        File customerFile = new File("Customer.txt");
        try (Scanner scanner = new Scanner(customerFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (userName.equals(data[0]) && password.equals(data[1])) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            displayMessage("Error reading customer data.");
            e.printStackTrace();
        }
        return false;
    }

    private void register() {
        setupFrame("Register", 400, 300);
        getFrame().getContentPane().removeAll();

        setUserNameField(new JTextField(15));
        setPasswordField(new JPasswordField(15));
        confirmPasswordField = new JPasswordField(15);

        submitButton = new JButton("Register");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = getUserNameField().getText();
                String password = new String(getPasswordField().getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (!userName.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                    if (password.equals(confirmPassword)) {
                        try (FileWriter writer = new FileWriter("Customer.txt", true)) {
                            writer.write(userName + "," + password + "\n");
                            displayMessage("Registration successful.");
                            login();
                        } catch (IOException ex) {
                            displayMessage("Error writing to file.");
                        }
                    } else {
                        displayMessage("Passwords do not match. Please try again.");
                    }
                } else {
                    displayMessage("Please fill up all information.");
                }
            }
        });

        // Layout for registration screen
        JPanel registrationPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        registrationPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        registrationPanel.add(new JLabel("User Name:"));
        registrationPanel.add(getUserNameField());
        registrationPanel.add(new JLabel("Password:"));
        registrationPanel.add(getPasswordField());
        registrationPanel.add(new JLabel("Confirm Password:"));
        registrationPanel.add(confirmPasswordField);

        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitButton.setPreferredSize(new Dimension(100, 30));
        submitPanel.add(submitButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(registrationPanel, BorderLayout.CENTER);
        mainPanel.add(submitPanel, BorderLayout.SOUTH);

        getFrame().add(mainPanel);
        getFrame().revalidate();
        getFrame().repaint();
        getFrame().setVisible(true);
    }

    public void setLoginCallback(Login.LoginCallback callback) {
        this.callback = callback;
    }

    public String getLoginName() {
        return loginName;
    }
}
