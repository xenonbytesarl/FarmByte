package cm.xenonbyte.farmbyte.common.adapter.api.handler;

import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
@Slf4j
public abstract class CommonResponseEntityExceptionHandler {

    protected static final String PATH_URI_REPLACE = "uri=";

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            WebRequest request) {

        List<ValidationError> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errors.add(
                        ValidationError.builder()
                                .field(fieldError.getField())
                                .message(MessageUtil.getMessage(
                                    Objects.requireNonNull(String.format(
                                        "%s.%s.%s", StringUtils.capitalize(fieldError.getObjectName()), fieldError.getField(), fieldError.getCode())
                                ), ""))
                                .build()
                ));
        log.error("", exception);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ApiErrorResponse.builder()
                                .timestamp(now().toString())
                                .code(BAD_REQUEST.value())
                                .status(BAD_REQUEST.name())
                                .success(false)
                                .reason(MessageUtil.getMessage("CommonResponseEntityExceptionHandler.1", ""))
                                .path(request.getDescription(false).replace(PATH_URI_REPLACE, ""))
                                .error(errors)
                                .build()
                );
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<ApiErrorResponse> handleIllegalArgumentException
            (RuntimeException exception, WebRequest request) {
        log.error("", exception);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ApiErrorResponse.builder()
                                .timestamp(now().toString())
                                .code(BAD_REQUEST.value())
                                .status(BAD_REQUEST.name())
                                .success(false)
                                .reason(MessageUtil.getMessage(exception.getLocalizedMessage(), ""))
                                .path(request.getDescription(false).replace(PATH_URI_REPLACE, ""))
                                .build()
                );
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ApiErrorResponse> handleException(RuntimeException exception, WebRequest request) {
        log.error("", exception);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ApiErrorResponse.builder()
                                .timestamp(now().toString())
                                .code(INTERNAL_SERVER_ERROR.value())
                                .status(INTERNAL_SERVER_ERROR.name())
                                .success(false)
                                .reason(MessageUtil.getMessage("CommonResponseEntityExceptionHandler.2", ""))
                                .path(request.getDescription(false).replace(PATH_URI_REPLACE, ""))
                                .build()
                );
    }

}
