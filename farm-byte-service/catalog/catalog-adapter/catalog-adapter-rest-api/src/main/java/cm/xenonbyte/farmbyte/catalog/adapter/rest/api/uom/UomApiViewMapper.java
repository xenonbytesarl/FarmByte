package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;


import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import jakarta.annotation.Nonnull;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.FIELD
)
public interface UomApiViewMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name.value", source = "name")
    @Mapping(target = "ratio.value",source = "ratio")
    @Mapping(target = "uomCategoryId.value",source = "uomCategoryId")
    @Mapping(target = "uomType", qualifiedByName = "toUomType1", source = "uomType")
    @Nonnull Uom toUom(@Nonnull CreateUomViewRequest createUomViewRequest);

    @Named("toUomType1")
    default UomType toUomType1(CreateUomViewRequest.UomTypeEnum uomTypeEnum) {
        return UomType.valueOf(uomTypeEnum.getValue());
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "ratio.value", target = "ratio")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "uomCategoryId.value", target = "uomCategoryId")
    @Mapping(source = "uomType", qualifiedByName = "toUomType2", target = "uomType")
    @Nonnull
    CreateUomViewResponse toCreateUomViewResponse(@Nonnull Uom uomResponse);

    @Named("toUomType2")
    default CreateUomViewResponse.UomTypeEnum toUomType2(UomType uomType) {
        return CreateUomViewResponse.UomTypeEnum.valueOf(uomType.name());
    }
}
