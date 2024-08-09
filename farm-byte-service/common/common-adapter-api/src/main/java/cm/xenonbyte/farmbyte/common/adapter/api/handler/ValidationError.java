package cm.xenonbyte.farmbyte.common.adapter.api.handler;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

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
    @NotBlank(message = "message is required")
    private String message;
}
