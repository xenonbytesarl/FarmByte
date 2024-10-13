package cm.xenonbyte.farmbyte.common.domain.validation;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainBadException;

import java.io.Serializable;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class InvalidFieldBadException extends BaseDomainBadException {

    public static final String NOT_NULL_VALUE = "InvalidFieldBadException.1";
    public static final String IS_ONE_OF_VALUE = "InvalidFieldBadException.2";
    public static final String NOT_EMPTY_VALUE = "InvalidFieldBadException.3";

    public InvalidFieldBadException(String message, Serializable... args) {
        super(message, args);
    }

    public static InvalidFieldBadException forNullValue(Serializable... args) {
        return new InvalidFieldBadException(NOT_NULL_VALUE, args);
    }

    public static InvalidFieldBadException forIsOneOfValue(Serializable... args) {
        return new InvalidFieldBadException(IS_ONE_OF_VALUE, args);
    }

    public static InvalidFieldBadException forEmptyValue(Serializable... args) {
        return new InvalidFieldBadException(NOT_EMPTY_VALUE, args);
    }
}
