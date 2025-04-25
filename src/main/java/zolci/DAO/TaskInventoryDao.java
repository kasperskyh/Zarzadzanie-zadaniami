package zolci.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import zolci.HibernateUtil;
import zolci.models.Inventory;
import zolci.models.TaskInventory;
import zolci.models.Tasks;

import java.util.List;

/**
 * Klasa DAO odpowiedzialna za operacje na encjach TaskInventory.
 */
public class TaskInventoryDao {
    private final SessionFactory sessionFactory;

    /**
     * Konstruktor inicjujący obiekt SessionFactory z HibernateUtil.
     */
    public TaskInventoryDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Pobiera materiały przypisane do danego zadania na podstawie identyfikatora zadania.
     *
     * @param taskId identyfikator zadania, dla którego pobierane są materiały
     * @return lista materiałów przypisanych do zadania
     */
    public List<TaskInventory> getMaterialsForTask(int taskId) {
        List<TaskInventory> taskList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<TaskInventory> query = session.createQuery("SELECT ti FROM TaskInventory ti WHERE ti.task.id = :taskId", TaskInventory.class);
            query.setParameter("taskId", taskId);
            taskList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    /**
     * Dodaje nowy wpis o materiale przypisanym do zadania do bazy danych.
     *
     * @param taskInventory nowy wpis o materiale do dodania
     */
    public void addTaskInventory(TaskInventory taskInventory) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(taskInventory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    /**
     * Pobiera wpisy o materiałach przypisanych do danego zadania na podstawie obiektu zadania.
     *
     * @param task zadanie, dla którego pobierane są wpisy o materiałach
     * @return lista wpisów o materiałach przypisanych do zadania
     */
    public List<TaskInventory> getTaskInventoriesByTask(Tasks task) {
        List<TaskInventory> taskInventoriesList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<TaskInventory> query = session.createQuery(
                    "FROM TaskInventory TI WHERE TI.task = :task", TaskInventory.class);
            query.setParameter("task", task);
            taskInventoriesList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskInventoriesList;
    }

    /**
     * Usuwa wpis o materiale przypisanym do zadania z bazy danych.
     *
     * @param taskInventory wpis o materiale do usunięcia
     */
    public void deleteTaskInventory(TaskInventory taskInventory) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(taskInventory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Pobiera wpisy o materiałach przypisanych do danego przedmiotu na podstawie obiektu przedmiotu.
     *
     * @param selectedItem przedmiot, dla którego pobierane są wpisy o materiałach
     * @return lista wpisów o materiałach przypisanych do przedmiotu
     */
    public List<TaskInventory> getTaskInventoriesByInventory(Inventory selectedItem) {
        List<TaskInventory> taskInventories = null;
        try (Session session = sessionFactory.openSession()) {
            Query<TaskInventory> query = session.createQuery(
                    "FROM TaskInventory TI WHERE TI.inventory = :selectedItem", TaskInventory.class);
            query.setParameter("selectedItem", selectedItem);
            taskInventories = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskInventories;
    }


}
