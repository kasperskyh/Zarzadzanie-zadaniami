package zolci.Controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zolci.DAO.*;
import zolci.models.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Kontroler odpowiedzialny za edycję projektów.
 */
public class EditProjectController {

    @FXML
    private TextField nameTextField;
    @FXML
    private ComboBox<String> prioComboBox;
    @FXML
    private TextField valuationTextField;
    @FXML
    private ComboBox<String> teamsComboBox;
    @FXML
    private ComboBox<String> statusComboBox;

    private Projects project;
    private ProjectDao projectDao = new ProjectDao();
    private StatusDao statusDao = new StatusDao();
    private TeamDao teamsDao = new TeamDao();

    private UserDao userDao = new UserDao();
    private TaskDao taskDao = new TaskDao();
    private UserTasksDao userTaskDao = new UserTasksDao();

    /**
     * Ustawia projekt do edycji i wypełnia pola tekstowe danymi z projektu.
     *
     * @param project Projekt do edycji.
     */
    public void setProject(Projects project) {
        this.project = project;

        nameTextField.setText(project.getProjectName());
        valuationTextField.setText(String.valueOf(project.getValuation()));
        teamsComboBox.setValue(project.getTeamId().getTeamName());
        statusComboBox.setValue(project.getStatusId().getStatusName());

        Status completedStatus = statusDao.getStatusByName("Zakończony");
        List<Teams> teamList = teamsDao.getTeamForProject(completedStatus);
        for (Teams team : teamList) {
            teamsComboBox.getItems().add(team.getTeamName());
        }

        ObservableList<String> statusOptions = FXCollections.observableArrayList(statusDao.getAllStatuses());
        statusComboBox.setItems(statusOptions);
    }

    /**
     * Zamyka okno edycji projektu.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * Zapisuje zmiany w projekcie po edycji i zamyka okno edycji.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void saveProject(ActionEvent event) {
        project.setName(nameTextField.getText());
        project.setValuation(Double.parseDouble(valuationTextField.getText()));
        Teams newTeam = teamsDao.getTeamByName(teamsComboBox.getValue());
        Teams currentTeam = project.getTeamId();
        project.setTeamId(newTeam);
        project.setStatusId(statusDao.getStatusByName(statusComboBox.getValue()));

        // Sprawdź, czy status zmieniony na "w trakcie", ustaw datę rozpoczęcia na dzisiejszą
        if (statusComboBox.getValue().equals("W trakcie")) {
            project.setStartDate(LocalDate.now());
        } else if (statusComboBox.getValue().equals("Oczekujący")) {
            // Jeśli status ustawiony na "Oczekujący", ustaw wartość null w polu startDate
            project.setStartDate(null);
        }

        // Jeśli status zmieniony na "zakończony", ustaw datę zakończenia na dzisiejszą
        if (statusComboBox.getValue().equals("Zakończony")) {
            project.setEndDate(LocalDate.now());
        } else if (statusComboBox.getValue().equals("Oczekujący")) {
            // Jeśli status ustawiony na "Oczekujący", ustaw wartość null w polu endDate
            project.setEndDate(null);
        }

        // Sprawdź, czy drużyna została zmieniona
        if (newTeam != null && !newTeam.equals(currentTeam)) {
            // Usuń powiązania zadań dla użytkowników przypisanych do drużyny
            List<Users> users = userDao.getUsersByTeamId(currentTeam);
            List<Tasks> tasks = taskDao.getTasksByProject(project);
            for (Users user : users) {
                for (Tasks task : tasks) {
                    userTaskDao.removeUserTask(user, task);
                }
            }
        }

        projectDao.updateProject(project);

        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }


}
