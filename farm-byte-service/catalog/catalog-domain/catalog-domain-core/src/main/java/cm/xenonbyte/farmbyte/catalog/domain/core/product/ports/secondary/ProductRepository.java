package cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public interface ProductRepository {
    @Nonnull Product save(@Nonnull Product product);

    Boolean existsByName(Name name);

    Optional<Product> findById(@Nonnull ProductId productId);

    @Nonnull PageInfo<Product> findAll(int page, int size, String attribute, Direction direction);

    @Nonnull PageInfo<Product> search(int page, int size, String attribute, Direction direction, @Nonnull Keyword keyword);
}
