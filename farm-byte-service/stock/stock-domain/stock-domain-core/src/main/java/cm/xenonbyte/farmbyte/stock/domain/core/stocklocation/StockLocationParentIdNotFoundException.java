package cm.xenonbyte.farmbyte.stock.domain.core.stocklocation;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;
import cm.xenonbyte.farmbyte.stock.domain.core.constant.StockDomainConstant;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class StockLocationParentIdNotFoundException extends BaseDomainNotFoundException {

    public StockLocationParentIdNotFoundException(Object[] args) {
        super(StockDomainConstant.STOCK_LOCATION_PARENT_ID_NOT_FOUND, args);
    }
}
