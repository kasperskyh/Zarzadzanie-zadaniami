package zolci.Controllers.Admin;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import zolci.DAO.TeamDao;
import zolci.DAO.UserDao;
import zolci.DAO.UserTasksDao;
import zolci.models.Users;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Kontroler obsługujący widok zarządzania użytkownikami i kierownikami.
 */
public class UsersController implements Initializable {

    @FXML
    private ImageView logoImage;

    @FXML
    private TableView<Users> userTableView;
    @FXML
    private TableView<Users> managerTableView;

    @FXML
    private TableColumn<Users, Integer> idColumn;

    @FXML
    private TableColumn<Users, String> firstNameColumn;

    @FXML
    private TableColumn<Users, String> lastNameColumn;

    @FXML
    private TableColumn<Users, String> emailColumn;

    @FXML
    private TableColumn<Users, String> teamColumn;

    @FXML
    private TableColumn<Users, Integer> managerIdColumn;

    @FXML
    private TableColumn<Users, String> managerFirstNameColumn;

    @FXML
    private TableColumn<Users, String> managerLastNameColumn;

    @FXML
    private TableColumn<Users, String> managerEmailColumn;

    @FXML
    private TableColumn<Users, String> managerTeamColumn;

    @FXML
    private Button deleteButton;
    private final UserDao userDao;

    /**
     * Konstruktor klasy UsersController.
     * Inicjalizuje pole userDao obiektem UserDao.
     */
    public UsersController() {
        this.userDao = new UserDao();
    }

    /**
     * Inicjalizuje widok użytkowników i kierowników.
     *
     * @param url            Adres URL, który nie jest używany w tej implementacji.
     * @param resourceBundle Zasób ResourceBundle, który nie jest używany w tej implementacji.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);

        List<Users> userList = userDao.getUsersWithUserRole();
        List<Users> managerList = userDao.getManagers();

        // Konwertuj listę na obserwowalną listę
        ObservableList<Users> observableList = FXCollections.observableArrayList(userList);
        ObservableList<Users> managerObservableList = FXCollections.observableArrayList(managerList);

        // Ustaw dane w TableView
        userTableView.setItems(observableList);
        managerTableView.setItems(managerObservableList);

        // Przypisz dane do odpowiednich kolumn
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSurname()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        managerIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        managerFirstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        managerLastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSurname()));
        managerEmailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
    }

    /**
     * Pobiera wybranego użytkownika z TableView.
     *
     * @return Wybrany użytkownik lub null, jeśli żaden nie jest zaznaczony.
     */
    private Users getSelectedUserFromTable() {
        Users selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            selectedUser = managerTableView.getSelectionModel().getSelectedItem();
        }
        return selectedUser;
    }

    /**
     * Obsługuje zdarzenie kliknięcia w logo aplikacji.
     *
     * @param event Zdarzenie kliknięcia myszą.
     * @throws IOException Wyjątek występujący w przypadku problemów z załadowaniem pliku FXML.
     */
    @FXML
    private void logoImageClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/menu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie wylogowania użytkownika.
     *
     * @param event Zdarzenie naciśnięcia przycisku "Wyloguj".
     * @throws IOException Wyjątek występujący w przypadku problemów z załadowaniem pliku FXML.
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję wylogowania użytkownika.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Błąd podczas ładowania pliku FXML.
     */
    @FXML
    public void addItemButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/add_material_form.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok inwentarza.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void inventoryButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Inventory/inventory.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok profilu.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void profileButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Profile/profile.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok użytkowników.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void usersButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Users/users.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok raportów.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void raportsButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Raports/raports.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok projektów.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void projectsButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Projects/projects.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok zadań.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void tasksButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Tasks/tasks.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Otwiera widok zespołów.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void teamButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/teams.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie usunięcia użytkownika.
     *
     * @param event Zdarzenie naciśnięcia przycisku "Usuń użytkownika".
     */
    @FXML
    public void DeleteOnAction(ActionEvent event) {
        // Pobranie zaznaczonego użytkownika
        Users selectedUser = userTableView.getSelectionModel().getSelectedItem();
        Users selectedManagerUser = managerTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null || selectedManagerUser != null) {
            // Tworzenie alertu potwierdzającego usunięcie użytkownika
            Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potwierdzenie");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Czy na pewno chcesz usunąć tego użytkownika?");

            // Pobranie odpowiedzi użytkownika
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                UserTasksDao userTasksDao = new UserTasksDao();
                TeamDao teamDao = new TeamDao();

                if (selectedManagerUser != null) {
                    // Sprawdzenie, czy manager jest przypisany do jakiejś drużyny
                    boolean isAssigned = teamDao.isManagerAssignedToTeam(selectedManagerUser);
                    if (isAssigned) {
                        // Wyświetlenie alertu z informacją o konieczności zmiany kierownika drużyny
                        Alert managerAssignedAlert = new Alert(AlertType.WARNING);
                        managerAssignedAlert.setTitle("Błąd");
                        managerAssignedAlert.setHeaderText(null);
                        managerAssignedAlert.setContentText("Ten kierownik jest przypisany do drużyny. Przed usunięciem zmień kierownika w drużynie.");
                        managerAssignedAlert.showAndWait();
                        return;
                    }
                }

                if (selectedUser != null) {
                    // Usunięcie przypisania zadań dla wybranego użytkownika
                    userTasksDao.deleteUserTasksByUser(selectedUser);
                    userDao.deleteUser(selectedUser);
                }

                if (selectedManagerUser != null) {
                    // Usunięcie przypisania zadań dla wybranego kierownika
                    userTasksDao.deleteUserTasksByUser(selectedManagerUser);
                    userDao.deleteUser(selectedManagerUser);
                }

                // Odświeżenie danych w tabeli
                refreshTable();
            }
        }
    }

    /**
     * Metoda odświeża dane w tabeli po modyfikacji użytkowników.
     */
    private void refreshTable() {
        List<Users> userList = userDao.getUsersWithUserRole();
        userTableView.getItems().setAll(userList);

        List<Users> managerList = userDao.getManagers();
        managerTableView.getItems().setAll(managerList);
    }

    /**
     * Obsługuje zdarzenie edycji użytkownika.
     *
     * @param event Zdarzenie naciśnięcia przycisku "Edytuj użytkownika".
     * @throws IOException Wyjątek występujący w przypadku problemów z załadowaniem pliku FXML.
     */
    @FXML
    public void editButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Users/edit_user.fxml"));
        Parent root = loader.load();
        EditUserController editUserController = loader.getController();

        // Pobranie wybranego użytkownika
        Users selectedUser = getSelectedUserFromTable();

        if (selectedUser != null) {
            editUserController.setSelectedUser(selectedUser);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Proszę wybrać użytkownika do edycji.");
            alert.showAndWait();
        }
    }

    /**
     * Obsługuje zdarzenie dodania nowego użytkownika.
     *
     * @param event Zdarzenie naciśnięcia przycisku "Dodaj użytkownika".
     * @throws IOException Wyjątek występujący w przypadku problemów z załadowaniem pliku FXML.
     */
    public void addUserButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Users/add_user.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
