package DataAccess;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import Connection.ConnectionFactory;

/**
 * Clasa generic AbstractDAO<T> implementeaza operatii CRUD de baza
 * (create, read, update, delete) utilizand reflectia.
 *
 * @param <T> tipul obiectului gestionat de DAO
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    /**
     * Constructorul determina clasa concreta a obiectului T folosind reflectia.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }


    private String createDeleteQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * Returneaza un obiect de tip T pe baza ID-ului specificat.
     *
     * @param id ID-ul entitatii cautate
     * @return obiectul gasit sau null daca nu exista
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            List<T> list = createObjects(resultSet);
            if (!list.isEmpty()) return list.get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Returneaza lista tuturor obiectelor de tip T din tabela asociata.
     *
     * @return lista de obiecte T
     */
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            list = createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return list;
    }

    /**
     * Insereaza in baza de date un obiect T.
     * Utilizeaza reflectia pentru a extrage campurile si valorile.
     *
     * @param t obiectul de inserat
     * @return obiectul inserat
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            StringBuilder fields = new StringBuilder();
            StringBuilder values = new StringBuilder();
            List<Object> parameters = new ArrayList<>();

            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                fields.append(field.getName()).append(", ");
                values.append("?, ");
                parameters.add(field.get(t));
            }
            fields.setLength(fields.length() - 2);
            values.setLength(values.length() - 2);

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ");
            sb.append(type.getSimpleName());
            sb.append(" (").append(fields).append(") VALUES (").append(values).append(")");

            statement = connection.prepareStatement(sb.toString());

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Insert error: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    /**
     * Actualizeaza in baza de date un obiect T.
     * Campul de identificare este determinat automat (id sau cevaID).
     *
     * @param t obiectul care trebuie actualizat
     * @return obiectul actualizat
     */
    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();
            StringBuilder sb = new StringBuilder();
            List<Object> parameters = new ArrayList<>();
            Object idValue = null;

            String idField = Arrays.stream(type.getDeclaredFields())
                    .map(Field::getName)
                    .filter(name -> name.equalsIgnoreCase("id") || name.endsWith("ID"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No ID field found"));

            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equalsIgnoreCase("id") || field.getName().equalsIgnoreCase(idField)) {
                    idValue = field.get(t);
                    continue;
                }
                sb.append(field.getName()).append(" = ?, ");
                parameters.add(field.get(t));
            }
            sb.setLength(sb.length() - 2); // remove last comma

            StringBuilder query = new StringBuilder();
            query.append("UPDATE ");
            query.append(type.getSimpleName());
            query.append(" SET ").append(sb).append(" WHERE ").append(idField).append(" = ?");

            statement = connection.prepareStatement(query.toString());
            int i = 1;
            for (Object param : parameters) {
                statement.setObject(i++, param);
            }
            statement.setObject(i, idValue); // set ID at the end

            statement.executeUpdate();

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Update error: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return t;
    }

    /**
     * Sterge un obiect din baza de date pe baza ID-ului.
     *
     * @param id ID-ul obiectului de sters
     */
    public void deleteById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        // Presupune că numele coloanei ID în DB este de forma: ClientID, ProductID etc.
        String fieldName = type.getSimpleName() + "ID"; // Ex: ClientID
        String query = createDeleteQuery(fieldName);

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Delete error: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /*private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        try {
            Constructor<T> ctor = type.getDeclaredConstructor();
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), type);
                    Method setter = pd.getWriteMethod();
                    setter.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }*/

    /**
     * Creeaza o lista de obiecte de tip T dintr-un ResultSet.
     * Foloseste constructorul implicit si metoda set pentru fiecare camp.
     *
     * @param resultSet setul de rezultate SQL
     * @return lista de obiecte de tip T
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        try {
            Constructor<T> ctor = type.getDeclaredConstructor();
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = ctor.newInstance();

                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();

                    // Search column name case-insensitively
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    String actualColumnName = null;
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        if (metaData.getColumnName(i).equalsIgnoreCase(fieldName)) {
                            actualColumnName = metaData.getColumnName(i);
                            break;
                        }
                    }

                    if (actualColumnName != null) {
                        Object value = resultSet.getObject(actualColumnName);
                        PropertyDescriptor pd = new PropertyDescriptor(fieldName, type);
                        Method setter = pd.getWriteMethod();
                        setter.invoke(instance, value);
                    }
                }

                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
