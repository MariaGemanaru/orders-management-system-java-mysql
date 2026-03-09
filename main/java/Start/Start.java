package Start;

import Presentation.ClientView;
import Presentation.OrderView;
import Presentation.ProductView;

import javax.swing.*;
/**
 * Clasa Start este punctul de intrare in aplicatia grafica.
 * Aceasta initializeaza si afiseaza toate ferestrele grafice pentru:
 * - managementul clientilor,
 * - managementul produselor,
 * - managementul comenzilor.
 */
public class Start {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClientView().setVisible(true);
            new ProductView().setVisible(true);
            new OrderView().setVisible(true);
        });
    }
}

