package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
public interface IProductApiAdapterService {
    @Nonnull CreateProductViewResponse createProduct(@Nonnull CreateProductViewRequest createProductViewRequest);
}
