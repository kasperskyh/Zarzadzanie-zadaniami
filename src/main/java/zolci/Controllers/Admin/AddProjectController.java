package zolci.Controllers.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zolci.DAO.ProjectDao;
import zolci.DAO.StatusDao;
import zolci.DAO.TeamDao;
import zolci.models.Projects;
import zolci.models.Status;
import zolci.models.Teams;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler odpowiedzialny za dodawanie nowych projektów.
 */
public class AddProjectController implements Initializable {
    private StatusDao statusDao = new StatusDao();
    private TeamDao teamDao = new TeamDao();
    private ProjectDao projectDao = new ProjectDao();
    @FXML
    private TextField nameTextField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private ComboBox<String>teamsComboBox;
    @FXML
    private TextField valuationTextField;

    /**
     * Inicjalizuje kontroler, wypełniając listy rozwijane statusów i zespołów.
     *
     * @param url Lokalizacja używana do rozwiązywania względnych ścieżek dla obiektu głównego lub null.
     * @param resourceBundle Zasoby lokalizowane lub null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Status> statusList = statusDao.getAllStatus();
        for (Status status : statusList) {
            statusComboBox.getItems().add(status.getStatusName());
        }
        Status completedStatus = statusDao.getStatusByName("Zakończony");
        List<Teams> teamList = teamDao.getTeamForProject(completedStatus);
        for (Teams team : teamList) {
            teamsComboBox.getItems().add(team.getTeamName());
        }
    }

    /**
     * Zamyka okno dodawania projektu.
     *
     * @param event Zdarzenie akcji.
     */
    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * Dodaje nowy projekt na podstawie danych wprowadzonych przez użytkownika.
     *
     * @param event Zdarzenie akcji.
     */
    public void addProject(ActionEvent event) {
        String projectName = nameTextField.getText();
        String selectedStatus = statusComboBox.getValue();
        String selectedTeamName = teamsComboBox.getValue();
        String valuationString = valuationTextField.getText();
        double valuation = Double.parseDouble(valuationString);

        if (projectName.isEmpty() || selectedStatus == null || selectedTeamName == null) {
            // Alert w przypadku pustych pól
            System.out.println("Proszę wypełnić wszystkie pola");
            return;
        }

        Status status = statusDao.getStatusByName(selectedStatus);
        Teams team = teamDao.getTeamByName(selectedTeamName);

        Projects project = new Projects();
        project.setName(projectName);
        project.setStatusId(status);
        project.setValuation(valuation);
        project.setTeamId(team);

        projectDao.addProject(project);

        closeWindow(event);
    }

}
