package cm.xenonbyte.farmbyte.common.adapter.file.storage;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainBadException;

/**
 * @author bamk
 * @version 1.0
 * @since 27/08/2024
 */
public final class StorageBadException extends BaseDomainBadException {
    private static final String SAVE_FILE_BAD_EXCEPTION = "StorageBadException.1";

    public StorageBadException(Object[] args) {
        super(SAVE_FILE_BAD_EXCEPTION, args);
    }

}
