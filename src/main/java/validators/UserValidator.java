package validators;

import entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {
    private static final List<String> errorMessages = new ArrayList<>();
    private static final String USERNAME = "^[A-Za-z][A-Za-z0-9_]{2,16}$";
    private static final String PASSWORD = "^(?=\\d*)(?=[a-z]*)(?=[A-Z]*)(?=[\\W]*).{2,16}$";
    private static final String EMAIL = "^[\\w-]{2,16}@([\\w-]{2,5}\\.)+[\\w-]{2,4}$";

    public static boolean isValid(User user) {
        return
                isUserNameValid(user.getUsername()) &&
                        isPasswordValid(user.getPassword()) &&
                        isEmailValid(user.getEmail());

    }

    private static boolean isUserNameValid(String username) {
        if (!username.matches(USERNAME)) {
            errorMessages.add("Incorrect username");
            return false;
        }
        return true;

    }

    public static boolean isPasswordValid(String password) {
        if (!password.matches(PASSWORD)) {
            errorMessages.add("Incorrect password");
            return false;
        }
        return true;
    }

    public static boolean isEmailValid(String email) {
        if (!email.matches(EMAIL)) {
            errorMessages.add("Incorrect email");
            return false;
        }
        return true;
    }
}
