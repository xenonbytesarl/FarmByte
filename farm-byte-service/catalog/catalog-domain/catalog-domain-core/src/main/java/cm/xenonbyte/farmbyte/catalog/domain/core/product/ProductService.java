package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.catalog.domain.ProductNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.ProductStockAndPurchaseUomBadException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.IProductService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
@DomainService
public final class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final UomRepository uomRepository;

    public ProductService(
            @Nonnull ProductRepository productRepository,
            @Nonnull UomRepository uomRepository) {
        this.productRepository = Objects.requireNonNull(productRepository);
        this.uomRepository = Objects.requireNonNull(uomRepository);
    }

    @Nonnull
    @Override
    public Product createProduct(@Nonnull Product product) {
        verify(product);
        product.validate();
        product.initiate();
        return productRepository.save(product);
    }

    private void verify(Product product) {
        verifyProductName(product.getName());
        verifyProductUom(product);
    }

    private void verifyProductName(Name name) {
        if(name != null && productRepository.existsByName(name)) {
            throw new ProductNameConflictException(new String[] {name.getText().getValue()});
        }
    }

    private void verifyProductUom(Product product) {
        if(product.isStorable() && product.getStockUomId() != null && product.getPurchaseUomId() != null) {
            Optional<Uom> optionalStockUom = uomRepository.findByUomId(product.getStockUomId());
            Optional<Uom> optionalPurchaseUom = uomRepository.findByUomId(product.getPurchaseUomId());
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
