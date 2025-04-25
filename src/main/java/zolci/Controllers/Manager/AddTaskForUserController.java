package zolci.Controllers.Manager;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import zolci.DAO.TaskDao;
import zolci.DAO.UserDao;
import zolci.DAO.UserTasksDao;
import zolci.UserPreferences;
import zolci.models.Tasks;
import zolci.models.Users;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler obsługujący przypisywanie zadań użytkownikom przez menedżera.
 */
public class AddTaskForUserController implements Initializable {

    @FXML
    ComboBox<String> userComboBox;

    @FXML
    ComboBox<String> tasksComboBox;

    UserDao userDao = new UserDao();
    TaskDao taskDao = new TaskDao();
    UserTasksDao userTasksDao = new UserTasksDao();

    /**
     * Inicjalizuje widok i wypełnia pola wyboru danymi użytkowników i zadań.
     *
     * @param location  lokalizacja URL
     * @param resources zasoby
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<String> userList = userDao.getUserNamesByManagerId(UserPreferences.getId());

        if (userList != null) {
            userComboBox.setItems(FXCollections.observableArrayList(userList));
        }

        List<String> tasksList = taskDao.getTaskNamesByManagerId(UserPreferences.getId());

        if (tasksList != null) {
            tasksComboBox.setItems(FXCollections.observableArrayList(tasksList));
        }

    }

    /**
     * Dodaje zadanie do użytkownika.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     */
    public void addTaskForUser(ActionEvent event) {
        String selectedUserName = userComboBox.getValue();
        String selectedTaskName = tasksComboBox.getValue();

        if (selectedUserName != null && selectedTaskName != null) {
            Users selectedUser = userDao.getUserByEmail(selectedUserName);
            Tasks selectedTask = taskDao.getTaskByName(selectedTaskName);

            if (selectedUser != null && selectedTask != null) {
                // Sprawdzenie, czy przypisanie już istnieje
                if (!userTasksDao.isUserTaskExists(selectedUser, selectedTask)) {
                    userTasksDao.addUserTask(selectedUser, selectedTask);
                    // Powiadomienie użytkownika o pomyślnym przypisaniu zadania
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Dodano zadanie");
                    alert.setHeaderText(null);
                    alert.setContentText("Zadanie zostało pomyślnie przypisane użytkownikowi.");
                    alert.showAndWait();
                } else {
                    // Powiadomienie użytkownika, że przypisanie już istnieje
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Przypisanie już istnieje");
                    alert.setHeaderText(null);
                    alert.setContentText("To zadanie jest już przypisane do wybranego użytkownika.");
                    alert.showAndWait();
                }
            } else {
                // Obsługa przypadku, gdy wybrany użytkownik lub zadanie nie zostało znalezione
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(null);
                alert.setContentText("Nie można przypisać zadania do użytkownika. Wybrany użytkownik lub zadanie nie istnieje.");
                alert.showAndWait();
            }
        } else {
            // Obsługa przypadku, gdy nie wybrano użytkownika lub zadania
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wybór użytkownika i zadania");
            alert.setHeaderText(null);
            alert.setContentText("Proszę wybrać użytkownika i zadanie.");
            alert.showAndWait();
        }
    }

    public void cancelAction(ActionEvent event) {
    }
}
