package cm.xenonbyte.farmbyte.admin.domain.core;

import cm.xenonbyte.farmbyte.admin.domain.core.vo.Code;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public final class SequenceDomainService implements SequenceService {
    private final SequenceRepository sequenceRepository;

    public SequenceDomainService(@Nonnull SequenceRepository sequenceRepository) {
        this.sequenceRepository = Objects.requireNonNull(sequenceRepository);
    }

    @Nonnull
    @Override
    public Sequence createSequence(@Nonnull Sequence sequence) {
        verifySequence(sequence);
        sequence.initializeWithDefaults();
        return sequenceRepository.save(sequence);
    }

    @Override
    public Sequence findSequenceByCode(@Nonnull Code code) {
        return sequenceRepository.findByCode(code).orElseThrow(
                () -> new SequenceCodeNotFoundException(new String[]{code.text().getValue()})
        );
    }

    @Nonnull
    @Override
    public Sequence findSequenceById(@Nonnull SequenceId sequenceId) {
        return sequenceRepository.findById(sequenceId).orElseThrow(
                () -> new SequenceNotFoundException(new String[]{sequenceId.getValue().toString()})
        );
    }

    @Override
    public PageInfo<Sequence> findSequences(Integer page, Integer size, String attribute, Direction direction) {
        return sequenceRepository.findAll(page, size, attribute, direction);
    }

    @Override
    public PageInfo<Sequence> searchSequences(Integer page, Integer size, String attribute, Direction direction, @Nonnull Keyword keyword) {
        return sequenceRepository.search(page, size, attribute, direction, keyword);
    }

    private void verifySequence(Sequence sequence) {
        verifyName(sequence);
        verifyCode(sequence);
    }

    private void verifyCode(Sequence sequence) {
        if(sequence.getId() == null && sequenceRepository.existsByCode(sequence.getCode())) {
            throw new SequenceCodeConflictException(new String[]{sequence.getCode().text().getValue()});
        }
    }

    private void verifyName(@Nonnull Sequence sequence) {
        if(sequence.getId() == null && sequenceRepository.existsByName(sequence.getName())) {
            throw new SequenceNameConflictException(new String[]{sequence.getName().getText().getValue()});
        }
    }
}
