package Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Clasa utilitara care construieste automat un JTable folosind o lista de obiecte generice.
 * Utilizeaza mecanismul de reflectie pentru a extrage numele campurilor si valorile din obiecte.
 */
public class ReflectionTableBuilder {

    /**
     * Construieste un JTable pe baza unei liste de obiecte.
     * Campurile obiectelor sunt folosite ca si coloane, iar valorile sunt populate in randuri.
     *
     * @param objects lista de obiecte generice din care se construieste tabelul
     * @param <T>     tipul obiectelor din lista
     * @return JTable completat cu datele extrase din obiecte
     */
    public static <T> JTable buildTable(List<T> objects) {
        if (objects == null || objects.isEmpty()) {
            return new JTable();
        }

        T sample = objects.get(0);
        Field[] fields = sample.getClass().getDeclaredFields();

        String[] columnNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            columnNames[i] = fields[i].getName();
        }

        Object[][] data = new Object[objects.size()][fields.length];

        for (int i = 0; i < objects.size(); i++) {
            T obj = objects.get(i);
            for (int j = 0; j < fields.length; j++) {
                fields[j].setAccessible(true);
                try {
                    data[i][j] = fields[j].get(obj);
                } catch (IllegalAccessException e) {
                    data[i][j] = "ERROR";
                }
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return new JTable(model);
    }
}
