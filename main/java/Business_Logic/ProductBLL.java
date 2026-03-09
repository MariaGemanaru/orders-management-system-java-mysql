package Business_Logic;

import Business_Logic.validators.StockValidator;
import Business_Logic.validators.Validator;
import DataAccess.ProductDAO;
import Model.Client;
import Model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Clasa ProductBLL reprezinta nivelul de logica de business pentru operatiile legate de produse.
 * Aceasta utilizeaza un Data Access Object (ProductDAO) pentru a interactiona cu baza de date
 * si o lista de validatori pentru a valida obiectele de tip Product.
 */

public class ProductBLL {
    private List<Validator<Product>> validators;
    private ProductDAO productDAO;

    /**
     * Constructorul initializeaza validatorii (ex: verificarea stocului)
     * si instanta DAO folosita pentru accesul la baza de date.
     */
    public ProductBLL() {
        validators = new ArrayList<>();
        validators.add(new StockValidator());
        productDAO = new ProductDAO();
    }

    /**
     * Cauta si returneaza un produs pe baza ID-ului sau.
     * Daca produsul nu este gasit, se arunca exceptia NoSuchElementException.
     * @param id ID-ul produsului cautat
     * @return produsul cu ID-ul specificat
     * @throws NoSuchElementException daca produsul nu este gasit
     */
    public Product findProductById(int id) {
        Product p = productDAO.findById(id);
        if (p == null) {
            throw new NoSuchElementException("Product with id =" + id + " was not found!");
        }
        return p;
    }
    /**
     * Insereaza un produs nou in baza de date dupa ce trece de validatori.
     * @param p produsul care urmeaza sa fie inserat
     * @return produsul inserat
     */
    public Product insertProduct(Product p) {
        for (Validator<Product> v : validators) {
            v.validate(p);
        }
        return productDAO.insert(p);
    }

    /**
     * Returneaza lista tuturor produselor din baza de date
     *
     * @return lista de produse
     */
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Sterge produsul cu ID-ul specificat din baza de date.
     *
     * @param id ID-ul produsului de sters
     */
    public void deleteProduct(int id) {
        productDAO.deleteById(id);
    }

    /**
     * Actualizeaza datele unui produs existent dupa ce trece de validatori.
     * @param product produsul cu noile valori care trebuie actualizat
     * @return produsul actualizat
     */
    public Product updateProduct(Product product) {
        for (Validator<Product> v : validators) {
            v.validate(product);
        }
        return productDAO.update(product);
    }
}
