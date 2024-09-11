package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.UpdateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.UpdateProductViewResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
public interface ProductServiceRestApiAdapter {
    @Nonnull
    CreateProductViewResponse createProduct(
            @Nonnull CreateProductViewRequest createProductViewRequest, @Nonnull MultipartFile image) throws IOException;

    @Nonnull @Valid FindProductByIdViewResponse findProductById(@Nonnull UUID productId);

    @Nonnull @Valid FindProductsPageInfoViewResponse findProducts(int page, int size, String attribute, String direction);

    @Nonnull @Valid SearchProductsPageInfoViewResponse searchProducts(int page, int size, String attribute, String direction, String keyword);

    @Nonnull @Valid UpdateProductViewResponse updateProduct(@Nonnull UUID productIdUUID, @Nonnull @Valid UpdateProductViewRequest updateProductViewRequest, @Nonnull MultipartFile multipartFile) throws IOException;
}
