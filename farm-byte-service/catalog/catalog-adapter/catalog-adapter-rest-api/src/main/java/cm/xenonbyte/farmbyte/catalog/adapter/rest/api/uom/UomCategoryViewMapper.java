package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.SearchUomCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.SearchUomCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.UpdateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.UpdateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
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
 * @since 15/08/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UomCategoryViewMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "parentUomCategoryId", expression = "java(createUomCategoryViewRequest.getParentUomCategoryId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId(createUomCategoryViewRequest.getParentUomCategoryId()))")
    UomCategory toUomCategory(@Nonnull @Valid CreateUomCategoryViewRequest createUomCategoryViewRequest);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(uomCategory.getParentUomCategoryId() == null? null: uomCategory.getParentUomCategoryId().getValue())", target = "parentUomCategoryId")
    @Nonnull @Valid CreateUomCategoryViewResponse toCreateUomCategoryViewResponse(UomCategory uomCategory);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(uomCategory.getParentUomCategoryId() == null? null: uomCategory.getParentUomCategoryId().getValue())", target = "parentUomCategoryId")
    @Nonnull @Valid FindUomCategoryByIdViewResponse toFindUomCategoryViewResponse(@Nonnull UomCategory uomCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "elements", qualifiedByName = "toFindUomCategoriesViewResponses", target = "elements")
    @Nonnull @Valid FindUomCategoriesPageInfoViewResponse toFindUomCategoriesPageInfoViewResponse(@Nonnull PageInfo<UomCategory> uomCategoriesPageInfo);

    @Named("toFindUomCategoriesViewResponses")
    @Nonnull @Valid List<FindUomCategoriesViewResponse> toFindUomCategoriesViewResponses(@Nonnull List<UomCategory> uomCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(uomCategory.getParentUomCategoryId() == null? null: uomCategory.getParentUomCategoryId().getValue())", target = "parentUomCategoryId")
    @Nonnull @Valid FindUomCategoriesViewResponse toFindUomCategoriesViewResponse(@Nonnull UomCategory uomCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "elements", qualifiedByName = "toSearchUomCategoriesViewResponses", target = "elements")
    @Nonnull @Valid SearchUomCategoriesPageInfoViewResponse toSearchUomCategoriesPageInfoViewResponse(@Nonnull @Valid PageInfo<UomCategory> uomCategoriesPageInfo);

    @Named("toSearchUomCategoriesViewResponses")
    @Nonnull @Valid List<SearchUomCategoriesViewResponse> toSearchUomCategoriesViewResponses(@Nonnull List<UomCategory> uomCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(uomCategory.getParentUomCategoryId() == null? null: uomCategory.getParentUomCategoryId().getValue())", target = "parentUomCategoryId")
    @Nonnull @Valid SearchUomCategoriesViewResponse toSearchUomCategoriesViewResponse(@Nonnull UomCategory uomCategory);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "name", target = "name.text.value")
    @Mapping(source = "active", target = "active.value")
    @Mapping(expression = "java(updateUomCategoryViewRequest.getParentUomCategoryId() == null? null: new cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId(updateUomCategoryViewRequest.getParentUomCategoryId()))", target = "parentUomCategoryId")
    @Nonnull UomCategory toUomCategory(@Nonnull @Valid UpdateUomCategoryViewRequest updateUomCategoryViewRequest);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "active.value", target = "active")
    @Mapping(expression = "java(uomCategory.getParentUomCategoryId() == null? null: uomCategory.getParentUomCategoryId().getValue())", target = "parentUomCategoryId")
    @Nonnull @Valid UpdateUomCategoryViewResponse toUpdateUomCategoryViewResponse(@Nonnull UomCategory uomCategory);
}
