package zolci.utils;

import org.mindrot.jbcrypt.*;

public class PasswordManager {

    // Metoda do haszowania hasła
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Metoda do weryfikacji hasła
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
