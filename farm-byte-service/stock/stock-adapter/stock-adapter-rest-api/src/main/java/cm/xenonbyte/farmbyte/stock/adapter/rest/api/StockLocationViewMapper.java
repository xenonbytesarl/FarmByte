package cm.xenonbyte.farmbyte.stock.adapter.rest.api;


import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewRequest;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.CreateStockLocationViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationByIdViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.FindStockLocationsViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.SearchStockLocationsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.SearchStockLocationsViewResponse;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.UpdateStockLocationViewRequest;
import cm.xenonbyte.farmbyte.stock.adapter.rest.api.generated.stocklocation.view.UpdateStockLocationViewResponse;
import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocation;
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
public interface StockLocationViewMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "type", expression = "java(cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationType.valueOf(createStockLocationViewRequest.getType().name()))")
    @Mapping(target = "parentId", expression = "java(createStockLocationViewRequest.getParentId() == null? null: new cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId(createStockLocationViewRequest.getParentId()))")
    @Nonnull
    StockLocation toStockLocation(@Nonnull @Valid CreateStockLocationViewRequest createStockLocationViewRequest);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(CreateStockLocationViewResponse.TypeEnum.valueOf(stockLocation.getType().name()))", target = "type")
    @Mapping(expression = "java(stockLocation.getParentId() == null? null : stockLocation.getParentId().getValue())", target = "parentId")
    @Mapping(source = "active.value", target = "active")
    @Nonnull @Valid
    CreateStockLocationViewResponse toCreateStockLocationViewResponse(StockLocation stockLocation);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(FindStockLocationByIdViewResponse.TypeEnum.valueOf(stockLocation.getType().name()))", target = "type")
    @Mapping(expression = "java(stockLocation.getParentId() == null? null : stockLocation.getParentId().getValue())", target = "parentId")
    @Mapping(source = "active.value", target = "active")
    @Nonnull @Valid
    FindStockLocationByIdViewResponse toFindStockLocationByIdViewResponse(@Nonnull StockLocation stockLocation);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "elements", qualifiedByName = "toFindStockLocationsViewResponses", target = "elements")
    @Nonnull @Valid
    FindStockLocationsPageInfoViewResponse toFindStockLocationsPageInfoViewResponse(@Nonnull PageInfo<StockLocation> stockLocationsPageInfo);

    @Named("toFindStockLocationsViewResponses")
    @Nonnull @Valid List<FindStockLocationsViewResponse> toFindStockLocationsViewResponses(@Nonnull List<StockLocation> stockLocations);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(FindStockLocationsViewResponse.TypeEnum.valueOf(stockLocation.getType().name()))", target = "type")
    @Mapping(expression = "java(stockLocation.getParentId() == null? null : stockLocation.getParentId().getValue())", target = "parentId")
    @Mapping(source = "active.value", target = "active")
    @Nonnull @Valid
    FindStockLocationsViewResponse toFindStockLocationsViewResponse(@Nonnull StockLocation stockLocation);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "first", target = "first")
    @Mapping(source = "last", target = "last")
    @Mapping(source = "pageSize", target = "pageSize")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "elements", qualifiedByName = "toSearchStockLocationsViewResponses", target = "elements")
    @Nonnull @Valid
    SearchStockLocationsPageInfoViewResponse toSearchStockLocationsPageInfoViewResponse(@Nonnull PageInfo<StockLocation> stockLocationsPageInfo);

    @Named("toSearchStockLocationsViewResponses")
    @Nonnull @Valid List<SearchStockLocationsViewResponse> toSearchStockLocationsViewResponses(@Nonnull List<StockLocation> stockLocations);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(SearchStockLocationsViewResponse.TypeEnum.valueOf(stockLocation.getType().name()))", target = "type")
    @Mapping(expression = "java(stockLocation.getParentId() == null? null : stockLocation.getParentId().getValue())", target = "parentId")
    @Mapping(source = "active.value", target = "active")
    @Nonnull @Valid SearchStockLocationsViewResponse toSearchStockLocationsViewResponse(@Nonnull StockLocation stockLocation);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "type", expression = "java(cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationType.valueOf(updateStockLocationViewRequest.getType().name()))")
    @Mapping(target = "parentId", expression = "java(updateStockLocationViewRequest.getParentId() == null? null: new cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId(updateStockLocationViewRequest.getParentId()))")
    @Mapping(target = "active.value", source = "active")
    @Nonnull StockLocation toStockLocation(@Nonnull @Valid UpdateStockLocationViewRequest updateStockLocationViewRequest);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(UpdateStockLocationViewResponse.TypeEnum.valueOf(stockLocation.getType().name()))", target = "type")
    @Mapping(expression = "java(stockLocation.getParentId() == null? null : stockLocation.getParentId().getValue())", target = "parentId")
    @Mapping(source = "active.value", target = "active")
    @Nonnull @Valid UpdateStockLocationViewResponse toUpdateStockLocationViewResponse(@Nonnull StockLocation stockLocation);
}
