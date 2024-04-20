import java.util.Random;
import java.util.HashSet;
import java.util.Set;
class IdGenerator {
    private static Set<String> usedIds = new HashSet<>(); // Set to store used IDs

    public static String generateUniqueId(String prefix) {
        Random random = new Random();
        String id;
        do {
            int randomNum = 1000 + random.nextInt(9000); // Generate random 4-digit number
            id = prefix + randomNum; // Prefix with the provided prefix
        } while (usedIds.contains(id)); // Ensure unique ID
        usedIds.add(id); // Add ID to used set
        return id;
    }
}