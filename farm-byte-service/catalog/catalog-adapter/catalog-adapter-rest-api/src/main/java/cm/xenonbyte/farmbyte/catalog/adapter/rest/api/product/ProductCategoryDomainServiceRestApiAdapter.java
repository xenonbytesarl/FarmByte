package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.SearchProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductCategoryService;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

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

    @Nonnull
    @Override
    public FindProductCategoryByIdViewResponse findProductCategoryById(UUID productCategoryIdUUID) {
        return productCategoryViewMapper.toFindProductCategoryViewResponse(
                productCategoryService.findProductCategoryById(new ProductCategoryId(productCategoryIdUUID))
        );
    }

    @Nonnull
    @Override
    public FindProductCategoriesPageInfoViewResponse findProductCategories(int page, int pageSize, String attribute, String direction) {
        return productCategoryViewMapper.toFindProductCategoriesPageInfoViewResponse(
                productCategoryService.findProductCategories(page, pageSize, attribute, Direction.valueOf(direction))
        );
    }

    @Nonnull
    @Override
    public SearchProductCategoriesPageInfoViewResponse searchProductCategories(int page, int pageSize, String attribute, String direction, @Nonnull String keyword) {
        return productCategoryViewMapper.toSearchProductCategoriesPageInfoViewResponse(
                productCategoryService.searchProductCategories(page, pageSize, attribute, Direction.valueOf(direction), Keyword.of(Text.of(keyword)))
        );
    }
}
