package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public interface SequenceRepository {
    @Nonnull Sequence save(@Nonnull Sequence sequence);

    Boolean existsByName(@Nonnull Name name);

    Boolean existsByCode(@Nonnull Code code);

    Optional<Sequence> findByCode(@Nonnull Code code);

    Optional<Sequence> findById(@Nonnull SequenceId sequenceId);

    PageInfo<Sequence> findAll(Integer page, Integer size, String attribute, @Nonnull Direction direction);

    PageInfo<Sequence> search(Integer page, Integer size, String attribute, @Nonnull Direction direction, Keyword keyword);
}
