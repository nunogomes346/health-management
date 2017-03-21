package exceptions;

public class NeedNotAssociatedToPatientException extends Exception {

    public NeedNotAssociatedToPatientException() {
    }

    public NeedNotAssociatedToPatientException(String msg) {
        super(msg);
    }
}
