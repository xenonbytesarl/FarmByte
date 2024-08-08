package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.entity.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.vo.UomType;
import org.mapstruct.*;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.FIELD
)
public interface UomMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "uomId.id", target = "id")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "ratio.value", target = "ratio")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "uomCategoryId.id", target = "uomCategoryJpa.id")
    @Mapping(source = "uomType", qualifiedByName = "toUomTypeJpa", target = "type")
    UomJpa fromUom(Uom uom);

    @Named("toUomTypeJpa")
    default UomTypeJpa toUomTypeJpa(UomType uomType) {
        return UomTypeJpa.valueOf(uomType.name());
    }

    @BeanMapping(ignoreByDefault = true)
    //@Mapping(target = "uomId.id", source = "id")
    @Mapping(target = "name.value", source = "name")
    @Mapping(target = "ratio.value",source = "ratio")
    //@Mapping(target = "active.value", source = "active")
    @Mapping(target = "uomCategoryId.id",source = "uomCategoryJpa.id")
    @Mapping(target = "uomType", qualifiedByName = "toUomType", source = "type")
    Uom fromUomJpa(UomJpa uomJpa);

    @Named("toUomType")
    default UomType toUomType(UomTypeJpa uomTypeJpa) {
        return UomType.valueOf(uomTypeJpa.name());
    }
}
