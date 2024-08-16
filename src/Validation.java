import java.util.Scanner;

public class Validation {
    public static final String BOLD_RED = "\u001B[1;31m";
    public static final String RESET = "\u001B[0m";
    public static Scanner scanner = new Scanner(System.in);

    public static String getValidInput(String prompt, String pattern, String errorMessage) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
            if(input.equals("-1"))return null;
            if (!input.matches(pattern)) {
                System.out.println(BOLD_RED + errorMessage + RESET);
            }
        } while (!input.matches(pattern));

        return input;
    }

    public static final String USERNAME_PATTERN = "^(?!\\d+$)[a-zA-Z0-9._-]{3,15}$";
    public static final String USERNAME_ERROR ="Invalid username. It should be 3-15 characters long, with no spaces, and can't be just numbers.";
    public static final String PASSWORD_PATTERN = "^.{8,}$";
    public static final String PASSWORD_ERROR = "Invalid password. It must be at least 8 characters long.";
    public static final String NAME_PATTERN = "^[a-zA-Z ]+$";
    public static final String NAME_ERROR = "Invalid name. It must contain only letters (A-Z)";
    public static final String IC_PATTERN =  "^(\\d{6}-\\d{2}-\\d{4}$)|(^[a-zA-Z0-9]{6,9})$";
    public static final String IC_ERROR = "Invalid ID. It must be in the format xxxxxx-xx-xxxx for IC or \n 6 to 9 alphanumeric characters for a passport.";
    public static final String PHONE_PATTERN = "^01\\d-\\d{7,8}$";
    public static final String PHONE_ERROR = "Invalid phone number. It must start with 01, followed by a -, and then 7 or 8 digits.";
    public static final String ADDRESS_PATTERN = "^.{1,255}$";
    public static final String ADDRESS_ERROR = "Invalid address. It must not exceed 255 characters";
}
