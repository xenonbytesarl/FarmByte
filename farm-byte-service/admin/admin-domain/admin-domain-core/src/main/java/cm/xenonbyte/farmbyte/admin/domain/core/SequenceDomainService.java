package cm.xenonbyte.farmbyte.admin.domain.core;

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
