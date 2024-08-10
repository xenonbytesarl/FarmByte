package cm.xenonbyte.farmbyte.common.adapter.api.handler;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.FIELD_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.common.adapter.api.constant.CommonAdapterRestApi.MESSAGES_IS_REQUIRED;

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
public class ValidationError{

    @NotBlank(message = FIELD_IS_REQUIRED)
    private String field;
    @NotBlank(message = MESSAGES_IS_REQUIRED)
    private String message;
}
