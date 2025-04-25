package zolci.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import zolci.DAO.UserDao;
import zolci.UserPreferences;
import zolci.models.Users;
import zolci.utils.PasswordManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Kontroler obsługujący widok logowania.
 */
public class LoginController implements Initializable {
    public Label errorLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField visiblePasswordField;
    @FXML
    private ImageView logoImage;
    @FXML
    private ImageView koparkaImage;
    @FXML
    private ImageView logoIconImage;
    @FXML
    private ImageView passwordIconImage;

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    private Users authenticatedUser;
    private boolean isPasswordVisible = false;

    /**
     * Inicjalizuje kontroler. Ustawia obrazy i powiązania między polami tekstowymi.
     *
     * @param url Lokalizacja używana do rozwiązywania względnych ścieżek dla obiektu głównego lub null.
     * @param resourceBundle Zasoby lokalizowane lub null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);

        File koparkaFile = new File("Images/koparka.png");
        Image koparkaIm = new Image(koparkaFile.toURI().toString());
        koparkaImage.setImage(koparkaIm);

        File logoiconFile = new File("Images/email.png");
        Image logoiconIm = new Image(logoiconFile.toURI().toString());
        logoIconImage.setImage(logoiconIm);

        File passwordIconFile = new File("Images/password.png");
        Image passwordIconIm = new Image(passwordIconFile.toURI().toString());
        passwordIconImage.setImage(passwordIconIm);

        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    /**
     * Obsługuje zdarzenie anulowania. Zamyka bieżące okno.
     */
    public void cancelButtonOnAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Sprawdza, czy podane dane logowania są poprawne.
     *
     * @param email    Adres e-mail użytkownika.
     * @param password Hasło użytkownika.
     * @return true, jeśli dane logowania są poprawne; false w przeciwnym razie.
     */
    @FXML
    private boolean isValidUser(String email, String password) {
        UserDao userDao = new UserDao();
        try {
            Users user = userDao.getUserByEmail(email);
            if (user != null && PasswordManager.verifyPassword(password, user.getPassword())) {
                UserPreferences.saveUser(user.getId(), user.getEmail(), user.getRole().getRoleName(), user.getName(), user.getSurname(), user.getTeamName());
                this.authenticatedUser = user;
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Exception during user validation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ładuje odpowiednią scenę na podstawie roli użytkownika.
     *
     * @param event Zdarzenie akcji.
     * @param user  Zalogowany użytkownik.
     * @throws IOException Jeśli wystąpi błąd podczas ładowania pliku FXML.
     */
    private void loadSceneForRole(ActionEvent event, Users user) throws IOException {
        String roleName = user.getRole().getRoleName().toLowerCase();
        String fxmlFile;

        switch (roleName) {
            case "admin":
                fxmlFile = "/Admin/menu.fxml";
                break;
            case "user":
                fxmlFile = "/User/User_profil.fxml";
                break;
            case "manager":
                fxmlFile = "/Manager/menu.fxml";
                break;
            default:
                throw new IllegalArgumentException("No view available for role: " + roleName);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje zdarzenie logowania. Weryfikuje dane logowania i ładuje odpowiednią scenę.
     *
     * @param event Zdarzenie akcji.
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordField.getText();

        // Sprawdzenie czy pola nie są puste
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Proszę wprowadzić adres email i hasło.");
            errorLabel.setVisible(true);
            return;
        }

        // Walidacja formatu adresu email (prosty przykład, można rozbudować z użyciem regex)
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            errorLabel.setText("Nieprawidłowy format adresu email.");
            errorLabel.setVisible(true);
            return;
        }

        // Walidacja długości hasła (np. co najmniej 6 znaków)
        if (password.length() < 6) {
            errorLabel.setText("Hasło musi mieć co najmniej 6 znaków.");
            errorLabel.setVisible(true);
            return;
        }

        // Sprawdzenie poprawności danych logowania
        if (isValidUser(email, password)) {
            try {
                loadSceneForRole(event, authenticatedUser);
            } catch (IOException e) {
                logger.error("Error loading scene for role: " + e.getMessage());
            }
        } else {
            // Obsłuż niepoprawne dane logowania
            errorLabel.setText("Nieprawidłowe dane logowania.");
            errorLabel.setVisible(true);
        }
    }

    /**
     * Przełącza widoczność hasła.
     *
     * @param event Zdarzenie kliknięcia myszą.
     */
    @FXML
    private void togglePasswordVisibility(MouseEvent event) {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            visiblePasswordField.setVisible(true);
            passwordField.setVisible(false);
        } else {
            visiblePasswordField.setVisible(false);
            passwordField.setVisible(true);
        }
    }
}
