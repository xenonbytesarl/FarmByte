package cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public interface ProductService {
    @Nonnull Product createProduct(@Nonnull Product product);

    @Nonnull Product findProductById(@Nonnull ProductId productId);

    @Nonnull PageInfo<Product> findProducts(int page, int size, String attribute, Direction direction);

    @Nonnull PageInfo<Product> searchProducts(int page, int size, String attribute, Direction direction, @Nonnull Keyword keyword);
}
