package cm.xenonbyte.farmbyte;


import cm.xenonbyte.farmbyte.admin.adapter.data.access.jpa.SequenceJpaMapper;
import cm.xenonbyte.farmbyte.admin.adapter.data.access.jpa.SequenceJpaRepository;
import cm.xenonbyte.farmbyte.admin.adapter.data.access.jpa.SequenceJpaRepositoryAdapter;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(DatabaseSetupExtension.class)
@Import(JpaRepositoryAdapterTest.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {SequenceJpaMapper.class, SequenceJpaRepository.class})
class SequenceJpaRepositoryAdapterIT extends SequenceRepositoryTest {

    @Autowired
    private SequenceJpaRepository sequenceJpaRepository;
    @Autowired
    private SequenceJpaMapper sequenceJpaMapper;

    @BeforeEach
    void setUp() {
        sequenceRepository = new SequenceJpaRepositoryAdapter(sequenceJpaRepository, sequenceJpaMapper);
        code = Code.of(Text.of("TRANSFER_DELIVERY"));
        name = Name.of(Text.of("Internal Sequence"));
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
        sequenceId = new SequenceId(UUID.fromString("0192d9c6-779b-7469-8bf2-9ac052e55a85"));
    }
}
