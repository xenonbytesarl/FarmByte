package cm.xenonbyte.farmbyte.common.adapter.api.handler;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.CODE_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.PATH_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.STATUS_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.SUCCESS_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.TIMESTAMPS_IS_REQUIRED;

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

        @NotBlank(message = TIMESTAMPS_IS_REQUIRED)
        private String timestamp;
        @NotNull(message = CODE_IS_REQUIRED)
        private Integer code;
        @NotBlank(message = STATUS_IS_REQUIRED)
        private String status;
        @NotNull(message = SUCCESS_IS_REQUIRED)
        private Boolean success;
        private String reason;
        private String message;
        @NotBlank(message = PATH_IS_REQUIRED)
        private String path;
        private UUID correlationId;
        private List<ValidationError> error;
}
