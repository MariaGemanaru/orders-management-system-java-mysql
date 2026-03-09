package Business_Logic;

import DataAccess.OrderDAO;
import DataAccess.ProductDAO;
import Model.Client;
import Model.Orders;
import Model.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.sql.Date;

/**
 * Clasa OrderBLL reprezinta nivelul de logica de business pentru comenzile clientilor.
 * Aceasta gestioneaza operatiile CRUD pentru comenzi si plasarea efectiva a comenzilor.
 */
public class OrderBLL {
    private OrderDAO orderDAO;
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Constructorul initializeaza DAO-ul pentru comenzi.
     */
    public OrderBLL() {
        orderDAO = new OrderDAO();
    }

    /**
     * Cauta si returneaza o comanda pe baza ID-ului sau.
     * Daca comanda nu este gasita, se arunca exceptia NoSuchElementException.
     *
     * @param id ID-ul comenzii cautate
     * @return comanda cu ID-ul specificat
     * @throws NoSuchElementException daca comanda nu este gasita
     */
    public Orders findOrderById(int id) {
        Orders o = orderDAO.findById(id);
        if (o == null) {
            throw new NoSuchElementException("Order with id =" + id + " was not found!");
        }
        return o;
    }

    /**
     * Insereaza o comanda noua in baza de date.
     *
     * @param o obiectul Orders care urmeaza sa fie inserat
     * @return comanda inserata
     */
    public Orders insertOrder(Orders o) {
        return orderDAO.insert(o);
    }

    /**
     * Returneaza lista tuturor comenzilor din baza de date.
     *
     * @return lista de comenzi
     */
    public List<Orders> findAllOrders() {
        return orderDAO.findAll();
    }

    /**
     * Sterge comanda cu ID-ul specificat din baza de date.
     *
     * @param id ID-ul comenzii care trebuie stearsa
     */
    public void deleteOrder(int id) {
        orderDAO.deleteById(id);
    }

    /**
     * Plaseaza o noua comanda pentru un client si un produs specificat.
     * Daca stocul este insuficient, se arunca exceptia IllegalArgumentException.
     * Comanda este inregistrata, iar stocul produsului este actualizat.
     *
     * @param client clientul care plaseaza comanda
     * @param product produsul comandat
     * @param quantity cantitatea dorita
     * @throws IllegalArgumentException daca stocul este insuficient
     */
    public void placeOrder(Client client, Product product, int quantity) {
        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        // Creeaza o noua comanda
        double total = product.getPrice() * quantity;
        Date sqlDate = Date.valueOf(LocalDate.now());
        Orders order = new Orders(0, client.getClientID(), sqlDate, total);
        orderDAO.insert(order);

        // Actualizeaza stocul produsului
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productDAO.update(product);
    }
}
