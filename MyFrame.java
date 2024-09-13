import java.awt.*;
import javax.swing.*;

public class MyFrame extends JFrame {
    private final JPanel headerPanel;
    private final JButton homeButton;
    private final JPanel sideMenuPanel;
    private final JButton userNameButton;

    // No-argument constructor
    public MyFrame() {
        this.headerPanel = new JPanel();  // Default JPanel
        this.homeButton = new JButton("Home");  // Default JButton with text "Home"
        this.sideMenuPanel = new JPanel();  // Default JPanel
        this.userNameButton = new JButton("User");  // Default JButton with text "User"

        // Set up JFrame properties
        setTitle("My Frame");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(sideMenuPanel, BorderLayout.WEST);
        add(homeButton, BorderLayout.CENTER);
        add(userNameButton, BorderLayout.EAST);
    }

    public MyFrame(String username) {
        // Frame settings
        setTitle("Inventory System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        headerPanel = new JPanel();
        homeButton = new JButton();
        sideMenuPanel = new JPanel();
        userNameButton = new JButton();

        // Header panel setup
        headerPanel.setBackground(new Color(0, 51, 102)); 
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1200, 80)); // Adjusted height for header

        // Home button in the header
        homeButton.setText("HOME");
        homeButton.setForeground(Color.WHITE);
        homeButton.setBackground(new Color(0, 51, 102)); 
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setFont(new Font("Arial", Font.BOLD, 20));
        if(username!="Staff    "){
            homeButton.addActionListener(e -> goToCustHomePage());
        }else{
            homeButton.addActionListener(e -> goToHomePage());
        }

        // Username button setup
        userNameButton.setText(username);
        userNameButton.setForeground(Color.WHITE);
        userNameButton.setBackground(new Color(0, 51, 102));
        userNameButton.setBorderPainted(false);
        userNameButton.setFocusPainted(false);
        userNameButton.setFont(new Font("Arial", Font.BOLD, 20));
        userNameButton.setHorizontalAlignment(SwingConstants.RIGHT);
        userNameButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        // Popup menu for logout
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setOpaque(true);
        popupMenu.setBackground(new Color(0, 51, 102));  // Match the button's background color

        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.setFont(new Font("Arial", Font.BOLD, 20)); // Match the button's font
        logoutMenuItem.setForeground(Color.WHITE); // Match the button's text color
        logoutMenuItem.setBackground(new Color(0, 51, 102)); // Match the button's background color
        logoutMenuItem.setOpaque(true); // Make background color visible

        logoutMenuItem.addActionListener(e -> logout());
        popupMenu.add(logoutMenuItem);

        // Show popup menu on button click with slight left offset
        userNameButton.addActionListener(e -> {
            int x = userNameButton.getWidth() - popupMenu.getPreferredSize().width; 
            popupMenu.show(userNameButton, x, userNameButton.getHeight());
        });


        // Add components to header
        headerPanel.add(homeButton, BorderLayout.WEST);
        headerPanel.add(userNameButton, BorderLayout.EAST);

        // Side menu panel (initially hidden)
        sideMenuPanel.setLayout(new BoxLayout(sideMenuPanel, BoxLayout.Y_AXIS));
        sideMenuPanel.setBackground(new Color(0, 51, 102));
        sideMenuPanel.setPreferredSize(new Dimension(250, 800));

        // Add the header panel to the frame
        add(headerPanel, BorderLayout.NORTH);

        // Initial state: hide the side menu
        sideMenuPanel.setVisible(false);
        add(sideMenuPanel, BorderLayout.WEST);
    }

    private void goToHomePage() {
        Home homePanel = new Home();  // Assuming you have a Home panel class
        setContentPanel(homePanel);   // Set the Home panel as the main content
    }

    private void goToCustHomePage() {
        CustHome homePanel = new CustHome();  // Assuming you have a Home panel class
        setContentPanel(homePanel);   // Set the Home panel as the main content
    }

    public void setContentPanel(JPanel contentPanel) {
        getContentPane().removeAll(); // Remove all previous panels
        add(headerPanel, BorderLayout.NORTH); // Re-add the header panel
        add(sideMenuPanel, BorderLayout.WEST); // Re-add the side menu
        add(contentPanel, BorderLayout.CENTER); // Add the new content panel
        revalidate(); // Revalidate the frame to update the layout
        repaint(); // Repaint the frame to apply visual changes
    }

    private void logout() {
        // Close the current frame
        dispose();
        
        // Show the login panel again
        LoginPanel.showLogin();
    }
    
    
}
