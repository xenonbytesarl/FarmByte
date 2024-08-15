package cm.xenonbyte.farmbyte.catalog.adapter.rest.api;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.ApiErrorResponse;
import cm.xenonbyte.farmbyte.common.adapter.api.handler.CommonResponseEntityExceptionHandler;
import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainBadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.PATH_URI_REPLACE;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@Slf4j
@RestControllerAdvice
public final class CatalogResponseEntityExceptionHandler extends CommonResponseEntityExceptionHandler {


    @ExceptionHandler({BaseDomainBadException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequestException
            (BaseDomainBadException exception, WebRequest request) {
        log.error("", exception);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        new ApiErrorResponse()
                                .timestamp(now().toString())
                                .code(BAD_REQUEST.value())
                                .status(BAD_REQUEST.name())
                                .success(false)
                                .reason(MessageUtil.getMessage(exception.getLocalizedMessage(), MessageUtil.toStringArray(exception.getArgs()) ))
                                .path(request.getDescription(false).replace(PATH_URI_REPLACE, ""))

                );
    }
}
