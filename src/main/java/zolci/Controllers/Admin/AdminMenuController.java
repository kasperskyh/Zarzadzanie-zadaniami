package zolci.Controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import zolci.DAO.UserTasksDao;
import zolci.UserPreferences;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Kontroler dla menu administratora.
 * Odpowiada za obsługę interakcji użytkownika w menu administratora.
 */
public class AdminMenuController implements Initializable {

    @FXML
    private ImageView logoImage;
    @FXML
    private Text welcomeText;
    private UserTasksDao userTasksDao = new UserTasksDao();

    /**
     * Inicjalizuje kontroler po załadowaniu jego elementów FXML.
     *
     * @param url              lokalizacja używana do rozwiązywania względnych ścieżek do pliku FXML.
     * @param resourceBundle   zasoby używane do lokalizacji tego kontrolera.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);
        welcomeText.setText("Witaj " + UserPreferences.getName());
    }

    /**
     * Obsługuje kliknięcie obrazu logo i przechodzi do widoku menu administratora.
     *
     * @param event zdarzenie kliknięcia myszą.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku wylogowania i przechodzi do widoku logowania.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje kliknięcie przycisku dodawania materiału i przechodzi do widoku dodawania materiału.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku inwentaryzacji i przechodzi do widoku inwentaryzacji.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku profilu i przechodzi do widoku profilu.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
     */
    @FXML
    public void profileButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Profile/profile.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void usersButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Users/users.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje kliknięcie przycisku użytkowników i przechodzi do widoku użytkowników.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku projektów i przechodzi do widoku projektów.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku zadań i przechodzi do widoku zadań.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
     */
    @FXML
    public void teamButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/teams.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}