package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.CreateProductViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.FindProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.SearchProductsPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.UpdateProductViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.product.view.UpdateProductViewResponse;
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
    public CreateProductViewResponse createProduct(@Nonnull CreateProductViewRequest createProductViewRequest, @Nonnull MultipartFile multipartFile) throws IOException {
        Image image = Image
                .with(Text.of(Objects.requireNonNull(multipartFile.getOriginalFilename())), multipartFile.getInputStream())
                .computeImageName(rootPathStorageProducts);
        createProductViewRequest.setFilename(image.getName().getValue());
        CreateProductViewResponse createProductViewResponse = productViewMapper.toCreateProductViewResponse(
                productService.createProduct(productViewMapper.toProduct(createProductViewRequest)));
        storageManager.store(image, StorageLocation.computeStoragePtah(rootPathStorageProducts));
        createProductViewResponse.setEncodedFile(storageManager.fileToBase64(createProductViewResponse.getFilename()));
        createProductViewResponse.setMime(storageManager.mimeType(createProductViewResponse.getFilename()));
        return createProductViewResponse;
    }

    @Nonnull
    @Override
    public FindProductByIdViewResponse findProductById(@Nonnull UUID productId) throws IOException {
        FindProductByIdViewResponse findProductByIdViewResponse = productViewMapper.toFindProductByIdViewResponse(
                productService.findProductById(new ProductId(productId))
        );
        findProductByIdViewResponse.setEncodedFile(storageManager.fileToBase64(findProductByIdViewResponse.getFilename()));
        findProductByIdViewResponse.setMime(storageManager.mimeType(findProductByIdViewResponse.getFilename()));
        return findProductByIdViewResponse;
    }

    @Nonnull
    @Override
    public FindProductsPageInfoViewResponse findProducts(int page, int size, String attribute, String direction) {
        return productViewMapper.toFindProductsPageInfoViewResponse(
                productService.findProducts(page, size, attribute, Direction.valueOf(direction))
        );
    }

    @Nonnull
    @Override
    public SearchProductsPageInfoViewResponse searchProducts(int page, int size, String attribute, String direction, String keyword) {
        return productViewMapper.toSearchProductsPageInfoViewResponse(
                productService.searchProducts(page, size, attribute, Direction.valueOf(direction), Keyword.of(Text.of(keyword)))
        );
    }

    @Nonnull
    @Override
    public UpdateProductViewResponse updateProduct(@Nonnull UUID productIdUUID, @Nonnull UpdateProductViewRequest updateProductViewRequest,
                                                   @Nonnull MultipartFile multipartFile) throws IOException {
        Image image = Image.with(Text.of(updateProductViewRequest.getFilename()), multipartFile.getInputStream());
        updateProductViewRequest.setFilename(image.getName().getValue());
        UpdateProductViewResponse updateProductViewResponse = productViewMapper.toUpdateProductViewResponse(
                productService.updateProduct(new ProductId(productIdUUID), productViewMapper.toProduct(updateProductViewRequest)));
        storageManager.store(image, StorageLocation.computeStoragePtah(rootPathStorageProducts));
        updateProductViewResponse.setEncodedFile(storageManager.fileToBase64(updateProductViewResponse.getFilename()));
        updateProductViewResponse.setMime(storageManager.mimeType(updateProductViewResponse.getFilename()));
        return updateProductViewResponse;
    }
}
