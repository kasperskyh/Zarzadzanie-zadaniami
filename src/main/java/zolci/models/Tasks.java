package zolci.models;

import jakarta.persistence.*;

/**
 * Klasa reprezentująca zadanie w systemie.
 */
@Entity
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "assignee")
    private String assignee;

    @Column(name = "priority")
    private String priority;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status statusId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Projects projectId;

    /**
     * Konstruktor tworzący zadanie na podstawie podanego identyfikatora.
     *
     * @param taskId identyfikator zadania
     */
    public Tasks(int taskId) {
    }

    /**
     * Pusty konstruktor, inicjuje obiekt bez przypisanych wartości.
     */
    public Tasks(){};

    /**
     * Zwraca identyfikator zadania.
     *
     * @return identyfikator zadania
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator zadania.
     *
     * @param id identyfikator zadania
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Zwraca nazwę zadania.
     *
     * @return nazwa zadania
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia nazwę zadania.
     *
     * @param name nazwa zadania
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca opis zadania.
     *
     * @return opis zadania
     */
    public String getDescription() {
        return description;
    }

    /**
     * Ustawia opis zadania.
     *
     * @param description opis zadania
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Zwraca osobę przypisaną do zadania.
     *
     * @return osoba przypisana do zadania
     */
    public String getAssignee() {
        return assignee;
    }

    /**
     * Ustawia osobę przypisaną do zadania.
     *
     * @param assignee osoba przypisana do zadania
     */
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    /**
     * Zwraca priorytet zadania.
     *
     * @return priorytet zadania
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Ustawia priorytet zadania.
     *
     * @param priority priorytet zadania
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Zwraca status zadania.
     *
     * @return status zadania
     */
    public Status getStatusId() {
        return statusId;
    }

    /**
     * Ustawia status zadania.
     *
     * @param statusId status zadania
     */
    public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }

    /**
     * Zwraca identyfikator projektu, do którego przypisane jest zadanie.
     *
     * @return identyfikator projektu
     */
    public Projects getProjectId() {
        return projectId;
    }

    /**
     * Ustawia identyfikator projektu, do którego przypisane jest zadanie.
     *
     * @param projectId identyfikator projektu
     */
    public void setProjectId(Projects projectId) {
        this.projectId = projectId;
    }
}
