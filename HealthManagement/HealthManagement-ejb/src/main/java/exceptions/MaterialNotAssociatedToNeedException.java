package exceptions;

public class MaterialNotAssociatedToNeedException extends Exception {

    public MaterialNotAssociatedToNeedException() {
    }

    public MaterialNotAssociatedToNeedException(String msg) {
        super(msg);
    }
}
