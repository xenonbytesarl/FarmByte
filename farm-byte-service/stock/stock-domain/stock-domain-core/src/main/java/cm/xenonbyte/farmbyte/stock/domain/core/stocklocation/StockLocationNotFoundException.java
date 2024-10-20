package cm.xenonbyte.farmbyte.stock.domain.core.stocklocation;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;
import cm.xenonbyte.farmbyte.stock.domain.core.constant.StockDomainConstant;

/**
 * @author bamk
 * @version 1.0
 * @since 19/10/2024
 */
public final class StockLocationNotFoundException extends BaseDomainNotFoundException {

    public StockLocationNotFoundException(Object[] args) {
        super(StockDomainConstant.STOCK_LOCATION_ID_NOT_FOUND, args);
    }
}
