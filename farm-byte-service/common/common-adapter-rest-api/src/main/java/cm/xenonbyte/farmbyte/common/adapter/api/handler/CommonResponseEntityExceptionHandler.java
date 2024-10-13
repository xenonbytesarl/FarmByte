package cm.xenonbyte.farmbyte.common.adapter.api.handler;

import cm.xenonbyte.farmbyte.common.adapter.api.messages.MessageUtil;
import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainBadException;
import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;
import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainNotFoundException;
import cm.xenonbyte.farmbyte.common.domain.validation.InvalidFieldBadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.PATH_URI_REPLACE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.SUCCESS_FALSE;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.UNEXPECTED_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
@Slf4j
public abstract class CommonResponseEntityExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            WebRequest request, Locale locale) {

        List<ValidationError> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String message = String.format(
                            "%s.%s.%s", StringUtils.capitalize(fieldError.getObjectName()), fieldError.getField(), fieldError.getCode());
                    if(!errors.stream().anyMatch(error -> error.getField().equals(fieldError.getField()))) {
                        errors.add(
                            ValidationError.builder()
                                .field(fieldError.getField())
                                .message(MessageUtil.getMessage(Objects.requireNonNull(message), locale, ""))
                                .build()
                        );
                    }
                });
        log.error("", exception);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ApiErrorResponse.builder()
                                .timestamp(now().toString())
                                .code(BAD_REQUEST.value())
                                .status(BAD_REQUEST.name())
                                .success(SUCCESS_FALSE)
                                .reason(MessageUtil.getMessage(VALIDATION_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, locale, ""))
                                .path(request.getDescription(SUCCESS_FALSE).replace(PATH_URI_REPLACE,  ""))
                                .error(errors)
                                .build()
                );
    }

    @ExceptionHandler({IllegalArgumentException.class, InvalidFieldBadException.class, MissingRequestHeaderException.class})
    protected ResponseEntity<ApiErrorResponse> handleIllegalArgumentException
            (RuntimeException exception, WebRequest request, Locale locale) {
        log.error("", exception);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ApiErrorResponse.builder()
                                .timestamp(now().toString())
                                .code(BAD_REQUEST.value())
                                .status(BAD_REQUEST.name())
                                .success(SUCCESS_FALSE)
                                .reason(MessageUtil.getMessage(exception.getLocalizedMessage(), locale, ""))
                                .path(request.getDescription(SUCCESS_FALSE).replace(PATH_URI_REPLACE, ""))
                                .build()
                );
    }

    @ExceptionHandler({BaseDomainBadException.class})
    protected ResponseEntity<ApiErrorResponse> handleBaseDomainBadException
            (BaseDomainBadException exception, WebRequest request) {
        log.error("", exception);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ApiErrorResponse.builder()
                                .timestamp(now().toString())
                                .code(BAD_REQUEST.value())
                                .status(BAD_REQUEST.name())
                                .success(false)
                                .reason(MessageUtil.getMessage(exception.getLocalizedMessage(), MessageUtil.toStringArray(exception.getArgs()) ))
                                .path(request.getDescription(false).replace(PATH_URI_REPLACE, ""))
                                .build()

                );
    }

    @ExceptionHandler({BaseDomainNotFoundException.class})
    protected ResponseEntity<ApiErrorResponse> handleBaseDomainNotFoundException
            (BaseDomainNotFoundException exception, WebRequest request) {
        log.error("", exception);
        return ResponseEntity
                .status(NOT_FOUND)
                .body(
                        ApiErrorResponse.builder()
                                .timestamp(now().toString())
                                .code(NOT_FOUND.value())
                                .status(NOT_FOUND.name())
                                .success(false)
                                .reason(MessageUtil.getMessage(exception.getLocalizedMessage(), MessageUtil.toStringArray(exception.getArgs()) ))
                                .path(request.getDescription(false).replace(PATH_URI_REPLACE, ""))
                                .build()

                );
    }

    @ExceptionHandler({BaseDomainConflictException.class})
    protected ResponseEntity<ApiErrorResponse> handleBaseDomainConflictException
            (BaseDomainConflictException exception, WebRequest request) {
        log.error("", exception);
        return ResponseEntity
                .status(CONFLICT)
                .body(
                        ApiErrorResponse.builder()
                                .timestamp(now().toString())
                                .code(CONFLICT.value())
                                .status(CONFLICT.name())
                                .success(false)
                                .reason(MessageUtil.getMessage(exception.getLocalizedMessage(), MessageUtil.toStringArray(exception.getArgs()) ))
                                .path(request.getDescription(false).replace(PATH_URI_REPLACE, ""))
                                .build()

                );
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ApiErrorResponse> handleException(Exception exception, WebRequest request, Locale locale ) {
        log.error("", exception);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ApiErrorResponse.builder()
                                .timestamp(now().toString())
                                .code(INTERNAL_SERVER_ERROR.value())
                                .status(INTERNAL_SERVER_ERROR.name())
                                .success(SUCCESS_FALSE)
                                .correlationId(UUID.randomUUID())
                                .reason(MessageUtil.getMessage(UNEXPECTED_ERROR_OCCURRED_WHEN_PROCESSING_REQUEST, locale, ""))
                                .path(request.getDescription(SUCCESS_FALSE).replace(PATH_URI_REPLACE, ""))
                                .build()
                );
    }

}
