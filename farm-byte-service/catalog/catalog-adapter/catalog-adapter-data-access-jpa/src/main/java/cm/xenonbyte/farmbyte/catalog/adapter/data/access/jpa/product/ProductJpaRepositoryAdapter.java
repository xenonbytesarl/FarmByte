package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Reference;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@Slf4j
@Service
public class ProductJpaRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductJpaMapper productJpaMapper;

    public ProductJpaRepositoryAdapter(@Nonnull ProductJpaRepository productJpaRepository, @Nonnull ProductJpaMapper productJpaMapper) {
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
    public Boolean existsByName(@Nonnull Name name) {
        return productJpaRepository.existsByNameIgnoreCase(name.getText().getValue());
    }

    @Override
    public Optional<Product> findById(@Nonnull ProductId productId) {
        return productJpaRepository.findById(productId.getValue())
                .map(productJpaMapper::toProduct);
    }

    @Nonnull
    @Override
    public PageInfo<Product> findAll(int page, int size, String attribute, @Nonnull Direction direction) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<ProductJpa> productJpaPage = productJpaRepository.findAll(PageRequest.of(page, size, sortDirection, attribute));
        return new PageInfo<Product>(
                productJpaPage.isFirst(),
                productJpaPage.isLast(),
                productJpaPage.getSize(),
                productJpaPage.getTotalElements(),
                productJpaPage.getTotalPages(),
                productJpaPage.getContent().stream().map(productJpaMapper::toProduct).toList()
        );
    }

    @Nonnull
    @Override
    public PageInfo<Product> search(int page, int size, String attribute, @Nonnull Direction direction, @Nonnull Keyword keyword) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<ProductJpa> productJpaPage = productJpaRepository.search(PageRequest.of(page, size, sortDirection, attribute), keyword.getText().getValue());
        return new PageInfo<Product>(
                productJpaPage.isFirst(),
                productJpaPage.isLast(),
                productJpaPage.getSize(),
                productJpaPage.getTotalElements(),
                productJpaPage.getTotalPages(),
                productJpaPage.getContent().stream().map(productJpaMapper::toProduct).toList()
        );
    }

    @Nonnull
    @Override
    public Product update(@Nonnull Product oldProduct, @Nonnull Product newProduct) {
        ProductJpa oldProductJpa = productJpaMapper.toProductJpa(oldProduct);
        ProductJpa newProductJpa = productJpaMapper.toProductJpa(newProduct);
        productJpaMapper.copyNewToOldProductJpa(newProductJpa, oldProductJpa);

        return productJpaMapper.toProduct(productJpaRepository.save(oldProductJpa));
    }

    @Override
    public boolean existsByReference(@Nonnull Reference reference) {
        return reference!= null && productJpaRepository.existsByReferenceIgnoreCase(reference.getText().getValue());
    }

    @Override
    public Optional<Product> findByName(@Nonnull Name name) {
        return productJpaRepository.findByNameIgnoreCase(name.getText().getValue())
                .map(productJpaMapper::toProduct);
    }

    @Override
    public Optional<Product> findByReference(@Nonnull Reference reference) {
        if (reference == null) {
            return Optional.empty();
        }
        return productJpaRepository.findByReferenceIgnoreCase(reference.getText().getValue())
                .map(productJpaMapper::toProduct);
    }
}
