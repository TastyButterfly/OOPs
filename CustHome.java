import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class CustHome extends JPanel implements ActionListener{
    
    JButton order = new JButton();
    public CustHome(){
        
        // Padding Size
        this.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100)); 

        this.setLayout(new GridLayout(2, 2, 20, 20)); // 2x2 grid with gaps
        configureButton(order, "Order", "order_icon.png");

        this.add(order);

        setupListeners();

    }
    private void configureButton(JButton button, String text, String iconPath) {
        button.setPreferredSize(new Dimension(250, 50)); 
        button.setLayout(new BorderLayout()); 
    
        JLabel textLabel = new JLabel(text);
        textLabel.setHorizontalAlignment(SwingConstants.LEFT);
        textLabel.setVerticalAlignment(SwingConstants.TOP);
        textLabel.setFont(new Font("Arial", Font.BOLD, 18));
        textLabel.setForeground(Color.WHITE);
        button.add(textLabel, BorderLayout.NORTH); 
    
        // Create a label for the icon and place it in the bottom-right corner
        ImageIcon icon = new ImageIcon(iconPath);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        iconLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        button.add(iconLabel, BorderLayout.SOUTH); // Add icon to the bottom-right
    
        // Set button background and border with rounded corners
        button.setBackground(new Color(176,196,222)); 
        button.setFocusPainted(false);
        button.setContentAreaFilled(false); 
        button.setOpaque(false);

        // Custom paintComponent for rounded corners
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(button.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 30, 30); // Rounded corners
                super.paint(g, c);
                g2.dispose();
            }
        });
    }

    private void setupListeners() {
        order.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
if (e.getSource() == order) {
            MyFrame mainFrame = (MyFrame) SwingUtilities.getWindowAncestor(this);
            mainFrame.setContentPanel(new OrderPanel()); // Navigate to PaymentPanel
            mainFrame.revalidate(); // Refresh the frame to display the new content
            mainFrame.repaint(); // Repaint the frame to apply visual changes
        }
}

    
}
