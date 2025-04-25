package zolci.Controllers.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zolci.DAO.TeamDao;
import zolci.DAO.UserDao;
import zolci.models.Teams;
import zolci.models.Users;

import java.util.List;

/**
 * Kontroler do edycji drużyny.
 */
public class EditTeamController {

    @FXML
    private TextField teamNameTextField;
    @FXML
    private ComboBox<String> managersComboBox;

    private Teams selectedTeam;
    private TeamDao teamDao;
    private UserDao userDao;

    /**
     * Konstruktor kontrolera.
     * Inicjalizuje obiekty DAO dla drużyn i użytkowników.
     */
    public EditTeamController() {
        this.teamDao = new TeamDao();
        this.userDao = new UserDao();
    }

    /**
     * Ustawia wybraną drużynę do edycji i wypełnia pola formularza jej danymi.
     *
     * @param selectedTeam Wybrana drużyna do edycji.
     */
    public void setSelectedTeam(Teams selectedTeam) {
        this.selectedTeam = selectedTeam;
        populateFields();
    }

    /**
     * Wypełnia pola formularza danymi wybranej drużyny.
     */
    private void populateFields() {
        teamNameTextField.setText(selectedTeam.getTeamName());

        // Wczytanie listy użytkowników do ComboBox'a
        List<Users> managersList = userDao.getUnassignedManagers();
        for (Users user : managersList) {
            managersComboBox.getItems().add(user.getEmail());
        }

        // Ustawienie wybranego kierownika
        managersComboBox.getSelectionModel().select(selectedTeam.getUser().getEmail());

    }

    /**
     * Zamyka okno edycji drużyny.
     */
    @FXML
    public void closeWindow() {
        Stage stage = (Stage) teamNameTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * Zapisuje dokonane zmiany w drużynie i zamyka okno edycji.
     */
    @FXML
    public void editTeam() {
        String newTeamName = teamNameTextField.getText();
        String selectedManager = managersComboBox.getValue();
        Users newManager= userDao.getUserByEmail(selectedManager);

        // Aktualizacja drużyny
        selectedTeam.setName(newTeamName);
        selectedTeam.setUser(newManager);

        teamDao.updateTeam(selectedTeam);

        closeWindow();
    }
}
