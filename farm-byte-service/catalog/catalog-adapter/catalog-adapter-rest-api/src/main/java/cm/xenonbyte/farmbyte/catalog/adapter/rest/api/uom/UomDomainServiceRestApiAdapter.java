package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@Slf4j
@Service
public final class UomDomainServiceRestApiAdapter implements UomServiceRestApiAdapter {

    private final UomService uomDomainService;
    private final UomApiViewMapper uomApiViewMapper;

    public UomDomainServiceRestApiAdapter(final @Nonnull UomService uomDomainService, final @Nonnull UomApiViewMapper uomApiViewMapper) {
        this.uomDomainService = Objects.requireNonNull(uomDomainService);
        this.uomApiViewMapper = Objects.requireNonNull(uomApiViewMapper);
    }

    @Nonnull
    @Override
    public CreateUomViewResponse createUom(@Nonnull CreateUomViewRequest request) {
        return uomApiViewMapper.toCreateUomViewResponse(uomDomainService.createUom(uomApiViewMapper.toUom(request)));
    }
}
