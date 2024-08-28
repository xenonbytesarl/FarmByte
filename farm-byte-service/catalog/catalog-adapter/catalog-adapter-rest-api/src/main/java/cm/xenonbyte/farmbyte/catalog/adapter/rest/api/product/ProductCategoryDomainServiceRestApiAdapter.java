package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductCategoryService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
@Slf4j
@Service
public class ProductCategoryDomainServiceRestApiAdapter implements ProductCategoryServiceRestApiAdapter {

    private final ProductCategoryService productCategoryService;
    private final ProductCategoryViewMapper productCategoryViewMapper;

    public ProductCategoryDomainServiceRestApiAdapter(
            final @Nonnull ProductCategoryService productCategoryService,
            final @Nonnull ProductCategoryViewMapper productCategoryViewMapper) {
        this.productCategoryService = Objects.requireNonNull(productCategoryService);
        this.productCategoryViewMapper = Objects.requireNonNull(productCategoryViewMapper);
    }

    @Nonnull
    @Override
    @Transactional
    public CreateProductCategoryViewResponse createProductCategory(@Nonnull CreateProductCategoryViewRequest createProductCategoryViewRequest) {

        return productCategoryViewMapper.toCreateProductCategoryViewResponse(
                productCategoryService.createProductCategory(
                        productCategoryViewMapper.toProductCategory(createProductCategoryViewRequest)
                )
        );

    }
}
