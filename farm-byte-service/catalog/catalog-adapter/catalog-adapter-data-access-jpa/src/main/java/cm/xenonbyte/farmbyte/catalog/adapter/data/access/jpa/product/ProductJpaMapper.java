package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.common.domain.mapper.ReferenceMapper;
import jakarta.annotation.Nonnull;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
    @Mapping(source = "imageName.text.value", target = "image")
    @Mapping(source = "purchasePrice.amount", target = "purchasePrice")
    @Mapping(source = "salePrice.amount", target = "salePrice")
    @Mapping(source = "purchasable.value", target = "purchasable")
    @Mapping(source = "sellable.value", target = "sellable")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(ProductTypeJpa.valueOf(product.getType().name()))", target = "typeJpa")
    @Mapping(source = "categoryId.value", target = "categoryJpa.id")
    @Mapping(expression = "java(product.getStockUomId() == null? null: cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom.UomJpa.builder().id(product.getStockUomId().getValue()).build())", target = "stockUomJpa")
    @Mapping(expression = "java(product.getPurchaseUomId() == null? null: cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom.UomJpa.builder().id(product.getPurchaseUomId().getValue()).build())", target = "purchaseUomJpa")
    @Nonnull ProductJpa toProductJpa(@Nonnull Product product);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "imageName.text.value", source = "image")
    @Mapping(target = "purchasePrice.amount", source = "purchasePrice")
    @Mapping(target = "salePrice.amount", source = "salePrice")
    @Mapping(target = "purchasable.value", source = "purchasable")
    @Mapping(target = "sellable.value", source = "sellable")
    @Mapping(target = "active.value", source = "active")
    @Mapping(target = "type", expression = "java(cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType.valueOf(productJpa.getTypeJpa().name()))")
    @Mapping(target = "categoryId.value", source = "categoryJpa.id")
    @Mapping(target = "stockUomId", expression = "java(productJpa.getStockUomJpa() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId(productJpa.getStockUomJpa().getId()))")
    @Mapping(target = "purchaseUomId", expression = "java(productJpa.getStockUomJpa() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId(productJpa.getPurchaseUomJpa().getId()))")
    @Nonnull Product toProduct(@Nonnull ProductJpa productJpa);

    void copyNewToOldProductJpa(@Nonnull ProductJpa newProductJpa, @Nonnull @MappingTarget ProductJpa oldProductJpa);
}
