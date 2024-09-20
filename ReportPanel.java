import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.Comparator;

public class ReportPanel extends JPanel {

    private JTextField searchBox;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JComboBox<String> sortComboBox;
    private JTable reportTable;

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Report Panel (Bottom panel)
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BorderLayout());
        reportPanel.setBorder(BorderFactory.createTitledBorder("Reports"));
        reportPanel.setPreferredSize(new Dimension(1200, 300));

        addReportPanelContent(reportPanel);
        add(reportPanel, BorderLayout.CENTER);
    }

    private void addReportPanelContent(JPanel reportPanel) {
        // Drop down lists and search box
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JComboBox<String> reportTypeComboBox = new JComboBox<>(new String[]{"All", "Summary Reports", "Exception Reports", "Detail Reports"}); // Added "All" option
        searchBox = new JTextField(15);

        // JComboBox for sorting options
        sortComboBox = new JComboBox<>(new String[]{"Sort A-Z", "Sort Z-A", "Priority"});
        sortComboBox.addActionListener(this::sortComboBoxActionPerformed); // Add action listener

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
            {"Revenue Report", "Unread", false, "Detail Reports"},
            {"Cancellation Report", "Unread", false, "Detail Reports"},
        };

        tableModel = new DefaultTableModel(data, columnNames);
        reportTable = new JTable(tableModel) {
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
        reportTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox(), reportTable, this));

        // Add button click listener for "View"
        reportTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = reportTable.getSelectedRow();
            if (selectedRow >= 0) {
                String reportName = (String) tableModel.getValueAt(selectedRow, 0);  // Get the report name
            
                // Check if the selected report is "Revenue Report"
                if ("Revenue Report".equals(reportName)) {
                    MyFrame mainFrame = (MyFrame) SwingUtilities.getWindowAncestor(this);
                    //new RevenueReportPanel(mainFrame);
                    //mainFrame.setContentPanel(new RevenueReportPanel()); // Switch to RevenueReportPanel
                    //mainFrame.revalidate();
                    //mainFrame.repaint();
                }

                // Check if the selected report is "Cancellation Report"
                if ("Cancellation Report".equals(reportName)) {
                    new CancellationReport();
                }
            }
        });

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

    private void changeStatusToViewed(int row) {
        reportTable.setValueAt("Viewed", row, 1);
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

    // ButtonEditor for "Action" column
    private static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private JTable table;
        private ReportPanel parentPanel;

        public ButtonEditor(JCheckBox checkBox, JTable table, ReportPanel parentPanel) {
            super(checkBox);
            this.table = table;
            this.parentPanel = parentPanel;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }
        

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText("View");
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                parentPanel.changeStatusToViewed(table.getSelectedRow());
            }
            clicked = false;
            return null;
        }
        

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
