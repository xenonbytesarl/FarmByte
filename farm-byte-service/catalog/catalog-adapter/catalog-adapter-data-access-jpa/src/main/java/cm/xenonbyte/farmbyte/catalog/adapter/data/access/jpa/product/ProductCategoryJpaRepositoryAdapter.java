package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
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
 * @since 15/08/2024
 */
@Slf4j
@Service
public class ProductCategoryJpaRepositoryAdapter implements ProductCategoryRepository {

    private final ProductCategoryJpaRepository productCategoryJpaRepository;
    private final ProductCategoryJpaMapper productCategoryJpaMapper;

    public ProductCategoryJpaRepositoryAdapter(final @Nonnull ProductCategoryJpaRepository productCategoryJpaRepository,
                                               final @Nonnull ProductCategoryJpaMapper productCategoryJpaMapper) {
        this.productCategoryJpaRepository = Objects.requireNonNull(productCategoryJpaRepository);
        this.productCategoryJpaMapper = Objects.requireNonNull(productCategoryJpaMapper);
    }


    @Override
    @Transactional(readOnly = true)
    public Boolean existsByName(@Nonnull Name name) {
        return productCategoryJpaRepository.existsByNameIgnoreCase(name.getText().getValue());
    }

    @Nonnull
    @Override
    public Optional<ProductCategory> findById(@Nonnull ProductCategoryId productCategoryId) {
        return productCategoryJpaRepository.findById(productCategoryId.getValue())
                .map(productCategoryJpaMapper::toProductCategory);
    }

    @Nonnull
    @Override
    public PageInfo<ProductCategory> findAll(Integer page, Integer size, String attribute, Direction direction) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<ProductCategoryJpa> productCategoryJpaPage = productCategoryJpaRepository.findAll(PageRequest.of(page, size, sortDirection, attribute));


        return new PageInfo<>(
                productCategoryJpaPage.isFirst(),
                productCategoryJpaPage.isLast(),
                productCategoryJpaPage.getSize(),
                productCategoryJpaPage.getTotalElements(),
                productCategoryJpaPage.getTotalPages(),
                productCategoryJpaPage.getContent().stream().map(productCategoryJpaMapper::toProductCategory).toList()
        );
    }

    @Nonnull
    @Override
    public PageInfo<ProductCategory> search(Integer page, Integer size, String attribute, Direction direction, @Nonnull Keyword keyword) {
        Sort.Direction sortDirection = Direction.ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<ProductCategoryJpa> productCategoryJpaPage = productCategoryJpaRepository.search(PageRequest.of(page, size, sortDirection, attribute), keyword.getText().getValue());


        return new PageInfo<>(
                productCategoryJpaPage.isFirst(),
                productCategoryJpaPage.isLast(),
                productCategoryJpaPage.getSize(),
                productCategoryJpaPage.getTotalElements(),
                productCategoryJpaPage.getTotalPages(),
                productCategoryJpaPage.getContent().stream().map(productCategoryJpaMapper::toProductCategory).toList()
        );
    }

    @Override
    public Optional<ProductCategory> findByName(@Nonnull Name name) {
        return productCategoryJpaRepository.findByNameIgnoreCase(name.getText().getValue())
                .map(productCategoryJpaMapper::toProductCategory);
    }

    @Nonnull
    @Override
    public ProductCategory update(@Nonnull ProductCategory oldProductCategory, ProductCategory newProductCategory) {
        ProductCategoryJpa oldProductCategoryJpa = productCategoryJpaMapper.toProductCategoryJpa(oldProductCategory);
        ProductCategoryJpa newProductCategoryJpa = productCategoryJpaMapper.toProductCategoryJpa(newProductCategory);
        productCategoryJpaMapper.copyNewToOldProductCategory(newProductCategoryJpa, oldProductCategoryJpa);
        return productCategoryJpaMapper.toProductCategory(
                productCategoryJpaRepository.save(oldProductCategoryJpa)
        );
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
