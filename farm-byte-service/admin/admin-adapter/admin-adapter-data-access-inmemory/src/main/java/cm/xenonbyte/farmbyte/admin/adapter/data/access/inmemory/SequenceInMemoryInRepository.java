package cm.xenonbyte.farmbyte.admin.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.admin.domain.core.Sequence;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceId;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceRepository;
import cm.xenonbyte.farmbyte.admin.domain.core.vo.Code;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public final class SequenceInMemoryInRepository implements SequenceRepository {

    private final Map<SequenceId, Sequence> sequences = new LinkedHashMap<>();

    @Nonnull
    @Override
    public Sequence save(@Nonnull Sequence sequence) {
        sequences.put(sequence.getId(), sequence);
        return sequence;
    }

    @Override
    public Boolean existsByName(@Nonnull Name name) {
        return sequences.values().stream()
                .anyMatch(sequence ->
                        sequence.getName().getText().getValue().equalsIgnoreCase(name.getText().getValue()));
    }

    @Override
    public Boolean existsByCode(@Nonnull Code code) {
        return sequences.values().stream()
                .anyMatch(sequence ->
                        sequence.getCode().text().getValue().equalsIgnoreCase(code.text().getValue()));
    }

    @Override
    public Optional<Sequence> findByCode(@Nonnull Code code) {
        return sequences.values().stream()
                .filter(sequence -> sequence.getCode().text().getValue().equalsIgnoreCase(code.text().getValue()))
                .findFirst();
    }

    @Override
    public Optional<Sequence> findById(@Nonnull SequenceId sequenceId) {
        return sequences.get(sequenceId) == null ? Optional.empty() : Optional.of(sequences.get(sequenceId));
    }
}
