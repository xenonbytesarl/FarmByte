package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
public interface IUomRestAPIAdapterService {
    @Nonnull CreateUomViewResponse createUom(@Nonnull CreateUomViewRequest request);
}
