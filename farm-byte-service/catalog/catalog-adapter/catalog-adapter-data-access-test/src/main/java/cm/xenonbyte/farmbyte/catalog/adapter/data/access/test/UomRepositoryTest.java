package cm.xenonbyte.farmbyte.catalog.adapter.data.access.test;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Ratio;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public abstract class UomRepositoryTest {

    protected UomRepository uomRepository;

    protected UomCategoryId uomCategoryId;
    protected UomType uomType;
    protected Name name;
    protected UomId uomId;


    @Nested
    class CreateUomRepositoryTest {
        @Test
        void should_return_false_when_non_existent_uom_id_are_given() {
            //Act
            boolean result = uomRepository.existsById(new UomId(UUID.randomUUID()));

            //Act + Then
            assertThat(result).isFalse();
        }

        @Test
        void should_return_true_when__existent_uom_id_are_given() {
            //Act
            boolean result = uomRepository.existsById(uomId);

            //Then
            assertThat(result).isTrue();
        }

        @Test
        protected void should_return_false_when_non_existent_uom_category_id_or_uom_type_are_given() {

            //Given
            UomCategoryId uomCategoryId1 = new UomCategoryId(UUID.randomUUID());


            //Act
            boolean result = uomRepository.existsByCategoryIdAndUomTypeAndActive(uomCategoryId1, uomType);

            //Then
            assertThat(result).isFalse();
        }

        @Test
        protected void should_return_true_when_existent_uom_category_id_and_uom_type_are_given() {

            //Act
            boolean result = uomRepository.existsByCategoryIdAndUomTypeAndActive(uomCategoryId, UomType.REFERENCE);

            //Then
            assertThat(result).isTrue();
        }

        @Test
        protected void should_return_true_when_existent_uom_name_and_category_are_given() {

            //Act
            boolean result = uomRepository.existsByNameAndCategoryAndActive(name, uomCategoryId);

            //Then
            assertThat(result).isTrue();
        }

        @Test
        protected void should_return_false_when_non_existent_name_or_category_are_given() {
            //Given
            Name name1 = Name.of(Text.of("Lot de 20"));
            UomCategoryId uomCategoryId1 = new UomCategoryId(UUID.randomUUID());
            //Act
            boolean result = uomRepository.existsByNameAndCategoryAndActive(name1, uomCategoryId1);

            //Then
            assertThat(result).isFalse();
        }

        @Test
        protected void should_save_given_valid_uom() {
            //Given
            UomCategoryId uomCategoryId1 = new UomCategoryId(UUID.fromString("01912c0f-2fcf-705b-ae59-d79d159f3ad0"));
            UomType uomType1 = UomType.GREATER;
            Uom uomToSave = createSomeUom(
                    Name.of(Text.of("Palette de 6")),
                    uomCategoryId1,
                    uomType1,
                    Ratio.of(6.0));
            //Act
            uomRepository.save(uomToSave);
            boolean result = uomRepository.existsByCategoryIdAndUomTypeAndActive(uomCategoryId1, uomType1);

            //Then
            assertThat(result).isTrue();
        }
    }

    @Nested
    class FindUomByIdRepositoryTest {

        @Test
        void should_success_when_find_uom_by_existing_id() {
            //Given + Act
            Optional<Uom> result = uomRepository.findById(uomId);

            //Then
            assertThat(result.isPresent()).isTrue();
        }

        @Test
        void should_fail_when_find_uom_with_non_existing_id() {
            //Given
            UomId uomId1 = new UomId(UUID.randomUUID());
            //Act
            Optional<Uom> result = uomRepository.findById(uomId1);
            assertThat(result.isEmpty()).isTrue();
        }
    }

    @Nested
    class FindUomsRepositoryTest {

        @Test
        void should_success_when_find_uoms() {
            Integer page = 0;
            Integer size = 2;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;

            //Act
            PageInfo<Uom> result = uomRepository.findAll(page, size, sortAttribute, direction);

            //Then
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
            assertThat(result.getContent().size()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchUomRepositoryTest {
        @Test
        void should_success_when_search_uom_by_keyword() {
            Integer page = 0;
            Integer size = 2;
            String sortAttribute = "name";
            Direction direction = Direction.ASC;
            Keyword keyword = Keyword.of(Text.of("n"));

            //Act
            PageInfo<Uom> result = uomRepository.search(page, size, sortAttribute, direction, keyword);

            //Then
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
            assertThat(result.getContent().size()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class UpdateUomRepositoryTest {

        @Test
        void should_success_when_find_existing_uom_by_name() {
            //Given + Act
            Optional<Uom> result = uomRepository.findByName(name);

            //Then
            assertThat(result.isPresent()).isTrue();
        }

        @Test
        void should_fail_when_find_non_existing_uom_by_name() {
            //Given + Act
            Optional<Uom> result = uomRepository.findByName(Name.of(Text.of("NaN")));

            //Then
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        void should_success_when_update_uom() {
            //Given
            Uom uom = Uom.builder()
                    .id(uomId)
                    .name(Name.of(Text.of("Carton de 16")))
                    .uomCategoryId(uomCategoryId)
                    .uomType(UomType.REFERENCE)
                    .ratio(Ratio.of(1.0))
                    .active(Active.with(true))
                    .build();

            //Act
            Uom result = uomRepository.save(uom);

            //Then
            assertThat(result).isNotNull().isEqualTo(uom);
        }

        @Test
        void should_success_when_find_uom_by_uom_category_and_uom_type_and_active() {

            //Given + Then
            Optional<Uom> result = uomRepository.findByCategoryIdAndUomTypeAndActive(uomCategoryId, uomType);

            //Then
            assertThat(result.isPresent()).isTrue();

        }

        @Test
        void should_fail_when_find_uom_by_uom_non_existing_category_and_uom_type_and_active() {

            //Given + Then
            Optional<Uom> result = uomRepository.findByCategoryIdAndUomTypeAndActive(
                    new UomCategoryId(UUID.fromString("0191d7e8-4c74-74e9-a0c2-eaa9111795e5")), uomType);

            //Then
            assertThat(result.isEmpty()).isTrue();

        }
    }

    protected Uom createSomeUom(Name name, UomCategoryId uomCategoryId, UomType uomType, Ratio ratio) {
        Uom uom = Uom.from(
                name,
                uomCategoryId,
                uomType,
                ratio
        );
        uom.initiate();
        return uom;
    }
}
