import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

public class SearchAndSortUtil {
    private final TableRowSorter<DefaultTableModel> rowSorter;

    // No-argument constructor
    public SearchAndSortUtil() {
        this.rowSorter = new TableRowSorter<>(new DefaultTableModel());
    }

    public SearchAndSortUtil(DefaultTableModel tableModel) {
        this.rowSorter = new TableRowSorter<>(tableModel);
    }

    public TableRowSorter<DefaultTableModel> getRowSorter() {
        return rowSorter;
    }

    // Method for searching the table based on search input
    public void filterTable(JTextField searchBox) {
        String searchText = searchBox.getText();
        if (searchText.trim().isEmpty()) {
            rowSorter.setRowFilter(null); // Show all rows if search box is empty
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText)); // Case-insensitive search
        }
    }

    // Method for sorting the table based on selected criteria
    public void sortTable(JComboBox<String> sortComboBox) {
        String selectedOption = (String) sortComboBox.getSelectedItem();
        rowSorter.setComparator(0, null); // Reset comparator
        switch (selectedOption) {
            case "Sort A-Z" -> rowSorter.setComparator(0, Comparator.comparing(String::toString));
            case "Sort Z-A" -> rowSorter.setComparator(0, Comparator.comparing(String::toString).reversed());
            case "Priority" ->
                rowSorter.setComparator(0, (o1, o2) -> {
                    String[] priorityOrder = {"Low Stock Report", "Inventory Report", "Sales Report"};
                    int index1 = indexOf(priorityOrder, o1.toString());
                    int index2 = indexOf(priorityOrder, o2.toString());
                    return Integer.compare(index1, index2);
                });
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

    // Method to sort by date
    public void sortTableByDate() {
        rowSorter.setComparator(1, Comparator.comparing(dateString -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                return sdf.parse(dateString.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }));
        rowSorter.sort();
    }

    // Method to sort by time
    public void sortTableByTime() {
        rowSorter.setComparator(2, Comparator.comparing(timeString -> {
            try {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                return timeFormat.parse(timeString.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }));
        rowSorter.sort();
    }

    // Method to sort by delivery type (incoming, outgoing)
    public void sortTableByDeliveryType() {
        rowSorter.setComparator(4, (o1, o2) -> {
            String type1 = o1.toString();
            String type2 = o2.toString();
            if (type1.equalsIgnoreCase("incoming") && type2.equalsIgnoreCase("outgoing")) {
                return -1;
            } else if (type1.equalsIgnoreCase("outgoing") && type2.equalsIgnoreCase("incoming")) {
                return 1;
            }
            return 0;
        });
        rowSorter.sort();
    }
}

