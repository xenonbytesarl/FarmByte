package cm.xenonbyte.farmbyte.catalog.adapter.rest.api;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.ApiErrorResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.ValidationError;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomException;
import cm.xenonbyte.farmbyte.common.adapter.api.handler.CommonResponseEntityExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<ValidationError> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errors.add(
                        new ValidationError()
                                .field(fieldError.getField())
                                .message(fieldError.getDefaultMessage())
                ));
        log.error("", exception);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        new ApiErrorResponse()
                                .timestamp(now().toString())
                                .code(BAD_REQUEST.value())
                                .status(BAD_REQUEST.name())
                                .success(false)
                                .developerMessage(exception.getLocalizedMessage())
                                .path(request.getDescription(false).replace(PATH_URI_REPLACE, ""))
                                .error(errors)
                );
    }

    @ExceptionHandler({UomException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequestException
            (RuntimeException exception, WebRequest request) {
        log.error("", exception);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        new ApiErrorResponse()
                                .timestamp(now().toString())
                                .code(BAD_REQUEST.value())
                                .status(BAD_REQUEST.name())
                                .success(false)
                                .reason(exception.getLocalizedMessage())
                                .path(request.getDescription(false).replace(PATH_URI_REPLACE, ""))

                );
    }
}
