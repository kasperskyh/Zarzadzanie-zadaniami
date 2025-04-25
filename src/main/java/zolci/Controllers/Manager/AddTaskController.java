package zolci.Controllers.Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zolci.DAO.ProjectDao;
import zolci.DAO.StatusDao;
import zolci.DAO.TaskDao;
import zolci.UserPreferences;
import zolci.models.Projects;
import zolci.models.Tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * Kontroler obsługujący dodawanie nowych zadań przez menedżera.
 */
public class AddTaskController {

    @FXML
    private ComboBox<String> projectComboBox;

    @FXML
    private ComboBox<Integer> prioComboBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    private ProjectDao projectDao = new ProjectDao();
    private TaskDao taskDao = new TaskDao();

    /**
     * Inicjalizuje widok i wypełnia pola wyboru danymi.
     */
    public void initialize() {
        // Wypełnienie ComboBox z projektami
        List<Projects> projectsList = projectDao.getCurrentProjectForManager(UserPreferences.getId());
        List<String> projectNames = new ArrayList<>();
        for (Projects project : projectsList) {
            projectNames.add(project.getName());
        }

        // Set the items in the ComboBox
        ObservableList<String> projectNamesObservableList = FXCollections.observableArrayList(projectNames);
        projectComboBox.setItems(projectNamesObservableList);

        // Set the first item as the default value
        if (!projectNamesObservableList.isEmpty()) {
            projectComboBox.getSelectionModel().select(0);
        }

        // Disable the ComboBox to make it non-selectable
        projectComboBox.setDisable(true);

        // Wypełnienie ComboBox z priorytetami od 1 do 10
        List<Integer> priorities = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            priorities.add(i);
        }
        prioComboBox.setItems(FXCollections.observableArrayList(priorities));
    }

    /**
     * Zamknięcie okna dialogowego.
     *
     * @param event Zdarzenie wywołane przez naciśnięcie przycisku.
     */
    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Dodanie nowego zadania do bazy danych.
     *
     * @param event Zdarzenie wywołane przez naciśnięcie przycisku.
     */
    public void addTask(ActionEvent event) {
        String projectName = projectComboBox.getValue();
        Integer priority = prioComboBox.getValue();
        String name = nameTextField.getText();
        String description = descriptionTextArea.getText();

        // Sprawdź, czy wszystkie pola zostały wypełnione
        if (projectName == null || priority == null || name.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak danych");
            alert.setHeaderText(null);
            alert.setContentText("Proszę wypełnić wszystkie pola.");
            alert.showAndWait();
            return;
        }

        // Pobierz projekt na podstawie nazwy
        Projects project = projectDao.getProjectByName(projectName);
        if (project == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Nie można odnaleźć wybranego projektu.");
            alert.showAndWait();
            return;
        }

        // Stwórz nowe zadanie
        Tasks newTask = new Tasks();
        newTask.setName(name);
        newTask.setDescription(description);
        newTask.setPriority(String.valueOf(priority));
        newTask.setProjectId(project);
        StatusDao statusDao = new StatusDao();
        newTask.setStatusId(statusDao.getStatusByName("Oczekujący"));


        // Dodaj zadanie do bazy danych
        taskDao.addTask(newTask);

        // Wyświetl komunikat potwierdzający dodanie zadania
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setHeaderText(null);
        alert.setContentText("Zadanie zostało dodane pomyślnie.");
        alert.showAndWait();

        // Zamknij okno
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}

