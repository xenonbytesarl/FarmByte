package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductService;
import cm.xenonbyte.farmbyte.common.domain.ports.primary.StorageManager;
import cm.xenonbyte.farmbyte.common.domain.vo.Image;
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
}
