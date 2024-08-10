package cm.xenonbyte.farmbyte.common.domain.exception;

/**
 * @author bamk
 * @version 1.0
 * @since 10/08/2024
 */
public abstract class BaseDomainException extends RuntimeException {

    protected Object[] args;

    protected BaseDomainException(String message) {
        super(message);
    }

    protected BaseDomainException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}
