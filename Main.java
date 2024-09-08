import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Based on registration page (their username)
            String loggedInUsername = "TestUser"; 

            // Main frame
            MyFrame mainFrame = new MyFrame(loggedInUsername);

            // Home panel
            Home homePanel = new Home();

            // Set the content panel of the main frame to the Home panel
            mainFrame.setContentPanel(homePanel);

            // Make the main frame visible
            mainFrame.setVisible(true);
        });
    }
}
