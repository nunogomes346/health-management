package exceptions;

public class NeedAssociatedToPatientException extends Exception {

    public NeedAssociatedToPatientException() {
    }

    public NeedAssociatedToPatientException(String msg) {
        super(msg);
    }
}
