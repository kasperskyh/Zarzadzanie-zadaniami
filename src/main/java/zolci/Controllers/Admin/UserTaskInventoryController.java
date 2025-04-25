package zolci.Controllers.Admin;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import zolci.DAO.TaskInventoryDao;
import zolci.DAO.UserTasksDao;
import zolci.models.TaskInventory;
import zolci.models.Tasks;
import zolci.models.UserTasks;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler obsługujący widok zarządzania przypisaniami pracowników do zadań oraz
 * przypisaniami inwentarza do zadań.
 */
public class UserTaskInventoryController implements Initializable {

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

    @FXML
    private TableView<UserTasks> userTaskTableView;
    @FXML
    private TableColumn<UserTasks, Integer> idColumn1;
    @FXML
    private TableColumn<UserTasks, String> emailColumn;
    @FXML
    private TableColumn<UserTasks, String> teamColumn;
    private Tasks selectedTask;
    private TaskInventoryDao taskInventoryDao = new TaskInventoryDao();
    private UserTasksDao userTasksDao = new UserTasksDao();

    /**
     * Inicjalizacja kontrolera, wczytuje dane i ustawia wartości początkowe tabel.
     *
     * @param url            URL zasobu, nieużywane w tej implementacji.
     * @param resourceBundle ResourceBundle, nieużywane w tej implementacji.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<UserTasks> userList = userTasksDao.getUserTasksByTask(selectedTask);
        ObservableList<UserTasks> observableList = FXCollections.observableArrayList(userList);
        userTaskTableView.setItems(observableList);

        idColumn1.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getEmail()));
        teamColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getTeam().getTeamName()));

        List<TaskInventory> inventoryList = taskInventoryDao.getTaskInventoriesByTask(selectedTask);
        if (inventoryList == null) {
            inventoryList = FXCollections.observableArrayList();
        }
        ObservableList<TaskInventory> observableList2 = FXCollections.observableArrayList(inventoryList);
        taskInventoryTableView.setItems(observableList2);

        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInventory().getName()));
        quantityColumn.setCellValueFactory(data -> {
            int quantity = (int) data.getValue().getQuantity();
            return new SimpleIntegerProperty(quantity).asObject();
        });
        priceColumn.setCellValueFactory(data -> {
            double value = data.getValue().getValue();
            return new SimpleDoubleProperty(value).asObject();
        });

    }

    /**
     * Ustawia wybrane zadanie i aktualizuje widok tabel.
     *
     * @param selectedTask Wybrane zadanie do wyświetlenia.
     */
    public void setSelectedTask(Tasks selectedTask) {
        this.selectedTask = selectedTask;
        updateView();
    }

    /**
     * Aktualizuje widok tabel na podstawie wybranego zadania.
     */
    private void updateView() {
        // Sprawdź, czy wybrane zadanie nie jest null
        if (selectedTask != null) {

            List<TaskInventory> taskInventoryList = taskInventoryDao.getTaskInventoriesByTask(selectedTask);
            List<UserTasks> userTasksList = userTasksDao.getUserTasksByTask(selectedTask);


            taskInventoryTableView.setItems(FXCollections.observableArrayList(taskInventoryList));
            userTaskTableView.setItems(FXCollections.observableArrayList(userTasksList));
        }
    }

    /**
     * Obsługuje akcję usunięcia przypisania użytkownika do zadania.
     *
     * @param event Zdarzenie akcji usunięcia.
     */
    @FXML
    public void deleteUserTask(ActionEvent event) {
        UserTasks selectedUserTask = userTaskTableView.getSelectionModel().getSelectedItem();

        if (selectedUserTask == null) {
            showAlert("Nie wybrano", "Nie wybrano", "Proszę zaznacz pracownika w tabeli.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Jesteś pewny, że chcesz usunąć przypisanie?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            userTasksDao.deleteUserTask(selectedUserTask);
            updateView();
        }
    }

    /**
     * Wyświetla alert z określonym tytułem, nagłówkiem i treścią.
     *
     * @param title   Tytuł alertu.
     * @param header  Nagłówek alertu.
     * @param content Treść alertu.
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
