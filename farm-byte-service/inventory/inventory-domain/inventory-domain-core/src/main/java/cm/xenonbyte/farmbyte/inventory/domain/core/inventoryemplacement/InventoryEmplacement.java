package cm.xenonbyte.farmbyte.inventory.domain.core.inventoryemplacement;

import cm.xenonbyte.farmbyte.common.domain.entity.BaseEntity;
import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class InventoryEmplacement extends BaseEntity<InventoryEmplacementId> {
    private final Name name;
    private final InventoryEmplacementType type;
    private Active active;
    private InventoryEmplacementId parentId;

    public InventoryEmplacement(Name name, InventoryEmplacementType type) {
        this.name = name;
        this.type = type;
    }

    private InventoryEmplacement(Builder builder) {
        setId(builder.id);
        name = builder.name;
        type = builder.type;
        active = builder.active;
        parentId = builder.parentId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initializeWithDefaults() {
        setId(new InventoryEmplacementId(UUID.randomUUID()));
        this.active = Active.with(true);
    }

    public Name getName() {
        return name;
    }

    public InventoryEmplacementType getType() {
        return type;
    }

    public Active getActive() {
        return active;
    }

    public InventoryEmplacementId getParentId() {
        return parentId;
    }

    public void validateMandatoryFields() {

        Assert.field("name", name)
                .notNull()
                .notNull(name.getText().getValue());

        Assert.field("type", type)
                .notNull()
                .notNull(type)
                .isOneOf(Arrays.stream(InventoryEmplacementType.values()).map(Object.class::cast).toList());
    }


    public static final class Builder {
        private InventoryEmplacementId id;
        private Name name;
        private InventoryEmplacementType type;
        private Active active;
        private InventoryEmplacementId parentId;

        private Builder() {
        }

        public Builder id(InventoryEmplacementId val) {
            id = val;
            return this;
        }

        public Builder name(Name val) {
            name = val;
            return this;
        }

        public Builder type(InventoryEmplacementType val) {
            type = val;
            return this;
        }

        public Builder active(Active val) {
            active = val;
            return this;
        }

        public Builder parentId(InventoryEmplacementId val) {
            parentId = val;
            return this;
        }

        public InventoryEmplacement build() {
            return new InventoryEmplacement(this);
        }
    }
}
