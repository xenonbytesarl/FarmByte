package cm.xenonbyte.farmbyte.catalog.domain.test;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.InMemoryUomCategoryRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomParentCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.IUomCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
final class UomCategoryTest {

    private IUomCategoryService uomCategoryService;
    private UomCategory parentUomCategory;

    @BeforeEach
    void setUp() {
        UomCategoryRepository uomCategoryRepository = new InMemoryUomCategoryRepository();
        uomCategoryService = new UomCategoryService(uomCategoryRepository);

        parentUomCategory = UomCategory.of(new Name(Text.of("Temps")));
        parentUomCategory.initiate();
        uomCategoryRepository.save(parentUomCategory);
    }

    @Test
    void should_create_uom_category_as_root_category() {
        //Given
        Name uomCategoryName = Name.of(Text.of("Unite"));
        UomCategory uomCategory = UomCategory.of(uomCategoryName);

        //Act
        UomCategory createUomCategory = uomCategoryService.createUomCategory(uomCategory);

        //Then
        assertThat(createUomCategory).isNotNull();
        assertThat(createUomCategory.getName()).isEqualTo(uomCategoryName);
        assertThat(createUomCategory.getActive().getValue()).isTrue();
        assertThat(createUomCategory.getId().getValue())
                .isInstanceOf(UUID.class)
                .isNotNull();
    }

    @Test
    void should_create_child_uom_category_when_child_exist() {
        //Given
        Name uomCategoryName = Name.of(Text.of("Unite"));
        UomCategory uomCategory = UomCategory.of(uomCategoryName, parentUomCategory.getId());

        //Act
        UomCategory childUomCategory = uomCategoryService.createUomCategory(uomCategory);

        //Then
        assertThat(childUomCategory).isNotNull();
        assertThat(childUomCategory.getName()).isEqualTo(uomCategoryName);
        assertThat(childUomCategory.getActive().getValue()).isTrue();
        assertThat(childUomCategory.getId().getValue())
                .isInstanceOf(UUID.class)
                .isNotNull();
        assertThat(childUomCategory.getParentUomCategoryId()).isEqualTo(parentUomCategory.getId());
    }

    @Test
    void should_throw_exception_when_uom_category_name_exists() {
        //Given
        Name uomCategoryName = Name.of(Text.of("Temps"));
        UomCategory uomCategory = UomCategory.of(uomCategoryName);

        //Act + Then
        assertThatThrownBy(() -> uomCategoryService.createUomCategory(uomCategory))
                .isInstanceOf(UomCategoryNameConflictException.class)
                .hasMessage(UOM_CATEGORY_NAME_CONFLICT_EXCEPTION);
    }

    @Test
    void should_throw_exception_when_create_child_category_with_non_existing_parent() {
        //Given
        Name uomCategoryName = Name.of(Text.of("Unite"));
        UomCategory uomCategory = UomCategory.of(uomCategoryName, new UomCategoryId(UUID.randomUUID()));

        assertThatThrownBy(() -> uomCategoryService.createUomCategory(uomCategory))
                .isInstanceOf(UomParentCategoryNotFoundException.class)
                .hasMessage(UOM_PARENT_CATEGORY_NOT_FOUND_EXCEPTION);
    }
}
