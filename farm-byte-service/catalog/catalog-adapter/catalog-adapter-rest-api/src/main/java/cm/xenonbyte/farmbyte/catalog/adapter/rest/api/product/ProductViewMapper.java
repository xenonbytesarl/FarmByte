package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomIdMapper;
import cm.xenonbyte.farmbyte.common.domain.mapper.ImageMapper;
import cm.xenonbyte.farmbyte.common.domain.mapper.MoneyMapper;
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
        uses = {ReferenceMapper.class, ImageMapper.class, UomIdMapper.class, MoneyMapper.class}
)
public interface ProductViewMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "image", source = "image")
    @Mapping(target = "purchasePrice", source = "purchasePrice")
    @Mapping(target = "salePrice", source = "salePrice")
    @Mapping(target = "purchasable.value", source = "purchasable")
    @Mapping(target = "sellable.value", source = "sellable")
    @Mapping(target = "type", qualifiedByName = "getType", source = "type")
    @Mapping(target = "categoryId.value", source = "categoryId")
    @Mapping(target = "stockUomId", source = "stockUomId")
    @Mapping(target = "purchaseUomId", source = "purchaseUomId")
    @Nonnull Product toProduct(@Nonnull CreateProductViewRequest createProductViewRequest);

    @Named("getType")
    default ProductType getType(@Nonnull CreateProductViewRequest.TypeEnum type) {
        return ProductType.valueOf(type.name());
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "reference", target = "reference")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "purchasePrice", target = "purchasePrice")
    @Mapping(source = "salePrice", target = "salePrice")
    @Mapping(source = "purchasable.value", target = "purchasable")
    @Mapping(source = "sellable.value", target = "sellable")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "type", qualifiedByName = "getTypeView", target = "type")
    @Mapping(source = "categoryId.value", target = "categoryId")
    @Mapping(source = "stockUomId", target = "stockUomId")
    @Mapping(source = "purchaseUomId", target = "purchaseUomId")
    @Nonnull CreateProductViewResponse toCreateProductViewResponse(@Nonnull Product product);

    @Named("getTypeView")
    default CreateProductViewResponse.TypeEnum getTypeView(@Nonnull ProductType type) {
        return CreateProductViewResponse.TypeEnum.valueOf(type.name());
    }


}
