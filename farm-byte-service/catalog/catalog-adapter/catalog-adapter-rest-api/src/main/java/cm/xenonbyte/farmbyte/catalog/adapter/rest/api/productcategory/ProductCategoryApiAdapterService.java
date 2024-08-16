package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.productcategory;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ports.primary.IProductCategoryService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
@Slf4j
@Service
public final class ProductCategoryApiAdapterService implements IProductCategoryApiAdapterService {

    private final IProductCategoryService productCategoryService;
    private final ProductCategoryViewMapper productCategoryViewMapper;

    public ProductCategoryApiAdapterService(
            final @Nonnull IProductCategoryService productCategoryService,
            final @Nonnull ProductCategoryViewMapper productCategoryViewMapper) {
        this.productCategoryService = Objects.requireNonNull(productCategoryService);
        this.productCategoryViewMapper = Objects.requireNonNull(productCategoryViewMapper);
    }

    @Nonnull
    @Override
    public CreateProductCategoryViewResponse createProductCategory(@Nonnull CreateProductCategoryViewRequest createProductCategoryViewRequest) {

        return productCategoryViewMapper.toCreateProductCategoryViewResponse(
                productCategoryService.createProductCategory(
                        productCategoryViewMapper.toProductCategory(createProductCategoryViewRequest)
                )
        );

    }
}
