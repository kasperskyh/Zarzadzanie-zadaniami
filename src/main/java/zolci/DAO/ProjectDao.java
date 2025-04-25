package zolci.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import zolci.HibernateUtil;
import zolci.models.Projects;
import zolci.models.Teams;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa DAO odpowiedzialna za operacje na encjach Projects.
 */
public class ProjectDao {

    private final SessionFactory sessionFactory;

    /**
     * Konstruktor inicjujący obiekt SessionFactory z HibernateUtil.
     */
    public ProjectDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Pobiera wszystkie projekty z bazy danych.
     *
     * @return lista wszystkich projektów
     */
    public List<Projects> getAllProjects() {
        List<Projects> projectsList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Projects> query = session.createQuery("FROM Projects", Projects.class);
            projectsList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectsList;
    }

    /**
     * Aktualizuje wycenę projektu o określoną wartość.
     *
     * @param project             projekt do zaktualizowania
     * @param additionalValuation dodatkowa wartość do wyceny projektu
     */
    public void updateProjectValuation(Projects project, double additionalValuation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            double currentValuation = project.getValuation();
            double updatedValuation = currentValuation + additionalValuation;
            project.setValuation(updatedValuation);
            session.update(project);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dodaje nowy projekt do bazy danych.
     *
     * @param project projekt do dodania
     */
    public void addProject(Projects project) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(project);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualizuje istniejący projekt w bazie danych.
     *
     * @param project projekt do aktualizacji
     */
    public void updateProject(Projects project) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.update(project);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Usuwa projekt z bazy danych.
     *
     * @param project projekt do usunięcia
     */
    public void deleteProject(Projects project) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(project);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pobiera projekt na podstawie jego nazwy.
     *
     * @param projectName nazwa projektu do wyszukania
     * @return projekt lub null, jeśli nie istnieje
     */
    public Projects getProjectByName(String projectName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Projects> query = session.createQuery("FROM Projects WHERE name = :name", Projects.class);
            query.setParameter("name", projectName);
            return query.uniqueResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Pobiera bieżące projekty przypisane do menedżera na podstawie jego ID.
     *
     * @param managerId ID menedżera
     * @return lista bieżących projektów dla danego menedżera
     */
    public List<Projects> getCurrentProjectForManager(int managerId) {
        List<Projects> projectsList = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            // Fetching projects for the given managerId with status different from "Zakończony"
            Query<Projects> projectQuery = session.createQuery(
                    "FROM Projects P " +
                            "JOIN FETCH P.statusId S " +
                            "WHERE P.teamId.user.id = :managerId " +
                            "AND S.statusName != :completedStatus", Projects.class);
            projectQuery.setParameter("managerId", managerId);
            projectQuery.setParameter("completedStatus", "Zakończony");
            projectsList = projectQuery.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectsList;
    }

    /**
     * Sprawdza, czy dany zespół ma aktywne projekty.
     *
     * @param team zespół do sprawdzenia
     * @return true, jeśli zespół ma aktywne projekty; false w przeciwnym razie
     */
    public boolean hasOngoingProjects(Teams team) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(P) FROM Projects P WHERE P.teamId = :team AND P.statusId.statusName != :completedStatus";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("team", team);
            query.setParameter("completedStatus", "Zakończony");
            Long count = query.uniqueResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ustawia wartość teamId na null dla zakończonych projektów przypisanych do danego zespołu.
     *
     * @param team zespół, dla którego mają być zaktualizowane projekty
     */
    public void setTeamIdToNullForCompletedProjects(Teams team) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            String hql = "UPDATE Projects P SET P.teamId = NULL WHERE P.teamId = :team AND P.statusId.statusName = :completedStatus";
            Query query = session.createQuery(hql);
            query.setParameter("team", team);
            query.setParameter("completedStatus", "Zakończony");
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
