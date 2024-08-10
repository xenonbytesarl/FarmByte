package cm.xenonbyte.farmbyte.common.adapter.api.handler;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotBlank(message = "field is required")
    private String field;
    @NotBlank(message = "messages is required")
    private String message;
}
