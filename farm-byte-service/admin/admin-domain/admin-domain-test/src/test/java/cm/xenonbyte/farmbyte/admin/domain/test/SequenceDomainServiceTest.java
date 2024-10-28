package cm.xenonbyte.farmbyte.admin.domain.test;

import cm.xenonbyte.farmbyte.admin.adapter.data.access.inmemory.SequenceInMemoryInRepository;
import cm.xenonbyte.farmbyte.admin.domain.core.Sequence;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceCodeConflictException;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceCodeNotFoundException;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceDomainService;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceId;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceNameConflictException;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceNotFoundException;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceRepository;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceService;
import cm.xenonbyte.farmbyte.admin.domain.core.vo.Code;
import cm.xenonbyte.farmbyte.admin.domain.core.vo.Prefix;
import cm.xenonbyte.farmbyte.common.domain.exception.BaseDomainConflictException;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.admin.domain.core.Sequence.PATTERN_MONTH;
import static cm.xenonbyte.farmbyte.admin.domain.core.Sequence.PATTERN_YEAR_WITH_CENTURY;
import static cm.xenonbyte.farmbyte.admin.domain.core.SequenceCodeConflictException.SEQUENCE_CODE_CONFLICT;
import static cm.xenonbyte.farmbyte.admin.domain.core.SequenceCodeNotFoundException.SEQUENCE_CODE_NOT_FOUND;
import static cm.xenonbyte.farmbyte.admin.domain.core.SequenceNameConflictException.SEQUENCE_NAME_CONFLICT;
import static cm.xenonbyte.farmbyte.admin.domain.core.SequenceNotFoundException.SEQUENCE_ID_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public final class SequenceDomainServiceTest {

    private SequenceService sequenceService;
    SequenceRepository sequenceRepository;
    private Sequence sequence;
    private SequenceId sequenceId;

    @BeforeEach
    void setUp() {
        sequenceRepository = new SequenceInMemoryInRepository();
        sequenceService = new SequenceDomainService(sequenceRepository);
        sequenceId = new SequenceId(UUID.fromString("0192d529-d95e-7291-8ad6-efe6884bcae0"));
        sequence = sequenceRepository.save(
                Sequence.builder()
                        .id(sequenceId)
                        .name(Name.of(Text.of("Sequence Transfer Delivery")))
                        .code(Code.of(Text.of("TRANSFER_DELIVERY")))
                        .prefix(Prefix.of(Text.of(String.format("%s.%s.%s", "IN", PATTERN_YEAR_WITH_CENTURY, PATTERN_MONTH))))
                        .build()
        );
    }

    @Nested
    class CreateSequenceDomainServiceTest {
        @Test
        void should_create_sequence() {
            //Given
            Sequence sequence = Sequence.builder()
                    .name(Name.of(Text.of("Sequence Transfer Receipt")))
                    .code(Code.of(Text.of("TRANSFER_RECEIPT")))
                    .prefix(Prefix.of(Text.of(String.format("%s.%s.%s", "IN", PATTERN_YEAR_WITH_CENTURY, PATTERN_MONTH))))
                    .build();
            //Act
            Sequence result = sequenceService.createSequence(sequence);
            //Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isNotNull();
            assertThat(result.getActive()).isNotNull();
            assertThat(result.getActive().getValue()).isTrue();
            assertThat(result.getSize()).isNotNull();
            assertThat(result.getNext()).isNotNull();
            assertThat(result.getStep()).isNotNull();
        }

        static Stream<Arguments> baseSequenceMethodSource() {
            return Stream.of(
                    Arguments.of(
                        Name.of(Text.of("Sequence Transfer Delivery")),
                        Code.of(Text.of("FAKE_TRANSFER_DELIVERY")),
                        SequenceNameConflictException.class,
                        SEQUENCE_NAME_CONFLICT
                    ),
                    Arguments.of(
                            Name.of(Text.of("Fake Sequence Transfer Delivery")),
                            Code.of(Text.of("TRANSFER_DELIVERY")),
                            SequenceCodeConflictException.class,
                            SEQUENCE_CODE_CONFLICT
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("baseSequenceMethodSource")
        void should_fail_when_create_sequence_with_duplicate_name(
            Name name,
            Code code,
            Class<? extends BaseDomainConflictException>  exceptionClass,
            String exceptionMessage
        ) {
            //Given
            Sequence badSequence = Sequence.builder()
                    .name(name)
                    .code(code)
                    .prefix(Prefix.of(Text.of(String.format("%s.%s.%s", "IN", PATTERN_YEAR_WITH_CENTURY, PATTERN_MONTH))))
                    .build();

            assertThatThrownBy(() -> sequenceService.createSequence(badSequence))
                    .isInstanceOf(exceptionClass)
                    .hasMessage(exceptionMessage);
        }

    }

    @Nested
    class FindSequenceByCodeDomainServiceTest {

        @Test
        void should_success_when_find_sequence_by_code() {
            //Given
            Code code = Code.of(Text.of("TRANSFER_DELIVERY"));
            //Act
            Sequence result = sequenceService.findSequenceByCode(code);
            //Then
            assertThat(result).isNotNull().isEqualTo(sequence);
        }

        @Test
        void should_fail_when_find_sequence_by_code() {
            //Given
            Code code = Code.of(Text.of("FAKE_TRANSFER_DELIVERY"));
            //Act + Then
            assertThatThrownBy(() -> sequenceService.findSequenceByCode(code))
                .isInstanceOf(SequenceCodeNotFoundException.class)
                .hasMessage(SEQUENCE_CODE_NOT_FOUND);
        }
    }

    @Nested
    class FindSequenceByIdDomainServiceTest {

        @Test
        void should_success_when_find_sequence_by_id() {
            //Given Act
            Sequence result = sequenceService.findSequenceById(sequenceId);
            //Then
            assertThat(result).isNotNull();
        }

        @Test
        void should_fail_when_find_sequence_by_code() {
            //Given
            SequenceId fakeSequenceId = new SequenceId(UUID.fromString("0192d536-e486-7efb-b384-8aaa8e2e838e"));//Act + Then
            assertThatThrownBy(() -> sequenceService.findSequenceById(fakeSequenceId))
                    .isInstanceOf(SequenceNotFoundException.class)
                    .hasMessage(SEQUENCE_ID_NOT_FOUND);
        }
    }

    @Nested
    class FindSequencesDomainServiceTest {
        @BeforeEach
        void setUp() {
            sequenceRepository.save(
                Sequence.builder()
                    .id(new SequenceId(UUID.fromString("0192d553-e5c2-766c-8c6e-9bd9f94facdb")))
                    .name(Name.of(Text.of("Sequence Transfer Receipt")))
                    .code(Code.of(Text.of("TRANSFER_RECEIPT")))
                    .prefix(Prefix.of(Text.of(String.format("%s.%s.%s", "IN", PATTERN_YEAR_WITH_CENTURY, PATTERN_MONTH))))
                    .build()
            );
            sequenceRepository.save(
                Sequence.builder()
                    .id(new SequenceId(UUID.fromString("0192d554-0766-78a7-ae07-426c20a1ef7d")))
                    .name(Name.of(Text.of("Sequence Transfer Internal")))
                    .code(Code.of(Text.of("TRANSFER_INTERNAL")))
                    .prefix(Prefix.of(Text.of(String.format("%s.%s.%s", "IN", PATTERN_YEAR_WITH_CENTURY, PATTERN_MONTH))))
                    .build()
            );
            sequenceRepository.save(
                Sequence.builder()
                    .id(new SequenceId(UUID.fromString("0192d554-da0b-7562-adf7-33837b77d5e6")))
                    .name(Name.of(Text.of("Sequence Inventory")))
                    .code(Code.of(Text.of("INVENTORY")))
                    .prefix(Prefix.of(Text.of(String.format("%s.%s.%s", "IN", PATTERN_YEAR_WITH_CENTURY, PATTERN_MONTH))))
                    .build()
            );
        }

        @Test
        void should_success_when_find_sequences() {
            //Given
            Integer page = 0;
            Integer size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;

            //Act
            PageInfo<Sequence> result = sequenceService.findSequences(page, size, attribute, direction);

            //Then
            assertThat(result).isNotNull();
            assertThat(result.getPageSize()).isPositive();
            assertThat(result.getElements().size()).isPositive();
            assertThat(result.getTotalElements()).isPositive();
            assertThat(result.getTotalPages()).isPositive();
        }
    }

    @Nested
    class SearchSequencesDomainServiceTest {
        @BeforeEach
        void setUp() {
            sequenceRepository.save(
                    Sequence.builder()
                            .id(new SequenceId(UUID.fromString("0192d553-e5c2-766c-8c6e-9bd9f94facdb")))
                            .name(Name.of(Text.of("Sequence Transfer Receipt")))
                            .code(Code.of(Text.of("TRANSFER_RECEIPT")))
                            .prefix(Prefix.of(Text.of(String.format("%s.%s.%s", "IN", PATTERN_YEAR_WITH_CENTURY, PATTERN_MONTH))))
                            .build()
            );
            sequenceRepository.save(
                    Sequence.builder()
                            .id(new SequenceId(UUID.fromString("0192d554-0766-78a7-ae07-426c20a1ef7d")))
                            .name(Name.of(Text.of("Sequence Transfer Internal")))
                            .code(Code.of(Text.of("TRANSFER_INTERNAL")))
                            .prefix(Prefix.of(Text.of(String.format("%s.%s.%s", "IN", PATTERN_YEAR_WITH_CENTURY, PATTERN_MONTH))))
                            .build()
            );
            sequenceRepository.save(
                    Sequence.builder()
                            .id(new SequenceId(UUID.fromString("0192d554-da0b-7562-adf7-33837b77d5e6")))
                            .name(Name.of(Text.of("Sequence Inventory")))
                            .code(Code.of(Text.of("INVENTORY")))
                            .prefix(Prefix.of(Text.of(String.format("%s.%s.%s", "IN", PATTERN_YEAR_WITH_CENTURY, PATTERN_MONTH))))
                            .build()
            );
        }

        @Test
        void should_success_when_search_sequences_good_keyword() {
            //Given
            Integer page = 0;
            Integer size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("In"));

            //Act
            PageInfo<Sequence> result = sequenceService.searchSequences(page, size, attribute, direction, keyword);

            //Then
            assertThat(result).isNotNull();
            assertThat(result.getPageSize()).isPositive();
            assertThat(result.getElements().size()).isPositive();
            assertThat(result.getTotalElements()).isPositive();
            assertThat(result.getTotalPages()).isPositive();
        }

        @Test
        void should_empty_when_search_sequences_with_bad_keyword() {
            //Given
            Integer page = 0;
            Integer size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("Indcdcsdcds"));

            //Act
            PageInfo<Sequence> result = sequenceService.searchSequences(page, size, attribute, direction, keyword);

            //Then
            assertThat(result).isNotNull();
            assertThat(result.getPageSize()).isZero();
            assertThat(result.getElements().size()).isZero();
            assertThat(result.getTotalElements()).isZero();
            assertThat(result.getTotalPages()).isZero();
        }
    }
}
