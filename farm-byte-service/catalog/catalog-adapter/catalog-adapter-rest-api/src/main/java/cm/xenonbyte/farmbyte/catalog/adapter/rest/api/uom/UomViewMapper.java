package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;


import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
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
 * @since 08/08/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.FIELD
)
public interface UomViewMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "ratio", expression = "java(createUomViewRequest.getRatio() == null? null: cm.xenonbyte.farmbyte.common.domain.vo.Ratio.of(createUomViewRequest.getRatio()))")
    @Mapping(target = "uomCategoryId.value", source = "uomCategoryId")
    @Mapping(target = "uomType", expression = "java(cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType.valueOf(createUomViewRequest.getUomType().name()))")
    @Nonnull Uom toUom(@Valid @Nonnull CreateUomViewRequest createUomViewRequest);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "ratio.value", target = "ratio")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "uomCategoryId.value", target = "uomCategoryId")
    @Mapping(expression = "java(CreateUomViewResponse.UomTypeEnum.valueOf(uom.getUomType().name()))", target = "uomType")
    @Valid @Nonnull CreateUomViewResponse toCreateUomViewResponse(@Nonnull Uom uom);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "ratio.value", target = "ratio")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "uomCategoryId.value", target = "uomCategoryId")
    @Mapping(expression = "java(FindUomByIdViewResponse.UomTypeEnum.valueOf(uom.getUomType().name()))", target = "uomType")
    @Valid @Nonnull  FindUomByIdViewResponse toFindUomByIdViewResponse(@Nonnull Uom uom);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "content", qualifiedByName = "toFindUomsViewResponses", target = "content")
    @Nonnull @Valid FindUomsPageInfoViewResponse toFindUomsPageInfoViewResponse(@Nonnull PageInfo<Uom> uomPageInfo);

    @Named("toFindUomsViewResponses")
    @Nonnull @Valid
    List<FindUomsViewResponse> toFindUomsViewResponses(@Nonnull List<Uom> uom);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "ratio.value", target = "ratio")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "uomCategoryId.value", target = "uomCategoryId")
    @Mapping(expression = "java(FindUomsViewResponse.UomTypeEnum.valueOf(uom.getUomType().name()))", target = "uomType")
    @Nonnull @Valid FindUomsViewResponse toFindUomsViewResponse(@Nonnull Uom uom);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "content", qualifiedByName = "toSearchUomsViewResponses", target = "content")
    @Nonnull @Valid SearchUomsPageInfoViewResponse toSearchUomsPageInfoViewResponse(@Nonnull PageInfo<Uom> uomPageInfo);

    @Named("toSearchUomsViewResponses")
    @Nonnull @Valid
    List<SearchUomsViewResponse> toSearchUomsViewResponses(@Nonnull List<Uom> uom);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "ratio.value", target = "ratio")
    @Mapping(source = "active.value", target = "active")
    @Mapping(source = "uomCategoryId.value", target = "uomCategoryId")
    @Mapping(expression = "java(SearchUomsViewResponse.UomTypeEnum.valueOf(uom.getUomType().name()))", target = "uomType")
    @Nonnull @Valid SearchUomsViewResponse toSearchUomsViewResponse(@Nonnull Uom uom);
}
