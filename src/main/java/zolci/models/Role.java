package zolci.models;

import jakarta.persistence.*;
/**
 * Klasa reprezentująca role użytkowników.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "role_name")
    private String roleName;

    /**
     * Zwraca identyfikator roli.
     *
     * @return identyfikator roli
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator roli.
     *
     * @param id identyfikator roli
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Zwraca nazwę roli.
     *
     * @return nazwa roli
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Ustawia nazwę roli.
     *
     * @param name nazwa roli
     */
    public void setRoleName(String name) {
        this.roleName = name;
    }
}

