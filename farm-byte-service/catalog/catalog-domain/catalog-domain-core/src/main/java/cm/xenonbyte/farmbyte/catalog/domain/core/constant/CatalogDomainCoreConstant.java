package cm.xenonbyte.farmbyte.catalog.domain.core.constant;

/**
 * @author bamk
 * @version 1.0
 * @since 11/08/2024
 */
public final class CatalogDomainCoreConstant {
    public static final String UOM_REFERENCE_CONFLICT_CATEGORY = "UomReferenceConflictException.1";
    public static final String UOM_NAME_CONFLICT_EXCEPTION = "UomNameConflictException.1";
    public static final String UOM_RATIO_IS_REQUIRED_WHEN_TYPE_IS_REFERENCE = "Uom.1";
    public static final String UOM_RATIO_MUST_BE_GREATER_THANT_ONE_WHEN_UOM_TYPE_IS_GREATER = "Uom.2";
    public static final String UOM_RATIO_MUST_BE_LOWER_THANT_ONE_WHEN_UOM_TYPE_IS_LOWER = "Uom.3";
    public static final String UOM_NOT_FOUND_EXCEPTION = "UomNotFoundException.1";

    public static final String UOM_CATEGORY_NAME_CONFLICT_EXCEPTION = "UomCategoryNameConflictException.1";
    public static final String UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION = "UomParentCategoryNotFoundException.1";
    public static final String UOM_CATEGORY_NOT_FOUND_EXCEPTION = "UomCategoryNotFoundException.1";
    public static final String UOM_CATEGORY_NAME_IS_REQUIRED = "UomCategory.1";
    public static final String UOM_CATEGORY_ID_IS_REQUIRED = "UomCategory.2";

    public static final String PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION = "ProductCategoryNameConflictException.1";
    public static final String PRODUCT_PARENT_CATEGORY_NOT_FOUND_EXCEPTION = "ProductParentCategoryNotFoundException.1";
    public static final String PRODUCT_CATEGORY_NAME_IS_REQUIRED = "ProductCategory.1";
    public static final String PRODUCT_CATEGORY_PARENT_ID_IS_REQUIRED = "ProductCategory.2";

    public static final String PRODUCT_NAME_IS_REQUIRED = "Product.1";
    public static final String PRODUCT_CATEGORY_IS_REQUIRED = "Product.2";
    public static final String PRODUCT_TYPE_IS_REQUIRED = "Product.3";
    public static final String PRODUCT_STOCK_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK = "Product.4";
    public static final String PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK = "Product.5";
    public static final String PRODUCT_PURCHASE_PRICE_SHOULD_BE_GREATER_THAN_ZERO = "Product.6";
    public static final String PRODUCT_SALE_PRICE_SHOULD_BE_GREATER_THAN_ZERO = "Product.7";
    public static final String PRODUCT_NAME_CONFLICT_EXCEPTION = "ProductNameConflictException.1";
    public static final String PRODUCT_STOCK_AND_PURCHASE_UOM_BAD_EXCEPTION = "ProductStockAndPurchaseUomBadException.1";
    public static final String PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION="ProductCategoryNotFoundException.1";
    public static final String PRODUCT_UOM_NOT_FOUND_EXCEPTION="ProductUomNotFoundException.1";
    public static final String PRODUCT_NOT_FOUND_EXCEPTION = "ProductNotFoundException.1";
}
