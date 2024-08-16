package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.productcategory;

import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
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
public final class ProductCategoryAdapterPostgresRepository implements ProductCategoryRepository {

    private final ProductCategoryJpaRepository productCategoryJpaRepository;
    private final ProductCategoryJpaMapper productCategoryJpaMapper;

    public ProductCategoryAdapterPostgresRepository(final @Nonnull ProductCategoryJpaRepository productCategoryJpaRepository,
                                                    final @Nonnull ProductCategoryJpaMapper productCategoryJpaMapper) {
        this.productCategoryJpaRepository = Objects.requireNonNull(productCategoryJpaRepository);
        this.productCategoryJpaMapper = Objects.requireNonNull(productCategoryJpaMapper);
    }


    @Override
    public Boolean existsByName(@Nonnull Name name) {
        return productCategoryJpaRepository.existsByName(name.getValue());
    }

    @Override
    public ProductCategory save(@Nonnull ProductCategory productCategory) {
        return productCategoryJpaMapper.toProductCategory(
                productCategoryJpaRepository.save(productCategoryJpaMapper.toProductCategoryJpa(productCategory)));
    }

    @Override
    public Boolean existsById(ProductCategoryId parentCategoryId) {
        return productCategoryJpaRepository.existsById(parentCategoryId.getValue());
    }
}
