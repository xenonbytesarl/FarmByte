package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
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

    private void validateUomCategory(ProductCategory productCategory) {

        if(productCategory.getParentProductCategoryId() != null && !productCategoryRepository.existsById(productCategory.getParentProductCategoryId())) {
            throw new ParentProductCategoryNotFoundException(new String[] {productCategory.getParentProductCategoryId().getValue().toString()});
        }
        if(productCategoryRepository.existsByName(productCategory.getName())) {
            throw new ProductCategoryNameConflictException(new String[] {productCategory.getName().getText().getValue()});
        }
    }
}
