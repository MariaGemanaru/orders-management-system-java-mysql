package Connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clasa care gestioneaza conexiunile la baza de date.
 * Asigura o instanta unica de ConnectionFactory.
 */
public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/proiecttp";
    private static final String USER = "proiecttp";
    private static final String PASS = "1234";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Constructor privat pentru a preveni instantierea externa.
     * Incarca driverul JDBC.
     */
    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creeaza o conexiune la baza de date folosind parametrii specificati.
     *
     * @return conexiunea la baza de date sau null daca a aparut o eroare
     */
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Returneaza o conexiune noua la baza de date.
     *
     * @return conexiunea obtinuta
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Inchide o conexiune la baza de date.
     *
     * @param connection conexiunea de inchis
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
            }
        }
    }

    /**
     * Inchide un obiect Statement.
     *
     * @param statement statement-ul de inchis
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
            }
        }
    }

    /**
     * Inchide un obiect ResultSet.
     *
     * @param resultSet rezultatul interogarii de inchis
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
            }
        }
    }
}
