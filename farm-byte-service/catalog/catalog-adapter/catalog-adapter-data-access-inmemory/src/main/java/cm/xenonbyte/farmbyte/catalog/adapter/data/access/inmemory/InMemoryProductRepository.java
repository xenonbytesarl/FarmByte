package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import jakarta.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public final class InMemoryProductRepository implements ProductRepository {

    private final Map<ProductId, Product> products = new HashMap<>();

    @Nonnull
    @Override
    public Product save(@Nonnull Product product) {
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public Boolean existsByName(Name name) {
        return products.values().stream()
                .anyMatch(product -> product.getName().equals(name));
    }
}
