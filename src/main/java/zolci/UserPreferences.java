package zolci;

import java.util.prefs.Preferences;

/**
 * Klasa dostarcza metod do zapisu i pobierania danych użytkownika przy użyciu interfejsu Preferences.
 */
public class UserPreferences {

    // Klucze dla danych użytkownika przechowywanych w preferencjach
    private static final String ID_KEY = "id";
    private static final String EMAIL_KEY = "email";
    private static final String NAME_KEY = "name";
    private static final String ROLE_KEY = "role";
    private static final String SURNAME_KEY = "surname";
    private static final String TEAM_KEY = "team";

    // Instancja Preferences do przechowywania danych użytkownika
    private static Preferences prefs = Preferences.userRoot().node(UserPreferences.class.getName());

    /**
     * Metoda zapisuje dane użytkownika do preferencji.
     *
     * @param id      ID użytkownika.
     * @param email   Adres email użytkownika.
     * @param role    Rola użytkownika.
     * @param name    Imię użytkownika.
     * @param surname Nazwisko użytkownika.
     * @param team    Zespół użytkownika.
     */
    public static void saveUser(int id, String email, String role, String name, String surname, String team) {
        prefs.putInt(ID_KEY, id);
        prefs.put(EMAIL_KEY, email);
        prefs.put(ROLE_KEY, role);
        prefs.put(NAME_KEY, name);
        prefs.put(SURNAME_KEY, surname);
        prefs.put(TEAM_KEY, team);
    }

    /**
     * Metoda pobiera ID użytkownika z preferencji.
     *
     * @return ID użytkownika, lub -1 jeśli nie znaleziono.
     */
    public static int getId() {
        return prefs.getInt(ID_KEY, -1); // -1 can be the default value if no ID is set
    }

    /**
     * Metoda pobiera adres email użytkownika z preferencji.
     *
     * @return Adres email użytkownika.
     */
    public static String getEmail() {
        return prefs.get(EMAIL_KEY, "");
    }

    /**
     * Metoda pobiera rolę użytkownika z preferencji.
     *
     * @return Rola użytkownika.
     */
    public static String getRole() {
        return prefs.get(ROLE_KEY, "");
    }

    /**
     * Metoda pobiera imię użytkownika z preferencji.
     *
     * @return Imię użytkownika.
     */
    public static String getName() {
        return prefs.get(NAME_KEY, "");
    }

    /**
     * Metoda pobiera nazwisko użytkownika z preferencji.
     *
     * @return Nazwisko użytkownika.
     */
    public static String getSurname() {
        return prefs.get(SURNAME_KEY, "");
    }

    /**
     * Metoda pobiera zespół użytkownika z preferencji.
     *
     * @return Zespół użytkownika.
     */
    public static String getTeam() {
        return prefs.get(TEAM_KEY, "");
    }
}
