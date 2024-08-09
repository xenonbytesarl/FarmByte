package cm.xenonbyte.farmbyte.common.adapter.api.handler;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
        @NotBlank(message = "timestamps is required")
        private String timestamp;
        @NotNull(message = "field is required")
        private Integer code;
        @NotBlank(message = "status is required")
        private String status;
        @NotNull(message = "success is required")
        private Boolean success;
        private String developerMessage;
        private String reason;
        private String message;
        @NotBlank(message = "path is required")
        private String path;
        private UUID correlationId;
        private List<ValidationError> error;
}
