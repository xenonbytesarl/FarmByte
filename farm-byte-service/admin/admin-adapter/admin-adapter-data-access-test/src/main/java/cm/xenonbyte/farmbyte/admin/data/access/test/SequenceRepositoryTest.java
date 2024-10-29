package cm.xenonbyte.farmbyte.admin.data.access.test;

import cm.xenonbyte.farmbyte.admin.domain.core.Code;
import cm.xenonbyte.farmbyte.admin.domain.core.Sequence;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceId;
import cm.xenonbyte.farmbyte.admin.domain.core.SequenceRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bamk
 * @version 1.0
 * @since 29/10/2024
 */
public abstract class SequenceRepositoryTest {

    protected SequenceRepository sequenceRepository;
    protected Sequence sequence;
    protected SequenceId sequenceId;
    protected Code code;
    protected Name name;

    @Nested
    class CreateSequenceRepositoryTest {

        @Test
        void should_true_when_check_if_sequence_exists_by_code() {
            //Given + Act
            Boolean result = sequenceRepository.existsByCode(code);

            //Then
            assertThat(result).isTrue();
        }

        @Test
        void should_true_when_check_if_sequence_exists_by_name() {
            //Given + Act
            Boolean result = sequenceRepository.existsByName(name);

            //Then
            assertThat(result).isTrue();
        }

        @Test
        void should_false_when_check_if_sequence_exists_by_code() {
            //Given + Act
            Boolean result = sequenceRepository.existsByCode(Code.of(Text.of("FAKE_CODE")));

            //Then
            assertThat(result).isFalse();
        }

        @Test
        void should_false_when_check_if_sequence_exists_by_name() {
            //Given + Act
            Boolean result = sequenceRepository.existsByName(Name.of(Text.of("Fake Name")));

            //Then
            assertThat(result).isFalse();
        }

        @Test
        void should_not_empty_when_find_sequence_by_code() {
            //Given + Act
            Optional<Sequence> result = sequenceRepository.findByCode(code);

            //Then
            assertThat(result).isNotEmpty();
        }

        @Test
        void should_empty_when_find_sequence_by_code() {
            //Given + Act
            Optional<Sequence> result = sequenceRepository.findByCode(Code.of(Text.of("FAKE_CODE")));

            //Then
            assertThat(result).isEmpty();
        }

        @Test
        void should_save_sequence() {
            //Given + Act
            Sequence result = sequenceRepository.save(sequence);

            //Then
            assertThat(result).isNotNull().isEqualTo(sequence);
        }
    }

    @Nested
    class FindSequenceByIdRepositoryTest {
        @Test
        void should_not_empty_when_find_sequence_by_id() {
            //Given + Act
            Optional<Sequence> result = sequenceRepository.findById(sequenceId);
            //Then
            assertThat(result).isNotEmpty();
        }

        @Test
        void should_empty_when_find_sequence_by_id() {
            //Given + Act
            Optional<Sequence> result = sequenceRepository.findById(new SequenceId(UUID.fromString("0192da00-926c-72e0-bee0-8903b0431a74")));
            //Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class FindSequencesByRepositoryTest {
        @Test
        void should_success_when_find_sequences() {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;

            //Act
            PageInfo<Sequence> result = sequenceRepository.findAll(page, size, attribute, direction);

            //Then
            assertThat(result.getElements().size()).isGreaterThan(0);
            assertThat(result.getFirst()).isTrue();
            assertThat(result.getLast()).isFalse();
            assertThat(result.getPageSize()).isEqualTo(size);
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchSequencesByKeywordRepositoryTest {
        @Test
        void should_success_when_search_sequence_by_keyword() {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;
            String keyword = "seq";

            //Act
            PageInfo<Sequence> result = sequenceRepository.search(page, size, attribute, direction, Keyword.of(Text.of(keyword)));

            //Then
            assertThat(result.getElements().size()).isGreaterThan(0);
            assertThat(result.getFirst()).isTrue();
            assertThat(result.getLast()).isFalse();
            assertThat(result.getPageSize()).isEqualTo(size);
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }

        @Test
        void should_empty_when_search_sequence_by_keyword() {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;
            String keyword = "www";

            //Act
            PageInfo<Sequence> result = sequenceRepository.search(page, size, attribute, direction, Keyword.of(Text.of(keyword)));

            //Then
            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
            assertThat(result.getElements().size()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
        }
    }
}
