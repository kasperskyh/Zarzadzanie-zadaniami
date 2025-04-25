package zolci.models;

import jakarta.persistence.*;

/**
 * Klasa reprezentująca zespół w systemie.
 */
@Entity
@Table(name = "teams")
public class Teams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "team_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Users user;

    /**
     * Konstruktor tworzący zespół na podstawie podanych danych.
     *
     * @param id   identyfikator zespołu
     * @param name nazwa zespołu
     * @param user użytkownik będący menedżerem zespołu
     */
    public Teams(int id, String name, Users user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    /**
     * Pusty konstruktor, inicjuje obiekt bez przypisanych wartości.
     */
    public Teams() {
    }

    /**
     * Konstruktor tworzący zespół na podstawie nazwy zespołu i wybranego menedżera.
     *
     * @param teamName       nazwa zespołu
     * @param selectedManager menedżer zespołu
     */
    public Teams(String teamName, Users selectedManager) {
        this.name = teamName;
        this.user = selectedManager;
    }

    /**
     * Zwraca identyfikator zespołu.
     *
     * @return identyfikator zespołu
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator zespołu.
     *
     * @param id identyfikator zespołu
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Zwraca nazwę zespołu.
     *
     * @return nazwa zespołu lub "brak drużyny" jeśli nazwa jest null
     */
    public String getTeamName() {
        if(name == null){
            return "brak druzyny";
        }
        return name;
    }

    /**
     * Ustawia nazwę zespołu.
     *
     * @param name nazwa zespołu
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca użytkownika będącego menedżerem zespołu.
     *
     * @return menedżer zespołu
     */
    public Users getUser() {
        return user;
    }

    /**
     * Ustawia użytkownika będącego menedżerem zespołu.
     *
     * @param user menedżer zespołu
     */
    public void setUser(Users user) {
        this.user = user;
    }
}
