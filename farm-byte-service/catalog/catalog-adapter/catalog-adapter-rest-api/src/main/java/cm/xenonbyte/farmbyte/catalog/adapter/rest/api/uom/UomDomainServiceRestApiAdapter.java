package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.CreateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.FindUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.SearchUomsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uom.view.UpdateUomViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomService;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 08/08/2024
 */
@Slf4j
@Service
public class UomDomainServiceRestApiAdapter implements UomServiceRestApiAdapter {

    private final UomService uomService;
    private final UomViewMapper uomViewMapper;

    public UomDomainServiceRestApiAdapter(final @Nonnull UomService uomService, final @Nonnull UomViewMapper uomViewMapper) {
        this.uomService = Objects.requireNonNull(uomService);
        this.uomViewMapper = Objects.requireNonNull(uomViewMapper);
    }

    @Nonnull
    @Override
    @Transactional
    public CreateUomViewResponse createUom(@Nonnull CreateUomViewRequest request) {
        return uomViewMapper.toCreateUomViewResponse(uomService.createUom(uomViewMapper.toUom(request)));
    }

    @Nonnull
    @Override
    public FindUomByIdViewResponse findUomById(@Nonnull UUID uomId) {
        return uomViewMapper.toFindUomByIdViewResponse(uomService.findUomById(new UomId(uomId)));
    }

    @Nonnull
    @Override
    public FindUomsPageInfoViewResponse findUoms(int page, int size, String attribute, String direction) {
        return uomViewMapper.toFindUomsPageInfoViewResponse(uomService.findUoms(page, size, attribute, Direction.valueOf(direction)));
    }

    @Nonnull
    @Override
    public SearchUomsPageInfoViewResponse searchUoms(int page, int size, String attribute, String direction, @Nonnull String keyword) {
        return uomViewMapper.toSearchUomsPageInfoViewResponse(uomService.searchUoms(page, size, attribute, Direction.valueOf(direction), Keyword.of(Text.of(keyword))));
    }

    @Nonnull
    @Override
    public UpdateUomViewResponse updateUom(@Nonnull UUID uomIdUUID, @Nonnull UpdateUomViewRequest updateUomViewRequest) {
        return uomViewMapper.toUpdateUomViewResponse(
                uomService.updateUom(new UomId(uomIdUUID), uomViewMapper.toUom(updateUomViewRequest))
        );
    }
}
