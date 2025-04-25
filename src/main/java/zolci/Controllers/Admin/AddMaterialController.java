package zolci.Controllers.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zolci.DAO.InventoryDao;
import zolci.models.Inventory;

import java.io.IOException;

/**
 * Kontroler odpowiedzialny za dodawanie nowych materiałów do magazynu.
 */
public class AddMaterialController {
    private InventoryController inventoryController;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField unitPriceTextField;
    @FXML
    private TextField supplierTextField;

    private InventoryDao inventoryDao;
    private static final Logger logger = LogManager.getLogger(AddMaterialController.class);

    /**
     * Inicjalizuje kontroler, tworząc nowy obiekt InventoryDao.
     */
    public void initialize() {
        // Inicjalizacja obiektu InventoryDao
        this.inventoryDao = new InventoryDao();
    }

    /**
     * Dodaje nowy przedmiot do magazynu na podstawie danych wprowadzonych przez użytkownika.
     *
     * @throws IOException Jeśli wystąpi błąd wejścia/wyjścia podczas dodawania przedmiotu.
     */
    @FXML
    private void addItem() throws IOException {
        // Pobierz dane z pól tekstowych i spinnera
        String name = nameTextField.getText();
        int quantity = Integer.parseInt(quantityTextField.getText());
        double unitPrice = Double.parseDouble(unitPriceTextField.getText());
        String supplier = supplierTextField.getText();

        // Utwórz nowy obiekt Inventory
        Inventory newInventory = new Inventory();
        newInventory.setName(name);
        newInventory.setQuantity(quantity);
        newInventory.setUnitPrice(unitPrice);
        newInventory.setSupplier(supplier);

        // Dodaj nowy materiał do bazy danych
        inventoryDao.addInventory(newInventory);
        logger.info("Dodano nowy obiekt");

        // Zamknij okno dodawania materiału
        closeAddMaterialForm();
    }


    /**
     * Zamykaj okno formularza dodawania materiału.
     *
     * @throws IOException Jeśli wystąpi błąd wejścia/wyjścia podczas zamykania formularza.
     */
    @FXML
    private void closeAddMaterialForm() throws IOException {
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }

}

