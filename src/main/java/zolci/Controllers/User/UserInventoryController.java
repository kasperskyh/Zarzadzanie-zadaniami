package zolci.Controllers.User;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import zolci.DAO.InventoryDao;
import zolci.UserPreferences;
import zolci.models.Inventory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler do obsługi widoku inwentarza użytkownika.
 */
public class UserInventoryController implements Initializable {
    @FXML
    private ImageView logoImage;
    @FXML
    private Text welcomeText;
    @FXML
    private TableView<Inventory> inventoryTableView;
    @FXML
    private TableColumn<Inventory, Integer> idColumn;
    @FXML
    private TableColumn<Inventory, String> nameColumn;
    @FXML
    private TableColumn<Inventory, Integer> quantityColumn;
    @FXML
    private TableColumn<Inventory, Double> unitPriceColumn;
    @FXML
    private TableColumn<Inventory, String> supplierColumn;


    private InventoryDao inventoryDao;

    /**
     * Konstruktor klasy UserInventoryController.
     * Inicjalizuje DAO do zarządzania inwentarzem.
     */
    public UserInventoryController() {
        this.inventoryDao = new InventoryDao();
    }

    /**
     * Inicjalizuje kontroler. Ustawia logo, tekst powitalny oraz wypełnia tabelę danymi inwentarza.
     *
     * @param url             Lokalizacja używana do rozwiązywania względnych ścieżek dla obiektu głównego lub null.
     * @param resourceBundle  Zasoby lokalizowane lub null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);

        List<Inventory> inventoryList = inventoryDao.getAllInventory();
        ObservableList<Inventory> observableList = FXCollections.observableArrayList(inventoryList);

        inventoryTableView.setItems(observableList);

        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        unitPriceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getUnitPrice()).asObject());
        supplierColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSupplier()));
    }

    /**
     * Obsługuje zdarzenie wylogowania użytkownika. Przełącza scenę na widok logowania.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie przejścia do widoku zadań użytkownika. Przełącza scenę na widok zadań.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    public void tasksButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/User_tasks.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie przejścia do widoku profilu użytkownika. Przełącza scenę na widok profilu.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    public void profileButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/User_profil.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie przejścia do widoku inwentarza użytkownika. Przełącza scenę na widok inwentarza.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    public void inventoryButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/User_inventory.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
