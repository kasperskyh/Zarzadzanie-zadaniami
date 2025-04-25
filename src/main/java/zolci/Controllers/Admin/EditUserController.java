package zolci.Controllers.Admin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zolci.DAO.TeamDao;
import zolci.DAO.UserDao;
import zolci.DAO.UserTasksDao;
import zolci.models.Role;
import zolci.models.Teams;
import zolci.models.Users;
import zolci.utils.PasswordManager;

import java.util.List;

/**
 * Kontroler do edycji użytkownika.
 */
public class EditUserController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private ComboBox<String> teamComboBox;

    private Users selectedUser;
    private UserDao userDao;
    private TeamDao teamDao;

    /**
     * Konstruktor kontrolera.
     * Inicjalizuje obiekty DAO dla użytkowników i drużyn.
     */
    public EditUserController() {
        this.userDao = new UserDao();
        this.teamDao = new TeamDao();
    }

    /**
     * Ustawia wybranego użytkownika do edycji i wypełnia pola formularza jego danymi.
     *
     * @param selectedUser Wybrany użytkownik do edycji.
     */
    public void setSelectedUser(Users selectedUser) {
        this.selectedUser = selectedUser;
        populateFields();
    }

    /**
     * Wypełnia pola formularza danymi wybranego użytkownika.
     */
    private void populateFields() {
        nameTextField.setText(selectedUser.getName());
        surnameTextField.setText(selectedUser.getSurname());
        emailTextField.setText(selectedUser.getEmail());
        passwordTextField.setText("");

        // Ustawienie roli
        roleComboBox.getSelectionModel().select(selectedUser.getRole().getRoleName());

        // Pokazanie lub ukrycie ComboBoxa zespołu w zależności od roli
        if ("User".equals(selectedUser.getRole().getRoleName())) {
            teamComboBox.setVisible(true);
            populateTeamComboBox();
            teamComboBox.getSelectionModel().select(selectedUser.getTeam().getTeamName());
        } else {
            teamComboBox.setVisible(false);
        }
    }

    /**
     * Wypełnia ComboBox zespołu danymi wszystkich dostępnych zespołów.
     */
    private void populateTeamComboBox() {
        List<Teams> teams = teamDao.getAllTeams();
        teamComboBox.setItems(FXCollections.observableArrayList(getTeamNames(teams)));
    }

    /**
     * Pobiera nazwy zespołów z listy zespołów.
     *
     * @param teams Lista zespołów.
     * @return Lista nazw zespołów.
     */
    private List<String> getTeamNames(List<Teams> teams) {
        return teams.stream()
                .map(Teams::getTeamName)
                .toList();
    }

    /**
     * Metoda obsługująca zapis dokonanych zmian w użytkowniku.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void editUser(ActionEvent event) {
        String newName = nameTextField.getText();
        String newSurname = surnameTextField.getText();
        String newEmail = emailTextField.getText();
        String newPassword = passwordTextField.getText();
        String newRoleName = roleComboBox.getSelectionModel().getSelectedItem();
        String newTeamName = teamComboBox.getSelectionModel().getSelectedItem();

        // Jeśli rola się zmienia, wykonujemy odpowiednie operacje
        if (!newRoleName.equals(selectedUser.getRole().getRoleName())) {
            if ("User".equals(newRoleName)) {
                // Zmiana z Managera na Usera
                if (teamDao.isManagerAssignedToTeam(selectedUser)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Błąd");
                    alert.setHeaderText(null);
                    alert.setContentText("Ten użytkownik jest przypisany do drużyny jako manager. Nie można zmienić rolę na 'User'.");
                    alert.showAndWait();
                    return;
                }
                // Usunięcie przypisanych zadań
                UserTasksDao userTasksDao = new UserTasksDao();
                userTasksDao.deleteUserTasksByUser(selectedUser);
                selectedUser.setTeam(null);
            } else {
                // Zmiana z Usera na Managera
                UserTasksDao userTasksDao = new UserTasksDao();
                userTasksDao.deleteUserTasksByUser(selectedUser);
                selectedUser.setTeam(null); // TeamId ustawiane w zależności od teamComboBox
            }
        }

        Role newRole = userDao.getRoleByName(newRoleName);
        Teams newTeam = teamDao.getTeamByName(newTeamName);
        String hashedpassword = PasswordManager.hashPassword(newPassword);
        selectedUser.setName(newName);
        selectedUser.setSurname(newSurname);
        selectedUser.setEmail(newEmail);
        selectedUser.setPassword(hashedpassword);
        selectedUser.setRole(newRole);
        selectedUser.setTeam(newTeam);

        userDao.updateUser(selectedUser);

        closeWindow();
    }

    /**
     * Zamyka okno edycji użytkownika.
     */
    @FXML
    public void closeWindow() {
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * Metoda obsługująca zmianę wybranej roli w ComboBoxie roli.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void roleComboBoxChanged(ActionEvent event) {
        String selectedRole = roleComboBox.getSelectionModel().getSelectedItem();
        if ("Manager".equals(selectedRole)) {
            teamComboBox.setVisible(false);
        } else {
            teamComboBox.setVisible(true);
            populateTeamComboBox();
        }
    }
}
