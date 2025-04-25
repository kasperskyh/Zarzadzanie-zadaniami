package zolci.Controllers.Admin;

import com.itextpdf.text.DocumentException;
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
import org.example.PdfReportGenerator;
import org.example.PdfTeamGenerator;
import zolci.DAO.ProjectDao;
import zolci.DAO.TeamDao;
import zolci.DAO.UserDao;
import zolci.UserPreferences;
import zolci.models.Teams;
import zolci.utils.SwitchScenery;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Kontroler obsługujący widok zarządzania zespołami w panelu administratora.
 */
public class TeamController implements SwitchScenery{

    /**
     * Obraz logo aplikacji.
     */
    public ImageView logoImage;

    /**
     * Obsługuje kliknięcie na logo aplikacji, przechodzi do menu głównego.
     *
     * @param event zdarzenie kliknięcia myszy.
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
     * Obsługuje kliknięcie przycisku wylogowania i przechodzi do ekranu logowania.
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
     * Obsługuje kliknięcie przycisku dodawania materiału i przechodzi do odpowiedniego widoku.
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
     * Obsługuje kliknięcie przycisku inwentarza i przechodzi do odpowiedniego widoku.
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
     * Obsługuje kliknięcie przycisku profilu i przechodzi do odpowiedniego widoku.
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

    /**
     * Obsługuje kliknięcie przycisku użytkowników i przechodzi do odpowiedniego widoku.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku raportów i przechodzi do odpowiedniego widoku.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
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
     * Obsługuje kliknięcie przycisku projektów i przechodzi do odpowiedniego widoku.
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
     * Obsługuje kliknięcie przycisku zadań i przechodzi do odpowiedniego widoku.
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
     * Obsługuje kliknięcie przycisku zespołów i przechodzi do odpowiedniego widoku.
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

    @FXML
    private TableView<Teams> teamTableView;
    @FXML
    private TableColumn<Teams, Integer> idColumn;
    @FXML
    private TableColumn<Teams, String> nameColumn;
    @FXML
    private TableColumn<Teams, String> managerColumn;

    private final TeamDao teamDao;

    /**
     * Konstruktor inicjalizujący obiekt DAO dla zespołów.
     */
    public TeamController() {
        this.teamDao = new TeamDao();
    }

    /**
     * Inicjalizuje widok tabeli zespołów, ładowanie danych i ustawienie wartości w kolumnach.
     */
    @FXML
    public void initialize() {

        File logoFile = new File("Images/logo.png");
        Image logoIm = new Image(logoFile.toURI().toString());
        logoImage.setImage(logoIm);
        
        List<Teams> teamList = teamDao.getAllTeams();
        ObservableList<Teams> observableList = FXCollections.observableArrayList(teamList);
        teamTableView.setItems(observableList);

        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTeamName()));
        managerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getEmail()));
    }

    /**
     * Obsługuje kliknięcie przycisku dodawania zespołu i otwiera odpowiedni widok.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
     */
    public void addTeam(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Teams/add_team.fxml"));
        Parent root = loader.load();
        AddTeamController addTeamController = loader.getController();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(root));
        popupStage.showAndWait();
    }

    /**
     * Obsługuje kliknięcie przycisku usuwania zespołu.
     *
     * @param event zdarzenie akcji.
     */
    public void deleteButtonOnAction(ActionEvent event) {
        // Pobranie zaznaczonej drużyny
        Teams selectedTeam = teamTableView.getSelectionModel().getSelectedItem();
        if (selectedTeam != null) {
            // Tworzenie alertu potwierdzającego usunięcie drużyny
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Potwierdzenie");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Czy na pewno chcesz usunąć tę drużynę?");

            // Pobranie odpowiedzi użytkownika
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Sprawdzenie, czy drużyna ma przypisany projekt o statusie innym niż "Zakończony"
                ProjectDao projectDao = new ProjectDao();
                boolean hasOngoingProjects = projectDao.hasOngoingProjects(selectedTeam);

                if (hasOngoingProjects) {
                    // Wyświetlenie alertu z informacją o konieczności zmiany drużyny w projekcie
                    Alert ongoingProjectAlert = new Alert(Alert.AlertType.WARNING);
                    ongoingProjectAlert.setTitle("Błąd");
                    ongoingProjectAlert.setHeaderText(null);
                    ongoingProjectAlert.setContentText("Ta drużyna ma przypisane projekty w toku. Przed usunięciem zmień drużynę w projekcie.");
                    ongoingProjectAlert.showAndWait();
                    return;
                }

                // Ustawienie teamId na NULL dla projektów ze statusem "Zakończony"
                projectDao.setTeamIdToNullForCompletedProjects(selectedTeam);
                UserDao userDao = new UserDao();
                userDao.setUsersToNullForTeam(selectedTeam);

                // Usunięcie drużyny
                TeamDao teamDao = new TeamDao();
                teamDao.deleteTeam(selectedTeam);

                // Usunięcie zaznaczonej drużyny z TableView
                teamTableView.getItems().remove(selectedTeam);
            }
        }
    }

    /**
     * Obsługuje kliknięcie przycisku edycji zespołu.
     *
     * @param event zdarzenie akcji.
     * @throws IOException w przypadku błędu ładowania zasobu.
     */
    @FXML
    public void editTeamOnAction(ActionEvent event) throws IOException {
        Teams selectedTeam = teamTableView.getSelectionModel().getSelectedItem();
        if (selectedTeam != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/Teams/edit_team.fxml"));
            Parent root = loader.load();

            EditTeamController editTeamController = loader.getController();
            editTeamController.setSelectedTeam(selectedTeam);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

            // Aktualizacja TableView po zakończeniu edycji
            teamTableView.refresh();
        }
    }

    @FXML
    public void generateReport(ActionEvent event) {
        Teams selectedTeam = teamTableView.getSelectionModel().getSelectedItem();
        if (selectedTeam == null) {
            // No team selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Drużyna nie wybrana.");
            alert.setHeaderText(null);
            alert.setContentText("Proszę wybrac drużyne aby wygenerowac raport.");
            alert.showAndWait();
        } else {
            try {
                PdfTeamGenerator pdfTeamGenerator = new PdfTeamGenerator();
                pdfTeamGenerator.generateTeamPDF(selectedTeam.getTeamName(), selectedTeam.getId());

                // Report generated successfully
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Report Wygenerowany");
                alert.setHeaderText(null);
                alert.setContentText("PDF Report został wygenerowany pomyślnie.");
                alert.showAndWait();
            } catch (IOException | SQLException | DocumentException e) {
                // Error generating report
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error generating PDF Report: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}
