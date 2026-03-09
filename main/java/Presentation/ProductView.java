package Presentation;

import Business_Logic.ProductBLL;
import Model.Product;
import Util.ReflectionTableBuilder;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

/**
 * Clasa responsabila pentru interfata grafica de gestionare a produselor.
 * Permite adaugarea, editarea, stergerea si vizualizarea produselor.
 */
public class ProductView extends JFrame {
    private final ProductBLL productBLL = new ProductBLL();

    private JTable productTable;
    private JScrollPane scrollPane;
    private final JButton addButton = new JButton("Add Product");
    private final JButton editButton = new JButton("Edit Product");
    private final JButton deleteButton = new JButton("Delete Product");
    private final JButton refreshButton = new JButton("Refresh Table");

    private final JTextField idField = new JTextField(5);
    private final JTextField nameField = new JTextField(15);
    private final JTextField priceField = new JTextField(10);
    private final JTextField stockField = new JTextField(10);

    /**
     * Constructorul clasei ProductView
     * Initializeaza interfata grafica si incarca datele initiale ale produselor
     */
    public ProductView() {
        /*List<Product> products = productBLL.findAllProducts();
        System.out.println("products: " + products.size());
        for (Product p : products) {
            System.out.println(p);
        }*/

        setTitle("Product Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout());

        productTable = ReflectionTableBuilder.buildTable(productBLL.findAllProducts());
        scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(2, 4));
        inputPanel.add(new JLabel("ID"));
        inputPanel.add(new JLabel("Name"));
        inputPanel.add(new JLabel("Price"));
        inputPanel.add(new JLabel("Stock"));
        inputPanel.add(idField);
        inputPanel.add(nameField);
        inputPanel.add(priceField);
        inputPanel.add(stockField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);

        addListeners();
    }

    private void addListeners() {
        addButton.addActionListener(e -> {
            try {
                Product product = extractProductFromFields();
                productBLL.insertProduct(product);
                clearInputFields();
                refreshTable();
            } catch (Exception ex) {
                showError("Add failed: " + ex.getMessage());
            }
        });

        editButton.addActionListener(e -> {
            try {
                Product product = extractProductFromFields();
                productBLL.updateProduct(product);
                clearInputFields();
                refreshTable();
            } catch (Exception ex) {
                showError("Edit failed: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                String idText = idField.getText().trim();
                if (idText.isEmpty()) {
                    throw new IllegalArgumentException("No product selected.");
                }
                int id = Integer.parseInt(idText);
                productBLL.deleteProduct(id);
                clearInputFields();
                refreshTable();
            } catch (Exception ex) {
                showError("Delete failed: " + ex.getMessage());
            }
        });

        refreshButton.addActionListener(e -> refreshTable());
        productTable.getSelectionModel().addListSelectionListener(createTableSelectionListener());
    }
    /**
     * Creeaza un listener pentru selectia randurilor din tabel
     * Populeaza campurile formularului cu datele produsului selectat
     */
    private ListSelectionListener createTableSelectionListener() {
        return new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    idField.setText(productTable.getValueAt(selectedRow, 0).toString());
                    nameField.setText(productTable.getValueAt(selectedRow, 1).toString());
                    priceField.setText(productTable.getValueAt(selectedRow, 2).toString());
                    stockField.setText(productTable.getValueAt(selectedRow, 3).toString());
                }
            }
        };
    }
    /**
     * Extrage datele introduse de utilizator si creeaza un obiect Product
     * @return un obiect Product cu valorile introduse
     */
    private Product extractProductFromFields() {
        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        return new Product(id, name, price, stock);
    }

    /**
     * Reincarca tabelul cu toate produsele actuale
     */
    private void refreshTable() {
        List<Product> products = productBLL.findAllProducts();
        productTable = ReflectionTableBuilder.buildTable(products);
        scrollPane.setViewportView(productTable);
        productTable.getSelectionModel().addListSelectionListener(createTableSelectionListener());
    }

    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Punctul de intrare al aplicatiei pentru lansarea interfetei ProductView
     *
     * @param args argumente din linia de comanda
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductView().setVisible(true));
    }
}
