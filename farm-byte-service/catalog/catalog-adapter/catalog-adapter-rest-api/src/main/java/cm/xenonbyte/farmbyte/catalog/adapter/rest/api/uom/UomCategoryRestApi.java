package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.UomCategoriesApi;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoriesViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoryByIdViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.SearchUomCategoriesViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.UpdateUomCategoryViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.UpdateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@RestController
public class UomCategoryRestApi implements UomCategoriesApi {

    private final UomCategoryServiceRestApiAdapter uomCategoryApiAdapterService;

    public static final String UOM_CATEGORY_CREATED_SUCCESSFULLY = "UomCategoryApiRest.1";
    public static final String UOM_CATEGORY_FIND_SUCCESSFULLY = "UomCategoryApiRest.2";
    public static final String UOM_CATEGORIES_FIND_SUCCESSFULLY = "UomCategoryApiRest.3";
    public static final String UOM_CATEGORY_UPDATED_SUCCESSFULLY = "UomCategoryApiRest.4";

    public UomCategoryRestApi(final @Nonnull UomCategoryServiceRestApiAdapter uomCategoryApiAdapterService) {
        this.uomCategoryApiAdapterService = Objects.requireNonNull(uomCategoryApiAdapterService);
    }

    @Override
    public ResponseEntity<CreateUomCategoryViewApiResponse> createUomCategory(String acceptLanguage, CreateUomCategoryViewRequest createUomCategoryViewRequest) {
        CreateUomCategoryViewResponse createUomCategoryViewResponse =
                uomCategoryApiAdapterService.createUomCategory(createUomCategoryViewRequest);
        return ResponseEntity.status(CREATED).body(
                new CreateUomCategoryViewApiResponse()
                        .code(CREATED.value())
                        .status(CREATED.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(UOM_CATEGORY_CREATED_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, createUomCategoryViewResponse))
        );
    }

    @Override
    public ResponseEntity<FindUomCategoriesViewApiResponse> findUomCategories(String acceptLanguage, Integer page, Integer size, String attribute, String direction) {
        return ResponseEntity.status(OK).body(
                new FindUomCategoriesViewApiResponse()
                        .code(OK.value())
                        .status(OK.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(UOM_CATEGORIES_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(Map.of(BODY, uomCategoryApiAdapterService.findUomCategories(page, size, attribute, direction)))
        );
    }

    @Override
    public ResponseEntity<FindUomCategoryByIdViewApiResponse> findUomCategoryById(String acceptLanguage, UUID uomCategoryId) {
        return ResponseEntity.status(OK).body(
                new FindUomCategoryByIdViewApiResponse()
                        .code(OK.value())
                        .status(OK.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(UOM_CATEGORY_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(Map.of(BODY, uomCategoryApiAdapterService.findUomCategoryById(uomCategoryId)))
        );
    }

    @Override
    public ResponseEntity<SearchUomCategoriesViewApiResponse> searchUomCategory(String acceptLanguage, Integer page, Integer size, String attribute, String direction, String keyword) {
        return ResponseEntity.status(OK).body(
                new SearchUomCategoriesViewApiResponse()
                        .code(OK.value())
                        .status(OK.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(UOM_CATEGORIES_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(Map.of(BODY, uomCategoryApiAdapterService.searchUomCategories(page, size, attribute, direction, keyword)))
        );
    }

    @Override
    public ResponseEntity<UpdateUomCategoryViewApiResponse> updateUomCategoryById(String acceptLanguage, UUID uomCategoryId, UpdateUomCategoryViewRequest updateUomCategoryViewRequest) {
        return ResponseEntity.status(OK).body(
                new UpdateUomCategoryViewApiResponse()
                    .code(OK.value())
                    .status(OK.name())
                    .success(true)
                    .timestamp(ZonedDateTime.now().toString())
                    .message(MessageUtil.getMessage(UOM_CATEGORY_UPDATED_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                    .data(Map.of(BODY, uomCategoryApiAdapterService.updateUomCategory(uomCategoryId, updateUomCategoryViewRequest)))
        );
    }

}
