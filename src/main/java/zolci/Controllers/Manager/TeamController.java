package zolci.Controllers.Manager;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zolci.DAO.TeamDao;
import zolci.DAO.UserDao;
import zolci.DAO.UserTasksDao;
import zolci.UserPreferences;
import zolci.models.Users;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Kontroler obsługujący widok drużyny.
 */
public class TeamController {
    @FXML
    private ImageView logoImage;

    @FXML
    private TableView<Users> teamTableView;
    @FXML
    private TableColumn<Users, Integer> idColumn;
    @FXML
    private TableColumn<Users, String> nameColumn;
    @FXML
    private TableColumn<Users, String> surnameColumn;
    @FXML
    private TableColumn<Users, String> emailColumn;

    private final TeamDao teamDao;
    private final UserDao userDao;

    private final UserTasksDao userTasksDao;

    /**
     * Konstruktor kontrolera drużyny.
     */
    public TeamController() {
        this.teamDao = new TeamDao();
        this.userDao = new UserDao();
        this.userTasksDao = new UserTasksDao();
    }

    /**
     * Inicjalizuje widok po załadowaniu.
     */
    @FXML
    public void initialize() {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);

        List<Users> userList = userDao.getUsersByManagerId(UserPreferences.getId());
        if (userList == null) {
            userList = FXCollections.observableArrayList();
        }
        ObservableList<Users> observableList = FXCollections.observableArrayList(userList);
        teamTableView.setItems(observableList);

        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        surnameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSurname()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
    }

    /**
     * Obsługuje kliknięcie na logo aplikacji.
     *
     * @param event zdarzenie kliknięcia
     * @throws IOException wyjątek wejścia-wyjścia
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
     * Obsługuje wylogowanie użytkownika.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia-wyjścia
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje dodanie elementu.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia-wyjścia
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
     * Obsługuje przejście do widoku inwentarza.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia-wyjścia
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
     * Obsługuje przejście do widoku profilu użytkownika.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia-wyjścia
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
     * Obsługuje przejście do widoku raportów.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia-wyjścia
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
     * Obsługuje przejście do widoku projektów.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia-wyjścia
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
     * Obsługuje przejście do widoku zadań.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia-wyjścia
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
     * Obsługuje przejście do widoku drużyn.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia-wyjścia
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
     * Obsługuje dodanie drużyny.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia-wyjścia
     */
    public void addTeam(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Teams/add_team.fxml"));
        Parent root = loader.load();
        AddTeamUserController addTeamUserController = loader.getController();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(root));
        popupStage.showAndWait();
    }

    /**
     * Obsługuje usunięcie użytkownika z drużyny.
     *
     * @param event zdarzenie akcji
     */
    @FXML
    public void deleteButtonOnAction(ActionEvent event) {
        Users selectedUser = teamTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potwierdzenie");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Czy na pewno chcesz usunąć tego użytkownika z drużyny?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Usunięcie relacji użytkownika z zadaniami
                userTasksDao.deleteUserTasksByUser(selectedUser);

                // Ustawienie teamId na NULL
                selectedUser.setTeam(null);
                userDao.updateUser(selectedUser);

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Proszę wybrać użytkownika do usunięcia z drużyny.");
            alert.showAndWait();
        }
    }
}
