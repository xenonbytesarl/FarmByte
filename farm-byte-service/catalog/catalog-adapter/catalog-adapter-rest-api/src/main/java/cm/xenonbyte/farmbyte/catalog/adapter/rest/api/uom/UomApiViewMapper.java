package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;


import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


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
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "ratio", expression = "java(createUomViewRequest.getRatio() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Ratio.of(createUomViewRequest.getRatio()))")
    @Mapping(target = "uomCategoryId.value", source = "uomCategoryId")
    @Mapping(target = "uomType", expression = "java(cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType.valueOf(createUomViewRequest.getUomType().name()))")
    @Nonnull Uom toUom(@Valid @Nonnull CreateUomViewRequest createUomViewRequest);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "ratio.value", target = "ratio")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "uomCategoryId.value", target = "uomCategoryId")
    @Mapping(expression = "java(CreateUomViewResponse.UomTypeEnum.valueOf(uom.getUomType().name()))", target = "uomType")
    @Valid @Nonnull CreateUomViewResponse toCreateUomViewResponse(@Nonnull Uom uom);

}
