package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomCategoryService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@Slf4j
@Service
public class UomCategoryDomainServiceRestApiAdapter implements UomCategoryServiceRestApiAdapter {

    private final UomCategoryService uomCategoryService;
    private final UomCategoryViewMapper uomCategoryViewMapper;

    public UomCategoryDomainServiceRestApiAdapter(final @Nonnull UomCategoryService uomCategoryService, final @Nonnull UomCategoryViewMapper uomCategoryViewMapper) {
        this.uomCategoryService = Objects.requireNonNull(uomCategoryService);
        this.uomCategoryViewMapper = Objects.requireNonNull(uomCategoryViewMapper);
    }

    @Nonnull
    @Override
    @Transactional
    public CreateUomCategoryViewResponse createUomCategory(@Nonnull CreateUomCategoryViewRequest createUomCategoryViewRequest) {
        return uomCategoryViewMapper.toCreateUomCategoryViewResponse(
                uomCategoryService.createUomCategory(
                        uomCategoryViewMapper.toUomCategory(createUomCategoryViewRequest)));
    }
}
