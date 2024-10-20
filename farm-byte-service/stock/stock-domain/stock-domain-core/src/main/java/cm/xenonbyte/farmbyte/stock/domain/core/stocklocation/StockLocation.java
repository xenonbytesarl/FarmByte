package cm.xenonbyte.farmbyte.stock.domain.core.stocklocation;

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
public final class StockLocation extends BaseEntity<StockLocationId> {
    private final Name name;
    private final StockLocationType type;
    private Active active;
    private StockLocationId parentId;

    public StockLocation(Name name, StockLocationType type) {
        this.name = name;
        this.type = type;
    }

    private StockLocation(Builder builder) {
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
        setId(new StockLocationId(UUID.randomUUID()));
        this.active = Active.with(true);
    }

    public Name getName() {
        return name;
    }

    public StockLocationType getType() {
        return type;
    }

    public Active getActive() {
        return active;
    }

    public StockLocationId getParentId() {
        return parentId;
    }

    public void validateMandatoryFields() {

        Assert.field("name", name)
                .notNull()
                .notNull(name.getText().getValue());

        Assert.field("type", type)
                .notNull()
                .notNull(type)
                .isOneOf(Arrays.stream(StockLocationType.values()).map(Object.class::cast).toList());
    }


    public static final class Builder {
        private StockLocationId id;
        private Name name;
        private StockLocationType type;
        private Active active;
        private StockLocationId parentId;

        private Builder() {
        }

        public Builder id(StockLocationId val) {
            id = val;
            return this;
        }

        public Builder name(Name val) {
            name = val;
            return this;
        }

        public Builder type(StockLocationType val) {
            type = val;
            return this;
        }

        public Builder active(Active val) {
            active = val;
            return this;
        }

        public Builder parentId(StockLocationId val) {
            parentId = val;
            return this;
        }

        public StockLocation build() {
            return new StockLocation(this);
        }
    }
}
