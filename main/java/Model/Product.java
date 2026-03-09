package Model;

/**
 * Clasa Product reprezinta un produs disponibil pentru comanda.
 * Aceasta contine atribute precum ID-ul produsului, numele, pretul si cantitatea in stoc.
 */
public class Product {
    private int productID;
    private String productName;
    private double price;
    private int stockQuantity;

    /**
     * Constructor fara parametrii
     */
    public Product(){}

    /**
     * Constructor cu parametri pentru initializarea unui obiect Product.
     * @param productID ID-ul produsului
     * @param productName Numele produsului
     * @param price Pretul produsului
     * @param stockQuantity Cantitatea disponibila in stoc
     */
    public Product(int productID, String productName, double price, int stockQuantity) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Suprascrie metoda toString pentru a returna numele produsului.
     * Aceasta este folosita pentru afisarea in elemente vizuale (ex: JComboBox).
     * @return numele produsului
     */
    @Override
    public String toString() {
        return productName;
    }
}
