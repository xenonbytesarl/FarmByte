package cm.xenonbyte.farmbyte.inventory.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacement;
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
public interface InventoryEmplacementJpaMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(expression = "java(InventoryEmplacementTypeJpa.valueOf(inventoryEmplacement.getType().name()))", target = "type")
    @Mapping(expression = "java(inventoryEmplacement.getParentId() == null? null: InventoryEmplacementJpa.builder().id(inventoryEmplacement.getParentId().getValue()).build())", target = "parentJpa")
    @Mapping(source = "active.value", target = "active")
    @Nonnull InventoryEmplacementJpa toInventoryEmplacementJpa(@Nonnull InventoryEmplacement inventoryEmplacement);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "type", expression = "java(cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementType.valueOf(inventoryEmplacementJpa.getType().name()))")
    @Mapping(target = "parentId", expression = "java(inventoryEmplacementJpa.getParentJpa() == null? null: new cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement.InventoryEmplacementId(inventoryEmplacementJpa.getParentJpa().getId()))")
    @Mapping(target = "active.value", source = "active")
    @Nonnull InventoryEmplacement toInventoryEmplacement(@Nonnull InventoryEmplacementJpa inventoryEmplacementJpa);
}
