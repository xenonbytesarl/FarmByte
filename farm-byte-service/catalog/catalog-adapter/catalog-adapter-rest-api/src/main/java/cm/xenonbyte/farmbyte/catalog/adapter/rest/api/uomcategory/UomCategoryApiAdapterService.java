package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uomcategory;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uomcategory.ports.primary.IUomCategoryService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@Slf4j
@Service
public final class UomCategoryApiAdapterService implements IUomCategoryApiAdapterService {

    private final IUomCategoryService uomCategoryService;
    private final UomCategoryViewMapper uomCategoryViewMapper;

    public UomCategoryApiAdapterService(final @Nonnull IUomCategoryService uomCategoryService, final @Nonnull UomCategoryViewMapper uomCategoryViewMapper) {
        this.uomCategoryService = Objects.requireNonNull(uomCategoryService);
        this.uomCategoryViewMapper = Objects.requireNonNull(uomCategoryViewMapper);
    }

    @Nonnull
    @Override
    public CreateUomCategoryViewResponse createUomCategory(@Nonnull CreateUomCategoryViewRequest createUomCategoryViewRequest) {
        return uomCategoryViewMapper.toCreateUomCategoryViewResponse(
                uomCategoryService.createUomCategory(
                        uomCategoryViewMapper.toUomCategory(createUomCategoryViewRequest)));
    }
}
