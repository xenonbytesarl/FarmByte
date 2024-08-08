package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import org.mapstruct.*;

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
    @Mapping(source = "uomId.identifier", target = "id")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "ratio.value", target = "ratio")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "uomCategoryId.identifier", target = "uomCategoryJpa.id")
    @Mapping(source = "uomType", qualifiedByName = "toUomTypeJpa", target = "uomTypeJpa")
    UomJpa fromUom(Uom uom);

    @Named("toUomTypeJpa")
    default UomTypeJpa toUomTypeJpa(UomType uomType) {
        return UomTypeJpa.valueOf(uomType.name());
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "uomId.identifier", source = "id")
    @Mapping(target = "name.value", source = "name")
    @Mapping(target = "ratio.value",source = "ratio")
    @Mapping(target = "active.value", source = "active")
    @Mapping(target = "uomCategoryId.identifier",source = "uomCategoryJpa.id")
    @Mapping(target = "uomType", qualifiedByName = "toUomType", source = "uomTypeJpa")
    Uom fromUomJpa(UomJpa uomJpa);

    @Named("toUomType")
    default UomType toUomType(UomTypeJpa uomTypeJpa) {
        return UomType.valueOf(uomTypeJpa.name());
    }
}
