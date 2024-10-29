package cm.xenonbyte.farmbyte.admin.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.admin.data.access.test.SequenceRepositoryTest;
import cm.xenonbyte.farmbyte.admin.domain.core.Code;
import cm.xenonbyte.farmbyte.admin.domain.core.Next;
import cm.xenonbyte.farmbyte.admin.domain.core.Prefix;
import cm.xenonbyte.farmbyte.admin.domain.core.Sequence;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceId;
import cm.xenonbyte.farmbyte.admin.domain.core.Size;
import cm.xenonbyte.farmbyte.admin.domain.core.Step;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 29/10/2024
 */
public final class SequenceInMemoryRepositoryTest extends SequenceRepositoryTest {

    @BeforeEach
    void setUp() {
        sequenceRepository = new SequenceInMemoryInRepository();
        code = Code.of(Text.of("TRANSFER_DELIVERY"));
        name = Name.of(Text.of("Internal Sequence"));
        sequenceRepository.save(
                Sequence.builder()
                        .id(new SequenceId(UUID.fromString("0192da18-6986-7393-821b-78122dc969c2")))
                        .name(Name.of(Text.of("Internal Sequence")))
                        .code(Code.of(Text.of("TRANSFER_INTERNAL")))
                        .step(Step.of(1L))
                        .size(Size.of(4L))
                        .next(Next.of(1L))
                        .prefix(Prefix.of(Text.of("PREFIX")))
                        .active(Active.with(true))
                        .build()
        );
        sequenceRepository.save(
                Sequence.builder()
                        .id(new SequenceId(UUID.fromString("0192da18-dd85-7985-85ee-51120a26e6b6")))
                        .name(Name.of(Text.of("Receipt Sequence")))
                        .code(Code.of(Text.of("TRANSFER_RECEIPT")))
                        .step(Step.of(1L))
                        .size(Size.of(4L))
                        .next(Next.of(1L))
                        .prefix(Prefix.of(Text.of("PREFIX")))
                        .active(Active.with(true))
                        .build()
        );
        sequenceRepository.save(
                Sequence.builder()
                        .id(new SequenceId(UUID.fromString("0192da19-1d62-747b-9963-e419102a72e7")))
                        .name(Name.of(Text.of("Delivery Sequence")))
                        .code(Code.of(Text.of("TRANSFER_DELIVERY")))
                        .step(Step.of(1L))
                        .size(Size.of(4L))
                        .next(Next.of(1L))
                        .prefix(Prefix.of(Text.of("PREFIX")))
                        .active(Active.with(true))
                        .build()
        );
        sequence = Sequence.builder()
                .id(new SequenceId(UUID.fromString("0192d9cf-e4bb-76a6-8cb2-e9e0172cde7b")))
                .name(Name.of(Text.of("Name test")))
                .code(Code.of(Text.of("CODE_TEST")))
                .step(Step.of(1L))
                .size(Size.of(4L))
                .next(Next.of(1L))
                .prefix(Prefix.of(Text.of("PREFIX")))
                .active(Active.with(true))
                .build();
        sequenceId = new SequenceId(UUID.fromString("0192da18-dd85-7985-85ee-51120a26e6b6"));
    }
}
