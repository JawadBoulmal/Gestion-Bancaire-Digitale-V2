package Utils;

import java.util.UUID;

public class Utils {
    public static boolean isValidUUID(String input) {
        if (input == null) return false;

        try {
            UUID.fromString(input);
            return true;  // successfully parsed → valid UUID
        } catch (IllegalArgumentException e) {
            return false; // not a valid UUID
        }
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email; // not a valid email
        }

        String[] parts = email.split("@", 2);
        String username = parts[0];
        String domain = parts[1];

        if (username.length() <= 6) {
            return username + "@" + domain; // too short, don’t mask
        }

        // keep first 6 chars, then mask
        String masked = username.substring(0, 6) + "..." + "@" + domain;
        return masked;
    }
}
