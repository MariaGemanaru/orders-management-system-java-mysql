package Business_Logic.validators;

import Model.Client;
import java.util.regex.Pattern;

/**
 * Clasa EmailValidator valideaza adresa de email a unui client.
 * Verifica daca adresa respecta formatul standard al unui email.
 */
public class EmailValidator implements Validator<Client> {
    private static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

    /**
     * Verifica daca email-ul clientului respecta formatul definit.
     * In caz contrar, arunca o exceptie.
     *
     * @param c obiectul Client care urmeaza sa fie validat
     * @throws IllegalArgumentException daca email-ul nu este valid
     */
    public void validate(Client c) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        if (!pattern.matcher(c.getEmail()).matches()) {
            throw new IllegalArgumentException("Email is not valid!");
        }
    }
}
