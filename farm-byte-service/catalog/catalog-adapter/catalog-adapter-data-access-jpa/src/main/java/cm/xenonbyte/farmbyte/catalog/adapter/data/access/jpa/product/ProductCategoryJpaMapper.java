package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
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
public interface ProductCategoryJpaMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "parentProductCategoryId.value", target = "parentProductCategoryJpa.id")
    @Mapping(source = "active.value", target = "active")
    ProductCategoryJpa toProductCategoryJpa(ProductCategory uomCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "name.value", source = "name")
    @Mapping(target = "parentProductCategoryId.value", source = "parentProductCategoryJpa.id")
    @Mapping(target = "active.value", source = "active")
    ProductCategory toProductCategory(ProductCategoryJpa uomCategoryJpa);
}
