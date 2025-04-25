package zolci.Controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import zolci.DAO.UserDao;
import zolci.UserPreferences;
import zolci.models.Users;
import zolci.utils.SwitchScenery;
import zolci.utils.PasswordManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Kontroler do obsługi menu użytkownika.
 */
public class UserMenuController implements Initializable, SwitchScenery {

    @FXML
    private ImageView logoImage;
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

     UserDao userDao = new UserDao();

    /**
     * Inicjalizuje kontroler. Ustawia logo, dane użytkownika oraz ukrywa panel zmiany hasła.
     *
     * @param url Lokalizacja używana do rozwiązywania względnych ścieżek dla obiektu głównego lub null.
     * @param resourceBundle Zasoby lokalizowane lub null.
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
     * Obsługuje zdarzenie wylogowania użytkownika. Przełącza scenę na widok logowania.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    @FXML
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        switchScene(event, "/login.fxml");
    }

    /**
     * Obsługuje zdarzenie przejścia do widoku zadań użytkownika. Przełącza scenę na widok zadań.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    public void tasksButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/User_tasks.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie przejścia do widoku profilu użytkownika. Przełącza scenę na widok profilu.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    public void profileButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/User_profil.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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
     * Obsługuje zdarzenie przejścia do widoku inwentarza użytkownika. Przełącza scenę na widok inwentarza.
     *
     * @param event Zdarzenie akcji.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    public void inventoryButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/User_inventory.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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
}
