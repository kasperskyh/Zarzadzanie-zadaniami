package zolci.DAO;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import zolci.HibernateUtil;
import zolci.models.Role;
import zolci.models.Tasks;
import zolci.models.Teams;
import zolci.models.Users;

import java.util.List;

/**
 * Klasa odpowiedzialna za operacje CRUD na obiekcie Users w bazie danych.
 */
public class UserDao {
    private final SessionFactory sessionFactory;

    /**
     * Pobiera użytkownika na podstawie jego adresu e-mail i hasła.
     *
     * @param email    Adres e-mail użytkownika.
     * @param password Hasło użytkownika.
     * @return Obiekt Users, jeśli znaleziono użytkownika, null w przeciwnym razie.
     */
    public Users getUserByEmailAndPassword(String email, String password) {
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("FROM Users U WHERE U.email = :email AND U.password = :password", Users.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            return query.uniqueResult();
        }
    }

    /**
     * Konstruktor klasy UserDao inicjalizujący fabrykę sesji Hibernate.
     */
    public UserDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }


    /**
     * Pobiera listę wszystkich użytkowników.
     *
     * @return Lista obiektów Users.
     */
    public List<Users> getAllUsers() {
        List<Users> userList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("FROM Users", Users.class);
            userList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * Aktualizuje hasło użytkownika na podstawie jego identyfikatora.
     *
     * @param userId      Identyfikator użytkownika.
     * @param newPassword Nowe hasło.
     * @return true, jeśli aktualizacja się powiodła, false w przeciwnym razie.
     */
    public boolean updatePassword(int userId, String newPassword) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Users user = session.get(Users.class, userId);
            if (user != null) {
                user.setPassword(newPassword);
                session.update(user); // Zaktualizuj użytkownika w bazie danych
                transaction.commit(); // Zatwierdź transakcję
                return true;
            } else {
                // Użytkownik o podanym identyfikatorze nie istnieje
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Pobiera listę użytkowników z rolą "user".
     *
     * @return Lista obiektów Users.
     */
    public List<Users> getUsersWithUserRole() {
        List<Users> userList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("FROM Users U WHERE U.role.roleName = :roleName", Users.class);
            query.setParameter("roleName", "user");
            userList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * Pobiera listę użytkowników z rolą "manager".
     *
     * @return Lista obiektów Users.
     */
    public List<Users> getManagers() {
        List<Users> userList = null;
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("FROM Users U WHERE U.role.roleName = :roleName", Users.class);
            query.setParameter("roleName", "manager");
            userList = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * Pobiera listę nieprzypisanych menedżerów.
     *
     * @return Lista obiektów Users.
     */
    public List<Users> getUnassignedManagers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("FROM Users U WHERE U.role.roleName = :roleName AND U.team IS NULL AND U.id NOT IN (SELECT T.user.id FROM Teams T WHERE T.user.id IS NOT NULL)", Users.class);
            query.setParameter("roleName", "manager");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera listę nieprzypisanych użytkowników.
     *
     * @return Lista obiektów Users.
     */
    public List<Users> getUnassignedUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Users> query = session.createQuery("FROM Users U WHERE U.role.roleName = :roleName AND (U.team IS NULL OR U.team.id IS NULL)", Users.class);
            query.setParameter("roleName", "user");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Dodaje nowego użytkownika do bazy danych.
     *
     * @param user Obiekt Users do dodania.
     */
    public void addUser(Users user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualizuje dane użytkownika w bazie danych.
     *
     * @param user Obiekt Users do aktualizacji.
     */
    public void updateUser(Users user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Usuwa użytkownika z bazy danych, w tym jego przypisane zadania.
     *
     * @param user Obiekt Users do usunięcia.
     */
    public void deleteUser(Users user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Sprawdź, czy użytkownik ma przypisane zadania
            TaskDao taskDao = new TaskDao();
            List<Tasks> tasks = taskDao.getTasksByUser(user);

            // Jeśli użytkownik ma przypisane zadania, usuń je
            if (!tasks.isEmpty()) {
                for (Tasks task : tasks) {
                    session.delete(task);
                }
            }

            // Usuń użytkownika
            session.delete(user);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pobiera listę wszystkich ról.
     *
     * @return Lista obiektów Role.
     */
    public List<Role> getAllRoles() {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("FROM Role", Role.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera listę wszystkich zespołów.
     *
     * @return Lista obiektów Teams.
     */
    public List<Teams> getAllTeams() {
        try (Session session = sessionFactory.openSession()) {
            Query<Teams> query = session.createQuery("FROM Teams", Teams.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera rolę na podstawie jej nazwy.
     *
     * @param roleName Nazwa roli.
     * @return Obiekt Role, jeśli znaleziono, null w przeciwnym razie.
     */
    public Role getRoleByName(String roleName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("FROM Role R WHERE R.roleName = :roleName", Role.class);
            query.setParameter("roleName", roleName);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera zespół na podstawie jego nazwy.
     *
     * @param teamName Nazwa zespołu.
     * @return Obiekt Teams, jeśli znaleziono, null w przeciwnym razie.
     */
    public Teams getTeamByName(String teamName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Teams> query = session.createQuery("FROM Teams T WHERE T.name = :name", Teams.class);
            query.setParameter("name", teamName);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera użytkownika na podstawie jego adresu e-mail.
     *
     * @param email Adres e-mail użytkownika.
     * @return Obiekt Users, jeśli znaleziono, null w przeciwnym razie.
     */
    public Users getUserByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Users> query = session.createQuery("FROM Users WHERE email = :email", Users.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera użytkownika na podstawie jego identyfikatora.
     *
     * @param id Identyfikator użytkownika.
     * @return Obiekt Users, jeśli znaleziono, null w przeciwnym razie.
     */
    public Users getUserById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Users> query = session.createQuery("FROM Users WHERE id = :id", Users.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera listę użytkowników przypisanych do konkretnego zespołu.
     *
     * @param selectedTeam Zespół, do którego przypisani są użytkownicy.
     * @return Lista obiektów Users.
     */
    public List<Users> getUsersByTeamId(Teams selectedTeam) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Users> query = session.createQuery("SELECT u FROM Users u WHERE u.team = :team", Users.class);
            query.setParameter("team", selectedTeam);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pobiera listę użytkowników należących do zespołu zarządzanego przez określonego managera.
     *
     * @param managerId ID managera
     * @return lista użytkowników należących do zespołu
     */
    public List<Users> getUsersByManagerId(int managerId) {
        List<Users> userList = null;
        try (Session session = sessionFactory.openSession()) {
            // Pobieranie team_id z tabeli teams dla danego managera
            Query<Teams> teamQuery = session.createQuery("FROM Teams T WHERE T.user.id = :managerId", Teams.class);
            teamQuery.setParameter("managerId", managerId);
            Teams team = teamQuery.uniqueResult();

            if (team != null) {
                // Pobieranie użytkowników z team_id równym team.id
                Query<Users> userQuery = session.createQuery("FROM Users U WHERE U.team.id = :teamId", Users.class);
                userQuery.setParameter("teamId", team.getId());
                userList = userQuery.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * Pobiera listę nazw użytkowników należących do zespołu zarządzanego przez określonego managera.
     *
     * @param managerId ID managera
     * @return lista nazw użytkowników należących do zespołu
     */
    public List<String> getUserNamesByManagerId(int managerId) {
        List<String> userNames = null;
        try (Session session = sessionFactory.openSession()) {
            // Pobieranie team_id z tabeli teams dla danego managera
            Query<Teams> teamQuery = session.createQuery("FROM Teams T WHERE T.user.id = :managerId", Teams.class);
            teamQuery.setParameter("managerId", managerId);
            Teams team = teamQuery.uniqueResult();

            if (team != null) {
                // Pobieranie nazw użytkowników z team_id równym team.id
                Query<String> userNameQuery = session.createQuery("SELECT U.email FROM Users U WHERE U.team.id = :teamId", String.class);
                userNameQuery.setParameter("teamId", team.getId());
                userNames = userNameQuery.list();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userNames;
    }

    /**
     * Ustawia null dla wszystkich użytkowników należących do danego zespołu.
     *
     * @param team zespół, dla którego mają zostać ustawieni użytkownicy na null
     */
    public void setUsersToNullForTeam(Teams team) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Ustaw null dla użytkowników należących do danego zespołu
            String updateUsersHql = "UPDATE Users U SET U.team = NULL WHERE U.team = :team";
            Query updateUsersQuery = session.createQuery(updateUsersHql);
            updateUsersQuery.setParameter("team", team);
            updateUsersQuery.executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
