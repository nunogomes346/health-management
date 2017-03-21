package exceptions;

public class PatientNotInPatientsException extends Exception {

    public PatientNotInPatientsException() {
    }

    public PatientNotInPatientsException(String msg) {
        super(msg);
    }
}
