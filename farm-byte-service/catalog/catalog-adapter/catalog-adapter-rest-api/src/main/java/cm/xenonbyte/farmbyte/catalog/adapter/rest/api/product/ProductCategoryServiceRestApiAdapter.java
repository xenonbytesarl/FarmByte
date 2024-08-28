package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public interface ProductCategoryServiceRestApiAdapter {
    @Nonnull CreateProductCategoryViewResponse createProductCategory(@Nonnull CreateProductCategoryViewRequest createProductCategoryViewRequest);
}
