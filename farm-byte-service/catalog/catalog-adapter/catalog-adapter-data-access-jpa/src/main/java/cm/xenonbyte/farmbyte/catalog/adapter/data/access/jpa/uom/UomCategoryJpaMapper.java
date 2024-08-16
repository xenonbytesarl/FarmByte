package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "parentUomCategoryId.value", target = "parentUomCategoryJpa.id")
    @Mapping(source = "active.value", target = "active")
    UomCategoryJpa toUomCategoryJpa(UomCategory uomCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "name.value", source = "name")
    @Mapping(target = "parentUomCategoryId.value", source = "parentUomCategoryJpa.id")
    @Mapping(target = "active.value", source = "active")
    UomCategory toUomCategory(UomCategoryJpa uomCategoryJpa);
}
