package zolci.Controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zolci.DAO.InventoryDao;
import zolci.models.Inventory;

/**
 * Kontroler odpowiedzialny za edytowanie materiałów w inwentarzu.
 */
public class EditMaterialController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField unitPriceTextField;
    @FXML
    private TextField supplierTextField;

    private Inventory inventory;
    private InventoryDao inventoryDao;

    /**
     * Konstruktor kontrolera. Inicjalizuje obiekt InventoryDao.
     */
    public EditMaterialController() {
        this.inventoryDao = new InventoryDao();
    }

    /**
     * Ustawia inwentarz, który ma zostać edytowany, i wypełnia pola tekstowe danymi z tego inwentarza.
     *
     * @param inventory Inwentarz do edycji.
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        nameTextField.setText(inventory.getName());
        quantityTextField.setText(String.valueOf(inventory.getQuantity()));
        unitPriceTextField.setText(String.valueOf(inventory.getUnitPrice()));
        supplierTextField.setText(inventory.getSupplier());
    }

    /**
     * Zapisuje zmiany w inwentarzu po edycji i zamyka okno edycji.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void save(ActionEvent event) {
        inventory.setName(nameTextField.getText());
        inventory.setQuantity(Integer.parseInt(quantityTextField.getText()));
        inventory.setUnitPrice(Double.parseDouble(unitPriceTextField.getText()));
        inventory.setSupplier(supplierTextField.getText());

        inventoryDao.updateInventory(inventory);

        close(event);
    }

    /**
     * Zamyka okno edycji materiału.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void close(ActionEvent event) {
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }
}
