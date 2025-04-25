package zolci.models;

import jakarta.persistence.*;

/**
 * Reprezentuje użytkownika w systemie.
 */
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Teams team;

    /**
     * Konstruktor tworzący użytkownika z pełnymi danymi.
     *
     * @param name     Imię użytkownika.
     * @param surname  Nazwisko użytkownika.
     * @param email    Adres e-mail użytkownika.
     * @param password Hasło użytkownika.
     * @param role     Rola użytkownika.
     * @param team     Zespół użytkownika.
     */
    public Users(String name, String surname, String email, String password, Role role, Teams team) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.team = team;
    }

    /**
     * Domyślny konstruktor bezargumentowy.
     */
    public Users(){

    }

    /**
     * Pobiera identyfikator użytkownika.
     *
     * @return Identyfikator użytkownika.
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator użytkownika.
     *
     * @param usersId Identyfikator użytkownika.
     */
    public void setUsersId(int usersId) {
        this.id = usersId;
    }

    /**
     * Pobiera imię użytkownika.
     *
     * @return Imię użytkownika.
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia imię użytkownika.
     *
     * @param name Imię użytkownika.
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Pobiera hasło użytkownika.
     *
     * @return Hasło użytkownika.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Ustawia hasło użytkownika.
     *
     * @param password Hasło użytkownika.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Pobiera nazwisko użytkownika.
     *
     * @return Nazwisko użytkownika.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Ustawia nazwisko użytkownika.
     *
     * @param surname Nazwisko użytkownika.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Pobiera adres e-mail użytkownika.
     *
     * @return Adres e-mail użytkownika.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia adres e-mail użytkownika.
     *
     * @param email Adres e-mail użytkownika.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Pobiera rolę użytkownika.
     *
     * @return Rola użytkownika.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Ustawia rolę użytkownika.
     *
     * @param role Rola użytkownika.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Pobiera zespół użytkownika.
     *
     * @return Zespół użytkownika.
     */
    public Teams getTeam() {
        return team;
    }

    /**
     * Pobiera nazwę zespołu użytkownika.
     *
     * @return Nazwa zespołu użytkownika lub "brak drużyny", jeśli zespół nie jest przypisany.
     */
    public String getTeamName() {
        if (team == null) {
            return "brak druzyny";
        }
        return team.getTeamName();
    }

    /**
     * Ustawia zespół użytkownika.
     *
     * @param team Zespół użytkownika.
     */
    public void setTeam(Teams team) {
        this.team = team;
    }
}
