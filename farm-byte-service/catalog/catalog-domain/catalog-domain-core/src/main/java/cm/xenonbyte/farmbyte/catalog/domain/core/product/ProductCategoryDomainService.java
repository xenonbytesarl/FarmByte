package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.Optional;

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

    @Nonnull
    @Override
    public ProductCategory updateProductCategory(@Nonnull ProductCategoryId productCategoryId, @Nonnull ProductCategory productCategoryToUpdate) {
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(productCategoryId);
        if (optionalProductCategory.isPresent()) {
            validateUomCategory(productCategoryToUpdate);
            return productCategoryRepository.update(optionalProductCategory.get(), productCategoryToUpdate);
        }
        throw new ProductCategoryNotFoundException(new String[] {productCategoryId.getValue().toString()});
    }

    private void validateUomCategory(ProductCategory productCategory) {

        if(productCategory.getParentProductCategoryId() != null && !productCategoryRepository.existsById(productCategory.getParentProductCategoryId())) {
            throw new ParentProductCategoryNotFoundException(new String[] {productCategory.getParentProductCategoryId().getValue().toString()});
        }

        //We check unique attribute in case of creation. At this step, id it's null
        if(isProductCategoryCreate(productCategory)) {
            throw new ProductCategoryNameConflictException(new String[] {productCategory.getName().getText().getValue()});
        }

        //We check unique attribute in case of creation. At this step, id it's null
        Optional<ProductCategory> existingProductCategoryByName =  productCategoryRepository.findByName(productCategory.getName());
        if(isProductCategoryUpdate(productCategory, existingProductCategoryByName)) {
            throw new ProductCategoryNameConflictException(new String[] {productCategory.getName().getText().getValue()});
        }
    }

    private static boolean isProductCategoryUpdate(ProductCategory productCategory, Optional<ProductCategory> existingProductCategoryByName) {
        return existingProductCategoryByName.isPresent() && productCategory.getId() != null
                && !existingProductCategoryByName.get().getId().equals(productCategory.getId());
    }

    private boolean isProductCategoryCreate(ProductCategory productCategory) {
        return productCategory.getId() == null && productCategoryRepository.existsByName(productCategory.getName());
    }
}
