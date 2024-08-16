package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import jakarta.validation.Valid;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

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
    @Mapping(target = "parentUomCategoryId", qualifiedByName = "getParentCategoryIdDomain", source = "parentUomCategoryId")
    UomCategory toUomCategory(@Valid CreateUomCategoryViewRequest createUomCategoryViewRequest);

    @Named("getParentCategoryIdDomain")
    default UomCategoryId getParentCategoryIdDomain(UUID parentUomCategoryId) {
        if(parentUomCategoryId == null) {
            return null;
        }
        return new UomCategoryId(parentUomCategoryId);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "parentUomCategoryId.value", target = "parentUomCategoryId")
    @Valid CreateUomCategoryViewResponse toCreateUomCategoryViewResponse(UomCategory uomCategory);
}
