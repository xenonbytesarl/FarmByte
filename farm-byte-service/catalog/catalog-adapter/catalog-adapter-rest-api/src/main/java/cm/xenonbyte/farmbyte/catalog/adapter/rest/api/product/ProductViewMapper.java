package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import jakarta.annotation.Nonnull;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ProductViewMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "reference", expression = "java(createProductViewRequest.getReference() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Reference.of(cm.xenonbyte.farmbyte.common.domain.vo.Text.of(createProductViewRequest.getReference())))")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "image", expression = "java(createProductViewRequest.getImage() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Image.with(cm.xenonbyte.farmbyte.common.domain.vo.Text.of(createProductViewRequest.getImage())))")
    @Mapping(target = "purchasePrice", expression = "java(createProductViewRequest.getImage() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Money.of(createProductViewRequest.getPurchasePrice()))")
    @Mapping(target = "salePrice", expression = "java(createProductViewRequest.getSalePrice() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Money.of(createProductViewRequest.getSalePrice()))")
    @Mapping(target = "purchasable", expression = "java(createProductViewRequest.getPurchasable() == null? null: cm.xenonbyte.farmbyte.catalog.domain.core.product.Purchasable.of(createProductViewRequest.getPurchasable()))")
    @Mapping(target = "sellable", expression = "java(createProductViewRequest.getSellable() == null? null: cm.xenonbyte.farmbyte.catalog.domain.core.product.Sellable.of(createProductViewRequest.getSellable()))")
    @Mapping(target = "type", expression = "java(cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType.valueOf(createProductViewRequest.getType().name()))")
    @Mapping(target = "categoryId.value", source = "categoryId")
    @Mapping(target = "stockUomId", expression = "java(createProductViewRequest.getStockUomId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId(createProductViewRequest.getStockUomId()))")
    @Mapping(target = "purchaseUomId", expression = "java(createProductViewRequest.getPurchaseUomId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId(createProductViewRequest.getPurchaseUomId()))")
    @Nonnull Product toProduct(@Nonnull CreateProductViewRequest createProductViewRequest);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(expression = "java(product.getReference() == null? null: product.getReference().getText().getValue())", target = "reference")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "image.text.value", target = "image")
    @Mapping(source = "purchasePrice.amount", target = "purchasePrice")
    @Mapping(source = "salePrice.amount", target = "salePrice")
    @Mapping(source = "purchasable.value", target = "purchasable")
    @Mapping(source = "sellable.value", target = "sellable")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(CreateProductViewResponse.TypeEnum.valueOf(product.getType().name()))", target = "type")
    @Mapping(source = "categoryId.value", target = "categoryId")
    @Mapping(expression = "java(product.getStockUomId() == null? null: product.getStockUomId().getValue())", target = "stockUomId")
    @Mapping(expression = "java(product.getPurchaseUomId() == null? null: product.getPurchaseUomId().getValue())", target = "purchaseUomId")
    @Nonnull CreateProductViewResponse toCreateProductViewResponse(@Nonnull Product product);


}
