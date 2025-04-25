package zolci.models;

import jakarta.persistence.*;

/**
 * Klasa reprezentująca status projektu.
 */
@Entity
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "status_name")
    private String statusName;

    /**
     * Zwraca identyfikator statusu.
     *
     * @return identyfikator statusu
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator statusu.
     *
     * @param id identyfikator statusu
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Zwraca nazwę statusu.
     *
     * @return nazwa statusu
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * Ustawia nazwę statusu.
     *
     * @param statusName nazwa statusu
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
