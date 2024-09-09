package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.UomsApi;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomByIdViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewApiResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewRequest;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@RestController
public class UomRestApi implements UomsApi {

    public static final String UOM_CREATED_SUCCESSFULLY = "UomApiRest.1";
    public static final String UOM_FIND_SUCCESSFULLY = "UomApiRest.2";
    public static final String UOMS_FIND_SUCCESSFULLY = "UomApiRest.3";
    public static final String UOM_UPDATED_SUCCESSFULLY = "UomApiRest.4";

    private final UomServiceRestApiAdapter uomApiAdapterService;

    public UomRestApi(final @Nonnull UomServiceRestApiAdapter uomApiAdapterService) {
        this.uomApiAdapterService = Objects.requireNonNull(uomApiAdapterService);
    }

    @Override
    public ResponseEntity<CreateUomViewApiResponse> createUom(String acceptLanguage, CreateUomViewRequest createUomViewRequest) {
        CreateUomViewResponse createUomViewResponse = uomApiAdapterService.createUom(createUomViewRequest);
        return ResponseEntity.status(CREATED).body(
                new CreateUomViewApiResponse()
                        .timestamp(ZonedDateTime.now().toString())
                        .success(true)
                        .status(CREATED.name())
                        .code(CREATED.value())
                        .message(MessageUtil.getMessage(UOM_CREATED_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, createUomViewResponse))
        );
    }

    @Override
    public ResponseEntity<FindUomByIdViewApiResponse> findUomById(String acceptLanguage, UUID uomId) {
        FindUomByIdViewResponse findUomByIdViewResponse = uomApiAdapterService.findUomById(uomId);
        return ResponseEntity.status(OK).body(
                new FindUomByIdViewApiResponse()
                        .timestamp(ZonedDateTime.now().toString())
                        .success(true)
                        .status(OK.name())
                        .code(OK.value())
                        .message(MessageUtil.getMessage(UOM_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, findUomByIdViewResponse))
        );
    }

    @Override
    public ResponseEntity<FindUomsViewApiResponse> findUoms(String acceptLanguage, Integer page, Integer size, String attribute, String direction) {
        FindUomsPageInfoViewResponse findUomsViewResponse = uomApiAdapterService.findUoms(page, size, attribute, direction);
        return ResponseEntity.status(OK).body(
                new FindUomsViewApiResponse()
                        .timestamp(ZonedDateTime.now().toString())
                        .success(true)
                        .status(OK.name())
                        .code(OK.value())
                        .message(MessageUtil.getMessage(UOMS_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, findUomsViewResponse))
        );
    }

    @Override
    public ResponseEntity<SearchUomsViewApiResponse> searchUom(String acceptLanguage, Integer page, Integer size, String attribute, String direction, String keyword) {
        SearchUomsPageInfoViewResponse searchUomsViewResponse = uomApiAdapterService.searchUoms(page, size, attribute, direction, keyword);
        return ResponseEntity.status(OK).body(
                new SearchUomsViewApiResponse()
                        .timestamp(ZonedDateTime.now().toString())
                        .success(true)
                        .status(OK.name())
                        .code(OK.value())
                        .message(MessageUtil.getMessage(UOMS_FIND_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, searchUomsViewResponse))
        );
    }

    @Override
    public ResponseEntity<UpdateUomViewApiResponse> updateUomById(String acceptLanguage, UUID uomId, UpdateUomViewRequest updateUomViewRequest) {
        return ResponseEntity.status(OK).body(
                new UpdateUomViewApiResponse()
                        .code(OK.value())
                        .status(OK.name())
                        .success(true)
                        .timestamp(ZonedDateTime.now().toString())
                        .message(MessageUtil.getMessage(UOM_UPDATED_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, uomApiAdapterService.updateUom(uomId, updateUomViewRequest)))
        );
    }
}
