package zolci.Controllers.Admin;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zolci.DAO.TeamDao;
import zolci.DAO.UserDao;
import zolci.models.Teams;
import zolci.models.Users;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler odpowiedzialny za dodawanie nowych zespołów.
 */
public class AddTeamController implements Initializable {
    @FXML
    private TextField teamNameTextField;
    @FXML
    private ComboBox<String> managersComboBox;
    private UserDao userDao = new UserDao();
    private TeamDao teamDao = new TeamDao();

    /**
     * Inicjalizuje kontroler, wypełniając listę rozwijaną dostępnych menedżerów.
     *
     * @param url              Lokalizacja używana do rozwiązywania względnych ścieżek dla obiektu głównego lub null.
     * @param resourceBundle   Zasoby lokalizowane lub null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Users> managersList = userDao.getUnassignedManagers();
        for (Users user : managersList) {
            managersComboBox.getItems().add(user.getEmail());
        }
    }

    /**
     * Zamyka okno dodawania zespołu.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) teamNameTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * Dodaje nowy zespół na podstawie danych wprowadzonych przez użytkownika.
     *
     * @param event Zdarzenie akcji.
     */
    public void addTeam(ActionEvent event) {
        String teamName = teamNameTextField.getText();
        String selectedManager = managersComboBox.getValue();
        Users manager = userDao.getUserByEmail(selectedManager);

        Teams newTeam = new Teams(teamName, manager);


        teamDao.addTeam(newTeam);

        Stage stage = (Stage) teamNameTextField.getScene().getWindow();
        stage.close();
    }

}
