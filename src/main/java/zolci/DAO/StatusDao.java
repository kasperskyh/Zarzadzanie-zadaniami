package zolci.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import zolci.HibernateUtil;
import zolci.models.Status;


import java.util.List;

/**
 * Klasa DAO odpowiedzialna za operacje na encjach Status.
 */
public class StatusDao {

    private final SessionFactory sessionFactory;

    /**
     * Konstruktor inicjujący obiekt SessionFactory z HibernateUtil.
     */
    public StatusDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Pobiera wszystkie statusy z bazy danych.
     *
     * @return lista wszystkich statusów
     */
    public List<Status> getAllStatus() {
        List<Status> statusList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Status> query = session.createQuery("FROM Status", Status.class);
            statusList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusList;
    }

    /**
     * Pobiera wszystkie nazwy statusów z bazy danych.
     *
     * @return lista wszystkich nazw statusów
     */
    public List<String> getAllStatuses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT statusName FROM Status", String.class).list();
        }
    }

    /**
     * Pobiera obiekt Status na podstawie jego nazwy.
     *
     * @param selectedStatus nazwa statusu do wyszukania
     * @return obiekt Status lub null, jeśli nie istnieje
     */
    public Status getStatusByName(String selectedStatus) {
        Status status = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Status> query = session.createQuery("FROM Status WHERE statusName = :statusName", Status.class);
            query.setParameter("statusName", selectedStatus);
            status = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}
