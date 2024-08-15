package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uomcategory;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.UomCategory;
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
    @Mapping(target = "name.value", source = "name")
    @Mapping(target = "parentUomCategoryId.value",source = "parentUomCategoryId")
    UomCategory toUomCategory(CreateUomCategoryViewRequest createUomCategoryViewRequest);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "parentUomCategoryId.value", target = "parentUomCategoryId")
    CreateUomCategoryViewResponse toCreateUomCategoryViewResponse(UomCategory uomCategory);
}
