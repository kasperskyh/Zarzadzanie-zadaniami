package zolci.models;

import jakarta.persistence.*;

/**
 * Reprezentuje przypisanie zadania do użytkownika w systemie.
 */
@Entity
@Table(name = "user_tasks")
public class UserTasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Tasks task;

    public UserTasks(Users user, Tasks task) {
        this.user = user;
        this.task = task;
    }
    public UserTasks(){

    }


    /**
     * Pobiera identyfikator przypisania.
     *
     * @return Identyfikator przypisania.
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator przypisania.
     *
     * @param id Identyfikator przypisania.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Pobiera użytkownika przypisanego do zadania.
     *
     * @return Użytkownik przypisany do zadania.
     */
    public Users getUser() {
        return user;
    }

    /**
     * Ustawia użytkownika przypisanego do zadania.
     *
     * @param user Użytkownik przypisany do zadania.
     */
    public void setUser(Users user) {
        this.user = user;
    }

    /**
     * Pobiera zadanie przypisane do użytkownika.
     *
     * @return Zadanie przypisane do użytkownika.
     */
    public Tasks getTask() {
        return task;
    }

    /**
     * Ustawia zadanie przypisane do użytkownika.
     *
     * @param task Zadanie przypisane do użytkownika.
     */
    public void setTask(Tasks task) {
        this.task = task;
    }
}
