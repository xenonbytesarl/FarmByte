package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.UpdateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.UpdateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

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
    @Mapping(target = "imageName", expression = "java(createProductViewRequest.getFilename() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Filename.of(cm.xenonbyte.farmbyte.common.domain.vo.Text.of(createProductViewRequest.getFilename())))")
    @Mapping(target = "purchasePrice", expression = "java(createProductViewRequest.getPurchasePrice() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Money.of(createProductViewRequest.getPurchasePrice()))")
    @Mapping(target = "salePrice", expression = "java(createProductViewRequest.getSalePrice() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Money.of(createProductViewRequest.getSalePrice()))")
    @Mapping(target = "purchasable", expression = "java(createProductViewRequest.getPurchasable() == null? null: cm.xenonbyte.farmbyte.catalog.domain.core.product.Purchasable.with(createProductViewRequest.getPurchasable()))")
    @Mapping(target = "sellable", expression = "java(createProductViewRequest.getSellable() == null? null: cm.xenonbyte.farmbyte.catalog.domain.core.product.Sellable.with(createProductViewRequest.getSellable()))")
    @Mapping(target = "type", expression = "java(cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType.valueOf(createProductViewRequest.getType().name()))")
    @Mapping(target = "categoryId.value", source = "categoryId")
    @Mapping(target = "stockUomId", expression = "java(createProductViewRequest.getStockUomId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId(createProductViewRequest.getStockUomId()))")
    @Mapping(target = "purchaseUomId", expression = "java(createProductViewRequest.getPurchaseUomId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId(createProductViewRequest.getPurchaseUomId()))")
    @Nonnull Product toProduct(@Nonnull @Valid CreateProductViewRequest createProductViewRequest);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(expression = "java(product.getReference() == null? null: product.getReference().getText().getValue())", target = "reference")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "imageName.text.value", target = "filename")
    @Mapping(source = "purchasePrice.amount", target = "purchasePrice")
    @Mapping(source = "salePrice.amount", target = "salePrice")
    @Mapping(source = "purchasable.value", target = "purchasable")
    @Mapping(source = "sellable.value", target = "sellable")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(CreateProductViewResponse.TypeEnum.valueOf(product.getType().name()))", target = "type")
    @Mapping(source = "categoryId.value", target = "categoryId")
    @Mapping(expression = "java(product.getStockUomId() == null? null: product.getStockUomId().getValue())", target = "stockUomId")
    @Mapping(expression = "java(product.getPurchaseUomId() == null? null: product.getPurchaseUomId().getValue())", target = "purchaseUomId")
    @Nonnull @Valid CreateProductViewResponse toCreateProductViewResponse(@Nonnull Product product);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(expression = "java(product.getReference() == null? null: product.getReference().getText().getValue())", target = "reference")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "imageName.text.value", target = "filename")
    @Mapping(source = "purchasePrice.amount", target = "purchasePrice")
    @Mapping(source = "salePrice.amount", target = "salePrice")
    @Mapping(source = "purchasable.value", target = "purchasable")
    @Mapping(source = "sellable.value", target = "sellable")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(FindProductByIdViewResponse.TypeEnum.valueOf(product.getType().name()))", target = "type")
    @Mapping(source = "categoryId.value", target = "categoryId")
    @Mapping(expression = "java(product.getStockUomId() == null? null: product.getStockUomId().getValue())", target = "stockUomId")
    @Mapping(expression = "java(product.getPurchaseUomId() == null? null: product.getPurchaseUomId().getValue())", target = "purchaseUomId")
    @Nonnull @Valid FindProductByIdViewResponse toFindProductByIdViewResponse(@Nonnull Product product);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "content", qualifiedByName = "toFindProductsViewResponses", target = "content")
    @Nonnull @Valid FindProductsPageInfoViewResponse toFindProductsPageInfoViewResponse(@Nonnull PageInfo<Product> productPageInfo);

    @Named("toFindProductsViewResponses")
    @Nonnull @Valid List<FindProductsViewResponse> toFindProductsViewResponses(@Nonnull List<Product> product);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(expression = "java(product.getReference() == null? null: product.getReference().getText().getValue())", target = "reference")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "imageName.text.value", target = "filename")
    @Mapping(source = "purchasePrice.amount", target = "purchasePrice")
    @Mapping(source = "salePrice.amount", target = "salePrice")
    @Mapping(source = "purchasable.value", target = "purchasable")
    @Mapping(source = "sellable.value", target = "sellable")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(FindProductsViewResponse.TypeEnum.valueOf(product.getType().name()))", target = "type")
    @Mapping(source = "categoryId.value", target = "categoryId")
    @Mapping(expression = "java(product.getStockUomId() == null? null: product.getStockUomId().getValue())", target = "stockUomId")
    @Mapping(expression = "java(product.getPurchaseUomId() == null? null: product.getPurchaseUomId().getValue())", target = "purchaseUomId")
    @Nonnull @Valid FindProductsViewResponse toFindProductsViewResponse(@Nonnull Product product);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "content", qualifiedByName = "toSearchProductsViewResponses", target = "content")
    @Nonnull @Valid SearchProductsPageInfoViewResponse toSearchProductsPageInfoViewResponse(@Nonnull PageInfo<Product> productPageInfo);

    @Named("toSearchProductsViewResponses")
    @Nonnull @Valid List<SearchProductsViewResponse> toSearchProductsViewResponses(@Nonnull List<Product> product);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(expression = "java(product.getReference() == null? null: product.getReference().getText().getValue())", target = "reference")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "imageName.text.value", target = "filename")
    @Mapping(source = "purchasePrice.amount", target = "purchasePrice")
    @Mapping(source = "salePrice.amount", target = "salePrice")
    @Mapping(source = "purchasable.value", target = "purchasable")
    @Mapping(source = "sellable.value", target = "sellable")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(SearchProductsViewResponse.TypeEnum.valueOf(product.getType().name()))", target = "type")
    @Mapping(source = "categoryId.value", target = "categoryId")
    @Mapping(expression = "java(product.getStockUomId() == null? null: product.getStockUomId().getValue())", target = "stockUomId")
    @Mapping(expression = "java(product.getPurchaseUomId() == null? null: product.getPurchaseUomId().getValue())", target = "purchaseUomId")
    @Nonnull @Valid SearchProductsViewResponse toSearchProductsViewResponse(@Nonnull Product product);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "reference", expression = "java(updateProductViewRequest.getReference() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Reference.of(cm.xenonbyte.farmbyte.common.domain.vo.Text.of(updateProductViewRequest.getReference())))")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "imageName", expression = "java(updateProductViewRequest.getFilename() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Filename.of(cm.xenonbyte.farmbyte.common.domain.vo.Text.of(updateProductViewRequest.getFilename())))")
    @Mapping(target = "purchasePrice", expression = "java(updateProductViewRequest.getPurchasePrice() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Money.of(updateProductViewRequest.getPurchasePrice()))")
    @Mapping(target = "salePrice", expression = "java(updateProductViewRequest.getSalePrice() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Money.of(updateProductViewRequest.getSalePrice()))")
    @Mapping(target = "purchasable", expression = "java(updateProductViewRequest.getPurchasable() == null? null: cm.xenonbyte.farmbyte.catalog.domain.core.product.Purchasable.with(updateProductViewRequest.getPurchasable()))")
    @Mapping(target = "sellable", expression = "java(updateProductViewRequest.getSellable() == null? null: cm.xenonbyte.farmbyte.catalog.domain.core.product.Sellable.with(updateProductViewRequest.getSellable()))")
    @Mapping(target = "type", expression = "java(cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType.valueOf(updateProductViewRequest.getType().name()))")
    @Mapping(target = "categoryId.value", source = "categoryId")
    @Mapping(target = "active.value", source = "active")
    @Mapping(target = "stockUomId", expression = "java(updateProductViewRequest.getStockUomId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId(updateProductViewRequest.getStockUomId()))")
    @Mapping(target = "purchaseUomId", expression = "java(updateProductViewRequest.getPurchaseUomId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId(updateProductViewRequest.getPurchaseUomId()))")
    @Nonnull Product toProduct(@Nonnull @Valid UpdateProductViewRequest updateProductViewRequest);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(expression = "java(product.getReference() == null? null: product.getReference().getText().getValue())", target = "reference")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "imageName.text.value", target = "filename")
    @Mapping(source = "purchasePrice.amount", target = "purchasePrice")
    @Mapping(source = "salePrice.amount", target = "salePrice")
    @Mapping(source = "purchasable.value", target = "purchasable")
    @Mapping(source = "sellable.value", target = "sellable")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(UpdateProductViewResponse.TypeEnum.valueOf(product.getType().name()))", target = "type")
    @Mapping(source = "categoryId.value", target = "categoryId")
    @Mapping(expression = "java(product.getStockUomId() == null? null: product.getStockUomId().getValue())", target = "stockUomId")
    @Mapping(expression = "java(product.getPurchaseUomId() == null? null: product.getPurchaseUomId().getValue())", target = "purchaseUomId")
    @Nonnull @Valid UpdateProductViewResponse toUpdateProductViewResponse(@Nonnull Product product);
}
