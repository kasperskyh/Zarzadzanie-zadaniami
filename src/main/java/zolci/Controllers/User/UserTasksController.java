package zolci.Controllers.User;

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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import zolci.DAO.StatusDao;
import zolci.DAO.TaskDao;
import zolci.UserPreferences;
import zolci.models.Tasks;
import zolci.models.Status;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler obsługujący widok zadań użytkownika.
 */
public class UserTasksController implements Initializable {
    @FXML
    private ImageView logoImage;
    @FXML
    private Text welcomeText;
    @FXML
    private TableView<Tasks> tasksTableView;
    @FXML
    private TableColumn<Tasks, Integer> idColumn;
    @FXML
    private TableColumn<Tasks, String> nameColumn;
    @FXML
    private TableColumn<Tasks, String> descriptionColumn;
    @FXML
    private TableColumn<Tasks, String> priorityColumn;
    @FXML
    private TableColumn<Tasks, String> statusColumn;
    @FXML
    private ComboBox<Integer> taskComboBox;
    @FXML
    private ComboBox<String> statusComboBox;

    private final TaskDao taskDao;
    private final StatusDao statusDao;

    /**
     * Konstruktor klasy UserTasksController.
     * Inicjalizuje instancje TaskDao i StatusDao.
     */
    public UserTasksController() {
        this.taskDao = new TaskDao();
        this.statusDao = new StatusDao();
    }

    /**
     * Inicjalizuje kontroler. Ustawia logo, wczytuje zadania i statusy oraz konfiguruje tabele.
     *
     * @param url Lokalizacja używana do rozwiązywania względnych ścieżek dla obiektu głównego lub null.
     * @param resourceBundle Zasoby lokalizowane lub null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);

        int loggedInUserId = UserPreferences.getId();

        List<Tasks> taskList = taskDao.getTasksForUser(UserPreferences.getId());
        List<Status> statusList = statusDao.getAllStatus();

        ObservableList<Tasks> observableList = FXCollections.observableArrayList(taskList);
        tasksTableView.setItems(observableList);

        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        priorityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPriority()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusId().getStatusName()));

        if (taskList != null) {
            for (Tasks task : taskList) {
                taskComboBox.getItems().add(task.getId());
            }
        } else {
            // Obsługa błędu - nie można pobrać ról z bazy danych
            System.out.println("Błąd podczas pobierania ról z bazy danych.");
        }
        if (statusList != null) {
            for (Status status : statusList) {
                statusComboBox.getItems().add(status.getStatusName());
            }
        } else {
            // Obsługa błędu - nie można pobrać ról z bazy danych
            System.out.println("Błąd podczas pobierania ról z bazy danych.");
        }
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
     * Obsługuje zdarzenie zapisania zmian statusu zadania. Aktualizuje status zadania w bazie danych.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void saveButtonOnAction(ActionEvent event) {
        Integer selectedTaskId = taskComboBox.getValue();
        String selectedStatus = statusComboBox.getValue();

        if (selectedTaskId != null && selectedStatus != null) {
            try {
                // Pobierz zadanie po ID
                Tasks task = taskDao.getTaskById(selectedTaskId);
                if (task != null) {
                    // Pobierz priorytet wybranego zadania
                    int taskPriority = Integer.parseInt(task.getPriority());

                    // Pobierz listę wszystkich zadań użytkownika
                    List<Tasks> taskList = taskDao.getTasksForUser(UserPreferences.getId());

                    // Sprawdź każde zadanie w liście
                    for (Tasks otherTask : taskList) {
                        // Jeśli zadanie ma inny ID niż wybrane zadanie i nie jest "zakończony"
                        if (otherTask.getId() != selectedTaskId &&
                                !otherTask.getStatusId().getStatusName().equals("Zakończony")) {

                            // Pobierz priorytet innego zadania
                            int otherTaskPriority = Integer.parseInt(otherTask.getPriority());

                            // Sprawdź, czy priorytet wybranego zadania jest niższy niż inne zadania
                            if (taskPriority < otherTaskPriority) {
                                // Wyświetl alert informujący użytkownika
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Uwaga");
                                alert.setHeaderText("Wybrane zadanie ma niższy priorytet niż inne zadania");
                                alert.setContentText("Nie można zmienić statusu na 'Zakończony' lub 'W trakcie' dla zadania o niższym priorytecie.");

                                alert.showAndWait();
                                return; // Przerwij pętlę, nie kontynuuj zapisu
                            }
                        }
                    }

                    // Pobierz status po nazwie
                    Status status = statusDao.getStatusByName(selectedStatus);

                    if (status != null) {
                        task.setStatusId(status);
                        taskDao.updateTask(task);
                        System.out.println("Task status updated successfully.");
                        refreshTableView();
                    } else {
                        System.out.println("Status not found.");
                    }
                } else {
                    System.out.println("Task not found.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please select a task and a status.");
        }
    }




    /**
     * Odświeża tabelę z zadaniami użytkownika.
     */
    private void refreshTableView() {
        List<Tasks> taskList = taskDao.getTasksForUser(UserPreferences.getId());
        ObservableList<Tasks> observableList = FXCollections.observableArrayList(taskList);
        tasksTableView.setItems(observableList);
        tasksTableView.refresh();
    }

    /**
     * Obsługuje zdarzenie dodania materiałów do wybranego zadania. Przełącza scenę na widok inwentarza zadania.
     *
     * @param event Zdarzenie akcji.
     */
    public void addMaterialView(ActionEvent event) {
        Tasks selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/User_task_inventory.fxml"));
                Parent root = loader.load();
                UserTasksInventoryController controller = loader.getController();
                controller.setTaskMaterials(selectedTask.getId());

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please select a task.");
        }
    }


}
