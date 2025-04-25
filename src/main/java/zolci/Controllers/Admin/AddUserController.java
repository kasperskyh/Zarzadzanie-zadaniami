package zolci.Controllers.Admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import zolci.DAO.UserDao;
import zolci.models.Role;
import zolci.models.Teams;
import zolci.models.Users;
import zolci.utils.PasswordManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler dla ekranu dodawania użytkownika.
 * Obsługuje dodawanie nowego użytkownika, walidację danych oraz przełączanie widoczności pól.
 */
public class AddUserController implements Initializable {

    public Label errorLabel;
    @FXML
    private Button cancelButton;

    @FXML
    private ImageView logoImage;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private ComboBox<String> teamComboBox;

    @FXML
    private Text teamText;

    private final UserDao userDao = new UserDao();

    /**
     * Inicjalizuje kontroler, ustawia obraz logo, wypełnia pola wyboru rolami i drużynami,
     * oraz dodaje nasłuchiwacz zmian do roleComboBox.
     *
     * @param url nieużywany parametr.
     * @param resourceBundle nieużywany parametr.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);
        List<Role> roles = userDao.getAllRoles(); // Pobranie wszystkich dostępnych ról z bazy danych
        if (roles != null) {
            // Pętla po wszystkich roliach i dodanie ich nazw do ComboBox
            for (Role role : roles) {
                roleComboBox.getItems().add(role.getRoleName()); // Załóżmy, że nazwa roli jest przechowywana jako String
            }
        } else {
            // Obsługa błędu - nie można pobrać ról z bazy danych
            System.out.println("Błąd podczas pobierania ról z bazy danych.");
        }
        List<Teams> teams = userDao.getAllTeams(); // Pobranie wszystkich dostępnych ról z bazy danych
        if (teams != null) {
            // Pętla po wszystkich roliach i dodanie ich nazw do ComboBox
            for (Teams teams1 : teams) {
                teamComboBox.getItems().add(teams1.getTeamName()); // Załóżmy, że nazwa roli jest przechowywana jako String
            }
        } else {
            // Obsługa błędu - nie można pobrać ról z bazy danych
            System.out.println("Błąd podczas pobierania ról z bazy danych.");
        }

        teamComboBox.setVisible(false);
        teamText.setVisible(false);


        // Nasłuchiwacz zmian dla roleComboBox
        roleComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if ("user".equals(newValue)) {
                    teamComboBox.setVisible(true);
                    teamText.setVisible(true);
                } else {
                    teamComboBox.setVisible(false);
                    teamText.setVisible(false);
                }
            }
        });

    }

    /**
     * Obsługuje kliknięcie obrazu logo, ładuje ekran menu głównego.
     *
     * @param event Zdarzenie kliknięcia.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
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
     * Obsługuje akcję wylogowania, ładuje ekran logowania.
     *
     * @param event Zdarzenie wywołane przez przycisk.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/add_material_form.fxml"));
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
     * Otwiera widok inwentarza.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
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
     * Otwiera widok profilu.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
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
     * Otwiera widok raportów.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
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
     * Otwiera widok projektów.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException W przypadku błędu ładowania zasobu.
     */
    @FXML
    public void projectsButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Projects/projects.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void tasksButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Tasks/tasks.fxml"));
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
    public void teamButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/teams.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję dodania użytkownika. Sprawdza poprawność danych,
     * dodaje nowego użytkownika do bazy danych i przechodzi do poprzedniego ekranu.
     *
     * @param event Zdarzenie wywołane przez przycisk.
     */
    @FXML
    public void addUserButtonOnAction(ActionEvent event) {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String roleName = roleComboBox.getValue();
        String teamName = teamComboBox.getValue();

        // Walidacja pól
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || roleName == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak danych");
            alert.setHeaderText(null);
            alert.setContentText("Wypełnij wszystkie pola!");
            alert.showAndWait();
            return;
        }

        // Walidacja adresu email
        if (!isValidEmail(email)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Niepoprawny adres email");
            alert.setHeaderText(null);
            alert.setContentText("Podano niepoprawny adres email!");
            alert.showAndWait();
            return;
        }

        // Jeśli rola to "user", sprawdź, czy wybrano drużynę
        if ("user".equals(roleName) && (teamName == null || teamName.isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak drużyny");
            alert.setHeaderText(null);
            alert.setContentText("Wybierz drużynę dla pracownika!");
            alert.showAndWait();
            return;
        }

        // Sprawdzenie, czy użytkownik o danym adresie email już istnieje
        if (userDao.getUserByEmail(email) != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplikat");
            alert.setHeaderText(null);
            alert.setContentText("Użytkownik o podanym adresie email już istnieje!");
            alert.showAndWait();
            return;
        }

        // Pobranie roli i drużyny z bazy danych na podstawie ich nazw
        Role role = userDao.getRoleByName(roleName);
        Teams team = userDao.getTeamByName(teamName);

        // Sprawdzenie, czy rola istnieje
        if (role == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nie znaleziono roli");
            alert.setHeaderText(null);
            alert.setContentText("Nie znaleziono roli o nazwie: " + roleName);
            alert.showAndWait();
            return;
        }

        // Hashowanie hasła
        String hashedPassword = PasswordManager.hashPassword(password);

        // Tworzenie nowego obiektu użytkownika
        Users newUser = new Users(firstName, lastName, email, hashedPassword, role, team);

        // Dodawanie użytkownika do bazy danych
        userDao.addUser(newUser);

        // Wyświetlenie komunikatu o sukcesie
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Sukces");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Dodano nowego użytkownika pomyślnie!");
        successAlert.showAndWait();

        // Powrót do poprzedniego ekranu (users.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Users/users.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda do sprawdzania poprawności adresu email za pomocą wyrażenia regularnego.
     *
     * @param email Adres email do sprawdzenia.
     * @return true, jeśli adres email jest poprawny, w przeciwnym razie false.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }


    public void editButtonOnAction(ActionEvent event) {

    }

    public void initData(Users selectedUser) {
        firstNameTextField.setText(selectedUser.getName());
        lastNameTextField.setText(selectedUser.getSurname());
        emailTextField.setText(selectedUser.getEmail());
        passwordField.setText(selectedUser.getPassword());
        roleComboBox.setValue(selectedUser.getRole().getRoleName());
        teamComboBox.setValue(selectedUser.getTeam().getTeamName());
    }

    /**
     * Obsługuje akcję anulowania, ładuje poprzedni ekran.
     *
     * @param event Zdarzenie wywołane przez przycisk.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    @FXML
    void cancelButtonOnAction(ActionEvent event) throws IOException {
        // Pobierz scenę i okno
        Scene scene = cancelButton.getScene();
        Stage stage = (Stage) scene.getWindow();

        // Wczytaj poprzednią scenę
        Parent root = FXMLLoader.load(getClass().getResource("/Admin/Users/users.fxml"));

        // Ustaw nową scenę na oknie
        stage.setScene(new Scene(root));
    }
}