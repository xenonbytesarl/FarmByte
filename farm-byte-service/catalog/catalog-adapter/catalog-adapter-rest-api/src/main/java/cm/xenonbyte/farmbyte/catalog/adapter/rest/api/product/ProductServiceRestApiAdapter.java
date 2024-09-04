package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import jakarta.annotation.Nonnull;
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

    @Nonnull FindProductByIdViewResponse findProductById(@Nonnull UUID productId);

    @Nonnull FindProductsPageInfoViewResponse findProducts(int page, int size, String attribute, Direction direction);

    @Nonnull SearchProductsPageInfoViewResponse searchProducts(int page, int size, String attribute, Direction direction, String keyword);
}
