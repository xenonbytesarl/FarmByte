package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Reference;
import jakarta.annotation.Nonnull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public final class ProductInMemoryRepositoryAdapter implements ProductRepository {

    private final Map<ProductId, Product> products = new HashMap<>();

    @Nonnull
    @Override
    public Product save(@Nonnull Product product) {
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public Boolean existsByName(@Nonnull Name name) {
        return products.values().stream()
                .anyMatch(product -> product.getName().equals(name));
    }

    @Override
    public Optional<Product> findById(@Nonnull ProductId productId) {
        return Optional.ofNullable(products.get(productId));
    }

    @Nonnull
    @Override
    public PageInfo<Product> findAll(int page, int size, String attribute, @Nonnull Direction direction) {
        PageInfo<Product> productPageInfo = new PageInfo<>();
        Comparator<Product> comparing = Comparator.comparing((Product a) -> a.getName().getText().getValue());
        return productPageInfo.with(
                page,
                size,
                products.values().stream()
                        .sorted(Direction.ASC.equals(direction) ? comparing : comparing.reversed())
                        .toList()
        );
    }

    @Nonnull
    @Override
    public PageInfo<Product> search(int page, int size, String attribute, @Nonnull Direction direction, @Nonnull Keyword keyword) {
        PageInfo<Product> productPageInfo = new PageInfo<>();
        Comparator<Product> comparing = Comparator.comparing((Product a) -> a.getName().getText().getValue());
        return productPageInfo.with(
                page,
                size,
                products.values().stream()
                        .filter(product -> product.getName().getText().getValue().toLowerCase().contains(keyword.getText().getValue().toLowerCase()))
                        .sorted(Direction.ASC.equals(direction) ? comparing : comparing.reversed())
                        .toList()
        );
    }

    @Nonnull
    @Override
    public Product update(@Nonnull Product oldProduct, @Nonnull Product newProduct) {
        products.replace(oldProduct.getId(), newProduct);
        return newProduct;
    }

    @Override
    public boolean existsByReference(@Nonnull Reference reference) {
        return products.values().stream()
                .anyMatch(product ->
                        product.getReference() != null && product.getReference().getText().getValue().equalsIgnoreCase(reference.getText().getValue()));
    }

    @Override
    public Optional<Product> findByName(@Nonnull Name name) {
        return products.values().stream()
                .filter(product -> product.getName().getText().getValue().equalsIgnoreCase(name.getText().getValue()))
                .findFirst();
    }

    @Override
    public Optional<Product> findByReference(@Nonnull Reference reference) {
        return products.values().stream()
                .filter(product -> product.getReference() != null && product.getReference().getText().getValue().equalsIgnoreCase(reference.getText().getValue()))
                .findFirst();
    }
}
