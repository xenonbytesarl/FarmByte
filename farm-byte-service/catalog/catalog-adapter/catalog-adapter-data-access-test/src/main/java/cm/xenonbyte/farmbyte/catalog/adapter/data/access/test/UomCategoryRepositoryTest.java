package cm.xenonbyte.farmbyte.catalog.adapter.data.access.test;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import org.junit.jupiter.api.Test;

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

    @Test
    protected void should_return_false_when_category_with_given_name_not_exist() {
        //Act
        Boolean result = uomCategoryRepository.existsByName(Name.of("Anything"));

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
        Name volumeCategory = Name.of("Volume");
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
