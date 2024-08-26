package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainBadException;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION;

/**
 * @author bamk
 * @version 1.0
 * @since 21/08/2024
 */
public final class ProductStockAndPurchaseUomBadException extends BaseDomainBadException {

    public ProductStockAndPurchaseUomBadException(Object[] args) {
        super(PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION, args);
    }
}
