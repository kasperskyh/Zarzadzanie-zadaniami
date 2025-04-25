package zolci;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Objects;

/**
 * Główna klasa aplikacji JavaFX.
 */
public class Main extends Application {

    /** Fabryka sesji Hibernate. */
    public static SessionFactory sessionFactory;

    /**
     * Metoda startująca aplikację JavaFX.
     *
     * @param stage Główny Stage aplikacji
     * @throws Exception Wyjątek występujący przy błędach inicjalizacji
     */
    @Override
    public void start(Stage stage) throws Exception {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml")));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 800, 500));
        stage.show();

    }

    /**
     * Metoda główna uruchamiająca aplikację.
     *
     * @param args Argumenty wiersza poleceń
     */
    public static void main(String[] args) {
        launch();
    }

}
