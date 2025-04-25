package zolci.DAO;

import org.hibernate.Transaction;
import zolci.models.Tasks;
import zolci.models.UserTasks;
import zolci.models.Users;
import zolci.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

import static zolci.Main.sessionFactory;

/**
 * Klasa DAO odpowiedzialna za operacje na encjach UserTasks.
 */
public class UserTasksDao {

    /**
     * Usuwa powiązanie użytkownika z zadaniem.
     *
     * @param userTask powiązanie użytkownika z zadaniem do usunięcia
     */
    public void deleteUserTask(UserTasks userTask) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(userTask);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Usuwa wszystkie powiązania użytkowników z zadaniami dla danego użytkownika.
     *
     * @param user użytkownik, którego powiązania z zadaniami mają być usunięte
     */
    public void deleteUserTasksByUser(Users user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM UserTasks UT WHERE UT.user = :user");
            query.setParameter("user", user);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pobiera listę powiązań użytkownika z zadaniami dla danego zadania.
     *
     * @param task zadanie, dla którego pobierane są powiązania użytkowników
     * @return lista powiązań użytkownika z zadaniami
     */
    public List<UserTasks> getUserTasksByTask(Tasks task) {
        List<UserTasks> userTasksList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<UserTasks> query = session.createQuery(
                    "FROM UserTasks UT WHERE UT.task = :task", UserTasks.class);
            query.setParameter("task", task);
            userTasksList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userTasksList;
    }

    /**
     * Dodaje powiązanie użytkownika z zadaniem.
     *
     * @param user użytkownik do powiązania
     * @param task zadanie do powiązania
     */
    public void addUserTask(Users user, Tasks task) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserTasks userTask = new UserTasks(user, task);
            Transaction tx = session.beginTransaction();
            session.save(userTask);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sprawdza, czy istnieje powiązanie użytkownika z zadaniem.
     *
     * @param user użytkownik do sprawdzenia powiązania
     * @param task zadanie do sprawdzenia powiązania
     * @return true, jeśli istnieje powiązanie użytkownika z zadaniem, w przeciwnym razie false
     */
    public boolean isUserTaskExists(Users user, Tasks task) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserTasks> query = session.createQuery("FROM UserTasks UT WHERE UT.user = :user AND UT.task = :task", UserTasks.class);
            query.setParameter("user", user);
            query.setParameter("task", task);
            UserTasks result = query.uniqueResult();
            return result != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Usuwa powiązanie użytkownika z zadaniem.
     *
     * @param user użytkownik do usunięcia powiązania
     * @param task zadanie do usunięcia powiązania
     */
    public void removeUserTask(Users user, Tasks task) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("DELETE FROM UserTasks UT WHERE UT.user = :user AND UT.task = :task");
            query.setParameter("user", user);
            query.setParameter("task", task);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
