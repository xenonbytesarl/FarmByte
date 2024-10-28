package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.admin.domain.core.vo.Code;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public interface SequenceRepository {
    @Nonnull Sequence save(@Nonnull Sequence sequence);

    Boolean existsByName(@Nonnull Name name);

    Boolean existsByCode(@Nonnull Code code);
}
