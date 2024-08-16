package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.productcategory;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.ProductCategoriesApi;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.ApiSuccessResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
@RestController
public class ProductCategoryApiRest implements ProductCategoriesApi {

    public static final String PRODUCT_CATEGORY_CREATED_SUCCESSFULLY = "ProductCategoryApiRest.1";

    private final IProductCategoryApiAdapterService productCategoryApiAdapterService;

    public ProductCategoryApiRest(final @Nonnull IProductCategoryApiAdapterService productCategoryApiAdapterService) {
        this.productCategoryApiAdapterService = Objects.requireNonNull(productCategoryApiAdapterService);
    }

    @Override
    public ResponseEntity<ApiSuccessResponse> createProductCategory(String acceptLanguage, CreateProductCategoryViewRequest createProductCategoryViewRequest) {
        CreateProductCategoryViewResponse createProductCategoryViewResponse =
                productCategoryApiAdapterService.createProductCategory(createProductCategoryViewRequest);
        return ResponseEntity.status(CREATED).body(
                new ApiSuccessResponse()
                        .code(CREATED.value())
                        .status(CREATED.name())
                        .timestamp(ZonedDateTime.now().toString())
                        .success(true)
                        .message(MessageUtil.getMessage(PRODUCT_CATEGORY_CREATED_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(Map.of(BODY, createProductCategoryViewResponse))
        );
    }
}
