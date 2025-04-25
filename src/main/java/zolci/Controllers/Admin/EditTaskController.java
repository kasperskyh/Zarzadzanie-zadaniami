package zolci.Controllers.Admin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zolci.DAO.ProjectDao;
import zolci.DAO.TaskDao;
import zolci.models.Projects;
import zolci.models.Tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * Kontroler odpowiedzialny za edycję zadań.
 */

public class EditTaskController {
    @FXML
    private TextField nameTextField;
    @FXML
    private ComboBox<String> projectComboBox;
    @FXML
    private ComboBox<Integer> prioComboBox;
    @FXML
    private TextArea descriptionTextArea;

    private ProjectDao projectDao = new ProjectDao();
    private TaskDao taskDao = new TaskDao();

    private Tasks task;

    /**
     * Ustawia zadanie do edycji i wypełnia formularz danymi zadania.
     *
     * @param task Zadanie do edycji.
     */
    public void setTask(Tasks task) {
        this.task = task;
        nameTextField.setText(task.getName());
        projectComboBox.setValue(task.getProjectId().getProjectName());
        prioComboBox.setValue(Integer.parseInt(task.getPriority()));
        descriptionTextArea.setText(task.getDescription());

        // Pobierz listę projektów i ustaw ją w ComboBox
        List<Projects> projectsList = projectDao.getAllProjects();
        List<String> projectNames = new ArrayList<>();
        for (Projects project : projectsList) {
            projectNames.add(project.getName());
        }
        projectComboBox.setItems(FXCollections.observableArrayList(projectNames));

        // Ustaw priorytety w ComboBox
        List<Integer> priorities = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            priorities.add(i);
        }
        prioComboBox.setItems(FXCollections.observableArrayList(priorities));
    }

    /**
     * Zapisuje zmiany w zadaniu po edycji i zamyka okno edycji.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void saveTask(ActionEvent event) {
        // Pobierz nowe dane z formularza
        String newName = nameTextField.getText();
        String newDescription = descriptionTextArea.getText();
        String newPriority = String.valueOf(prioComboBox.getValue());
        String projectName = projectComboBox.getValue();

        // Sprawdź, czy wszystkie pola zostały wypełnione
        if (newName.isEmpty() || newDescription.isEmpty() || newPriority == null || projectName == null) {
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

        // Ustaw nowe dane w obiekcie task
        task.setName(newName);
        task.setDescription(newDescription);
        task.setPriority(newPriority);
        task.setProjectId(project);

        // Tutaj wywołaj metodę DAO do zapisania zmian w bazie danych
        taskDao.updateTask(task);

        // Zamknij okno
        closeWindow(event);
    }


    /**
     * Zamyka okno edycji zadania.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
