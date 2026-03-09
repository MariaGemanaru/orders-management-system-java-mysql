package Model;

import java.sql.Date;

/**
 * Clasa  Orders reprezinta o comanda plasata de un client.
 * Contine informatii despre ID-ul comenzii, ID-ul clientului, data comenzii si suma totala
 */
public class Orders {
    private int orderID;
    private int clientID;
    private Date orderDate;
    private double totalAmount;
    /**
     * Constructor implicit pentru Orders.
     */
    public Orders(){

    }
    /**
     * Constructor pentru initializarea unei comenzi complete.
     *
     * @param orderID      ID-ul comenzii
     * @param clientID     ID-ul clientului care a plasat comanda
     * @param orderDate    Data la care a fost plasata comanda
     * @param totalAmount  Suma totala a comenzii
     */
    public Orders(int orderID, int clientID, Date orderDate, double totalAmount) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    /**
     * Constructor alternativ folosit pentru adaugarea unei comenzi fara ID explicit (generat automat).
     *
     * @param clientID     ID-ul clientului
     * @param orderDate    Data comenzii
     * @param totalAmount  Suma totala a comenzii
     */
    public Orders(int clientID, Date orderDate, double totalAmount) {
        this.clientID = clientID;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }
}
