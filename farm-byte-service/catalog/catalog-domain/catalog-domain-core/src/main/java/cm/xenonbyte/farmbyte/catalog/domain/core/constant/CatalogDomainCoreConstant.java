package cm.xenonbyte.farmbyte.catalog.domain.core.constant;

/**
 * @author bamk
 * @version 1.0
 * @since 11/08/2024
 */
public final class CatalogDomainCoreConstant {
    public static final String UOM_REFERENCE_DUPLICATE_IN_SAME_CATEGORY = "UomReferenceDuplicateException.1";
    public static final String UOM_NAME_DUPLICATE_EXCEPTION = "UomNameDuplicateException.1";
    public static final String RATIO_IS_REQUIRED_WHEN_UOM_TYPE_IS_REFERENCE = "Uom.1";
    public static final String UOM_RATIO_MUST_BE_GREATER_THANT_ONE_WHEN_UOM_TYPE_IS_GREATER = "Uom.2";
    public static final String UOM_RATIO_MUST_BE_LOWER_THANT_ONE_WHEN_UOM_TYPE_IS_LOWER = "Uom.3";
    public static final String RATION_CAN_NOT_BE_NULL = "Ration.1";

    public static final String UOM_CATEGORY_NAME_DUPLICATE_EXCEPTION = "UomCategoryNameDuplicateException.1";
    public static final String UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION = "UomParentCategoryNotFoundException.1";
    public static final String NAME_UOM_CATEGORY_IS_REQUIRED = "ParentUomCategory.1";
    public static final String PARENT_UOM_CATEGORY_ID_IS_REQUIRED = "ParentUomCategory.2";
}
