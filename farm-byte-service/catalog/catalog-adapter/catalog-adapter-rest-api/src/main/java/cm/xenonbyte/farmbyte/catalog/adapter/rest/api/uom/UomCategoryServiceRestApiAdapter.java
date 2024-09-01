package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.SearchUomCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;

import java.util.UUID;

/**
* @author bamk
* @version 1.0
* @since 15/08/2024
*/public interface UomCategoryServiceRestApiAdapter {
    @Nonnull @Valid CreateUomCategoryViewResponse createUomCategory(@Nonnull @Valid CreateUomCategoryViewRequest createUomCategoryViewRequest);

    @Nonnull @Valid FindUomCategoryByIdViewResponse findUomCategoryById(@Nonnull UUID uomCategoryId);

    @Nonnull @Valid FindUomCategoriesPageInfoViewResponse findUomCategories(int page, int size, String attribute, Direction direction);

    @Nonnull @Valid SearchUomCategoriesPageInfoViewResponse searchUomCategories(int page, int pageSize, String attribute, Direction direction, @Nonnull String keyword);
}
