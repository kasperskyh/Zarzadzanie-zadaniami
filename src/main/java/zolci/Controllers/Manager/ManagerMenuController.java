package zolci.Controllers.Manager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zolci.DAO.TaskDao;
import zolci.DAO.UserTasksDao;
import zolci.UserPreferences;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler dla głównego menu menedżera w aplikacji.
 */
public class ManagerMenuController implements Initializable {

    private static final Logger logger = LogManager.getLogger(ManagerMenuController.class);


    @FXML
    private ImageView logoImage;
    @FXML
    private Text welcomeText;
    @FXML
    private PieChart taskStatusPieChart;
    @FXML
    private ProgressBar completedTasksProgressBar;
    private UserTasksDao userTasksDao = new UserTasksDao();
    private TaskDao taskDao = new TaskDao();

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
        welcomeText.setText("Witaj " + UserPreferences.getName());

        List<Object[]> taskStatusList = taskDao.getTaskStatusForManagerTeam(UserPreferences.getId());

        // Utwórz listę obiektów PieChart.Data do przechowywania danych do wykresu
        if (taskStatusList != null) {
            for (Object[] result : taskStatusList) {
                String taskStatus = (String) result[0];
                long count = (long) result[1];
                taskStatusPieChart.getData().add(new PieChart.Data(taskStatus + " (" + count + ")", count));
            }
        } else {
            // Obsługa błędów lub braku danych
        }
        taskStatusPieChart.setTitle("Statusy zadań");

        // Przesunięcie legendy na dół
        taskStatusPieChart.setLegendSide(Side.BOTTOM);

        initializeProgressBar(taskStatusList);
    }

    /**
     * Inicjalizuje pasek postępu na podstawie danych dotyczących zadań.
     *
     * @param taskStatusList lista statusów zadań
     */
    private void initializeProgressBar(List<Object[]> taskStatusList) {
        long totalTasks = taskStatusList.stream()
                .mapToLong(array -> (long) array[1])
                .sum();

        long completedTasks = taskStatusList.stream()
                .filter(array -> "Zakończony".equals(array[0]))
                .mapToLong(array -> (long) array[1])
                .sum();

        double completedPercentage = (double) completedTasks / totalTasks;

        completedTasksProgressBar.setProgress(completedPercentage);
    }

    /**
     * Obsługuje kliknięcie na logo, przenosząc użytkownika do menu.
     *
     * @param event zdarzenie wywołane przez kliknięcie myszką
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
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
     * Obsługuje akcję wylogowania użytkownika.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    public void logoutButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Obsługuje akcję dodawania nowego materiału.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
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
     * Obsługuje akcję przejścia do widoku inwentarza.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
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
     * Obsługuje akcję przejścia do widoku profilu.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
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
     * Obsługuje akcję przejścia do widoku raportów.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
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
     * Obsługuje akcję przejścia do widoku projektów.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
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
     * Obsługuje akcję przejścia do widoku zadań.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
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
     * Obsługuje akcję przejścia do widoku zespołów.
     *
     * @param event zdarzenie wywołane przez naciśnięcie przycisku
     * @throws IOException jeśli wystąpi błąd wejścia/wyjścia
     */
    @FXML
    public void teamButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Manager/teams.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}