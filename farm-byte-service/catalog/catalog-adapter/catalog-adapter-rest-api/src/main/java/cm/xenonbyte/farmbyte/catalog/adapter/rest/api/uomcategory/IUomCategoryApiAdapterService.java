package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uomcategory;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import jakarta.annotation.Nonnull;

/**
* @author bamk
* @version 1.0
* @since 15/08/2024
*/public interface IUomCategoryApiAdapterService {
    @Nonnull CreateUomCategoryViewResponse createUomCategory(@Nonnull CreateUomCategoryViewRequest createUomCategoryViewRequest);
}
