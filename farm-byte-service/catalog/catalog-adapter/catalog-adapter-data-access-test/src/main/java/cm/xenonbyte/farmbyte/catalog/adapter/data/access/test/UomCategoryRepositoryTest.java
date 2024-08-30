package cm.xenonbyte.farmbyte.catalog.adapter.data.access.test;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Page;
import cm.xenonbyte.farmbyte.common.domain.vo.Sort;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public abstract class UomCategoryRepositoryTest {

    protected UomCategoryRepository uomCategoryRepository;
    protected Name name;
    protected UomCategoryId parentUomCategoryId;

    @Nested
    class CreateUomCategoryTest {

        @Test
        protected void should_return_false_when_category_with_given_name_not_exist() {
            //Act
            Boolean result = uomCategoryRepository.existsByName(Name.of(Text.of("Anything")));

            //Then
            assertThat(result).isFalse();

        }

        @Test
        protected void should_return_true_when_category_with_given_name_exist() {
            //Act
            Boolean result = uomCategoryRepository.existsByName(name);

            //Then
            assertThat(result).isTrue();

        }

        @Test
        protected void should_return_false_when_given_parent_uom_category_id_does_not_exist() {
            //Act
            Boolean result = uomCategoryRepository.existsById(new UomCategoryId(UUID.randomUUID()));

            //Then
            assertThat(result).isFalse();

        }

        @Test
        protected void should_return_true_when_given_parent_uom_category_id__exist() {
            //Act
            Boolean result = uomCategoryRepository.existsById(parentUomCategoryId);

            //Then
            assertThat(result).isTrue();

        }

        @Test
        protected void should_create_new_uom_category() {
            //Given
            Name volumeCategory = Name.of(Text.of("Volume"));
            UomCategory uomCategory = UomCategory.of(volumeCategory);
            uomCategory.initiate();

            //Act
            UomCategory createdUomCategory = uomCategoryRepository.save(uomCategory);
            Boolean result = uomCategoryRepository.existsByName(volumeCategory);

            //Then
            assertThat(createdUomCategory.getId()).isNotNull();
            assertThat(result).isTrue();

        }
    }

    @Nested
    class FindUomCategoryByIdTest {

        @Test
        void should_success_when_find_uom_category_by_existing_id() {
            //Given + Act
            Optional<UomCategory> result = uomCategoryRepository.findById(parentUomCategoryId);

            //Then
            assertThat(result.isPresent())
                    .isTrue();
        }

        @Test
        void should_fail_when_find_uom_category_by_non_existing_id() {
            //Given
            UomCategoryId uomCategoryId = new UomCategoryId(UUID.randomUUID());

            //Act
            Optional<UomCategory> result = uomCategoryRepository.findById(uomCategoryId);

            //Then
            assertThat(result.isEmpty())
                    .isTrue();
        }
    }

    @Nested
    class FindUomCategories {

        @Test
        void should_success_when_find_uom_categories() {
            //Given
            Integer page = 2;
            Integer size = 2;
            String sortAttribute = "name";
            Sort sortDirection = Sort.ASC;

            //Then
            Page<UomCategory> result = uomCategoryRepository.findAll(page, size, sortAttribute, sortDirection);

            assertThat(result.getTotalElements()).isEqualTo(5);
            assertThat(result.getTotalPages()).isEqualTo(3);
            assertThat(result.getContent().size()).isEqualTo(2);
            assertThat(result.getFirst()).isFalse();
            assertThat(result.getLast()).isFalse();
            assertThat(result.getTotalPages()).isEqualTo(3);
        }

    }

    @Nested
    class FindUomCategoryByKeyword {
        @Test
        void should_success_when_find_uom_category_with_keyword() {
            //Given
            Integer page = 1;
            Integer size = 1;
            String sortAttribute = "name";
            Sort sortDirection = Sort.ASC;
            Keyword keyword = Keyword.of(Text.of("m"));

            //Then
            Page<UomCategory> result = uomCategoryRepository.findByKeyWord(page, size, sortAttribute, sortDirection, keyword);

            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(2);
            assertThat(result.getContent().size()).isEqualTo(1);
            assertThat(result.getFirst()).isTrue();
            assertThat(result.getLast()).isFalse();
            assertThat(result.getTotalPages()).isEqualTo(2);
        }

        @Test
        void should_return_empty_page_when_find_uom_category_with_keyword() {
            //Given
            Integer page = 1;
            Integer size = 1;
            String sortAttribute = "name";
            Sort sortDirection = Sort.ASC;
            Keyword keyword = Keyword.of(Text.of("w"));

            //Then
            Page<UomCategory> result = uomCategoryRepository.findByKeyWord(page, size, sortAttribute, sortDirection, keyword);

            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
            assertThat(result.getContent().size()).isEqualTo(0);
            assertThat(result.getFirst()).isFalse();
            assertThat(result.getLast()).isFalse();
            assertThat(result.getTotalPages()).isEqualTo(0);
        }
    }


}
