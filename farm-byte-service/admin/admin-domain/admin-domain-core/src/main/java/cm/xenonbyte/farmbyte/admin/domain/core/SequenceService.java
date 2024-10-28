package cm.xenonbyte.farmbyte.admin.domain.core;

import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public interface SequenceService {
    @Nonnull Sequence createSequence(@Nonnull Sequence sequence);
}
