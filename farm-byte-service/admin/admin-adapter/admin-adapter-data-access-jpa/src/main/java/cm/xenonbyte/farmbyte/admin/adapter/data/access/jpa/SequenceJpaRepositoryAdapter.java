package cm.xenonbyte.farmbyte.admin.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.admin.domain.core.Code;
import cm.xenonbyte.farmbyte.admin.domain.core.Sequence;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceId;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Hello world!
 *
 */
@Slf4j
@Service
public class SequenceJpaRepositoryAdapter implements SequenceRepository {
    private final SequenceJpaRepository sequenceJpaRepository;
    private final SequenceJpaMapper sequenceJpaMapper;

    public SequenceJpaRepositoryAdapter(
            @Nonnull SequenceJpaRepository sequenceJpaRepository,
            @Nonnull SequenceJpaMapper sequenceJpaMapper) {
        this.sequenceJpaRepository = Objects.requireNonNull(sequenceJpaRepository);
        this.sequenceJpaMapper = Objects.requireNonNull(sequenceJpaMapper);
    }

    @Nonnull
    @Override
    public Sequence save(@Nonnull Sequence sequence) {
        return sequenceJpaMapper.toSequence(
                sequenceJpaRepository.save(sequenceJpaMapper.toSequenceJpa(sequence)));
    }

    @Override
    public Boolean existsByName(@Nonnull Name name) {
        return sequenceJpaRepository.existsByNameIgnoreCase(name.getText().getValue());
    }

    @Override
    public Boolean existsByCode(@Nonnull Code code) {
        return sequenceJpaRepository.existsByCode(code.text().getValue());
    }

    @Override
    public Optional<Sequence> findByCode(@Nonnull Code code) {
        return sequenceJpaRepository.findByCode(code.text().getValue())
                .map(sequenceJpaMapper::toSequence);
    }

    @Override
    public Optional<Sequence> findById(@Nonnull SequenceId sequenceId) {
        return sequenceJpaRepository.findById(sequenceId.getValue())
                .map(sequenceJpaMapper::toSequence);
    }

    @Override
    public PageInfo<Sequence> findAll(Integer page, Integer size, String sortAttribute, @Nonnull Direction direction) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<SequenceJpa> sequenceJpaPage =
                sequenceJpaRepository.findAll(PageRequest.of(page, size, sortDirection, sortAttribute));
        return new PageInfo<>(
                sequenceJpaPage.isFirst(),
                sequenceJpaPage.isLast(),
                sequenceJpaPage.getSize(),
                sequenceJpaPage.getTotalElements(),
                sequenceJpaPage.getTotalPages(),
                sequenceJpaPage.getContent().stream().map(sequenceJpaMapper::toSequence).toList()
        );
    }

    @Override
    public PageInfo<Sequence> search(Integer page, Integer size, String sortAttribute, @Nonnull Direction direction, Keyword keyword) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<SequenceJpa> sequenceJpaPage =
                sequenceJpaRepository.search(PageRequest.of(page, size, sortDirection, sortAttribute), keyword.getText().getValue());
        return new PageInfo<>(
                sequenceJpaPage.isFirst(),
                sequenceJpaPage.isLast(),
                sequenceJpaPage.getSize(),
                sequenceJpaPage.getTotalElements(),
                sequenceJpaPage.getTotalPages(),
                sequenceJpaPage.getContent().stream().map(sequenceJpaMapper::toSequence).toList()
        );
    }
}
