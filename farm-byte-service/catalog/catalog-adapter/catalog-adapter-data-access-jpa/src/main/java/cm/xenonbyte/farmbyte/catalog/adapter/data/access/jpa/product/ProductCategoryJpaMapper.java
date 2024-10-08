package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import jakarta.annotation.Nonnull;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(uomCategory.getParentProductCategoryId() == null? null: cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product.ProductCategoryJpa.builder().id(uomCategory.getParentProductCategoryId().getValue()).build())", target = "parentProductCategoryJpa")
    @Mapping(source = "active.value", target = "active")
    ProductCategoryJpa toProductCategoryJpa(ProductCategory uomCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "parentProductCategoryId", expression = "java(uomCategoryJpa.getParentProductCategoryJpa() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId(uomCategoryJpa.getParentProductCategoryJpa().getId()))")
    @Mapping(target = "active.value", source = "active")
    ProductCategory toProductCategory(ProductCategoryJpa uomCategoryJpa);

    void copyNewToOldProductCategory(@Nonnull ProductCategoryJpa newProductCategoryJpa, @Nonnull @MappingTarget ProductCategoryJpa oldProductCategoryJpa);
}
