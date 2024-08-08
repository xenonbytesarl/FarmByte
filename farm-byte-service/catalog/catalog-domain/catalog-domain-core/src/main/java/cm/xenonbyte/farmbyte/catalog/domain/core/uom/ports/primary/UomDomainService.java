package cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.entity.Uom;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public interface UomDomainService {

    @Nonnull
    Uom createUom(Uom uom);
}
