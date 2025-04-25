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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import zolci.DAO.UserDao;
import zolci.UserPreferences;
import zolci.utils.PasswordManager;
import zolci.models.Role;
import zolci.models.Teams;
import zolci.models.Users;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler obsługujący widok profilu użytkownika.
 */
public class ProfileController implements Initializable {

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
    private Label userNameLabel;
    @FXML
    private Label userEmailLabel;
    @FXML
    private Label userSurnameLabel;
    @FXML
    private Label userTeamLabel;
    @FXML
    private Pane changePasswordPanel;
    @FXML
    private TextField oldPasswordTextField;
    @FXML
    private TextField newPasswordTextField;
    @FXML
    private TextField repeatNewPasswordTextField;

    private final UserDao userDao = new UserDao();

    /**
     * Inicjalizuje klasę kontrolera. Ta metoda jest automatycznie wywoływana
     * po załadowaniu pliku FXML.
     *
     * @param url            Lokalizacja używana do rozwiązywania ścieżek względnych dla obiektu głównego, lub null jeśli lokalizacja nie jest znana.
     * @param resourceBundle Zasoby używane do lokalizacji obiektu głównego, lub null jeśli obiekt główny nie był lokalizowany.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);

        userNameLabel.setText(UserPreferences.getName());
        userEmailLabel.setText(UserPreferences.getEmail());
        userSurnameLabel.setText(UserPreferences.getSurname());
        userTeamLabel.setText(UserPreferences.getTeam());
        changePasswordPanel.setVisible(false);
    }

    /**
     * Obsługuje zdarzenie zapisu nowego hasła użytkownika.
     *
     * @param event Zdarzenie akcji.
     */
    public void saveButtonOnAction(ActionEvent event) {
        int userId = UserPreferences.getId();
        String oldPassword = oldPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String repeatNewPassword = repeatNewPasswordTextField.getText();

        // Pobranie użytkownika z bazy danych
        Users user = userDao.getUserById(userId);
        if (user != null) {
            // Sprawdzenie poprawności starego hasła
            if (PasswordManager.verifyPassword(oldPassword, user.getPassword())) {
                // Sprawdzenie czy nowe hasło i jego powtórzenie są identyczne
                if (newPassword.equals(repeatNewPassword)) {
                    // Haszowanie nowego hasła przed aktualizacją w bazie danych
                    String hashedPassword = PasswordManager.hashPassword(newPassword);

                    // Aktualizacja hasła użytkownika w bazie danych
                    boolean success = userDao.updatePassword(userId, hashedPassword);

                    // Wyświetlenie odpowiedniego alertu w zależności od wyniku aktualizacji
                    Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                    alert.setTitle(success ? "Sukces" : "Błąd");
                    alert.setHeaderText(null);
                    alert.setContentText(success ? "Hasło zostało pomyślnie zaktualizowane." : "Wystąpił błąd podczas aktualizacji hasła.");
                    alert.showAndWait();
                } else {
                    // Alert informujący o niezgodności nowego hasła i jego powtórzenia
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ostrzeżenie");
                    alert.setHeaderText(null);
                    alert.setContentText("Nowe hasło i jego powtórzenie nie są takie same.");
                    alert.showAndWait();
                }
            } else {
                // Alert informujący o niepoprawnym starym haśle
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ostrzeżenie");
                alert.setHeaderText(null);
                alert.setContentText("Stare hasło jest niepoprawne.");
                alert.showAndWait();
            }
        }

        // Wyczyszczenie pól tekstowych
        clearTextFields();
    }

    /**
     * Czyści pola tekstowe w panelu zmiany hasła.
     */
    private void clearTextFields() {
        oldPasswordTextField.clear();
        newPasswordTextField.clear();
        repeatNewPasswordTextField.clear();
    }

    /**
     * Obsługuje zdarzenie wyświetlenia lub ukrycia panelu zmiany hasła.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    public void changePasswordButtonOnAction(ActionEvent event){
        changePasswordPanel.setVisible(!changePasswordPanel.isVisible());
    }

    @FXML
    private void logoImageClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/menu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie akcji przycisku wylogowania.
     *
     * @param event Zdarzenie akcji
     * @throws IOException Jeśli wystąpi błąd podczas ładowania widoku logowania
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie akcji przycisku dodania przedmiotu.
     *
     * @param event Zdarzenie akcji
     * @throws IOException Jeśli wystąpi błąd podczas ładowania widoku dodawania materiału
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
     * Metoda obsługuje akcję przycisku nawigacyjnego "Inventory".
     *
     * @param event Zdarzenie akcji
     * @throws IOException Jeśli wystąpi błąd podczas ładowania widoku magazynu
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
     * Metoda obsługuje akcję przycisku nawigacyjnego "Profile".
     *
     * @param event Zdarzenie akcji
     * @throws IOException Jeśli wystąpi błąd podczas ładowania widoku profilu
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
     * Metoda obsługuje akcję przycisku nawigacyjnego "Users".
     *
     * @param event Zdarzenie akcji
     * @throws IOException Jeśli wystąpi błąd podczas ładowania widoku użytkowników
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
     * Metoda obsługuje akcję przycisku nawigacyjnego "Raports".
     *
     * @param event Zdarzenie akcji
     * @throws IOException Jeśli wystąpi błąd podczas ładowania widoku raportów
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
     * Metoda obsługuje akcję przycisku nawigacyjnego "Projects".
     *
     * @param event Zdarzenie akcji
     * @throws IOException Jeśli wystąpi błąd podczas ładowania widoku projektów
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
     * Metoda obsługuje akcję przycisku nawigacyjnego "Tasks".
     *
     * @param event Zdarzenie akcji
     * @throws IOException Jeśli wystąpi błąd podczas ładowania widoku zadań
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
     * Metoda obsługuje akcję przycisku nawigacyjnego "Team".
     *
     * @param event Zdarzenie akcji
     * @throws IOException Jeśli wystąpi błąd podczas ładowania widoku zespołów
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
     * Obsługuje zdarzenie akcji przycisku dodania użytkownika.
     *
     * @param event Zdarzenie akcji
     */
    @FXML
    public void addUserButtonOnAction(ActionEvent event) {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String roleName = roleComboBox.getValue();
        String teamName = teamComboBox.getValue();

        String hashedPassword = new PasswordManager().hashPassword(password);

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || roleName == null) {
            // Wyświetlenie alertu informującego o konieczności wypełnienia wszystkich pól
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak danych");
            alert.setHeaderText(null);
            alert.setContentText("Wypełnij wszystkie pola!");
            alert.showAndWait();
            return;
        }

        // Jeśli rola to "user", sprawdź, czy teamName jest ustawione
        if ("user".equals(roleName) && (teamName == null || teamName.isEmpty())) {
            // Wyświetlenie alertu informującego o konieczności wyboru drużyny
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Brak danych");
            alert.setHeaderText(null);
            alert.setContentText("Wybierz drużynę dla pracownika");
            alert.showAndWait();
            return;
        }

        // Sprawdzenie, czy użytkownik o danym adresie email już istnieje
        if (userDao.getUserByEmail(email) != null) {
            // Wyświetlenie alertu informującego o istnieniu użytkownika o podanym emailu
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplikat");
            alert.setHeaderText(null);
            alert.setContentText("Użytkownik o podanym adresie email już istnieje!");
            alert.showAndWait();
            return;
        }

        // Pobranie roli z bazy danych na podstawie jej nazwy
        Role role = userDao.getRoleByName(roleName);
        Teams team = userDao.getTeamByName(teamName);



        // Sprawdzenie, czy rola istnieje
        if (role == null) {
            // Obsługa błędu - rola nie istnieje
            System.out.println("Nie znaleziono roli o nazwie: " + roleName);
            return;
        }

        // Tworzenie obiektu użytkownika
        Users newUser = new Users(firstName, lastName, email, hashedPassword, role, team);

        // Dodawanie użytkownika do bazy danych za pomocą UserDao
        userDao.addUser(newUser);

        // Wyświetlenie komunikatu o sukcesie dodania użytkownika
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Sukces");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Dodano nowego użytkownika pomyślnie!");
        successAlert.showAndWait();

        // Wczytanie poprzedniej sceny z pliku FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Users/users.fxml"));
        try {
            Parent root = loader.load();

            // Pobranie aktualnego Stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Ustawienie nowej sceny na aktualnym Stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obsługuje zdarzenie akcji przycisku anulowania.
     *
     * @param event Zdarzenie akcji
     * @throws IOException Jeśli wystąpi błąd podczas ładowania widoku użytkowników
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