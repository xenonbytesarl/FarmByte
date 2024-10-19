package cm.xenonbyte.farmbyte.inventory.adapter.rest.api;

import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewRequest;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.CreateInventoryEmplacementViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementByIdViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.FindInventoryEmplacementsViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.SearchInventoryEmplacementsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.inventory.adapter.rest.api.generated.uom.view.SearchInventoryEmplacementsViewResponse;
import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
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
 * @since 17/10/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface InventoryEmplacementViewMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "type", expression = "java(cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementType.valueOf(createInventoryEmplacementViewRequest.getType().name()))")
    @Mapping(target = "parentId", expression = "java(createInventoryEmplacementViewRequest.getParentId() == null? null: new cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId(createInventoryEmplacementViewRequest.getParentId()))")
    @Nonnull InventoryEmplacement toInventoryEmplacement(@Nonnull @Valid CreateInventoryEmplacementViewRequest createInventoryEmplacementViewRequest);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(CreateInventoryEmplacementViewResponse.TypeEnum.valueOf(inventoryEmplacement.getType().name()))", target = "type")
    @Mapping(expression = "java(inventoryEmplacement.getParentId() == null? null : inventoryEmplacement.getParentId().getValue())", target = "parentId")
    @Mapping(source = "active.value", target = "active")
    @Nonnull @Valid CreateInventoryEmplacementViewResponse toCreateInventoryEmplacementViewResponse(InventoryEmplacement inventoryEmplacement);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(FindInventoryEmplacementByIdViewResponse.TypeEnum.valueOf(inventoryEmplacement.getType().name()))", target = "type")
    @Mapping(expression = "java(inventoryEmplacement.getParentId() == null? null : inventoryEmplacement.getParentId().getValue())", target = "parentId")
    @Mapping(source = "active.value", target = "active")
    @Nonnull @Valid FindInventoryEmplacementByIdViewResponse toFindInventoryEmplacementByIdViewResponse(@Nonnull InventoryEmplacement inventoryEmplacement);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "elements", qualifiedByName = "toFindInventoryEmplacementsViewResponses", target = "elements")
    @Nonnull @Valid FindInventoryEmplacementsPageInfoViewResponse toFindInventoryEmplacementsPageInfoViewResponse(@Nonnull PageInfo<InventoryEmplacement> inventoryEmplacementsPageInfo);

    @Named("toFindInventoryEmplacementsViewResponses")
    @Nonnull @Valid List<FindInventoryEmplacementsViewResponse> toFindInventoryEmplacementsViewResponses(@Nonnull List<InventoryEmplacement> inventoryEmplacements);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(FindInventoryEmplacementsViewResponse.TypeEnum.valueOf(inventoryEmplacement.getType().name()))", target = "type")
    @Mapping(expression = "java(inventoryEmplacement.getParentId() == null? null : inventoryEmplacement.getParentId().getValue())", target = "parentId")
    @Mapping(source = "active.value", target = "active")
    @Nonnull @Valid FindInventoryEmplacementsViewResponse toFindInventoryEmplacementsViewResponse(@Nonnull InventoryEmplacement inventoryEmplacement);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "elements", qualifiedByName = "toSearchInventoryEmplacementsViewResponses", target = "elements")
    @Nonnull @Valid SearchInventoryEmplacementsPageInfoViewResponse toSearchInventoryEmplacementsPageInfoViewResponse(@Nonnull PageInfo<InventoryEmplacement> inventoryEmplacementsPageInfo);

    @Named("toSearchInventoryEmplacementsViewResponses")
    @Nonnull @Valid List<SearchInventoryEmplacementsViewResponse> toSearchInventoryEmplacementsViewResponses(@Nonnull List<InventoryEmplacement> inventoryEmplacements);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(SearchInventoryEmplacementsViewResponse.TypeEnum.valueOf(inventoryEmplacement.getType().name()))", target = "type")
    @Mapping(expression = "java(inventoryEmplacement.getParentId() == null? null : inventoryEmplacement.getParentId().getValue())", target = "parentId")
    @Mapping(source = "active.value", target = "active")
    @Nonnull @Valid SearchInventoryEmplacementsViewResponse toSearchInventoryEmplacementsViewResponse(@Nonnull InventoryEmplacement inventoryEmplacement);
}
