package zolci.Controllers.Admin;

import com.itextpdf.text.DocumentException;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


import org.example.PdfReportGenerator;
import zolci.DAO.InventoryDao;
import zolci.DAO.TaskInventoryDao;
import zolci.UserPreferences;
import zolci.models.Inventory;
import zolci.models.TaskInventory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler obsługujący widok zarządzania magazynem.
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
    private TaskInventoryDao taskInventoryDao = new TaskInventoryDao();

    /**
     * Kontroler obsługujący widok zarządzania magazynem.
     */
    public InventoryController() {
        this.inventoryDao = new InventoryDao();
    }

    /**
     * Inicjalizuje widok po załadowaniu.
     * Ustawia logo, powitanie użytkownika oraz dane w tabeli magazynu.
     *
     * @param url            URL do zasobu FXML (nieużywane).
     * @param resourceBundle Zasoby do lokalizacji (nieużywane).
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

    @FXML
    private void logoImageClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/menu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję wylogowania użytkownika.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Błąd podczas ładowania pliku FXML.
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję wylogowania użytkownika.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Błąd podczas ładowania pliku FXML.
     */
    @FXML
    public void addItemButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/add_material_form.fxml"));
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
     * Odświeża dane w tabeli magazynu.
     */
    public void refreshTable() {
        List<Inventory> inventoryList = inventoryDao.getAllInventory();
        ObservableList<Inventory> observableList = FXCollections.observableArrayList(inventoryList);
        inventoryTableView.setItems(observableList);
    }

    /**
     * Otwiera widok inwentarza.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void inventoryButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Inventory/inventory.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję usuwania wybranego przedmiotu z magazynu.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    private void deleteItemButtonOnAction(ActionEvent event) {
        // Pobierz zaznaczone wiersze z tabeli
        ObservableList<Inventory> selectedItems = inventoryTableView.getSelectionModel().getSelectedItems();

        // Sprawdź, czy coś zostało zaznaczone
        if (!selectedItems.isEmpty()) {
            for (Inventory selectedItem : selectedItems) {
                // Pobierz powiązane rekordy TaskInventory dla bieżącego przedmiotu
                List<TaskInventory> taskInventories = taskInventoryDao.getTaskInventoriesByInventory(selectedItem);

                // Usuń powiązania przedmiotu z zadaniami
                for (TaskInventory taskInventory : taskInventories) {
                    taskInventoryDao.deleteTaskInventory(taskInventory);
                }

                // Usuń sam przedmiot
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
     * Otwiera widok profilu.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void profileButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Profile/profile.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok użytkowników.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void usersButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Users/users.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok raportów.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void raportsButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Raports/raports.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok projektów.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void projectsButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Projects/projects.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok zadań.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void tasksButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Tasks/tasks.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok zespołów.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void teamButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/teams.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję edycji wybranego przedmiotu z magazynu.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Błąd podczas ładowania pliku FXML.
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

    /**
     * Obsługuje akcję generowania raportu PDF.
     *
     * @param event Zdarzenie akcji.
     * @throws SQLException      Błąd podczas operacji na bazie danych.
     * @throws DocumentException Błąd podczas generowania dokumentu PDF.
     * @throws IOException       Błąd podczas zapisu pliku PDF.
     */
    public void generateReport(ActionEvent event) throws SQLException, DocumentException, IOException {
        PdfReportGenerator pdf = new PdfReportGenerator();
        pdf.generateReportPDF(UserPreferences.getName(), UserPreferences.getSurname());
    }
}