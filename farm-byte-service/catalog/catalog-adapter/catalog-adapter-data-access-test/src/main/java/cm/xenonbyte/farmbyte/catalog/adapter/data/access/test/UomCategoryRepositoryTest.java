package cm.xenonbyte.farmbyte.catalog.adapter.data.access.test;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
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
    protected UomCategory oldUomCategory;

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
            Integer page = 0;
            Integer size = 2;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;

            //Act
            PageInfo<UomCategory> result = uomCategoryRepository.findAll(page, size, sortAttribute, direction);

            //Then
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
            assertThat(result.getContent().size()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }

    }

    @Nested
    class FindUomCategoryByKeyword {
        @Test
        void should_success_when_find_uom_category_with_keyword() {
            //Given
            Integer page = 0;
            Integer size = 1;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("m"));

            //Then
            PageInfo<UomCategory> result = uomCategoryRepository.search(page, size, sortAttribute, direction, keyword);

            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
            assertThat(result.getContent().size()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }

        @Test
        void should_return_empty_page_when_find_uom_category_with_keyword() {
            //Given
            Integer page = 1;
            Integer size = 1;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("w"));

            //Then
            PageInfo<UomCategory> result = uomCategoryRepository.search(page, size, sortAttribute, direction, keyword);

            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
            assertThat(result.getContent().size()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);
        }
    }

    @Nested
    class UpdateUomCategoryRepositoryTest {

        @Test
        void should_success_when_update_uom_category() {
            //Given
            UomCategory uomCategoryToUpdate = UomCategory.builder()
                    .id(oldUomCategory.getId())
                    .name(Name.of(Text.of("New Temps")))
                    .build();

            //Act
            UomCategory result = uomCategoryRepository.updateUomCategory(oldUomCategory, uomCategoryToUpdate);

            //Then
            assertThat(result).isNotNull().isEqualTo(uomCategoryToUpdate);
        }

        @Test
        void should_return_true_when_find_uom_category_with_existing_name() {
            //Given + Act
            Optional<UomCategory> result = uomCategoryRepository.findByName(name);

            //Then
            assertThat(result.isPresent()).isTrue();

        }

        @Test
        void should_return_false_when_find_uom_category_with_non_existing_name() {
            //Given + Act
            Optional<UomCategory> result = uomCategoryRepository.findByName(Name.of(Text.of("Le temps")));

            //Then
            assertThat(result.isPresent()).isFalse();
        }
    }


}
