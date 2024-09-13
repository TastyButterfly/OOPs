import javax.swing.*;

public class BankTransfer extends PaymentMethod {

    public BankTransfer(){}

    @Override
    public void handlePaymentMethod(PaymentPanel panel) {
        JLabel imageLabel = new JLabel(new ImageIcon("bankQR.jpg"));
        imageLabel.setPreferredSize(new java.awt.Dimension(350, 340)); // Set preferred size to 300x300 pixels

        JPanel panel1 = new JPanel();
        panel1.add(imageLabel);
        int result = JOptionPane.showConfirmDialog(panel, panel1, "Bank Transfer", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(panel, "Payment process cancelled.");
            panel.returnToOrderPanel(); // Navigate back to home page
            return;
        }
        
        JOptionPane.showMessageDialog(panel, "Payment Confirmed!");
        panel.displayReceiptPanel(); // Navigate to reciept
    }
}
