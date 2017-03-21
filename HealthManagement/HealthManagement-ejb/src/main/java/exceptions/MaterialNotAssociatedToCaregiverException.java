package exceptions;

public class MaterialNotAssociatedToCaregiverException extends Exception {

    public MaterialNotAssociatedToCaregiverException() {
    }

    public MaterialNotAssociatedToCaregiverException(String msg) {
        super(msg);
    }
}
