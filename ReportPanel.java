import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.Comparator;

public class ReportPanel extends JPanel {

    private final JButton btnLowStock;
    private final JButton btnMaxStock;
    private final JButton btnOutOfStock;
    private JTextField searchBox;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JComboBox<String> sortComboBox;

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Summary Panel (Top panel)
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(null);
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setPreferredSize(new Dimension(1200, 300));

        JPanel innerSummaryPanel = new JPanel();
        innerSummaryPanel.setBackground(new Color(176, 196, 222));
        innerSummaryPanel.setLayout(null);
        innerSummaryPanel.setBounds(50, 30, 900, 250);

        JPanel headerRectangle = new JPanel();
        headerRectangle.setBackground(new Color(100, 149, 237));
        headerRectangle.setBounds(2, 10, 900, 50);
        JLabel summaryLabel = new JLabel("SUMMARY");
        summaryLabel.setForeground(Color.WHITE);
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerRectangle.add(summaryLabel);

        // Buttons (read numbers from txt file)
        btnLowStock = createSummaryButton("Low Stock Product", readProductCount("lowStock.txt"));
        btnOutOfStock = createSummaryButton("Out Of Stock Product", readProductCount("outOfStock.txt"));
        btnMaxStock = createSummaryButton("Max Stock Product", readProductCount("maxStock.txt"));

        btnLowStock.setBounds(100, 80, 220, 120);
        btnOutOfStock.setBounds(340, 80, 220, 120);
        btnMaxStock.setBounds(580, 80, 220, 120);

        innerSummaryPanel.add(headerRectangle);
        innerSummaryPanel.add(btnLowStock);
        innerSummaryPanel.add(btnOutOfStock);
        innerSummaryPanel.add(btnMaxStock);

        summaryPanel.add(innerSummaryPanel);
        add(summaryPanel, BorderLayout.NORTH);

        // Report Panel (Bottom panel)
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BorderLayout());
        reportPanel.setBorder(BorderFactory.createTitledBorder("Reports"));
        reportPanel.setPreferredSize(new Dimension(1200, 300));

        addReportPanelContent(reportPanel);
        add(reportPanel, BorderLayout.CENTER);
    }

    private JButton createSummaryButton(String text, int count) {
        JButton button = new JButton("<html><center>" + count + "<br>" + text + "</center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        return button;
    }

    private int readProductCount(String filePath) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                count += Integer.parseInt(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private void addReportPanelContent(JPanel reportPanel) {
        // Drop down lists and search box
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JComboBox<String> yearComboBox = new JComboBox<>(new String[]{"2020", "2021", "2022", "2023"});
        JComboBox<String> monthComboBox = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        JComboBox<String> reportTypeComboBox = new JComboBox<>(new String[]{"All", "Summary Reports", "Exception Reports", "Detail Reports"}); // Added "All" option
        searchBox = new JTextField(15);

        // JComboBox for sorting options
        sortComboBox = new JComboBox<>(new String[]{"Sort A-Z", "Sort Z-A", "Priority"});
        sortComboBox.addActionListener(this::sortComboBoxActionPerformed); // Add action listener

        filterPanel.add(new JLabel("Year:"));
        filterPanel.add(yearComboBox);
        filterPanel.add(new JLabel("Month:"));
        filterPanel.add(monthComboBox);
        filterPanel.add(new JLabel("Report Type:"));
        filterPanel.add(reportTypeComboBox); // Adding reportTypeComboBox to panel
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchBox);
        filterPanel.add(new JLabel("Sort:"));
        filterPanel.add(sortComboBox);

        reportPanel.add(filterPanel, BorderLayout.NORTH);

        // Define columns, including a hidden "Report Type" column
        String[] columnNames = {"Report Name", "Status", "Action", "Report Type"};
        Object[][] data = {
            {"Inventory Stock Summary Report", "Unread", false, "Summary Reports"},
            {"Sales Summary Report", "Unread", false, "Summary Reports"},
            {"Low Stock Alert Report", "Unread", false, "Exception Reports"},
            {"Stock Discrepancy Report", "Unread", false, "Exception Reports"},
            {"Purchase Order Detail Report", "Unread", false, "Detail Reports"},
            {"Inventory Movement Detail Report", "Unread", false, "Detail Reports"},
        };

        tableModel = new DefaultTableModel(data, columnNames);
        JTable reportTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only allow editing in the Action column
            }
        };

        rowSorter = new TableRowSorter<>(tableModel);
        reportTable.setRowSorter(rowSorter);

        // Hide the "Report Type" column from the view
        reportTable.removeColumn(reportTable.getColumnModel().getColumn(3));

        reportTable.getColumnModel().getColumn(1).setCellRenderer(new StatusRenderer());

        // Add button to the "Action" column for viewing reports
        reportTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        reportTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox(), reportTable));

        JScrollPane scrollPane = new JScrollPane(reportTable);
        reportPanel.add(scrollPane, BorderLayout.CENTER);

        // Add listener to search box
        searchBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }
        });

        // Add listener to report type combo box
        reportTypeComboBox.addActionListener(e -> filterByReportType((String) reportTypeComboBox.getSelectedItem()));
    }

    private void sortComboBoxActionPerformed(ActionEvent e) {
        String selectedOption = (String) sortComboBox.getSelectedItem();
        switch (selectedOption) {
            case "Sort A-Z":
                rowSorter.setComparator(0, Comparator.comparing(String::toString));
                break;
            case "Sort Z-A":
                rowSorter.setComparator(0, Comparator.comparing(String::toString).reversed());
                break;
            case "Priority":
                // Example: Assume a priority column with predefined order
                rowSorter.setComparator(0, (o1, o2) -> {
                    // Define your priority logic here
                    String[] priorityOrder = {"Low Stock Alert Report", "Inventory Stock Summary Report", "Sales Summary Report"};
                    int index1 = indexOf(priorityOrder, o1.toString());
                    int index2 = indexOf(priorityOrder, o2.toString());
                    return Integer.compare(index1, index2);
                });
                break;
        }
        rowSorter.sort();
    }

    private int indexOf(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1; // Not found
    }

    private void filter() {
        String searchText = searchBox.getText();
        if (searchText.trim().length() == 0) {
            rowSorter.setRowFilter(null); // Show all rows if search box is empty
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText)); // Case-insensitive search
        }
    }

    private void filterByReportType(String reportType) {
        if ("All".equals(reportType)) {
            rowSorter.setRowFilter(null); // Show all rows if "All" is selected
        } else {
            rowSorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    String reportTypeValue = (String) entry.getValue(3); // Get value from the "Report Type" column
                    return reportType.equals(reportTypeValue);
                }
            });
        }
    }

    // Cell renderer for the "Status" column
    private static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }
    }

    // Dummy button renderer and editor (implement your own logic for these)
    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("View");
            return this;
        }
    }

    private static class ButtonEditor extends DefaultCellEditor {
        private final JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return new JButton("View");
        }

        @Override
        public Object getCellEditorValue() {
            return "View";
        }
    }
}
