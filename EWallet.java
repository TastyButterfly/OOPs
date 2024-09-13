import javax.swing.*;

public class EWallet extends PaymentMethod {

    public EWallet(){}

    @Override
    public void handlePaymentMethod(PaymentPanel panel) {
        JLabel imageLabel = new JLabel(new ImageIcon("e-walletQR.jpg"));
        imageLabel.setPreferredSize(new java.awt.Dimension(300, 300)); // Set preferred size to 300x300 pixels

        JPanel panel1 = new JPanel();
        panel1.add(imageLabel);
        int result = JOptionPane.showConfirmDialog(panel, panel1, "Touch 'n Go e-Wallet", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(panel, "Payment process cancelled.");
            panel.returnToOrderPanel(); // Navigate back to home page
            return;
        }
        
        JOptionPane.showMessageDialog(panel, "Payment Confirmed!");
        panel.displayReceiptPanel(); // Navigate to reciept
    }
}
