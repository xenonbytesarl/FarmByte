package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.uom;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.CreateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.FindUomCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.SearchUomCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.UpdateUomCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.uomcategory.view.UpdateUomCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomCategoryService;
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

    @Nonnull
    @Override
    public FindUomCategoryByIdViewResponse findUomCategoryById(@Nonnull UUID uomCategoryId) {
        return uomCategoryViewMapper.toFindUomCategoryViewResponse(uomCategoryService.findUomCategoryById(new UomCategoryId(uomCategoryId)));
    }

    @Nonnull
    @Override
    public FindUomCategoriesPageInfoViewResponse findUomCategories(int page, int size, String attribute, String direction) {
        return uomCategoryViewMapper.toFindUomCategoriesPageInfoViewResponse(uomCategoryService.findUomCategories(page, size, attribute, Direction.valueOf(direction)));
    }

    @Nonnull
    @Override
    public SearchUomCategoriesPageInfoViewResponse searchUomCategories(int page, int pageSize, String attribute, String direction, @Nonnull String keyword) {
        return uomCategoryViewMapper.toSearchUomCategoriesPageInfoViewResponse(uomCategoryService.searchUomCategories(page, pageSize,attribute, Direction.valueOf(direction), Keyword.of(Text.of(keyword))));
    }

    @Nonnull
    @Override
    public UpdateUomCategoryViewResponse updateUomCategory(@Nonnull UUID uomCategoryIdUUID, @Nonnull UpdateUomCategoryViewRequest updateUomCategoryViewRequest) {
        return uomCategoryViewMapper.toUpdateUomCategoryViewResponse(
                uomCategoryService.updateUomCategory(new UomCategoryId(uomCategoryIdUUID), uomCategoryViewMapper.toUomCategory(updateUomCategoryViewRequest))
        );
    }
}
