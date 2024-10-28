package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.common.domain.vo.BaseId;
import jakarta.annotation.Nonnull;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public final class SequenceId extends BaseId<UUID> {
    public SequenceId(@Nonnull UUID value) {
        super(value);
    }
}
