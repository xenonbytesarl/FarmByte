package cm.xenonbyte.farmbyte.common.adapter.file.storage;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainBadException;

/**
 * @author bamk
 * @version 1.0
 * @since 27/08/2024
 */
public final class StorageBadException extends BaseDomainBadException {
    private static final String SAVE_FILE_BAD_EXCEPTION = "StorageBadException.1";
    public static final String CANNOT_STORE_FILE_OUTSIDE_CURRENT_DIRECTORY = "StorageBadException.2";

    public StorageBadException(Object[] args) {
        super(SAVE_FILE_BAD_EXCEPTION, args);
    }

    public StorageBadException(String message) {
        super(message);
    }
}
