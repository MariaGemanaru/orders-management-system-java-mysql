package Presentation;
import Business_Logic.ClientBLL;
import Model.Client;
import Util.ReflectionTableBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Clasa responsabila pentru interfata grafica a modulului de management al clientilor.
 * Permite adaugarea, editarea, stergerea si afisarea clientilor folosind un JTable si JTextFields.
 */
public class ClientView extends JFrame {
    private final ClientBLL clientBLL = new ClientBLL();

    private JTable clientTable;
    private JScrollPane scrollPane;
    private final JButton addButton = new JButton("Add Client");
    private final JButton editButton = new JButton("Edit Client");
    private final JButton deleteButton = new JButton("Delete Client");
    private final JButton refreshButton = new JButton("Refresh Table");

    private final JTextField idField = new JTextField(5);
    private final JTextField lastNameField = new JTextField(10);
    private final JTextField firstNameField = new JTextField(10);
    private final JTextField emailField = new JTextField(15);
    private final JTextField streetField = new JTextField(10);
    private final JTextField cityField = new JTextField(10);
    private final JTextField countryField = new JTextField(10);

    /**
     * Constructorul interfetei grafice ClientView.
     * Initializeaza componentele si configureaza layout-ul principal.
     */
    public ClientView() {
        setTitle("Client Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        clientTable = ReflectionTableBuilder.buildTable(clientBLL.findAllClients());
        scrollPane = new JScrollPane(clientTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(2, 7));
        inputPanel.add(new JLabel("ID"));
        inputPanel.add(new JLabel("Last Name"));
        inputPanel.add(new JLabel("First Name"));
        inputPanel.add(new JLabel("Email"));
        inputPanel.add(new JLabel("Street"));
        inputPanel.add(new JLabel("City"));
        inputPanel.add(new JLabel("Country"));
        inputPanel.add(idField);
        inputPanel.add(lastNameField);
        inputPanel.add(firstNameField);
        inputPanel.add(emailField);
        inputPanel.add(streetField);
        inputPanel.add(cityField);
        inputPanel.add(countryField);

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

    /**
     * Adauga listeneri pentru butoanele interfetei si selectia din tabel.
     */
    private void addListeners() {
        addButton.addActionListener(e -> {
            try {
                Client client = extractClientFromFields();
                clientBLL.insertClient(client);
                refreshTable();
            } catch (Exception ex) {
                showError("Add failed: " + ex.getMessage());
            }
        });

        editButton.addActionListener(e -> {
            try {
                Client client = extractClientFromFields();
                clientBLL.updateClient(client);
                refreshTable();
            } catch (Exception ex) {
                showError("Edit failed: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                String idText = idField.getText().trim();
                if (idText.isEmpty()) {
                    throw new IllegalArgumentException("No client selected.");
                }
                int id = Integer.parseInt(idText);
                clientBLL.deleteClient(id);
                clearInputFields();  // curăță câmpurile după ștergere
                refreshTable();
            } catch (Exception ex) {
                showError("Delete failed: " + ex.getMessage());
            }
        });

        refreshButton.addActionListener(e -> refreshTable());

        clientTable.getSelectionModel().addListSelectionListener(createTableSelectionListener());
    }

    /**
     * Reseteaza campurile de introducere a datelor la valori goale.
     */
    private void clearInputFields() {
        idField.setText("");
        lastNameField.setText("");
        firstNameField.setText("");
        emailField.setText("");
        streetField.setText("");
        cityField.setText("");
        countryField.setText("");
    }

    /**
     * Creeaza un listener pentru selectia din JTable care incarca datele in campurile de text.
     *
     * @return ListSelectionListener care gestioneaza selectia randurilor din tabel
     */
    private ListSelectionListener createTableSelectionListener() {
        return new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = clientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    idField.setText(clientTable.getValueAt(selectedRow, 0).toString());
                    lastNameField.setText(clientTable.getValueAt(selectedRow, 1).toString());
                    firstNameField.setText(clientTable.getValueAt(selectedRow, 2).toString());
                    emailField.setText(clientTable.getValueAt(selectedRow, 3).toString());
                    streetField.setText(clientTable.getValueAt(selectedRow, 4).toString());
                    cityField.setText(clientTable.getValueAt(selectedRow, 5).toString());
                    countryField.setText(clientTable.getValueAt(selectedRow, 6).toString());
                }
            }
        };
    }

    /**
     * Extrage datele din campurile text si le construieste intr-un obiect de tip Client.
     *
     * @return un obiect Client completat cu valorile din interfata
     */
    private Client extractClientFromFields() {
        int id = Integer.parseInt(idField.getText());
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String email = emailField.getText();
        String street = streetField.getText();
        String city = cityField.getText();
        String country = countryField.getText();
        return new Client(id, lastName, firstName, email, street, city, country);
    }

    /**
     * Reincarca tabelul cu lista actualizata de clienti din baza de date.
     */
    private void refreshTable() {
        List<Client> clients = clientBLL.findAllClients();
        clientTable = ReflectionTableBuilder.buildTable(clients);

        scrollPane.setViewportView(clientTable);
        clientTable.getSelectionModel().addListSelectionListener(createTableSelectionListener());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Metoda principala care porneste aplicatia grafica pentru gestionarea clientilor.
     * @param args argumente din linia de comanda
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientView().setVisible(true));
    }
}