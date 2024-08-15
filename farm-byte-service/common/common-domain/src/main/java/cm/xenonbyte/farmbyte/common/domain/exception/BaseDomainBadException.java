package cm.xenonbyte.farmbyte.common.domain.exception;

/**
 * @author bamk
 * @version 1.0
 * @since 10/08/2024
 */
public abstract class BaseDomainBadException extends RuntimeException {

    protected Object[] args;

    protected BaseDomainBadException(String message) {
        super(message);
    }

    protected BaseDomainBadException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}
