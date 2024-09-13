import javax.swing.*;

public abstract class User {
    private JFrame frame;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private String userName;
    private String password;

    public User() {
        frame = new JFrame();
    }

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;

    }

    public void setupFrame(String title, int width, int height) {
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.dispose();
    }

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JTextField getUserNameField() {
        return userNameField;
    }

    public void setUserNameField(JTextField userNameField) {
        this.userNameField = userNameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract void login();


}
