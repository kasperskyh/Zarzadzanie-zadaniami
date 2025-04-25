package zolci.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import zolci.models.Inventory;


import java.util.List;

import static zolci.Main.sessionFactory;

/**
 * Klasa DAO odpowiedzialna za operacje na encjach Inventory.
 */
public class InventoryDao {

    /**
     * Pobiera wszystkie elementy z tabeli Inventory.
     *
     * @return lista wszystkich elementów Inventory
     */
    public List<Inventory> getAllInventory() {
        try (Session session = sessionFactory.openSession()) {
            Query<Inventory> query = session.createQuery("FROM Inventory", Inventory.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera listę nazw wszystkich elementów Inventory.
     *
     * @return lista nazw elementów Inventory
     */
    public List<String> getAllInventoryNames() {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT i.name FROM Inventory i", String.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Pobiera element Inventory na podstawie nazwy.
     *
     * @param name nazwa elementu Inventory
     * @return element Inventory lub null, jeśli nie istnieje
     */
    public Inventory getInventoryByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Inventory> query = session.createQuery("FROM Inventory i WHERE i.name = :name", Inventory.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera cenę jednostkową elementu Inventory na podstawie nazwy.
     *
     * @param name nazwa elementu Inventory
     * @return cena jednostkowa elementu Inventory lub 0.0, jeśli nie istnieje
     */
    public double getUnitPriceByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Double> query = session.createQuery("SELECT i.unitPrice FROM Inventory i WHERE i.name = :name", Double.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * Dodaje nowy element Inventory.
     *
     * @param inventory element Inventory do dodania
     */
    public void addInventory(Inventory inventory) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(inventory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Usuwa element Inventory.
     *
     * @param inventory element Inventory do usunięcia
     */
    public void deleteInventory(Inventory inventory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(inventory);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualizuje element Inventory.
     *
     * @param inventory element Inventory do aktualizacji
     */
    public void updateInventory(Inventory inventory) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(inventory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
