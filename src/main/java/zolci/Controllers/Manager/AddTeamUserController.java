package zolci.Controllers.Manager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import zolci.DAO.TeamDao;
import zolci.DAO.UserDao;
import zolci.UserPreferences;
import zolci.models.Teams;
import zolci.models.Users;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler obsługujący przypisywanie użytkowników do zespołów przez menedżera.
 */
public class AddTeamUserController implements Initializable {
    @FXML
    private Label teamNameTextField;
    @FXML
    private ComboBox<String> teamUsersComboBox;
    private UserDao userDao = new UserDao();
    private TeamDao teamDao = new TeamDao();

    /**
     * Inicjalizuje widok i wypełnia pole wyboru danymi użytkowników, którzy nie są przypisani do żadnego zespołu.
     *
     * @param url lokalizacja URL
     * @param resourceBundle zasoby
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Users> teamUsersList = userDao.getUnassignedUsers();
        for (Users user : teamUsersList) {
            teamUsersComboBox.getItems().add(user.getEmail());
        }
    }

    /**
     * Zamyka bieżące okno.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     */
    @FXML
    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) teamNameTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * Dodaje użytkownika do zespołu.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     */
    @FXML
    public void addTeam(ActionEvent event) {
        String selectedUserEmail = teamUsersComboBox.getValue();
        if (selectedUserEmail != null) {
            Users user = userDao.getUserByEmail(selectedUserEmail);

            int managerId = UserPreferences.getId();
            Teams team = teamDao.getTeamByManager(managerId);

            if (team != null && user != null) {
                user.setTeam(team);
                userDao.updateUser(user);

                Stage stage = (Stage) teamNameTextField.getScene().getWindow();
                stage.close();
            } else {
                System.out.println("Team or user not found");
            }
        } else {
            System.out.println("No user selected");
        }
    }
}
