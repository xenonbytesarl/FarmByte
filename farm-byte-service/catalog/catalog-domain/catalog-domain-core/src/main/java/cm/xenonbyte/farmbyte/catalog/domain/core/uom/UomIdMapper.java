package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.common.domain.annotation.DomainMapper;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 24/08/2024
 */
@DomainMapper
public class UomIdMapper {

    public UomId map(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return new UomId(uuid);
    }

    public UUID map(UomId uomId) {
        if (uomId == null) {
            return null;
        }
        return uomId.getValue();
    }
}
