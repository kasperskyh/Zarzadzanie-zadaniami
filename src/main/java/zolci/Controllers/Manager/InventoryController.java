package zolci.Controllers.Manager;

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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zolci.Controllers.Admin.AddMaterialController;
import zolci.Controllers.Admin.EditMaterialController;
import zolci.DAO.InventoryDao;
import zolci.models.Inventory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler dla zarządzania inwentarzem w aplikacji.
 */
public class InventoryController implements Initializable {

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

    public InventoryController() {
        this.inventoryDao = new InventoryDao();
    }

    /**
     * Inicjalizuje widok kontrolera.
     *
     * @param url lokalizacja URL
     * @param resourceBundle zasoby
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
     * Obsługuje akcję dodawania nowego materiału.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    public void addItemButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/add_material_form.fxml"));
        Parent root = loader.load();
        AddMaterialController addMaterialController = loader.getController();

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(root));
        popupStage.showAndWait();

        // Po zamknięciu okna odśwież dane w tabeli
        refreshTable();
    }

    /**
     * Odświeża dane w tabeli inwentarza.
     */
    public void refreshTable() {
        List<Inventory> inventoryList = inventoryDao.getAllInventory();
        ObservableList<Inventory> observableList = FXCollections.observableArrayList(inventoryList);
        inventoryTableView.setItems(observableList);
    }

    /**
     * Obsługuje akcję usunięcia zaznaczonego materiału.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     */
    @FXML
    private void deleteItemButtonOnAction(ActionEvent event) {
        // Pobierz zaznaczone wiersze z tabeli
        ObservableList<Inventory> selectedItems = inventoryTableView.getSelectionModel().getSelectedItems();

        // Sprawdź, czy coś zostało zaznaczone
        if (!selectedItems.isEmpty()) {
            // Usuń zaznaczone rekordy z bazy danych
            for (Inventory selectedItem : selectedItems) {
                inventoryDao.deleteInventory(selectedItem);
            }

            // Odśwież tabelę
            refreshTable();
        } else {
            // Wyświetl komunikat, że nie wybrano żadnego rekordu do usunięcia
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak wyboru");
            alert.setHeaderText(null);
            alert.setContentText("Proszę zaznaczyć rekord(y) do usunięcia.");
            alert.showAndWait();
        }
    }

    /**
     * Obsługuje kliknięcie na logo, przenosząc użytkownika do menu.
     *
     * @param event zdarzenie wywołane przez kliknięcie myszką
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    private void logoImageClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/menu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję wylogowania użytkownika.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję przejścia do widoku inwentarza.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    public void inventoryButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Inventory/inventory.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

    /**
     * Obsługuje akcję przejścia do widoku profilu.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    public void profileButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Profile/profile.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję przejścia do widoku raportów.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    public void raportsButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Raports/raports.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję przejścia do widoku projektów.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    public void projectsButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Projects/projects.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję przejścia do widoku zadań.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    public void tasksButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Tasks/tasks.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję przejścia do widoku zespołów.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    public void teamButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/teams.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję edycji zaznaczonego materiału.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    public void editItemOnAction(ActionEvent event) throws IOException {
        Inventory selectedInventory = inventoryTableView.getSelectionModel().getSelectedItem();
        if (selectedInventory != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Inventory/edit_inventory.fxml"));
            Parent root = loader.load();

            EditMaterialController editMaterialController = loader.getController();
            editMaterialController.setInventory(selectedInventory);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

            // Po zamknięciu okna odśwież dane w tabeli
            refreshTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak wyboru");
            alert.setHeaderText(null);
            alert.setContentText("Proszę zaznaczyć rekord do edycji.");
            alert.showAndWait();
        }
    }
}