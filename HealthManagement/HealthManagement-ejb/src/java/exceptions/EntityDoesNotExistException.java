package exceptions;

public class EntityDoesNotExistException extends Exception {

    public EntityDoesNotExistException() {
    }

    public EntityDoesNotExistException(String msg) {
        super(msg);
    }
}
