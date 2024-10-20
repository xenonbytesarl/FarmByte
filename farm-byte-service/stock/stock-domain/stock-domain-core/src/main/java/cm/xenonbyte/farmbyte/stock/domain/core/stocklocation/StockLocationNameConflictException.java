package cm.xenonbyte.farmbyte.stock.domain.core.stocklocation;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;
import cm.xenonbyte.farmbyte.stock.domain.core.constant.StockDomainConstant;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class StockLocationNameConflictException extends BaseDomainConflictException {

    public StockLocationNameConflictException(Object[] args) {
        super(StockDomainConstant.STOCK_LOCATION_NAME_CONFLICT, args);
    }
}
