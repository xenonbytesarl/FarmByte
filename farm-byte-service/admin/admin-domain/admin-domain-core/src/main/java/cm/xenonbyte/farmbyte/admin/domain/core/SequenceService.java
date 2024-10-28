package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.admin.domain.core.vo.Code;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public interface SequenceService {
    @Nonnull Sequence createSequence(@Nonnull Sequence sequence);

    @Nonnull Sequence findSequenceByCode(@Nonnull Code code);

    @Nonnull Sequence findSequenceById(@Nonnull SequenceId sequenceId);
}
