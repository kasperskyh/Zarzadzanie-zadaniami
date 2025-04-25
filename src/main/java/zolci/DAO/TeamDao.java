package zolci.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import zolci.HibernateUtil;
import zolci.models.Status;
import zolci.models.Teams;
import zolci.models.Users;

import java.util.List;

import static zolci.Main.sessionFactory;

/**
 * Klasa dostępu do danych (DAO) dla operacji CRUD na zespołach.
 */
public class TeamDao {

    /**
     * Pobiera wszystkie zespoły z bazy danych.
     *
     * @return lista wszystkich zespołów
     */
    public List<Teams> getAllTeams() {
        List<Teams> userList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Teams> query = session.createQuery("FROM Teams", Teams.class);
            userList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * Dodaje nowy zespół do bazy danych.
     *
     * @param team zespół do dodania
     */
    public void addTeam(Teams team) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(team);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pobiera zespół po nazwie.
     *
     * @param name nazwa zespołu do pobrania
     * @return zespół o podanej nazwie lub null, jeśli nie znaleziono
     */
    public Teams getTeamByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Teams> query = session.createQuery("FROM Teams T WHERE T.name = :name", Teams.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera zespoły dostępne do przydzielenia projektu na podstawie statusu ukończenia projektów.
     *
     * @param completedStatus status wskazujący ukończenie projektów
     * @return lista zespołów dostępnych do przydzielenia projektu
     */
    public List<Teams> getTeamForProject(Status completedStatus) {
        List<Teams> teamList = null;
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Teams T WHERE " +
                    "NOT EXISTS (SELECT 1 FROM Projects P WHERE P.teamId = T) " +
                    "OR NOT EXISTS (SELECT 1 FROM Projects P WHERE P.teamId = T AND P.statusId != :completedStatus)";
            Query<Teams> query = session.createQuery(hql, Teams.class);
            query.setParameter("completedStatus", completedStatus);
            teamList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamList;
    }

    /**
     * Pobiera zespół zarządzany przez określonego managera.
     *
     * @param managerId ID managera
     * @return zespół zarządzany przez określonego managera lub null, jeśli nie znaleziono
     */
    public Teams getTeamByManager(int managerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Teams> query = session.createQuery("FROM Teams T WHERE T.user.id = :managerId", Teams.class);
            query.setParameter("managerId", managerId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sprawdza, czy manager jest przypisany do jakiegokolwiek zespołu.
     *
     * @param manager manager do sprawdzenia
     * @return true, jeśli manager jest przypisany do zespołu, false w przeciwnym razie
     */
    public boolean isManagerAssignedToTeam(Users manager) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(T) FROM Teams T WHERE T.user = :managerId", Long.class);
            query.setParameter("managerId", manager);
            Long count = query.uniqueResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Usuwa zespół z bazy danych.
     *
     * @param team zespół do usunięcia
     */
    public void deleteTeam(Teams team) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(team);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualizuje zespół w bazie danych.
     *
     * @param selectedTeam zespół do aktualizacji
     */
    public void updateTeam(Teams selectedTeam) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(selectedTeam);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
