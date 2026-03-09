package Presentation;

import Business_Logic.ClientBLL;
import Business_Logic.OrderBLL;
import Business_Logic.ProductBLL;
import Model.Client;
import Model.Orders;
import Model.Product;
import Util.ReflectionTableBuilder;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Clasa responsabila pentru interfata grafica de gestionare a comenzilor.
 * Permite vizualizarea, adaugarea de comenzi si actualizarea stocurilor aferente produselor comandate.
 */
public class OrderView extends JFrame {
    private final OrderBLL orderBLL = new OrderBLL();
    private final ClientBLL clientBLL = new ClientBLL();
    private final ProductBLL productBLL = new ProductBLL();

    private JTable orderTable;
    private JScrollPane scrollPane;
    private final JButton addButton = new JButton("Add Order");
    //private final JButton deleteButton = new JButton("Delete Order");
    private final JButton refreshButton = new JButton("Refresh Table");

    private final JComboBox<Client> clientBox = new JComboBox<>();
    private final JComboBox<Product> productBox = new JComboBox<>();
    private final JTextField quantityField = new JTextField(10);

    /**
     * Constructorul clasei OrderView.
     * Initializeaza interfata grafica, componentele si datele necesare pentru gestionarea comenzilor.
     */
    public OrderView() {
        setTitle("Order Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        orderTable = ReflectionTableBuilder.buildTable(orderBLL.findAllOrders());
        scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(2, 3));
        inputPanel.add(new JLabel("Client"));
        inputPanel.add(new JLabel("Product"));
        inputPanel.add(new JLabel("Quantity"));
        inputPanel.add(clientBox);
        inputPanel.add(productBox);
        inputPanel.add(quantityField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
       // buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);

        loadComboBoxes();
        addListeners();
    }
    /**
     * Incarca datele din baza de date in combo box urile pentru clienti si produse.
     */
    private void loadComboBoxes() {
        clientBox.removeAllItems();
        productBox.removeAllItems();

        for (Client c : clientBLL.findAllClients()) {
            clientBox.addItem(c);
        }

        for (Product p : productBLL.findAllProducts()) {
            productBox.addItem(p);
        }
    }
    /**
     * Adaug listeners pentru butoanele Add si Refresh.
     * Gestioneaza logica de plasare a unei comenzi si actualizare a stocului produsului.
     */
    private void addListeners() {
        addButton.addActionListener(e -> {
            try {
                Client client = (Client) clientBox.getSelectedItem();
                Product product = (Product) productBox.getSelectedItem();
                int quantity = Integer.parseInt(quantityField.getText());

                if (product.getStockQuantity() < quantity) {
                    showError("Not enough stock available.");
                    return;
                }

                double total = quantity * product.getPrice();
                Orders order = new Orders(client.getClientID(), Date.valueOf(LocalDate.now()), total);
                orderBLL.insertOrder(order);
                product.setStockQuantity(product.getStockQuantity() - quantity);
                productBLL.updateProduct(product);

                clearInputs();
                refreshTable();
            } catch (Exception ex) {
                showError("Add failed: " + ex.getMessage());
            }
        });

        /*deleteButton.addActionListener(e -> {
            try {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = Integer.parseInt(orderTable.getValueAt(selectedRow, 0).toString());
                    orderBLL.deleteOrder(id);
                    refreshTable();
                } else {
                    showError("No order selected.");
                }
            } catch (Exception ex) {
                showError("Delete failed: " + ex.getMessage());
            }
        });*/

        refreshButton.addActionListener(e -> refreshTable());
    }

    /**
     * Reseteaza campurile de input la valori goale dupa adaugarea unei comenzi.
     */
    private void clearInputs() {
        quantityField.setText("");
    }
    /**
     * Reincarca tabelul cu toate comenzile actuale din baza de date.
     */
    private void refreshTable() {
        orderTable = ReflectionTableBuilder.buildTable(orderBLL.findAllOrders());
        scrollPane.setViewportView(orderTable);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Punctul de intrare principal pentru pornirea interfetei OrderView.
     *
     * @param args argumente din linia de comanda
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderView().setVisible(true));
    }
}