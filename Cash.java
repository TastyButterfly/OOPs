import javax.swing.*;
import java.text.DecimalFormat;

public class Cash extends PaymentMethod {

    public Cash(){}

    @Override
    public void handlePaymentMethod(PaymentPanel panel) {
        JTextField cashPaidField = new JTextField(10);
        int result = JOptionPane.showConfirmDialog(panel, cashPaidField, "Enter Cash Paid", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(panel, "Payment process cancelled.");
            panel.returnToOrderPanel(); // Navigate back to home page
            return;
        }
        
        try {
            double cashPaid = Double.parseDouble(cashPaidField.getText());
            double change = cashPaid - panel.getOrder().getPayAmt();
            DecimalFormat df = new DecimalFormat("#.00");
            if (change < 0) {
                JOptionPane.showMessageDialog(panel, "Insufficient cash. Please enter a valid amount.");
            } else {
                JOptionPane.showMessageDialog(panel, "Change to return: RM" + df.format(change));
                JOptionPane.showMessageDialog(panel, "Payment Confirmed!");
                panel.displayReceiptPanel(); // Navigate to reciept
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Invalid cash amount entered.");
        }
    }
}
