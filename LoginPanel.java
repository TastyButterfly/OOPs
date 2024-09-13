import javax.swing.SwingUtilities;

public class LoginPanel {

    public LoginPanel(){}

    public static void showLogin() {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setLoginCallback(username -> {
                if (username != null && !username.trim().isEmpty()) {
                    MyFrame mainFrame;
                    if (username.trim().equals("Staff")) {
                        // Handle staff home page
                        mainFrame = new MyFrame(username);
                        Home homePanel = new Home(); // Assuming Home is for staff
                        mainFrame.setContentPanel(homePanel);
                    } else {
                        // Handle customer home page
                        mainFrame = new MyFrame(username);
                        CustHome custHomePanel = new CustHome(); // Assuming CustHome is for customer
                        mainFrame.setContentPanel(custHomePanel);
                    }
                    mainFrame.setVisible(true);
                } else {
                    // Handle invalid login case
                    System.out.println("Error: Invalid username.");
                }
            });
            login.loginPage();  // Assuming loginPage will create a new Login frame
        });
    }
}
