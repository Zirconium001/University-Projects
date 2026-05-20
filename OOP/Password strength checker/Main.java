import java.util.*;

public class Main {
    static final String[] COMMON = {"password", "123456", "qwerty", "admin", "hello"};
    static final String SPECIALS = "!@#$%^&*";
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
        if (password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("Password cannot be empty or blank!");
        if (password.length() > 16)
            throw new IllegalArgumentException("Password too long! Maximum 16 characters allowed.");

        int score = 0;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

        if (password.length() >= 8) score += 2;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }

        if (hasUpper) score += 1;
        if (hasLower) score += 1;
        if (hasDigit) score += 1;
        if (hasSpecial) score += 2;

        boolean hasCommon = false;
        for (String word : COMMON) {
            if (password.toLowerCase().contains(word)) {
                score -= 3;
                hasCommon = true;
                break;
            }
        }

        System.out.println("Score: " + score);

        String rating;
        if (score <= 3) rating = "Weak";
        else if (score <= 6) rating = "Medium";
        else if (score <= 9) rating = "Strong";
        else rating = "Very Strong";

        System.out.println(rating + " Password");

        if (score <= 6) {
            System.out.println("\nYour password is " + rating.toLowerCase() + ".");
            System.out.println("Here's a stronger suggestion based on your input:");
            System.out.println("  >> " + suggestPassword(password));
            System.out.println("(Tip: it adds uppercase, digits, and special characters.)");
        }
    }

    static String suggestPassword(String original) {
        String base = original.replaceAll("(?i)password|123456|qwerty|admin|hello", "");
        if (base.isEmpty()) base = "Secure";

        base = Character.toUpperCase(base.charAt(0)) + base.substring(1);

        StringBuilder sb = new StringBuilder(base);
        for (int i = 1; i < sb.length(); i++) {
            if ("aeiou".indexOf(sb.charAt(i)) >= 0 && i % 3 == 0)
                sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
        }

        String digits = String.valueOf(10 + rand.nextInt(90));
        char special = SPECIALS.charAt(rand.nextInt(SPECIALS.length()));
        sb.append(digits).append(special);

        if (sb.length() < 8) sb.append("Xk!").append(SPECIALS.charAt(rand.nextInt(SPECIALS.length())));
        if (sb.length() > 16) sb = new StringBuilder(sb.substring(0, 16));

        return sb.toString();
    }
}
