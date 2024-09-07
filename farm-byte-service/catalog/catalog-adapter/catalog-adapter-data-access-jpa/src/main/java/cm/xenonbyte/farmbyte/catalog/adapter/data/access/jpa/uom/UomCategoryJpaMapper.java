package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UomCategoryJpaMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(uomCategory.getParentUomCategoryId() == null? null: cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom.UomCategoryJpa.builder().id(uomCategory.getParentUomCategoryId().getValue()).build())", target = "parentUomCategoryJpa")
    @Mapping(source = "active.value", target = "active")
    UomCategoryJpa toUomCategoryJpa(UomCategory uomCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "parentUomCategoryId", expression = "java(uomCategoryJpa.getParentUomCategoryJpa() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId(uomCategoryJpa.getParentUomCategoryJpa().getId()))")
    @Mapping(target = "active.value", source = "active")
    UomCategory toUomCategory(UomCategoryJpa uomCategoryJpa);

    void copyNewToOldUomCategory(@MappingTarget UomCategoryJpa oldUomCategoryJpa, UomCategoryJpa newUomCategoryJpa);
}
