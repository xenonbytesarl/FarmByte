package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.SearchProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.SearchProductCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.UpdateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.UpdateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
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
    @Mapping(target = "parentProductCategoryId", expression = "java(createProductCategoryViewRequest.getParentProductCategoryId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId(createProductCategoryViewRequest.getParentProductCategoryId()))")
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
    @Mapping(expression = "java(productCategory.getParentProductCategoryId() == null? null: productCategory.getParentProductCategoryId().getValue())", target = "parentProductCategoryId")
    @Nonnull CreateProductCategoryViewResponse toCreateProductCategoryViewResponse(@Nonnull ProductCategory productCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(productCategory.getParentProductCategoryId() == null? null: productCategory.getParentProductCategoryId().getValue())", target = "parentProductCategoryId")
    @Nonnull @Valid FindProductCategoryByIdViewResponse toFindProductCategoryViewResponse(@Nonnull ProductCategory productCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "elements", qualifiedByName = "toFindProductCategoriesViewResponses", target = "elements")
    @Nonnull @Valid FindProductCategoriesPageInfoViewResponse toFindProductCategoriesPageInfoViewResponse(@Nonnull PageInfo<ProductCategory> productCategoriesPageInfo);

    @Named("toFindProductCategoriesViewResponses")
    @Nonnull @Valid List<FindProductCategoriesViewResponse> toFindProductCategoriesViewResponses(@Nonnull List<ProductCategory> productCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(productCategory.getParentProductCategoryId() == null? null: productCategory.getParentProductCategoryId().getValue())", target = "parentProductCategoryId")
    @Nonnull @Valid FindProductCategoriesViewResponse toFindProductCategoriesViewResponse(@Nonnull ProductCategory productCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "elements", qualifiedByName = "toSearchProductCategoriesViewResponses", target = "elements")
    @Nonnull @Valid SearchProductCategoriesPageInfoViewResponse toSearchProductCategoriesPageInfoViewResponse(@Nonnull PageInfo<ProductCategory> productCategoriesPageInfo);

    @Named("toSearchProductCategoriesViewResponses")
    @Nonnull @Valid List<SearchProductCategoriesViewResponse> toSearchProductCategoriesViewResponses(@Nonnull List<ProductCategory> productCategory);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(productCategory.getParentProductCategoryId() == null? null: productCategory.getParentProductCategoryId().getValue())", target = "parentProductCategoryId")
    @Nonnull @Valid SearchProductCategoriesViewResponse toSearchProductCategoriesViewResponse(@Nonnull ProductCategory productCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "active.value", source = "active")
    @Mapping(target = "parentProductCategoryId", expression = "java(updateProductCategoryViewRequest.getParentProductCategoryId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId(updateProductCategoryViewRequest.getParentProductCategoryId()))")
    @Nonnull ProductCategory toProductCategory(@Nonnull @Valid UpdateProductCategoryViewRequest updateProductCategoryViewRequest);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(productCategory.getParentProductCategoryId() == null? null: productCategory.getParentProductCategoryId().getValue())", target = "parentProductCategoryId")
    @Nonnull @Valid UpdateProductCategoryViewResponse toUpdateProductCategoryViewResponse(@Nonnull ProductCategory productCategory);
}
