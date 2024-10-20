package cm.xenonbyte.farmbyte.stock.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocation;
import jakarta.annotation.Nonnull;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author bamk
 * @version 1.0
 * @since 16/10/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface StockLocationJpaMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(StockLocationTypeJpa.valueOf(stockLocation.getType().name()))", target = "type")
    @Mapping(expression = "java(stockLocation.getParentId() == null? null: StockLocationJpa.builder().id(stockLocation.getParentId().getValue()).build())", target = "parentJpa")
    @Mapping(source = "active.value", target = "active")
    @Nonnull StockLocationJpa toStockLocationJpa(@Nonnull StockLocation stockLocation);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "type", expression = "java(cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationType.valueOf(stockLocationJpa.getType().name()))")
    @Mapping(target = "parentId", expression = "java(stockLocationJpa.getParentJpa() == null? null: new cm.xenonbyte.farmbyte.stock.domain.core.stocklocation.StockLocationId(stockLocationJpa.getParentJpa().getId()))")
    @Mapping(target = "active.value", source = "active")
    @Nonnull
    StockLocation toStockLocation(@Nonnull StockLocationJpa stockLocationJpa);
}
