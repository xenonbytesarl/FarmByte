package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.common.domain.mapper.ReferenceMapper;
import jakarta.annotation.Nonnull;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ReferenceMapper.class}
)
public interface ProductJpaMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "reference", target = "reference")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "image.text.value", target = "image")
    @Mapping(source = "purchasePrice.amount", target = "purchasePrice")
    @Mapping(source = "salePrice.amount", target = "salePrice")
    @Mapping(source = "purchasable.value", target = "purchasable")
    @Mapping(source = "sellable.value", target = "sellable")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "type", qualifiedByName = "getTypeJpa", target = "typeJpa")
    @Mapping(source = "categoryId.value", target = "categoryJpa.id")
    @Mapping(source = "stockUomId.value", target = "stockUomJpa.id")
    @Mapping(source = "purchaseUomId.value", target = "purchaseUomJpa.id")
    @Nonnull ProductJpa toProductJpa(@Nonnull Product product);

    @Named("getTypeJpa")
    default ProductTypeJpa getTypeJpa(@Nonnull ProductType type) {
        return ProductTypeJpa.valueOf(type.name());
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "image.text.value", source = "image")
    @Mapping(target = "purchasePrice.amount", source = "purchasePrice")
    @Mapping(target = "salePrice.amount", source = "salePrice")
    @Mapping(target = "purchasable.value", source = "purchasable")
    @Mapping(target = "sellable.value", source = "sellable")
    @Mapping(target = "active.value", source = "active")
    @Mapping(target = "type", qualifiedByName = "getType", source = "typeJpa")
    @Mapping(target = "categoryId.value", source = "categoryJpa.id")
    @Mapping(target = "stockUomId.value", source = "stockUomJpa.id")
    @Mapping(target = "purchaseUomId.value", source = "purchaseUomJpa.id")
    @Nonnull Product toProduct(@Nonnull ProductJpa productJpa);

    @Named("getType")
    default ProductType getType(@Nonnull ProductTypeJpa typeJpa) {
        return ProductType.valueOf(typeJpa.name());
    }


}
