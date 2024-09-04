package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.ProductsApi;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductByIdViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsViewApiResponse;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author bamk
 * @version 1.0
 * @since 26/08/2024
 */
@RestController
public class ProductRestApi implements ProductsApi {

    public static final String PRODUCT_CREATED_SUCCESSFULLY = "ProductApiRest.1";

    private final ProductDomainServiceRestApiAdapter productDomainServiceApiAdapter;

    public ProductRestApi(@Nonnull ProductDomainServiceRestApiAdapter productDomainServiceApiAdapter) {
        this.productDomainServiceApiAdapter = Objects.requireNonNull(productDomainServiceApiAdapter);
    }

    @Override
    public ResponseEntity<CreateProductViewApiResponse> createProduct(String acceptLanguage, CreateProductViewRequest createProductViewRequest, MultipartFile image) throws Exception {
        return ResponseEntity.status(CREATED).body(
                new CreateProductViewApiResponse()
                        .code(CREATED.value())
                        .status(CREATED.name())
                        .timestamp(ZonedDateTime.now().toString())
                        .success(true)
                        .message(MessageUtil.getMessage(PRODUCT_CREATED_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(Map.of(BODY, productDomainServiceApiAdapter.createProduct(createProductViewRequest, image)))
        );
    }

    @Override
    public ResponseEntity<FindProductByIdViewApiResponse> findProductById(String acceptLanguage, UUID productId) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<FindProductsViewApiResponse> findProducts(String acceptLanguage, Integer page, Integer size, String attribute, String direction) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<SearchProductsViewApiResponse> searchProduct(String acceptLanguage, Integer page, Integer size, String attribute, String direction, String keyword) throws Exception {
        return null;
    }
}
