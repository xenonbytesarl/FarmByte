package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UomJpaMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "ratio.value", target = "ratio")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "uomCategoryId.value", target = "uomCategoryJpa.id")
    @Mapping(expression = "java(cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom.UomTypeJpa.valueOf(uom.getUomType().name()))", target = "uomTypeJpa")
    UomJpa toUomJpa(Uom uom);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "ratio.value",source = "ratio")
    @Mapping(target = "active.value", source = "active")
    @Mapping(target = "uomCategoryId.value",source = "uomCategoryJpa.id")
    @Mapping(target = "uomType", expression = "java(cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType.valueOf(uomJpa.getUomTypeJpa().name()))")
    Uom toUom(UomJpa uomJpa);

    void copyNewToOldUom(@MappingTarget UomJpa oldUomJpa, UomJpa newUomJpa);
}
