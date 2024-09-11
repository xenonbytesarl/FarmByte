package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
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
 * @since 20/08/2024
 */
@DomainService
public final class ProductDomainService implements ProductService {

    private final ProductRepository productRepository;
    private final UomRepository uomRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductDomainService(
            @Nonnull ProductRepository productRepository,
            @Nonnull UomRepository uomRepository,
            @Nonnull ProductCategoryRepository productCategoryRepository) {
        this.productRepository = Objects.requireNonNull(productRepository);
        this.uomRepository = Objects.requireNonNull(uomRepository);
        this.productCategoryRepository = Objects.requireNonNull(productCategoryRepository);
    }

    @Nonnull
    @Override
    public Product createProduct(@Nonnull Product product) {
        verify(product);
        product.validate();
        product.initiate();
        return productRepository.save(product);
    }

    @Nonnull
    @Override
    public Product findProductById(@Nonnull ProductId productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(new String[] {productId.getValue().toString()}));
    }

    @Nonnull
    @Override
    public PageInfo<Product> findProducts(int page, int size, String attribute, Direction direction) {
        return productRepository.findAll(page, size, attribute, direction);
    }

    @Nonnull
    @Override
    public PageInfo<Product> searchProducts(int page, int size, String attribute, Direction direction, Keyword keyword) {
        return productRepository.search(page, size, attribute, direction, keyword);
    }

    @Nonnull
    @Override
    public Product updateProduct(@Nonnull ProductId productId, @Nonnull Product productToUpdate) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()) {
            Product oldProduct = optionalProduct.get();
            verify(productToUpdate);
            return productRepository.update(oldProduct, productToUpdate);
        }
        throw new ProductNotFoundException(new String[] {productId.getValue().toString()});
    }

    private void verify(Product product) {
        checkUniqueProperties(product);
        verifyProductCategory(product.getCategoryId());
        verifyUom(product);
    }

    //In product object unique field are name and reference. Name is required and reference can be null
    private void checkUniqueProperties(Product product) {

        //We check unique attribute(name & reference) in case of creation. At this step, id it's null
        if(product.getId() == null && productRepository.existsByName(product.getName())) {
            throw new ProductNameConflictException(new String[] {product.getName().getText().getValue()});
        }

        if(product.getId() == null && product.getReference() != null && productRepository.existsByReference(product.getReference())) {
            throw new ProductReferenceConflictException(new String[] {product.getReference().getText().getValue()});
        }

        //We check unique attribute(name & reference) in case of update. At this step, id it's not null
        Optional<Product> existingProductByName = productRepository.findByName(product.getName());
        if(product.getId() != null && existingProductByName.isPresent() && !existingProductByName.get().getId().equals(product.getId())) {
            throw new ProductNameConflictException(new String[] {product.getName().getText().getValue()});
        }

        Optional<Product> existingProductByReference = productRepository.findByReference(product.getReference());
        if(product.getId() != null && product.getReference() != null && existingProductByReference.isPresent() &&
                !existingProductByReference.get().getId().equals(product.getId())) {
            throw new ProductReferenceConflictException(new String[] {product.getReference().getText().getValue()});
        }
    }

    private void verifyProductCategory(ProductCategoryId categoryId) {
        if(categoryId != null && !productCategoryRepository.existsById(categoryId)) {
            throw new ProductCategoryNotFoundException(new String[] {categoryId.getValue().toString()});
        }
    }

    private void verifyUom(Product product) {

        if(product.getStockUomId() != null && !uomRepository.existsById(product.getStockUomId())) {
            throw new ProductUomNotFoundException(new String[] {product.getStockUomId().getValue().toString()});
        }
        if(product.getPurchaseUomId() != null && !uomRepository.existsById(product.getPurchaseUomId())) {
            throw new ProductUomNotFoundException(new String[] {product.getPurchaseUomId().getValue().toString()});
        }

        if(product.isStorable() && product.getStockUomId() != null && product.getPurchaseUomId() != null) {
            Optional<Uom> optionalStockUom = uomRepository.findById(product.getStockUomId());
            Optional<Uom> optionalPurchaseUom = uomRepository.findById(product.getPurchaseUomId());
            if(optionalStockUom.isPresent() && optionalPurchaseUom.isPresent() &&
                    !optionalStockUom.get().getUomCategoryId().equals(optionalPurchaseUom.get().getUomCategoryId())) {
                throw new ProductStockAndPurchaseUomBadException(new String[] {
                        optionalStockUom.get().getName().getText().getValue(),
                        optionalPurchaseUom.get().getName().getText().getValue()
                });
            }
        }
    }

}
