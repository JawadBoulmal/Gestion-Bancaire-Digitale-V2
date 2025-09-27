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
}
