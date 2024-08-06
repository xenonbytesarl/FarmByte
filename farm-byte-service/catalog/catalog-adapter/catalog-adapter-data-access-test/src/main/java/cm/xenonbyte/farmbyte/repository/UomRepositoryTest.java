package cm.xenonbyte.farmbyte.repository;

import cm.xenonbyte.farmbyte.uom.entity.Uom;
import cm.xenonbyte.farmbyte.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.uom.vo.Name;
import cm.xenonbyte.farmbyte.uom.vo.Ratio;
import cm.xenonbyte.farmbyte.uom.vo.UomCategoryId;
import cm.xenonbyte.farmbyte.uom.vo.UomType;
import org.junit.jupiter.api.Test;

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




    @Test
    void should_return_false_when_non_existent_uom_category_id_or_uom_type_are_given() {

        //Given
        UomCategoryId uomCategoryId1 = UomCategoryId.of(UUID.randomUUID());
        UomType uomType1 = UomType.REFERENCE;

        //Act
        boolean result = uomRepository.existsByCategoryIdAndUomTypeAndActive(uomCategoryId1, uomType1);

        //Then
        assertThat(result).isFalse();
    }

    @Test
    void should_return_true_when_existent_uom_category_id_and_uom_type_are_given() {

        //Act
        boolean result = uomRepository.existsByCategoryIdAndUomTypeAndActive(uomCategoryId, uomType);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    void should_return_true_when_existent_uom_name_and_category_are_given() {

        //Act
        boolean result = uomRepository.existsByNameAndCategoryAndActive(name, uomCategoryId);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    void should_return_false_when_non_existent_name_or_category_are_given() {
        //Given
        Name name1 = Name.of("Lot de 20");
        UomCategoryId uomCategoryId1 = UomCategoryId.of(UUID.randomUUID());
        //Act
        boolean result = uomRepository.existsByNameAndCategoryAndActive(name1, uomCategoryId1);

        //Then
        assertThat(result).isFalse();
    }

    @Test
    void should_save_given_valid_uom() {
        //Given
        UomCategoryId uomCategoryId1 = UomCategoryId.of(UUID.randomUUID());
        UomType uomType1 = UomType.GREATER;
        Uom uomToSave = createSomeUom(
                Name.of("Palette de 6"),
                uomCategoryId1,
                uomType1,
                Ratio.of(6.0));
        //Act
        uomRepository.save(uomToSave);
        boolean result = uomRepository.existsByCategoryIdAndUomTypeAndActive(uomCategoryId1, uomType1);

        //Then
        assertThat(result).isTrue();
    }

    protected Uom createSomeUom(Name name, UomCategoryId uomCategoryId, UomType uomType, Ratio ratio) {
        Uom uom = Uom.from(
                Name.of("Unite"),
                uomCategoryId,
                uomType,
                ratio

        );
        uom.initiate();
        return uom;
    }
}
