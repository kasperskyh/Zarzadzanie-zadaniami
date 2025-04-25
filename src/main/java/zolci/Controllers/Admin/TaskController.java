package zolci.Controllers.Admin;

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
import zolci.DAO.TaskDao;
import zolci.DAO.TaskInventoryDao;
import zolci.DAO.UserTasksDao;
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
 * Kontroler obsługujący widok zadań w panelu administratora.
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
     * Konstruktor kontrolera zadań.
     */
    public TaskController() {
        this.taskDao = new TaskDao();
    }

    /**
     * Metoda wywoływana po załadowaniu widoku.
     *
     * @param url             lokalizacja używana do rozwiązywania względnych ścieżek dla obiektu root, lub null jeśli nie jest dostępna.
     * @param resourceBundle  zasoby używane do lokalizacji obiektu root, lub null jeśli nie są dostępne.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);

        List<Tasks> tasksList = taskDao.getAllTasks();

        // Konwertuj listę na obserwowalną listę
        ObservableList<Tasks> observableList = FXCollections.observableArrayList(tasksList);

        // Ustaw dane w TableView
        taskTableView.setItems(observableList);

        // Przypisz dane do odpowiednich kolumn
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getName()));
        descriptionColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDescription()));
        assigneeColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProjectId().getProjectName()));
        priorityColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPriority()));
        statusColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStatusId().getStatusName()));
    }

    /**
     * Obsługuje kliknięcie na obraz logo i przechodzi do menu głównego.
     *
     * @param event zdarzenie kliknięcia myszy.
     * @throws IOException w przypadku błędu ładowania zasobu.
     */
    @FXML
    private void logoImageClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/menu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje kliknięcie przycisku wylogowania i przechodzi do ekranu logowania.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje kliknięcie przycisku dodawania materiału i otwiera okno dodawania materiału.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
     */
    @FXML
    public void addItemButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/add_material_form.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje kliknięcie przycisku inwentarza i przechodzi do widoku inwentarza.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku profilu i przechodzi do widoku profilu.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku użytkowników i przechodzi do widoku użytkowników.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku raportów i przechodzi do widoku raportów.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku projektów i przechodzi do widoku projektów.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku zadań i przechodzi do widoku zadań.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku zespołów i przechodzi do widoku zespołów.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku dodawania zadania i otwiera okno dodawania zadania.
     *
     * @param event zdarzenie akcji.
     */
    public void addTaskButtonOnAction(ActionEvent event) {
        try {
            // Ładowanie pliku FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Admin/Tasks/add_tasks.fxml"));
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
     * Obsługuje kliknięcie przycisku edytowania zadania i otwiera okno edytowania zadania.
     *
     * @param event zdarzenie akcji.
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
     * Obsługuje kliknięcie przycisku usunięcia zadania.
     *
     * @param event zdarzenie akcji.
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
     * Obsługuje kliknięcie przycisku wyświetlania szczegółów zadania.
     *
     * @param event zdarzenie akcji.
     */
    @FXML
    public void viewTaskButtonOnAction(ActionEvent event) {
        Tasks selectedTask = taskTableView.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showAlert("Nie wybrano", "Nie wybrano zadania", "Proszę wybrać zadanie z tabeli.");
            return;
        }

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

            // Ustaw modalność okna
            stage.initModality(Modality.APPLICATION_MODAL);

            // Pokaż okno i zablokuj interakcję z oknem głównym, dopóki to okno nie zostanie zamknięte
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wyświetla alert z podanym tytułem, nagłówkiem i treścią.
     *
     * @param title   tytuł alertu.
     * @param header  nagłówek alertu.
     * @param content treść alertu.
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
