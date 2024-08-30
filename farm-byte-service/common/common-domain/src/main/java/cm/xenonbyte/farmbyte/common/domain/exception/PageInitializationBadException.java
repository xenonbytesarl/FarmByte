package cm.xenonbyte.farmbyte.common.domain.exception;

import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.PAGE_INVALID_CURRENT_PAGE;
import static cm.xenonbyte.farmbyte.common.domain.constant.CommonDomainConstant.PAGE_INVALID_SUB_LIST_INDEX;

/**
 * @author bamk
 * @version 1.0
 * @since 30/08/2024
 */
public final class PageInitializationBadException extends BaseDomainBadException {

    public PageInitializationBadException() {
        super(PAGE_INVALID_SUB_LIST_INDEX);
    }

    public PageInitializationBadException(Object[] args) {
        super(PAGE_INVALID_CURRENT_PAGE, args);
    }
}
