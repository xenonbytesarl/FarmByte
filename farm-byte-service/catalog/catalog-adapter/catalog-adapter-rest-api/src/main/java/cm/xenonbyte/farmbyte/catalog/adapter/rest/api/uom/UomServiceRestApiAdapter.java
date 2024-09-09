package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
public interface UomServiceRestApiAdapter {
    @Nonnull @Valid CreateUomViewResponse createUom(@Nonnull CreateUomViewRequest request);

    @Nonnull @Valid  FindUomByIdViewResponse findUomById(@Nonnull UUID uomId);

    @Nonnull @Valid FindUomsPageInfoViewResponse findUoms(int page, int size, String attribute, String direction);

    @Nonnull @Valid
    SearchUomsPageInfoViewResponse searchUoms(int page, int size, String attribute, String direction, String keyword);

    @Nonnull @Valid UpdateUomViewResponse updateUom(@Nonnull UUID uomIdUUID, @Nonnull UpdateUomViewRequest updateUomViewRequest);
}
