import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class PickUpAndDeliverySchedule extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton bulkUpdateButton;
    private JButton bulkUpdateStatusButton;
    private JComboBox<String> statusSortComboBox;
    private SearchAndSortUtil searchAndSortUtil;
    private JTextField searchBox;

    public PickUpAndDeliverySchedule() {
        setLayout(new BorderLayout());
        initUI();
        loadDataFromFile("delivery_details.txt");
        setupSearchAndSort();
    }

    private void initUI() {
        JLabel titleLabel = new JLabel("Pick Up & Delivery Schedule");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 21));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        JPanel roundBorderPanel = new JPanel(new BorderLayout());
        roundBorderPanel.setBorder(new LineBorder(Color.BLACK, 5, true));

        String[] columnNames = {"Bulk Update", "Logistic ID", "Date", "Time", "Receiver", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;  // checkboxes
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
        };
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        add(tableScrollPane, BorderLayout.CENTER);

        bulkUpdateButton = new JButton("Bulk Update");
        bulkUpdateStatusButton = new JButton("Bulk Update Status");
        bulkUpdateStatusButton.setEnabled(false);

        // Dropdown for sorting by status
        String[] statusOptions = {"All", "Pending", "In Transit", "Delivered", "Delayed"};
        statusSortComboBox = new JComboBox<>(statusOptions);
        statusSortComboBox.addActionListener(e -> sortTableByStatus());

        searchBox = new JTextField(15);
        searchBox.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchReceiver(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchReceiver(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchReceiver(); }
        });

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(new JLabel("Sort by Status:"));
        controlPanel.add(statusSortComboBox);
        controlPanel.add(new JLabel("Search Receiver:"));
        controlPanel.add(searchBox);
        controlPanel.add(bulkUpdateButton);
        controlPanel.add(bulkUpdateStatusButton);
        add(controlPanel, BorderLayout.NORTH);

        bulkUpdateButton.addActionListener(e -> toggleBulkUpdateColumn());
        bulkUpdateStatusButton.addActionListener(e -> showBulkStatusUpdateDialog());

        addCustomButtonsToTable();
    }

    private void searchReceiver() {
        String text = searchBox.getText();
        if (text.trim().length() == 0) {
            searchAndSortUtil.getRowSorter().setRowFilter(null);
        } else {
            searchAndSortUtil.getRowSorter().setRowFilter(RowFilter.regexFilter("(?i)" + text, 4));
        }
    }

    private void sortTableByStatus() {
        String selectedStatus = (String) statusSortComboBox.getSelectedItem();
        if ("All".equals(selectedStatus)) {
            searchAndSortUtil.getRowSorter().setRowFilter(null);
        } else {
            searchAndSortUtil.getRowSorter().setRowFilter(RowFilter.regexFilter("(?i)" + selectedStatus, 5));
        }
    }

    private void addCustomButtonsToTable() {
        table.getColumn("Status").setCellRenderer(new StatusCellRenderer());
    }

    private void toggleBulkUpdateColumn() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(false, i, 0); 
        }
        bulkUpdateStatusButton.setEnabled(true);
    }

    private void showBulkStatusUpdateDialog() {
        String[] statusOptions = {"Pending", "In Transit", "Delivered", "Delayed"};
        String newStatus = (String) JOptionPane.showInputDialog(
                null,
                "Bulk Update Status:",
                "Bulk Status Update",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statusOptions,
                "Pending");

        if (newStatus != null) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if ((Boolean) tableModel.getValueAt(i, 0)) { // Check if the checkbox is selected
                    tableModel.setValueAt(newStatus, i, 5);
                    if ("Delayed".equals(newStatus)) {
                        showDelayedDatePicker(i);
                    }
                }
            }
        }
    }

    private void showDelayedDatePicker(int row) {
        String datePattern = "^\\d{4}/\\d{2}/\\d{2}$";
        String dateInput;

        // First window: yyyy/mm/dd 
        do {
            dateInput = JOptionPane.showInputDialog(null, "Enter the new delivery date (yyyy/mm/dd):");
            if (dateInput == null) return; // If user cancels

        } while (!isValidDate(dateInput, datePattern));

        // Second window: hh:mm
        String timePattern = "^\\d{2}:\\d{2}$";
        String timeInput;

        do {
            timeInput = JOptionPane.showInputDialog(null, "Enter the new delivery time (hh:mm):");
            if (timeInput == null) return; // If user cancels
        } while (!isValidTime(timeInput, timePattern));

        // Update the table with the new date and time
        table.setValueAt(dateInput, row, 2); // Update Date column
        table.setValueAt(timeInput + ":00", row, 3); // Update Time column
    }

    private boolean isValidDate(String date, String pattern) {
        if (!Pattern.matches(pattern, date)) {
            JOptionPane.showMessageDialog(null, "Invalid date format! Please enter in yyyy/mm/dd format.");
            return false;
        }
        String[] parts = date.split("/");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        if (month < 1 || month > 12) {
            JOptionPane.showMessageDialog(null, "Month must be between 1 and 12.");
            return false;
        }

        if (day < 1 || day > 31) {
            JOptionPane.showMessageDialog(null, "Day must be between 1 and 31.");
            return false;
        }

        return true;
    }

    private boolean isValidTime(String time, String pattern) {
        if (!Pattern.matches(pattern, time)) {
            JOptionPane.showMessageDialog(null, "Invalid time format! Please enter in hh:mm format.");
            return false;
        }

        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        if (hours < 0 || hours >= 24) {
            JOptionPane.showMessageDialog(null, "Hours must be between 0 and 23.");
            return false;
        }

        if (minutes < 0 || minutes >= 60) {
            JOptionPane.showMessageDialog(null, "Minutes must be between 0 and 59.");
            return false;
        }

        return true;
    }

    private void loadDataFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Object[] rowData = new Object[6];
                rowData[0] = false;  // Unchecked checkbox for "Bulk Update"
                rowData[1] = values[0]; // Logistic ID
                String[] dateTime = values[2].split(" ");
                rowData[2] = dateTime[0]; // Date
                rowData[3] = dateTime[1]; // Time
                rowData[4] = values[3]; // Receiver name
                rowData[5] = "Pending";  // Default status
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSearchAndSort() {
        searchAndSortUtil = new SearchAndSortUtil(tableModel);
        searchAndSortUtil.getRowSorter().setModel(tableModel);
        table.setRowSorter(searchAndSortUtil.getRowSorter());
    }
    
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value != null) {
                String status = value.toString();
                switch (status) {
                    case "Delayed":
                        cellComponent.setForeground(Color.RED);
                        break;
                    case "Delivered":
                        cellComponent.setForeground(Color.GREEN);
                        break;
                    default:
                        cellComponent.setForeground(Color.BLACK);
                        break;
                }
            }
            return cellComponent;
        }
    }
}
