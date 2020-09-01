package data.market;

public class SecurityNotFoundException extends Exception {

    public SecurityNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
