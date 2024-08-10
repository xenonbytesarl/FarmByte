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

    private final UomApiAdapterService uomApiAdapterService;

    @Override
    public ResponseEntity<ApiSuccessResponse> createUom(CreateUomViewRequest createUomViewRequest) {
        CreateUomViewResponse createUomViewResponse = uomApiAdapterService.createUom(createUomViewRequest);
        return ResponseEntity.status(CREATED).body(
                new ApiSuccessResponse()
                        .timestamp(ZonedDateTime.now().toString())
                        .success(true)
                        .status(CREATED.name())
                        .code(CREATED.value())
                        .message(MessageUtil.getMessage("UomApiRest.1", ""))
                        .data(of("body", createUomViewResponse))
        );
    }
}
