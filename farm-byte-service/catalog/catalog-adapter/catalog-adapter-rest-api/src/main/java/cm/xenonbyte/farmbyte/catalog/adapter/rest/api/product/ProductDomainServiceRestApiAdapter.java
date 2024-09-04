package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductService;
import cm.xenonbyte.farmbyte.common.domain.ports.primary.StorageManager;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.StorageLocation;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@Slf4j
@Service
public class ProductDomainServiceRestApiAdapter implements ProductServiceRestApiAdapter {

    private final ProductService productService;
    private final ProductViewMapper productViewMapper;
    private final StorageManager storageManager;

    @Value("${root.path.storage.products}")
    private String rootPathStorageProducts;

    public ProductDomainServiceRestApiAdapter(
            @Nonnull ProductService productService,
            @Nonnull ProductViewMapper productViewMapper,
            @Nonnull StorageManager storageManager) {

        this.productService = Objects.requireNonNull(productService);
        this.productViewMapper = Objects.requireNonNull(productViewMapper);
        this.storageManager = Objects.requireNonNull(storageManager);
    }

    @Nonnull
    @Override
    @Transactional
    public CreateProductViewResponse createProduct(@Nonnull CreateProductViewRequest createProductViewRequest,
                                                   @Nonnull MultipartFile multipartFile) throws IOException {
        Image image = Image
                .with(Text.of(multipartFile.getOriginalFilename()), multipartFile.getInputStream())
                .computeImageName(rootPathStorageProducts);
        createProductViewRequest.setFilename(image.getName().getValue());
        CreateProductViewResponse createProductViewResponse = productViewMapper.toCreateProductViewResponse(
                productService.createProduct(productViewMapper.toProduct(createProductViewRequest)));
        storageManager.store(image, StorageLocation.computeStoragePtah(rootPathStorageProducts));
        return createProductViewResponse;
    }

    @Nonnull
    @Override
    public FindProductByIdViewResponse findProductById(@Nonnull UUID productId) {
        return productViewMapper.toFindProductByIdViewResponse(
                productService.findProductById(new ProductId(productId))
        );
    }

    @Nonnull
    @Override
    public FindProductsPageInfoViewResponse findProducts(int page, int size, String attribute, Direction direction) {
        return productViewMapper.toFindProductsPageInfoViewResponse(
                productService.findProducts(page, size, attribute, direction)
        );
    }

    @Nonnull
    @Override
    public SearchProductsPageInfoViewResponse searchProducts(int page, int size, String attribute, Direction direction, String keyword) {
        return productViewMapper.toSearchProductsPageInfoViewResponse(
                productService.searchProducts(page, size, attribute, direction, Keyword.of(Text.of(keyword)))
        );
    }
}
