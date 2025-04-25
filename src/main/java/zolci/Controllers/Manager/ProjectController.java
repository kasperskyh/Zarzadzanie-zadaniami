package zolci.Controllers.Manager;

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
 * Kontroler obsługujący zarządzanie projektami dla menedżera.
 */
public class ProjectController implements Initializable {
    public Button editProjectButtonOnAction;
    public Button addProject;
    public Button deleteProjectOnAction;
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
    private TableColumn<Projects, String> priorityColumn;

    @FXML
    private TableColumn<Projects, String> valuationColumn;

    @FXML
    private TableColumn<Projects, String> statusColumn;

    ProjectDao pd = new ProjectDao();
    UserDao userDao = new UserDao();
    TaskDao taskDao = new TaskDao();
    StatusDao statusDao = new StatusDao();
    UserTasksDao userTasksDao = new UserTasksDao();
    TaskInventoryDao taskInventoryDao = new TaskInventoryDao();

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

        List<Projects> projectsList = pd.getCurrentProjectForManager(UserPreferences.getId());

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
     * Odświeża widok tabeli projektów.
     */
    private void refreshProjectTableView() {
        List<Projects> projectsList = pd.getCurrentProjectForManager(UserPreferences.getId());
        ObservableList<Projects> observableList = FXCollections.observableArrayList(projectsList);
        projectTableView.setItems(observableList);
    }

    /**
     * Obsługuje zdarzenie rozpoczęcia wybranego projektu.
     *
     * @param event zdarzenie akcji
     */
    @FXML
    public void startProject(ActionEvent event) {
        Projects selectedProject = projectTableView.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            selectedProject.setStartDate(LocalDate.now());
            Status status = statusDao.getStatusByName("W trakcie");
            selectedProject.setStatusId(status);
            pd.updateProject(selectedProject);
            refreshProjectTableView();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak wybranego projektu");
            alert.setHeaderText(null);
            alert.setContentText("Proszę wybrać projekt do rozpoczęcia.");
            alert.showAndWait();
        }
    }


    /**
     * Obsługuje zdarzenie zakończenia wybranego projektu.
     *
     * @param event zdarzenie akcji
     */
    @FXML
    public void endProject(ActionEvent event) {
        Projects selectedProject = projectTableView.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            // Display confirmation alert
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potwierdzenie zakończenia projektu");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Czy na pewno chcesz zakończyć ten projekt?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Proceed with ending the project
                List<Tasks> tasksList = taskDao.getTasksByProject(selectedProject);
                boolean allTasksCompleted = tasksList.stream().allMatch(task -> task.getStatusId().getStatusName().equals("Zakończony"));
                if (allTasksCompleted) {
                    selectedProject.setEndDate(LocalDate.now());
                    Status status = statusDao.getStatusByName("Zakończony");
                    selectedProject.setStatusId(status);
                    pd.updateProject(selectedProject);
                    refreshProjectTableView();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Niezakończone zadania");
                    alert.setHeaderText(null);
                    alert.setContentText("Wszystkie zadania muszą być zakończone, aby zakończyć projekt.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak wybranego projektu");
            alert.setHeaderText(null);
            alert.setContentText("Proszę wybrać projekt do zakończenia.");
            alert.showAndWait();
        }
    }

    /**
     * Generuje raport kosztów dla wybranego projektu.
     *
     * @param event zdarzenie akcji
     * @throws SQLException       wyjątek SQL
     * @throws DocumentException  wyjątek generowania dokumentu
     * @throws IOException        wyjątek wejścia/wyjścia
     */
    public void generateRaport(ActionEvent event) throws SQLException, DocumentException, IOException {
        ProjectCostReportGenerator projectCostReportGenerator = new ProjectCostReportGenerator();
        Projects selectedProject = projectTableView.getSelectionModel().getSelectedItem();
        int id = selectedProject.getId();
        projectCostReportGenerator.generateProjectCostReportPDF(id, UserPreferences.getName(), UserPreferences.getSurname());
    }
}