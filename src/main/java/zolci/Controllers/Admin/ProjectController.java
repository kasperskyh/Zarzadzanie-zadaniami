package zolci.Controllers.Admin;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.ProjectCostReportGenerator;
import zolci.DAO.*;
import zolci.UserPreferences;
import zolci.models.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Kontroler dla widoku projektów administratora.
 * Odpowiada za obsługę interakcji użytkownika w widoku projektów.
 */
public class ProjectController implements Initializable {
    @FXML
    private ImageView logoImage;

    @FXML
    private TableView<Projects> projectTableView;

    @FXML
    private TableColumn<Projects, Integer> idColumn;

    @FXML
    private TableColumn<Projects, String> nameColumn;

    @FXML
    private TableColumn<Projects, LocalDate> startDateColumn;

    @FXML
    private TableColumn<Projects, LocalDate> endDateColumn;

    @FXML
    private TableColumn<Projects, String> valuationColumn;

    @FXML
    private TableColumn<Projects, String> statusColumn;

    ProjectDao pd = new ProjectDao();
    UserDao userDao = new UserDao();
    TaskDao taskDao = new TaskDao();
    UserTasksDao userTasksDao = new UserTasksDao();
    TaskInventoryDao taskInventoryDao = new TaskInventoryDao();

    /**
     * Inicjalizuje kontroler po załadowaniu jego elementów FXML.
     *
     * @param url              lokalizacja używana do rozwiązywania względnych ścieżek do pliku FXML.
     * @param resourceBundle   zasoby używane do lokalizacji tego kontrolera.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);

        List<Projects> projectsList = pd.getAllProjects();

        ObservableList<Projects> observableList = FXCollections.observableArrayList(projectsList);

        projectTableView.setItems(observableList);

        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProjectName()));
        startDateColumn.setCellValueFactory(data -> {
            LocalDate startDate = data.getValue().getStartDate();
            if (startDate != null) {
                return new ReadOnlyObjectWrapper<>(startDate);
            } else {
                return new ReadOnlyObjectWrapper<>(null);
            }
        });

        endDateColumn.setCellValueFactory(data -> {
            LocalDate endDate = data.getValue().getEndDate();
            if (endDate != null) {
                return new ReadOnlyObjectWrapper<>(endDate);
            } else {
                return new ReadOnlyObjectWrapper<>(null);
            }
        });

        valuationColumn.setCellValueFactory(data -> {
            double valuation = data.getValue().getValuation();
            return new ReadOnlyObjectWrapper<>(valuation).asString();
        });
        statusColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStatusId().getStatusName()));
    }

    /**
     * Obsługuje kliknięcie obrazu logo i przechodzi do widoku menu administratora.
     *
     * @param event zdarzenie kliknięcia myszą.
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
     * Obsługuje kliknięcie przycisku wylogowania i przechodzi do widoku logowania.
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
     * Obsługuje kliknięcie przycisku dodawania materiału i przechodzi do widoku dodawania materiału.
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
     * Obsługuje kliknięcie przycisku inwentaryzacji i przechodzi do widoku inwentaryzacji.
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
     * Obsługuje kliknięcie przycisku dodawania projektu i otwiera okno dodawania projektu.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
     */
    public void addProject(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Projects/add_project.fxml"));
        Parent root = loader.load();
        AddProjectController c = loader.getController();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(root));
        popupStage.showAndWait();
    }

    /**
     * Obsługuje kliknięcie przycisku edytowania projektu i otwiera okno edytowania projektu.
     * Jeśli żaden projekt nie jest wybrany, wyświetla ostrzeżenie.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
     */
    @FXML
    public void editProjectButtonOnAction(ActionEvent event) throws IOException {
        Projects selectedProject = projectTableView.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Projects/edit_project.fxml"));
            Parent root = loader.load();

            EditProjectController editProjectController = loader.getController();
            editProjectController.setProject(selectedProject);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

            // Po zamknięciu okna odśwież dane w tabeli
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak wyboru");
            alert.setHeaderText(null);
            alert.setContentText("Proszę zaznaczyć rekord do edycji.");
            alert.showAndWait();
        }
    }

    /**
     * Obsługuje kliknięcie przycisku usuwania projektu i usuwa wybrany projekt oraz związane z nim zadania.
     * Jeśli żaden projekt nie jest wybrany, wyświetla ostrzeżenie.
     *
     * @param event zdarzenie akcji.
     */
    @FXML
    public void deleteProjectOnAction(ActionEvent event) {
        Projects selectedProject = projectTableView.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potwierdzenie usuwania");
            alert.setHeaderText(null);
            alert.setContentText("Czy na pewno chcesz usunąć wybrany projekt? Wszystkie zadania przypisane do tego projektu zostaną również usunięte.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Usuwanie zadań przypisanych do projektu
                List<Tasks> tasksToRemove = taskDao.getTasksByProject(selectedProject);
                for (Tasks task : tasksToRemove) {
                    // Usuwanie relacji przypisania zadania do materiału
                    List<TaskInventory> taskInventoriesToRemove = taskInventoryDao.getTaskInventoriesByTask(task);
                    for (TaskInventory taskInventory : taskInventoriesToRemove) {
                        taskInventoryDao.deleteTaskInventory(taskInventory);
                    }
                    // Usuwanie przypisania zadania do użytkownika
                    List<UserTasks> userTasksToRemove = userTasksDao.getUserTasksByTask(task);
                    for (UserTasks userTask : userTasksToRemove) {
                        userTasksDao.deleteUserTask(userTask);
                    }
                    taskDao.deleteTask(task);
                }

                // Usuwanie projektu
                pd.deleteProject(selectedProject);

                // Odświeżenie widoku
                refreshProjectTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak wyboru");
            alert.setHeaderText(null);
            alert.setContentText("Proszę zaznaczyć rekord do usunięcia.");
            alert.showAndWait();
        }
    }

    /**
     * Odświeża dane w tabeli projektów.
     */
    private void refreshProjectTableView() {
        List<Projects> projectsList = pd.getAllProjects();
        ObservableList<Projects> observableList = FXCollections.observableArrayList(projectsList);
        projectTableView.setItems(observableList);
    }

    /**
     * Generuje raport kosztów projektu w formacie PDF dla wybranego projektu.
     *
     * @param event zdarzenie akcji.
     * @throws SQLException         w przypadku błędu bazy danych.
     * @throws DocumentException    w przypadku błędu podczas generowania dokumentu PDF.
     * @throws IOException          w przypadku błędu IO.
     */
    public void generateReport(ActionEvent event) throws SQLException, DocumentException, IOException {
        ProjectCostReportGenerator projectCostReportGenerator = new ProjectCostReportGenerator();
        Projects selectedProject = projectTableView.getSelectionModel().getSelectedItem();
        int id = selectedProject.getId();
        projectCostReportGenerator.generateProjectCostReportPDF(id, UserPreferences.getName(), UserPreferences.getSurname());
    }
}