import java.util.*;

public class Main {
    static final String[] COMMON = {
        "password", "123456", "qwerty", "admin", "hello",
        "letmein", "welcome", "monkey", "dragon", "iloveyou",
        "sunshine", "shadow", "superman", "trustno1", "football",
        "baseball", "abc123", "pass123", "batman", "starwars"
    };

    static final Random rand = new Random();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        try {
            checkStrength(password);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void checkStrength(String password) {
        // Basic sanity checks
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty.");

        if (password.contains(" "))
            throw new IllegalArgumentException("Password cannot contain spaces.");

        if (password.length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters.");

        if (password.length() > 16)
            throw new IllegalArgumentException("Password cannot exceed 16 characters.");

        int score = 0;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

        if (password.length() >= 8) score += 2;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))      hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c))     hasDigit = true;
            else                               hasSpecial = true;
        }

        if (hasUpper)   score += 1;
        if (hasLower)   score += 1;
        if (hasDigit)   score += 1;
        if (hasSpecial) score += 2;

        for (String word : COMMON) {
            if (password.toLowerCase().contains(word)) {
                score -= 3;
                break;
            }
        }

        String rating;
        if      (score <= 3) rating = "Weak";
        else if (score <= 6) rating = "Medium";
        else if (score <= 9) rating = "Strong";
        else                 rating = "Very Strong";

        System.out.println("Score : " + score);
        System.out.println("Rating: " + rating);

        if (score <= 6) {
            System.out.println("\nTip: Try something like -> " + suggest(password));
        }
    }

    static String suggest(String base) {
        for (String word : COMMON)
            base = base.replaceAll("(?i)" + word, "");

        if (base.isEmpty()) base = "key";

        String suggestion = Character.toUpperCase(base.charAt(0)) + base.substring(1)
                          + (10 + rand.nextInt(90))
                          + "!@#$".charAt(rand.nextInt(4));

        if (suggestion.length() > 16) suggestion = suggestion.substring(0, 16);

        return suggestion;
    }
}
