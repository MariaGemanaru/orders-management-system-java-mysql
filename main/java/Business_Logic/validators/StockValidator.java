package Business_Logic.validators;

import Model.Product;

/**
 * Clasa StockValidator valideaza stocul disponibil al unui produs.
 * Asigura faptul ca stocul nu este un numar negativ.
 */
public class StockValidator implements Validator<Product> {

    /**
     * Verifica daca stocul produsului este valid (adica >= 0).
     * In caz contrar, arunca o exceptie.
     * @param p obiectul Product care urmeaza sa fie validat
     * @throws IllegalArgumentException daca stocul este negativ
     */
    public void validate(Product p) {
        if (p.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative!");
        }
    }
}
