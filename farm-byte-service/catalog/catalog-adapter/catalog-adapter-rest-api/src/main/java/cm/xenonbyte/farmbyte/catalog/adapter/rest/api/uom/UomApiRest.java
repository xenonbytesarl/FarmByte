package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.UomsApi;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.ApiSuccessResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Locale;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.BODY;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@RestController
@RequiredArgsConstructor
public class UomApiRest implements UomsApi {

    public static final String UOM_CREATED_SUCCESSFULLY = "UomApiRest.1";

    private final UomApiAdapterService uomApiAdapterService;

    @Override
    public ResponseEntity<ApiSuccessResponse> createUom(String acceptLanguage, CreateUomViewRequest createUomViewRequest) {
        CreateUomViewResponse createUomViewResponse = uomApiAdapterService.createUom(createUomViewRequest);
        return ResponseEntity.status(CREATED).body(
                new ApiSuccessResponse()
                        .timestamp(ZonedDateTime.now().toString())
                        .success(true)
                        .status(CREATED.name())
                        .code(CREATED.value())
                        .message(MessageUtil.getMessage(UOM_CREATED_SUCCESSFULLY, Locale.forLanguageTag(acceptLanguage), ""))
                        .data(of(BODY, createUomViewResponse))
        );
    }
}
