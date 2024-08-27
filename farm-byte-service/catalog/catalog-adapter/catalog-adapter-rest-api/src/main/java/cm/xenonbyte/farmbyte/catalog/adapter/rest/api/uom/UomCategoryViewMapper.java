package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import jakarta.validation.Valid;
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
public interface UomCategoryViewMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "parentUomCategoryId", expression = "java(createUomCategoryViewRequest.getParentUomCategoryId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId(createUomCategoryViewRequest.getParentUomCategoryId()))")
    UomCategory toUomCategory(@Valid CreateUomCategoryViewRequest createUomCategoryViewRequest);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(uomCategory.getParentUomCategoryId() == null? null: uomCategory.getParentUomCategoryId().getValue())", target = "parentUomCategoryId")
    @Valid CreateUomCategoryViewResponse toCreateUomCategoryViewResponse(UomCategory uomCategory);
}
