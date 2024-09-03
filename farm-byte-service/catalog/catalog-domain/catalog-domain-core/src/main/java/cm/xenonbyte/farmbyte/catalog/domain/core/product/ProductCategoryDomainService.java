package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
@DomainService
public final class ProductCategoryDomainService implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryDomainService(@Nonnull ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = Objects.requireNonNull(productCategoryRepository);
    }

    @Nonnull
    @Override
    public ProductCategory createProductCategory(@Nonnull ProductCategory productCategory) {
        validateUomCategory(productCategory);
        productCategory.initiate();
        return productCategoryRepository.save(productCategory);
    }

    @Nonnull
    @Override
    public ProductCategory findProductCategoryById(@Nonnull ProductCategoryId productCategoryId) {
        return productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ProductCategoryNotFoundException(new String[] {productCategoryId.getValue().toString()}));
    }

    @Nonnull
    @Override
    public PageInfo<ProductCategory> findProductCategories(Integer page, Integer size, String attribute, Direction direction) {
        return productCategoryRepository.findAll(page, size, attribute, direction);
    }

    @Nonnull
    @Override
    public PageInfo<ProductCategory> searchProductCategories(Integer page, Integer size, String attribute, Direction direction, @Nonnull Keyword keyword) {
        return productCategoryRepository.search(page, size, attribute, direction, keyword);
    }

    private void validateUomCategory(ProductCategory productCategory) {

        if(productCategory.getParentProductCategoryId() != null && !productCategoryRepository.existsById(productCategory.getParentProductCategoryId())) {
            throw new ParentProductCategoryNotFoundException(new String[] {productCategory.getParentProductCategoryId().getValue().toString()});
        }
        if(productCategoryRepository.existsByName(productCategory.getName())) {
            throw new ProductCategoryNameConflictException(new String[] {productCategory.getName().getText().getValue()});
        }
    }
}
