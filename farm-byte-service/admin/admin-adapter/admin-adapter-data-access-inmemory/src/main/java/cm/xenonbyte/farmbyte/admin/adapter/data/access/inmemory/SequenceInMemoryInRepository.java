package cm.xenonbyte.farmbyte.admin.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.admin.domain.core.Sequence;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceId;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceRepository;
import cm.xenonbyte.farmbyte.admin.domain.core.vo.Code;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Comparator;
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

    @Override
    public PageInfo<Sequence> findAll(Integer page, Integer size, String attribute, Direction direction) {
        PageInfo<Sequence> sequencePageInfo = new PageInfo<>();
        Comparator<Sequence> comparing = Comparator.comparing((Sequence sequence) -> sequence.getName().getText().getValue());
        return sequencePageInfo.with(
                page,
                size,
                sequences.values().stream()
                    .sorted(Direction.ASC.equals(direction)? comparing: comparing.reversed())
                    .toList()
        );
    }

    @Override
    public PageInfo<Sequence> search(Integer page, Integer size, String attribute, @Nonnull Direction direction, Keyword keyword) {
        PageInfo<Sequence> sequencePageInfo = new PageInfo<>();
        Comparator<Sequence> comparing = Comparator.comparing((Sequence sequence) -> sequence.getName().getText().getValue());
        return sequencePageInfo.with(
                page,
                size,
                sequences.values().stream()
                        .filter(sequence -> sequence.getName().getText().getValue().toLowerCase().contains(keyword.getText().getValue().toLowerCase()))
                        .sorted(Direction.ASC.equals(direction)? comparing: comparing.reversed())
                        .toList()
        );
    }
}
