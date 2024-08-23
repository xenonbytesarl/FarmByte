package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
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
public class ProductCategoryAdapterPostgresRepository implements ProductCategoryRepository {

    private final ProductCategoryJpaRepository productCategoryJpaRepository;
    private final ProductCategoryJpaMapper productCategoryJpaMapper;

    public ProductCategoryAdapterPostgresRepository(final @Nonnull ProductCategoryJpaRepository productCategoryJpaRepository,
                                                    final @Nonnull ProductCategoryJpaMapper productCategoryJpaMapper) {
        this.productCategoryJpaRepository = Objects.requireNonNull(productCategoryJpaRepository);
        this.productCategoryJpaMapper = Objects.requireNonNull(productCategoryJpaMapper);
    }


    @Override
    @Transactional(readOnly = true)
    public Boolean existsByName(@Nonnull Name name) {
        return productCategoryJpaRepository.existsByName(name.getText().getValue());
    }

    @Override
    @Transactional
    public ProductCategory save(@Nonnull ProductCategory productCategory) {
        return productCategoryJpaMapper.toProductCategory(
                productCategoryJpaRepository.save(productCategoryJpaMapper.toProductCategoryJpa(productCategory)));
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsById(ProductCategoryId parentCategoryId) {
        return productCategoryJpaRepository.existsById(parentCategoryId.getValue());
    }
}
