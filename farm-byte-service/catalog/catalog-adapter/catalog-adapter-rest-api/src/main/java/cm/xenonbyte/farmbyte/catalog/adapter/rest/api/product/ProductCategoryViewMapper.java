package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import jakarta.annotation.Nonnull;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ProductCategoryViewMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "parentProductCategoryId", qualifiedByName = "getParentCategoryIdDomain", source = "parentProductCategoryId")
    @Nonnull ProductCategory toProductCategory(@Nonnull CreateProductCategoryViewRequest createProductCategoryViewRequest);

    @Named("getParentCategoryIdDomain")
    default ProductCategoryId getParentCategoryIdDomain(UUID parentProductCategoryId) {
        if(parentProductCategoryId == null) {
            return null;
        }
        return new ProductCategoryId(parentProductCategoryId);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "parentProductCategoryId.value", target = "parentProductCategoryId")
    @Nonnull CreateProductCategoryViewResponse toCreateProductCategoryViewResponse(@Nonnull ProductCategory productCategory);
}
