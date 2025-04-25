package zolci.Controllers.User;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zolci.DAO.TaskInventoryDao;
import zolci.UserPreferences;
import zolci.models.TaskInventory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler obsługujący widok inwentarza zadań użytkownika.
 */
public class UserTasksInventoryController implements Initializable {
    @FXML
    private ImageView logoImage;
    @FXML
    private Text welcomeText;
    @FXML
    private TableView<TaskInventory> taskInventoryTableView;
    @FXML
    private TableColumn<TaskInventory, Integer> idColumn;
    @FXML
    private TableColumn<TaskInventory, String> nameColumn;
    @FXML
    private TableColumn<TaskInventory, Integer> quantityColumn;
    @FXML
    private TableColumn<TaskInventory, Double> priceColumn;

    private final TaskInventoryDao taskInventoryDao;

    private int currentTaskId; // Pole do przechowywania aktualnego taskId

    /**
     * Konstruktor klasy UserTasksInventoryController.
     * Inicjalizuje instancję TaskInventoryDao.
     */
    public UserTasksInventoryController() {
        this.taskInventoryDao = new TaskInventoryDao();
    }


    /**
     * Inicjalizuje kontroler. Ustawia logo oraz wiadomość powitalną.
     *
     * @param url Lokalizacja używana do rozwiązywania względnych ścieżek dla obiektu głównego lub null.
     * @param resourceBundle Zasoby lokalizowane lub null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);
    }

    /**
     * Ustawia materiały dla konkretnego zadania.
     *
     * @param taskId Identyfikator zadania.
     */
    public void setTaskMaterials(int taskId) {
        this.currentTaskId = taskId;
        List<TaskInventory> materialsList = taskInventoryDao.getMaterialsForTask(taskId);
        ObservableList<TaskInventory> observableList = FXCollections.observableArrayList(materialsList);

        taskInventoryTableView.setItems(observableList);
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInventory().getName()));
        quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty((int) data.getValue().getQuantity()).asObject());
        priceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getValue()));

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
     * Obsługuje zdarzenie przejścia do widoku profilu użytkownika. Przełącza scenę na widok profilu.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    @FXML
    public void profileButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/User_profil.fxml"));
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
    @FXML
    public void tasksButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/User_tasks.fxml"));
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

    /**
     * Obsługuje zdarzenie dodania materiałów do zadania. Otwiera nowe okno do dodawania materiałów i odświeża widok po zamknięciu okna.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    public void addMaterialsForTask(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/add_material_for_task.fxml"));
        Parent root = loader.load();
        UserAddMaterialForTaskController addMaterialController = loader.getController();
        addMaterialController.setTaskId(currentTaskId); // Przekaz taskId do kontrolera
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(root));
        popupStage.setOnHidden(e -> {
            setTaskMaterials(currentTaskId);
        });
        popupStage.showAndWait();
    }

}
