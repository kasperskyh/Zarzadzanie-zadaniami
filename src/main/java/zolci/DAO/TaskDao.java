package zolci.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import zolci.HibernateUtil;
import zolci.models.Projects;
import zolci.models.Tasks;
import zolci.models.Teams;
import zolci.models.Users;

import java.util.List;

/**
 * Klasa DAO odpowiedzialna za operacje na encjach Tasks.
 */
public class TaskDao {
    private final SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(TaskDao.class);


    /**
     * Konstruktor inicjujący obiekt SessionFactory z HibernateUtil.
     */
    public TaskDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Pobiera wszystkie zadania z bazy danych.
     *
     * @return lista wszystkich zadań
     */
    public List<Tasks> getAllTasks() {
        List<Tasks> taskList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Tasks> query = session.createQuery("FROM Tasks", Tasks.class);
            taskList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    /**
     * Usuwa zadanie z bazy danych.
     *
     * @param task zadanie do usunięcia
     */
    public void deleteTask(Tasks task) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(task);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pobiera zadania przypisane do danego użytkownika.
     *
     * @param user użytkownik, dla którego pobierane są zadania
     * @return lista zadań przypisanych do użytkownika
     */
    public List<Tasks> getTasksByUser(Users user) {
        List<Tasks> taskList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Tasks> query = session.createQuery("SELECT ut.task FROM UserTasks ut WHERE ut.user = :user", Tasks.class);
            query.setParameter("user", user);
            taskList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    /**
     * Pobiera zadania przypisane do danego użytkownika na podstawie identyfikatora użytkownika.
     *
     * @param userId identyfikator użytkownika, dla którego pobierane są zadania
     * @return lista zadań przypisanych do użytkownika
     */
    public List<Tasks> getTasksForUser(int userId) {
        List<Tasks> taskList = null;
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT ut.task " +
                    "FROM UserTasks ut " +
                    "JOIN ut.task t " +
                    "JOIN t.projectId p " +
                    "JOIN p.statusId s " +
                    "WHERE ut.user.id = :userId " +
                    "AND s.statusName = 'W trakcie'";
            Query<Tasks> query = session.createQuery(hql, Tasks.class);
            query.setParameter("userId", userId);
            taskList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    /**
     * Aktualizuje istniejące zadanie w bazie danych.
     *
     * @param task zadanie do zaktualizowania
     */
    public void updateTask(Tasks task) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(task);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Pobiera zadanie na podstawie jego identyfikatora.
     *
     * @param selectedTaskId identyfikator zadania do pobrania
     * @return zadanie lub null, jeśli nie istnieje
     */
    public Tasks getTaskById(Integer selectedTaskId) {
        Tasks task = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Tasks> query = session.createQuery("FROM Tasks WHERE id = :id", Tasks.class);
            query.setParameter("id", selectedTaskId);
            task = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    /**
     * Pobiera zadania przypisane do wybranego projektu.
     *
     * @param selectedProject projekt, dla którego pobierane są zadania
     * @return lista zadań przypisanych do wybranego projektu
     */
    public List<Tasks> getTasksByProject(Projects selectedProject) {
        List<Tasks> tasksList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Tasks> query = session.createQuery(
                    "FROM Tasks T WHERE T.projectId = :selectedProject", Tasks.class);
            query.setParameter("selectedProject", selectedProject);
            tasksList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasksList;
    }

    /**
     * Dodaje nowe zadanie do bazy danych.
     *
     * @param newTask nowe zadanie do dodania
     */
    public void addTask(Tasks newTask) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // Rozpoczęcie transakcji
            transaction = session.beginTransaction();

            // Zapis nowego zadania
            session.save(newTask);

            // Zakończenie transakcji
            transaction.commit();
        } catch (Exception ex) {
            // Wycofanie transakcji w przypadku błędu
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Pobiera zadania przypisane do managera na podstawie jego identyfikatora.
     *
     * @param managerId identyfikator managera, dla którego pobierane są zadania
     * @return lista zadań przypisanych do managera
     */
    public List<Tasks> getTasksByManagerId(int managerId) {
        List<Tasks> taskList = null;
        try (Session session = sessionFactory.openSession()) {
            // Fetching the team_id from the teams table for the given manager
            Query<Teams> teamQuery = session.createQuery("FROM Teams T WHERE T.user.id = :managerId", Teams.class);
            teamQuery.setParameter("managerId", managerId);
            Teams team = teamQuery.uniqueResult();

            if (team != null) {
                // Fetching tasks associated with the team_id and project status not equal to "Zakończony"
                Query<Tasks> taskQuery = session.createQuery(
                        "FROM Tasks TS " +
                                "JOIN TS.projectId P " +
                                "JOIN P.statusId S " +
                                "WHERE P.teamId.id = :teamId AND S.statusName != :completedStatus", Tasks.class);
                taskQuery.setParameter("teamId", team.getId());
                taskQuery.setParameter("completedStatus", "Zakończony");
                taskList = taskQuery.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    /**
     * Pobiera nazwy zadań przypisanych do managera na podstawie jego identyfikatora.
     *
     * @param managerId identyfikator managera, dla którego pobierane są nazwy zadań
     * @return lista nazw zadań przypisanych do managera
     */
    public List<String> getTaskNamesByManagerId(int managerId) {
        List<String> taskNames = null;
        try (Session session = sessionFactory.openSession()) {
            // Pobieranie team_id z tabeli teams dla danego managera
            Query<Teams> teamQuery = session.createQuery("FROM Teams T WHERE T.user.id = :managerId", Teams.class);
            teamQuery.setParameter("managerId", managerId);
            Teams team = teamQuery.uniqueResult();

            if (team != null) {
                // Pobieranie nazw zadań związanych z team_id oraz status projektu różny od "Zakończony"
                Query<String> taskNameQuery = session.createQuery(
                        "SELECT TS.name FROM Tasks TS " +
                                "JOIN TS.projectId P " +
                                "JOIN P.statusId S " +
                                "WHERE P.teamId.id = :teamId AND S.statusName != :completedStatus", String.class);
                taskNameQuery.setParameter("teamId", team.getId());
                taskNameQuery.setParameter("completedStatus", "Zakończony");
                taskNames = taskNameQuery.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskNames;
    }

    /**
     * Pobiera nazwy nieprzypisanych zadań przypisanych do managera na podstawie jego identyfikatora.
     *
     * @param managerId identyfikator managera, dla którego pobierane są nazwy nieprzypisanych zadań
     * @return lista nazw nieprzypisanych zadań przypisanych do managera
     */
    public List<String> getUnassignedTaskNamesByManagerId(int managerId) {
        List<String> taskNames = null;
        try (Session session = sessionFactory.openSession()) {
            // Retrieve team_id from the teams table for the given manager
            Query<Teams> teamQuery = session.createQuery("FROM Teams T WHERE T.user.id = :managerId", Teams.class);
            teamQuery.setParameter("managerId", managerId);
            Teams team = teamQuery.uniqueResult();

            if (team != null) {
                // Retrieve task names associated with team_id that are not assigned to any user
                Query<String> taskNameQuery = session.createQuery(
                        "SELECT TS.name FROM Tasks TS " +
                                "JOIN TS.projectId P " +
                                "JOIN P.statusId S " +
                                "WHERE P.teamId.id = :teamId " +
                                "AND S.statusName != :completedStatus " +
                                "AND TS.id NOT IN (SELECT UT.task.id FROM UserTasks UT)", String.class);
                taskNameQuery.setParameter("teamId", team.getId());
                taskNameQuery.setParameter("completedStatus", "Zakończony");
                taskNames = taskNameQuery.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskNames;
    }

    /**
     * Pobiera zadanie na podstawie jego nazwy.
     *
     * @param selectedTaskName nazwa zadania do pobrania
     * @return zadanie lub null, jeśli nie istnieje
     */
    public Tasks getTaskByName(String selectedTaskName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Tasks> query = session.createQuery("FROM Tasks T WHERE T.name = :taskName", Tasks.class);
            query.setParameter("taskName", selectedTaskName);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Lub obsługa błędu, np. rzucenie wyjątku
        }
    }

    /**
     * Pobiera statusy zadań dla teamu managera na podstawie jego identyfikatora.
     *
     * @param managerId identyfikator managera, dla którego pobierane są statusy zadań
     * @return lista obiektów zawierających nazwy statusów i liczbę zadań w danym statusie
     */
    public List<Object[]> getTaskStatusForManagerTeam(int managerId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String queryString = "SELECT S.statusName, COUNT(*) " +
                    "FROM Tasks TS " +
                    "JOIN TS.projectId P " +
                    "JOIN P.teamId T " +
                    "JOIN TS.statusId S " +
                    "WHERE T.user.id = :managerId " +
                    "AND P.statusId.statusName != 'Zakończony' " + // Dodany warunek
                    "GROUP BY S.statusName";

            Query<Object[]> query = session.createQuery(queryString, Object[].class);
            query.setParameter("managerId", managerId);

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
