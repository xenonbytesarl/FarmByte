package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uomcategory;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.UomCategoriesApi;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.ApiSuccessResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Objects;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@RestController
public class UomCategoryApiRest implements UomCategoriesApi {

    private final IUomCategoryApiAdapterService uomCategoryApiAdapterService;

    public static final String UOM_CATEGORY_CREATED_SUCCESSFULLY = "UomCategoryApiRest.1";

    public UomCategoryApiRest(final @Nonnull IUomCategoryApiAdapterService uomCategoryApiAdapterService) {
        this.uomCategoryApiAdapterService = Objects.requireNonNull(uomCategoryApiAdapterService);
    }

    @Override
    public ResponseEntity<ApiSuccessResponse> createUomCategory(String acceptLanguage, CreateUomCategoryViewRequest createUomCategoryViewRequest) {
        CreateUomCategoryViewResponse createUomCategoryViewResponse =
                uomCategoryApiAdapterService.createUomCategory(createUomCategoryViewRequest);
        return ResponseEntity.status(CREATED).body(
                new ApiSuccessResponse()
                        .code(CREATED.value())
                        .status(CREATED.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(UOM_CATEGORY_CREATED_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, createUomCategoryViewResponse))
        );
    }
}
