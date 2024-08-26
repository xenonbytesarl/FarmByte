package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.IProductService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@Slf4j
@Service
public final class ProductApiAdapterService implements IProductApiAdapterService {

    private final IProductService productService;
    private final ProductViewMapper productViewMapper;

    public ProductApiAdapterService(@Nonnull IProductService productService, @Nonnull ProductViewMapper productViewMapper) {

        this.productService = Objects.requireNonNull(productService);
        this.productViewMapper = Objects.requireNonNull(productViewMapper);
    }

    @Nonnull
    @Override
    public CreateProductViewResponse createProduct(@Nonnull CreateProductViewRequest createProductViewRequest) {
        return productViewMapper.toCreateProductViewResponse(
                productService.createProduct(productViewMapper.toProduct(createProductViewRequest)));
    }
}
