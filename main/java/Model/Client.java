package Model;

/**
 * Clasa Client reprezinta un client inregistrat in sistem.
 * Aceasta contine datele personale si de contact ale clientului.
 */
public class Client {
    private int clientID;
    private String lastName;
    private String firstName;
    private String email;
    private String street;
    private String city;
    private String country;

    /**
     * Constructor implicit pentru Client.
     */
    public Client() {}
    /**
     * Constructor pentru initializarea unui obiect Client cu toate atributele.
     *
     * @param clientID   ID-ul clientului
     * @param lastName   Numele de familie al clientului
     * @param firstName  Prenumele clientului
     * @param email      Adresa de email a clientului
     * @param street     Strada din adresa clientului
     * @param city       Orasul clientului
     * @param country    Tara clientului
     */
    public Client(int clientID, String lastName, String firstName, String email, String street, String city, String country) {
        this.clientID = clientID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returneaza o reprezentare sub forma de text a obiectului Client.
     * Aceasta este folosita in afisarea clientilor in interfete precum JComboBox.
     *
     * @return prenumele si numele clientului
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
