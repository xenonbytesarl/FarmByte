package cm.xenonbyte.farmbyte.common.domain.exception;

/**
 * @author bamk
 * @version 1.0
 * @since 10/08/2024
 */
public abstract class BaseDomainNotFoundException extends RuntimeException {

    protected Object[] args;

    protected BaseDomainNotFoundException(String message) {
        super(message);
    }

    protected BaseDomainNotFoundException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}
