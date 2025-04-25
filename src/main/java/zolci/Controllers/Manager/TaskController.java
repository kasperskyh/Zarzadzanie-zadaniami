package zolci.Controllers.Manager;

import javafx.beans.property.SimpleObjectProperty;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import zolci.Controllers.Admin.EditTaskController;
import zolci.Controllers.Admin.UserTaskInventoryController;
import zolci.DAO.TaskDao;
import zolci.DAO.TaskInventoryDao;
import zolci.DAO.UserTasksDao;
import zolci.UserPreferences;
import zolci.models.TaskInventory;
import zolci.models.Tasks;
import zolci.models.UserTasks;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Kontroler obsługujący widok zadań.
 */
public class TaskController implements Initializable {
    @FXML
    private ImageView logoImage;

    @FXML
    private TableView<Tasks>taskTableView;

    @FXML
    private TableColumn<Tasks, Integer> idColumn;

    @FXML
    private TableColumn<Tasks, String> nameColumn;

    @FXML
    private TableColumn<Tasks, String> descriptionColumn;
    @FXML
    private TableColumn<Tasks, String> statusColumn;
    @FXML
    private TableColumn<Tasks, String> assigneeColumn;
    @FXML
    private TableColumn<Tasks, String> priorityColumn;


    private final TaskDao taskDao;
    private UserTasksDao userTasksDao = new UserTasksDao();
    private TaskInventoryDao taskInventoryDao = new TaskInventoryDao();

    /**
     * Konstruktor kontrolera zadań, inicjuje Dao dla zadań.
     */
    public TaskController() {
        this.taskDao = new TaskDao();
    }

    /**
     * Inicjalizuje widok po załadowaniu.
     *
     * @param url            adres URL inicjalizujący
     * @param resourceBundle zasób ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);

        List<Tasks> tasksList = taskDao.getTasksByManagerId(UserPreferences.getId());

        // Konwertuj listę na obserwowalną listę
        ObservableList<Tasks> observableList = FXCollections.observableArrayList(tasksList);

        // Ustaw dane w TableView
        taskTableView.setItems(observableList);

        // Przypisz dane do odpowiednich kolumn
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getName()));
        descriptionColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDescription()));
        priorityColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPriority()));
        statusColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStatusId().getStatusName()));

        int managerId = UserPreferences.getId(); // Assuming there's a method to get the managerId
        List<String> unassignedTaskNames = taskDao.getUnassignedTaskNamesByManagerId(managerId);
        if (unassignedTaskNames != null && !unassignedTaskNames.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nieprzypisane zadania");
            alert.setHeaderText("Istnieją zadania które nie są przyisane:");
            StringBuilder content = new StringBuilder();
            for (String taskName : unassignedTaskNames) {
                content.append(taskName).append("\n");
            }
            alert.setContentText(content.toString());
            alert.showAndWait();
        }
    }

    /**
     * Obsługuje zdarzenie kliknięcia na logo, przenosząc użytkownika do głównego menu.
     *
     * @param event zdarzenie kliknięcia myszy
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie wylogowania użytkownika, przenosząc go do ekranu logowania.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie przejścia do formularza dodawania materiałów.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
     */
    @FXML
    public void addItemButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/add_material_form.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie przejścia do widoku inwentarza.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku profilu użytkownika.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku raportów.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku projektów.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku zadań.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku zespołów.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie kliknięcia przycisku dodawania zadania.
     *
     * @param event zdarzenie akcji
     */
    public void addTaskButtonOnAction(ActionEvent event) {
        try {
            // Ładowanie pliku FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Manager/Tasks/add_tasks.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Dodaj zadanie");

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); // Pokaż okno i czekaj, aż zostanie zamknięte
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku edycji zadania.
     *
     * @param event zdarzenie akcji
     */
    @FXML
    public void editTaskOnAction(ActionEvent event) {
        Tasks selectedTask = taskTableView.getSelectionModel().getSelectedItem();

        try {
            // Ładowanie pliku FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Admin/Tasks/edit_task.fxml"));
            Parent root = fxmlLoader.load();

            // Pobierz kontroler okna edycji
            EditTaskController editTaskController = fxmlLoader.getController();
            // Ustaw zadanie do edycji
            editTaskController.setTask(selectedTask);

            // Utwórz scenę i okno
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edytuj zadanie");

            // Ustaw okno jako modalne (blokujące)
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); // Pokaż okno i czekaj, aż zostanie zamknięte

            // Po zamknięciu okna odśwież TableView
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku usuwania zadania.
     *
     * @param event zdarzenie akcji
     */
    @FXML
    public void deleteTaskButtonOnAction(ActionEvent event) {
        Tasks selectedTask = taskTableView.getSelectionModel().getSelectedItem();

        if (selectedTask != null) {
            // Wyświetl potwierdzenie usunięcia zadania
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie usunięcia");
            alert.setHeaderText(null);
            alert.setContentText("Czy na pewno chcesz usunąć wybrane zadanie wraz z relacjami do użytkowników i inwentarza?");

            // Pobierz odpowiedź użytkownika
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Usuń relacje zadań użytkowników
                List<UserTasks> userTasks = userTasksDao.getUserTasksByTask(selectedTask);
                for (UserTasks userTask : userTasks) {
                    userTasksDao.deleteUserTask(userTask);
                }

                // Usuń relacje zadań inwentarza
                List<TaskInventory> taskInventories = taskInventoryDao.getTaskInventoriesByTask(selectedTask);
                for (TaskInventory taskInventory : taskInventories) {
                    taskInventoryDao.deleteTaskInventory(taskInventory);
                }

                // Usuń same zadanie
                taskDao.deleteTask(selectedTask);

                // Odśwież TableView po usunięciu zadania
                taskTableView.getItems().remove(selectedTask);
            }
        } else {
            // Wyświetl komunikat o braku zaznaczonego zadania do usunięcia
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak zaznaczenia");
            alert.setHeaderText(null);
            alert.setContentText("Proszę zaznaczyć zadanie do usunięcia.");
            alert.showAndWait();
        }
    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku wyświetlenia szczegółów zadania.
     *
     * @param event zdarzenie akcji
     */
    @FXML
    public void viewTaskButtonOnAction(ActionEvent event) {
        Tasks selectedTask = taskTableView.getSelectionModel().getSelectedItem();

        try {
            // Ładowanie pliku FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Admin/Tasks/user_task_inventory.fxml"));
            Parent root = fxmlLoader.load();

            // Pobierz kontroler widoku
            UserTaskInventoryController controller = fxmlLoader.getController();

            // Przekazanie zaznaczonego zadania do kontrolera
            controller.setSelectedTask(selectedTask);

            // Utwórz scenę i okno
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Szczegóły zadania");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje zdarzenie kliknięcia przycisku przypisania zadania.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
     */
    public void assignTaskButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Manager/task_for_user.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
