package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@Slf4j
@Service
public class ProductAdapterPostgresRepository implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductJpaMapper productJpaMapper;

    public ProductAdapterPostgresRepository(@Nonnull ProductJpaRepository productJpaRepository, @Nonnull ProductJpaMapper productJpaMapper) {
        this.productJpaRepository = Objects.requireNonNull(productJpaRepository);
        this.productJpaMapper = Objects.requireNonNull(productJpaMapper);
    }

    @Nonnull
    @Override
    @Transactional
    public Product save(@Nonnull Product product) {
        return productJpaMapper.toProduct(productJpaRepository.save(productJpaMapper.toProductJpa(product)));
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsByName(Name name) {
        return productJpaRepository.existsByName(name.getText().getValue());
    }
}
