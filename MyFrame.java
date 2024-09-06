import java.awt.*;
import javax.swing.*;

public class MyFrame extends JFrame {
    private final JPanel headerPanel;
    private final JLabel userNameLabel;
    private final JButton homeButton;
    private final JPanel sideMenuPanel;

    public MyFrame(String username) {
        // Frame settings
        setTitle("Inventory System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        headerPanel = new JPanel();
        userNameLabel = new JLabel();
        homeButton = new JButton();
        sideMenuPanel = new JPanel();

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
        homeButton.addActionListener(e -> toggleSideMenu());

        // Username label setup
        userNameLabel.setText(username);
        userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); 

        // Add components to header
        headerPanel.add(homeButton, BorderLayout.WEST);
        headerPanel.add(userNameLabel, BorderLayout.EAST);

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

    private void toggleSideMenu() {
        // Toggle side menu visibility
        sideMenuPanel.setVisible(!sideMenuPanel.isVisible());
    }

    public void setContentPanel(JPanel contentPanel) {
        getContentPane().removeAll(); // Remove all previous panels
        add(headerPanel, BorderLayout.NORTH); // Re-add the header panel
        add(sideMenuPanel, BorderLayout.WEST); // Re-add the side menu
        add(contentPanel, BorderLayout.CENTER); // Add the new content panel
        revalidate(); // Revalidate the frame to update the layout
        repaint(); // Repaint the frame to apply visual changes
    }
    
}
