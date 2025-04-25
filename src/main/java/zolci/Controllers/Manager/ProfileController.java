package zolci.Controllers.Manager;

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
import javafx.stage.Stage;
import zolci.DAO.UserDao;
import zolci.UserPreferences;
import zolci.utils.PasswordManager;
import zolci.models.Users;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Kontroler dla profilu użytkownika w aplikacji menedżera.
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
     * Inicjalizuje widok kontrolera.
     *
     * @param url lokalizacja URL
     * @param resourceBundle zasoby
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

    /**
     * Obsługuje zdarzenie kliknięcia na logo, przenosząc użytkownika do głównego menu.
     *
     * @param event zdarzenie kliknięcia myszy
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie wylogowania użytkownika, przenosząc go do ekranu logowania.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie przejścia do formularza dodawania materiałów.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku inwentarza.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku profilu użytkownika.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku użytkowników.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
     */
    @FXML
    public void usersButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/Users/users.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie przejścia do widoku raportów.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku projektów.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku zadań.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie przejścia do widoku zespołów.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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
     * Obsługuje zdarzenie edycji profilu użytkownika.
     *
     * @param event zdarzenie akcji
     */
    public void editButtonOnAction(ActionEvent event) {

    }

    /**
     * Obsługuje zdarzenie anulowania edycji i powrotu do widoku użytkowników.
     *
     * @param event zdarzenie akcji
     * @throws IOException wyjątek wejścia/wyjścia
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