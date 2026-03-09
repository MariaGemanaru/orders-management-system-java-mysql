package Business_Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import Business_Logic.validators.EmailValidator;
import Business_Logic.validators.Validator;
import DataAccess.ClientDAO;
import Model.Client;

/**
 * Clasa ClientBLL reprezinta nivelul de logica de business pentru gestionarea clientilor.
 * Aceasta se ocupa de validarea si manipularea obiectelor Client inainte de a fi trimise catre nivelul de acces la date.
 */
public class ClientBLL {
    private List<Validator<Client>> validators;
    private ClientDAO clientDAO;

    /**
     * Constructorul initializeaza lista de validatori si instanta DAO pentru clienti.
     */
    public ClientBLL() {
        validators = new ArrayList<>();
        validators.add(new EmailValidator());
        clientDAO = new ClientDAO();
    }

    /**
     * Cauta si returneaza un client pe baza ID-ului sau.
     * Daca clientul nu este gasit, se arunca o exceptie.
     * @param id ID-ul clientului cautat
     * @return clientul cu ID-ul respectiv
     * @throws NoSuchElementException daca clientul nu este gasit
     */
    public Client findClientById(int id) {
        Client c = clientDAO.findById(id);
        if (c == null) {
            throw new NoSuchElementException("Client with id =" + id + " was not found!");
        }
        return c;
    }

    /**
     * Insereaza un nou client in baza de date dupa validarea acestuia.
     * @param c obiectul Client care urmeaza sa fie inserat
     * @return clientul inserat
     */
    public Client insertClient(Client c) {
        for (Validator<Client> v : validators) {
            v.validate(c);
        }
        return clientDAO.insert(c);
    }

    /**
     * Returneaza lista tuturor clientilor existenti in baza de date.
     * @return lista cu toti clientii
     */
    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }

    /**
     * Sterge clientul cu ID-ul specificat din baza de date.
     * @param id ID-ul clientului care trebuie sters
     */
    public void deleteClient(int id) {
        clientDAO.deleteById(id);
    }

    /**
     * Actualizeaza informatiile unui client dupa ce acesta este validat.
     * @param client obiectul Client cu informatii noi
     * @return clientul actualizat
     */
    public Client updateClient(Client client) {
        for (Validator<Client> v : validators) {
            v.validate(client);
        }
        return clientDAO.update(client);
    }
}
