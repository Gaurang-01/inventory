import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;


public class StationaryManagerFinal extends JFrame {


    private static final String URL = "jdbc:mysql://localhost:3306/stationary";
    private static final String USER = "root";
    private static final String PASSWORD = "gaurang31";


    private Connection conn;
    private JTable table;
    private DefaultTableModel model;
    private JPanel formPanel;
    private JComboBox<String> tableSelector;
    private String currentTable = "Item";
    private boolean darkMode = true;


    private JTextField searchField;


    // Reusable inputs and labels
    private JTextField[] inputs = new JTextField[10];
    private JLabel[] labels = new JLabel[10];


    public StationaryManagerFinal() {
        // Default dark theme
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.out.println("Failed to apply theme");
        }


        setTitle("Stationary Management System - Final Edition");
        setSize(1200, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));


        // Connect DB
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed: " + e.getMessage());
            return;
        }


        // === TOP PANEL ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tableSelector = new JComboBox<>(new String[]{
                "Item", "Supplier", "Customer", "Purchase", "Sales", "Sales_Details", "Supplier_Purchase"
        });
        JButton btnSwitch = new JButton("Switch Table");
        JButton btnTheme = new JButton("Toggle Theme");
        searchField = new JTextField(15);
        JButton btnFind = new JButton("Find Info");


        topPanel.add(new JLabel("Select Table:"));
        topPanel.add(tableSelector);
        topPanel.add(btnSwitch);
        topPanel.add(btnTheme);
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(btnFind);
        add(topPanel, BorderLayout.NORTH);


        // === FORM PANEL ===
        formPanel = new JPanel();
        add(formPanel, BorderLayout.CENTER);


        // === BUTTON PANEL ===
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        add(buttonPanel, BorderLayout.SOUTH);


        // === TABLE PANEL ===
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(600, 500));
        add(scroll, BorderLayout.EAST);


        // Load default view
        loadForm("Item");
        loadData("Item");


        // === EVENT LISTENERS ===
        btnSwitch.addActionListener(e -> {
            currentTable = (String) tableSelector.getSelectedItem();
            loadForm(currentTable);
            loadData(currentTable);
            clearInputs();
        });


        btnTheme.addActionListener(e -> toggleTheme());


        btnAdd.addActionListener(e -> {
            addRecord();
            clearInputs();
        });


        btnUpdate.addActionListener(e -> updateRecord());


        btnDelete.addActionListener(e -> {
            deleteRecord();
            clearInputs();
        });


        btnRefresh.addActionListener(e -> {
            loadData(currentTable);
            clearInputs();
        });


        btnFind.addActionListener(e -> searchRecords());


        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                fillFormFromTable();
            }
        });


        setVisible(true);
    }


    // --- THEME TOGGLE ---
    private void toggleTheme() {
        try {
            if (darkMode) UIManager.setLookAndFeel(new FlatLightLaf());
            else UIManager.setLookAndFeel(new FlatDarkLaf());
            darkMode = !darkMode;
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to switch theme!");
        }
    }


    // --- FORM GENERATOR ---
    private void loadForm(String tableName) {
        formPanel.removeAll();
        formPanel.setLayout(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(tableName + " Details"));


        String[] cols;
        switch (tableName) {
            case "Item" -> cols = new String[]{"Item_ID", "Name", "Quantity_In_Stock", "Unit_Price", "Brand", "Category", "Supplier_ID"};
            case "Supplier" -> cols = new String[]{"Supplier_ID", "Name", "Contact_Number", "Address", "Email"};
            case "Customer" -> cols = new String[]{"Customer_ID", "Name", "Contact_Number", "Address", "Email"};
            case "Purchase" -> cols = new String[]{"Purchase_ID", "Date", "Total_Amount", "Supplier_ID"};
            case "Sales" -> cols = new String[]{"Sales_ID", "Date", "Total_Amount", "Customer_ID"};
            case "Sales_Details" -> cols = new String[]{"Sales_ID", "Item_ID", "Quantity", "Price"};
            case "Supplier_Purchase" -> cols = new String[]{"Purchase_ID", "Item_ID", "Quantity", "Price"};
            default -> cols = new String[0];
        }


        for (int i = 0; i < cols.length; i++) {
            labels[i] = new JLabel(cols[i] + ":");
            inputs[i] = new JTextField();
            formPanel.add(labels[i]);
            formPanel.add(inputs[i]);
        }


        formPanel.revalidate();
        formPanel.repaint();
    }


    // --- LOAD DATA ---
    private void loadData(String tableName) {
        model.setRowCount(0);
        model.setColumnCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + tableName)) {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++)
                model.addColumn(meta.getColumnName(i));
            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 0; i < colCount; i++)
                    row[i] = rs.getObject(i + 1);
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading " + tableName + ": " + e.getMessage());
        }
    }


    // --- ADD RECORD ---
    private void addRecord() {
        try {
            int colCount = formPanel.getComponentCount() / 2;
            StringBuilder q = new StringBuilder("INSERT INTO " + currentTable + " VALUES(");
            for (int i = 0; i < colCount; i++) {
                q.append("?");
                if (i < colCount - 1) q.append(",");
            }
            q.append(")");
            PreparedStatement ps = conn.prepareStatement(q.toString());
            for (int i = 0; i < colCount; i++)
                ps.setString(i + 1, inputs[i].getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record added successfully!");
            loadData(currentTable);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Add failed: " + e.getMessage());
        }
    }


    // --- UPDATE RECORD ---
    private void updateRecord() {
        try {
            int colCount = formPanel.getComponentCount() / 2;
            String idCol = labels[0].getText().replace(":", "");
            StringBuilder q = new StringBuilder("UPDATE " + currentTable + " SET ");
            for (int i = 1; i < colCount; i++) {
                q.append(labels[i].getText().replace(":", "")).append("=?");
                if (i < colCount - 1) q.append(",");
            }
            q.append(" WHERE ").append(idCol).append("=?");


            PreparedStatement ps = conn.prepareStatement(q.toString());
            for (int i = 1; i < colCount; i++)
                ps.setString(i, inputs[i].getText());
            ps.setString(colCount, inputs[0].getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record updated!");
            loadData(currentTable);
            clearInputs();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
        }
    }


    // --- DELETE RECORD ---
    private void deleteRecord() {
        try {
            String idCol = labels[0].getText().replace(":", "");
            PreparedStatement ps = conn.prepareStatement("DELETE FROM " + currentTable + " WHERE " + idCol + "=?");
            ps.setString(1, inputs[0].getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record deleted!");
            loadData(currentTable);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Delete failed: " + e.getMessage());
        }
    }


    // --- SEARCH RECORDS ---
    private void searchRecords() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadData(currentTable);
            return;
        }
        try {
            String idCol = labels[0].getText().replace(":", "");
            String query = "SELECT * FROM " + currentTable + " WHERE " + idCol + " LIKE ? OR " + labels[1].getText().replace(":", "") + " LIKE ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();


            model.setRowCount(0);
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 0; i < colCount; i++)
                    row[i] = rs.getObject(i + 1);
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage());
        }
    }


    // --- CLEAR INPUTS ---
    private void clearInputs() {
        for (JTextField input : inputs) {
            if (input != null) input.setText("");
        }
    }


    // --- FILL FORM ON TABLE CLICK ---
    private void fillFormFromTable() {
        int i = table.getSelectedRow();
        if (i < 0) return;
        for (int c = 0; c < model.getColumnCount(); c++) {
            if (c < inputs.length && inputs[c] != null)
                inputs[c].setText(String.valueOf(model.getValueAt(i, c)));
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(StationaryManagerFinal::new);
    }
}

