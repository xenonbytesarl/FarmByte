package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
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

    PageInfo<Sequence> findSequences(Integer page, Integer size, String attribute, Direction direction);

    PageInfo<Sequence> searchSequences(Integer page, Integer size, String attribute, Direction direction, @Nonnull Keyword keyword);
}
