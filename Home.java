import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class Home extends JPanel implements ActionListener {

    JButton editProduct = new JButton();
    JButton cancelOrder = new JButton();
    JButton order = new JButton();
    JButton logistic = new JButton();
    JButton stockIn = new JButton();
    JButton stockOut = new JButton();
    JButton stockRequest = new JButton();
    JButton report = new JButton();

    public Home() {
        // Padding Size
        this.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100)); 

        this.setLayout(new GridLayout(2, 2, 20, 20)); // 2x2 grid with gaps

        // Adjust button sizes and setup
        configureButton(editProduct, "Edit Product", "dashboard_icon.png");
        configureButton(cancelOrder, "Order Cancellation", "order_icon.png");
        configureButton(order, "Order", "order_icon.png");
        configureButton(logistic, "Logistic", "logistic_icon.png");
        configureButton(stockIn, "Stock In", "instock_icon.png");
        configureButton(stockOut, "Stock Out", "instock_icon.png");
        configureButton(stockRequest, "Stock Request", "instock_icon.png");
        configureButton(report, "Report", "reporting_icon.png");

        // Add buttons to the panel
        this.add(editProduct);
        this.add(cancelOrder);
        this.add(order);
        this.add(logistic);
        this.add(stockIn);
        this.add(stockOut);
        this.add(stockRequest);
        this.add(report);

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
        editProduct.addActionListener(this);
        cancelOrder.addActionListener(this);
        order.addActionListener(this);
        logistic.addActionListener(this);
        stockIn.addActionListener(this);
        stockOut.addActionListener(this);
        stockRequest.addActionListener(this);
        report.addActionListener(this);
    }

        @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == report) {
            MyFrame mainFrame = (MyFrame) SwingUtilities.getWindowAncestor(this);
            mainFrame.setContentPanel(new ReportPanel()); // Navigate to Report Panel
            mainFrame.revalidate(); // Refresh the frame to display the new content
            mainFrame.repaint(); // Repaint the frame to apply visual changes
        }
        if (e.getSource() == order) {
            MyFrame mainFrame = (MyFrame) SwingUtilities.getWindowAncestor(this);
            mainFrame.setContentPanel(new OrderPanel()); // Navigate to PaymentPanel
            mainFrame.revalidate(); // Refresh the frame to display the new content
            mainFrame.repaint(); // Repaint the frame to apply visual changes
        }
        if (e.getSource() == editProduct) {
            MyFrame mainFrame = (MyFrame) SwingUtilities.getWindowAncestor(this);
            mainFrame.setContentPanel(new EditProductPanel()); // Navigate to EditProduct
            mainFrame.revalidate(); // Refresh the frame to display the new content
            mainFrame.repaint(); // Repaint the frame to apply visual changes
        }
        if (e.getSource() == logistic) {
            MyFrame mainFrame = (MyFrame) SwingUtilities.getWindowAncestor(this);
            mainFrame.setContentPanel(new PickUpAndDeliverySchedule()); // Navigate to Logistic Module
            mainFrame.revalidate(); // Refresh the frame to display the new content
            mainFrame.repaint(); // Repaint the frame to apply visual changes
        }
        if (e.getSource() == cancelOrder) {
            new CancelMenu();
        }
        if (e.getSource() == stockIn) {
            new StockInMenu();
        }
        if (e.getSource() == stockOut) {
            new StockOutMenu();
        }
        if (e.getSource() == stockRequest) {
            new SRMenu();
        }

    }


}
