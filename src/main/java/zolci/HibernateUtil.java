package zolci;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import zolci.models.*;

/**
 * Klasa pomocnicza do zarządzania SessionFactory Hibernate'a.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Metoda budująca SessionFactory na podstawie konfiguracji Hibernate.
     *
     * @return gotowy SessionFactory
     */
    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure();
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            configuration.addAnnotatedClass(Users.class);
            configuration.addAnnotatedClass(Projects.class);
            configuration.addAnnotatedClass(Role.class);
            configuration.addAnnotatedClass(Status.class);
            configuration.addAnnotatedClass(Tasks.class);
            configuration.addAnnotatedClass(UserTasks.class);
            configuration.addAnnotatedClass(Teams.class);
            configuration.addAnnotatedClass(Inventory.class);
            configuration.addAnnotatedClass(TaskInventory.class);

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            // Obsługa błędów
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Metoda zwracająca obiekt SessionFactory.
     *
     * @return obiekt SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
