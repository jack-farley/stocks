package marketdata;

public class SecurityNotFoundException extends Exception {

    public SecurityNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
