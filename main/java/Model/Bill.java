package Model;

/**
 * Reprezinta o factura generata in urma unei comenzi. Clasa este imutabila si utilizeaza
 * un record Java pentru a stoca detaliile unei comenzi facturate.
 *
 * @param orderId     ID-ul unic al comenzii pentru care a fost emisa factura.
 * @param clientName  Numele clientului care a plasat comanda.
 * @param productName Numele produsului comandat.
 * @param quantity    Cantitatea de produs comandata.
 * @param totalPrice  Pretul total al comenzii (cantitate * pret unitar).
 */
public record Bill(int orderId, String clientName, String productName, int quantity, double totalPrice) {
}