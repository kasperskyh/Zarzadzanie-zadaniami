package zolci.Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zolci.DAO.InventoryDao;
import zolci.DAO.ProjectDao;
import zolci.DAO.TaskDao;
import zolci.DAO.TaskInventoryDao;
import zolci.models.Inventory;
import zolci.models.Projects;
import zolci.models.TaskInventory;
import zolci.models.Tasks;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler do obsługi dodawania materiałów do zadania przez użytkownika.
 */
public class UserAddMaterialForTaskController implements Initializable {

    @FXML
    private Label prizeLabel;
    @FXML
    private TextField quantityTextField;
    @FXML
    private ComboBox<String> nameComboBox;

    private InventoryDao inventoryDao = new InventoryDao();
    private ProjectDao projectDao = new ProjectDao();
    private TaskInventoryDao taskInventoryDao = new TaskInventoryDao();
    private TaskDao tasksDao = new TaskDao();  // Dodane DAO dla Tasks
    private int taskId; // Dodane pole do przechowywania taskId

    /**
     * Inicjalizuje kontroler. Ustawia domyślną wartość etykiety ceny oraz wypełnia listę rozwijaną nazwami materiałów.
     *
     * @param url             Lokalizacja używana do rozwiązywania względnych ścieżek dla obiektu głównego lub null.
     * @param resourceBundle  Zasoby lokalizowane lub null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prizeLabel.setText("0");
        populateComboBox();
        setupListeners();
    }

    /**
     * Ustawia ID zadania.
     *
     * @param taskId ID zadania.
     */
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    /**
     * Wypełnia listę rozwijaną nazwami wszystkich dostępnych materiałów.
     */
    private void populateComboBox() {
        List<String> inventoryNames = inventoryDao.getAllInventoryNames();
        if (inventoryNames != null) {
            nameComboBox.getItems().addAll(inventoryNames);
        }
    }

    /**
     * Ustawia słuchaczy dla listy rozwijanej i pola tekstowego ilości, aby zaktualizować cenę.
     */
    private void setupListeners() {
        nameComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updatePrice());
        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> updatePrice());
    }

    /**
     * Aktualizuje cenę na podstawie wybranego materiału i wprowadzonej ilości.
     */
    private void updatePrice() {
        String selectedName = nameComboBox.getValue();
        String quantityText = quantityTextField.getText();

        if (selectedName != null && !quantityText.isEmpty() && quantityText.matches("\\d+")) {
            double unitPrice = inventoryDao.getUnitPriceByName(selectedName);
            int quantity = Integer.parseInt(quantityText);
            double totalPrice = unitPrice * quantity;
            prizeLabel.setText(String.valueOf(totalPrice));
        } else {
            prizeLabel.setText("0");
        }
    }

    /**
     * Zamyka okno.
     *
     * @param event Zdarzenie akcji.
     */
    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) quantityTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * Dodaje wybrany materiał do zadania.
     *
     * @param event Zdarzenie akcji.
     */
    public void addMaterial(ActionEvent event) {
        String selectedName = nameComboBox.getValue();
        String quantityText = quantityTextField.getText();

        if (selectedName != null && !quantityText.isEmpty() && quantityText.matches("\\d+")) {
            int quantity = Integer.parseInt(quantityText);
            double unitPrice = inventoryDao.getUnitPriceByName(selectedName);
            double totalPrice = unitPrice * quantity;

            Inventory selectedInventory = inventoryDao.getInventoryByName(selectedName);
            int availableQuantity = selectedInventory.getQuantity();

            if (quantity > availableQuantity) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(null);
                alert.setContentText("Podana ilość przekracza dostępną ilość materiału w magazynie!");
                alert.showAndWait();
                return;
            }

            Tasks task = tasksDao.getTaskById(taskId);

            TaskInventory taskInventory = new TaskInventory();
            taskInventory.setTask(task);
            taskInventory.setInventory(selectedInventory);
            taskInventory.setQuantity(quantity);
            taskInventory.setValue(totalPrice);

            taskInventoryDao.addTaskInventory(taskInventory);

            // Aktualizuj ilość w magazynie
            selectedInventory.setQuantity(availableQuantity - quantity);
            inventoryDao.updateInventory(selectedInventory);

            // Aktualizuj wartość w projekcie
            Projects project = task.getProjectId();
            projectDao.updateProjectValuation(project, totalPrice);

            closeWindow(event);
        }
    }
}
