package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.IUomDomainService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public final class UomRestAPIAdapterService implements IUomRestAPIAdapterService {

    private final IUomDomainService uomDomainService;
    private final UomMapper uomMapper;

    @Nonnull
    @Override
    public CreateUomViewResponse createUom(@Nonnull CreateUomViewRequest request) {
        return uomMapper.toCreateUomViewResponse(uomDomainService.createUom(uomMapper.toUom(request)));
    }
}
