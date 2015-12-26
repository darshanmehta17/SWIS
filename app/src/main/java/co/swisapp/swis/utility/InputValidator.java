package co.swisapp.swis.utility;


public class InputValidator {

    //Not Allowing objects of this class

    private InputValidator() {

    }

    public static boolean validate_email(String email) {
        return !(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean vaildate_pass(String password) {
        return !(password.isEmpty() || password.length() < 8 || password.length() > 16);
    }

    //TODO : Add UserName Authentication
    public static boolean validate_username(String username) {
        return !(username.isEmpty());
    }
}
