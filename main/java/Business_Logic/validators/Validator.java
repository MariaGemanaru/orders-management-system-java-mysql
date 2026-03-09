package Business_Logic.validators;

/**
 * Interfata generică pentru validatori.
 * Fiecare clasa care implementeaza aceasta interfata va defini o regula de validare
 * pentru tipul de obiect specificat.
 * @param <T> tipul obiectului ce urmeaza sa fie validat
 */
public interface Validator<T> {

    /**
     * Metoda care realizeaza validarea obiectului dat.
     * @param t obiectul care trebuie validat
     * @throws IllegalArgumentException daca obiectul nu respecta regulile de validare
     */
    public void validate(T t);
}
