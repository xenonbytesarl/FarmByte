package cm.xenonbyte.farmbyte.common.adapter.file.storage;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;

/**
 * @author bamk
 * @version 1.0
 * @since 08/10/2024
 */
public class StorageNotFoundException extends BaseDomainNotFoundException {
    private static final String FILE_NOT_FOUND_EXCEPTION = "StorageNotFoundException.1";

    protected StorageNotFoundException(Object[] args) {
        super(FILE_NOT_FOUND_EXCEPTION, args);
    }
}
