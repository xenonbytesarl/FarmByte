package cm.xenonbyte.farmbyte.catalog.domain.core.constant;

/**
 * @author bamk
 * @version 1.0
 * @since 11/08/2024
 */
public final class CatalogDomainCoreConstant {
    public static final String UOM_REFERENCE_CONFLICT_CATEGORY = "UomReferenceConflictException.1";
    public static final String UOM_NAME_CONFLICT_EXCEPTION = "UomNameConflictException.1";
    public static final String RATIO_IS_REQUIRED_WHEN_UOM_TYPE_IS_REFERENCE = "Uom.1";
    public static final String UOM_RATIO_MUST_BE_GREATER_THANT_ONE_WHEN_UOM_TYPE_IS_GREATER = "Uom.2";
    public static final String UOM_RATIO_MUST_BE_LOWER_THANT_ONE_WHEN_UOM_TYPE_IS_LOWER = "Uom.3";
    public static final String RATION_CAN_NOT_BE_NULL = "Ration.1";

    public static final String UOM_CATEGORY_NAME_CONFLICT_EXCEPTION = "UomCategoryNameConflictException.1";
    public static final String UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION = "UomParentCategoryNotFoundException.1";
    public static final String NAME_UOM_CATEGORY_IS_REQUIRED = "ParentUomCategory.1";
    public static final String PARENT_UOM_CATEGORY_ID_IS_REQUIRED = "ParentUomCategory.2";

    public static final String PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION = "ProductCategoryNameConflictException.1";
    public static final String PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION = "ParentProductCategoryNotFoundException.1";
    public static final String PRODUCT_CATEGORY_NAME_IS_REQUIRED = "ProductCategory.1";
    public static final String PRODUCT_CATEGORY_PARENT_ID_IS_REQUIRED = "ParentProductCategory.2";
}
