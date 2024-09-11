package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.SearchProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.UpdateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.UpdateProductCategoryViewResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public interface ProductCategoryServiceRestApiAdapter {
    @Nonnull @Valid CreateProductCategoryViewResponse createProductCategory(@Nonnull @Valid CreateProductCategoryViewRequest createProductCategoryViewRequest);

    @Nonnull @Valid FindProductCategoryByIdViewResponse findProductCategoryById(@Nonnull UUID productCategoryIdUUID);

    @Nonnull @Valid FindProductCategoriesPageInfoViewResponse findProductCategories(int page, int pageSize, String attribute, String direction);

    @Nonnull @Valid SearchProductCategoriesPageInfoViewResponse searchProductCategories(int page, int pageSize, String attribute, String direction, String keyword);

    @Nonnull @Valid UpdateProductCategoryViewResponse updateProductCategory(@Nonnull UUID productCategoryIdUUID, @Nonnull @Valid UpdateProductCategoryViewRequest updateProductCategoryViewRequest);
}
