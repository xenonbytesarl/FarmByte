package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ParentProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductCategoryService;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_PARENT_ID_IS_REQUIRED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author bamk
 * @version 1.0
 * @since 15/08/2024
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
final class ProductCategoryDomainServiceApiAdapterTest {

    private ProductCategoryServiceRestApiAdapter productCategoryApiAdapterService;
    @Mock
    private ProductCategoryViewMapper productCategoryViewMapper;
    @Mock
    private ProductCategoryService productCategoryService;

    @BeforeEach
    void setUp() {
        productCategoryApiAdapterService = new ProductCategoryDomainServiceRestApiAdapter(
                productCategoryService,
                productCategoryViewMapper
        );
    }

    static Stream<Arguments> createProductCategoryMethodSourceArgs() {
        return Stream.of(
                Arguments.of(
                     "Raw of material",
                     null,
                     UUID.randomUUID()
                ),
                Arguments.of(
                        "Manufactured",
                        UUID.randomUUID(),
                        UUID.randomUUID()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createProductCategoryMethodSourceArgs")
    void should_create_root_product_category_or_child_category(
            String nameAsString,
            UUID parentProductCategoryUUID,
            UUID newProductCategoryUUID
    ) {
        //Given
        CreateProductCategoryViewRequest createProductCategoryViewRequest = new CreateProductCategoryViewRequest()
                .name(nameAsString)
                .parentProductCategoryId(parentProductCategoryUUID);
        ProductCategory productCategory = ProductCategory.builder()
                .name(Name.of(Text.of(nameAsString)))
                .build();
        CreateProductCategoryViewResponse createProductCategoryViewResponse = new CreateProductCategoryViewResponse()
                .id(newProductCategoryUUID)
                .active(true)
                .parentProductCategoryId(parentProductCategoryUUID)
                .name(nameAsString);

        when(productCategoryViewMapper.toProductCategory(createProductCategoryViewRequest)).thenReturn(productCategory);
        when(productCategoryService.createProductCategory(productCategory)).thenReturn(productCategory);
        when(productCategoryViewMapper.toCreateProductCategoryViewResponse(productCategory)).thenReturn(createProductCategoryViewResponse);

        ArgumentCaptor<CreateProductCategoryViewRequest> createProductCategoryViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(CreateProductCategoryViewRequest.class);
        ArgumentCaptor<ProductCategory> productCategoryArgumentCaptor = ArgumentCaptor.forClass(ProductCategory.class);

        //Act
        CreateProductCategoryViewResponse result = productCategoryApiAdapterService.createProductCategory(createProductCategoryViewRequest);

        assertThat(result)
                .isNotNull()
                .isEqualTo(createProductCategoryViewResponse);

        verify(productCategoryViewMapper, times(1))
                .toProductCategory(createProductCategoryViewRequestArgumentCaptor.capture());
        assertThat(createProductCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(createProductCategoryViewRequest);

        verify(productCategoryService, times(1))
                .createProductCategory(productCategoryArgumentCaptor.capture());
        assertThat(productCategoryArgumentCaptor.getValue()).isEqualTo(productCategory);

        verify(productCategoryViewMapper, times(1))
                .toCreateProductCategoryViewResponse(productCategoryArgumentCaptor.capture());
        assertThat(productCategoryArgumentCaptor.getValue()).isEqualTo(productCategory);
    }

    static Stream<Arguments> createProductCategoryThrowExceptionMethodSourceArgs() {
        return Stream.of(
                Arguments.of(
                        "Raw of material",
                        null,
                        new ProductCategoryNameConflictException(new String[]{PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION}),
                        PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION
                ),
                Arguments.of(
                        "Manufactured",
                        UUID.randomUUID(),
                        new ParentProductCategoryNotFoundException(new String[]{PRODUCT_CATEGORY_PARENT_ID_IS_REQUIRED}),
                        PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION
                )
        );
    }

    @ParameterizedTest
    @MethodSource("createProductCategoryThrowExceptionMethodSourceArgs")
    void should_throw_exception_when_create_product_category(
            String nameAsString,
            UUID parentProductCategoryUUID,
            RuntimeException exceptionClass,
            String exceptionMessage
    ) {

        //Given
        CreateProductCategoryViewRequest createProductCategoryViewRequest = new CreateProductCategoryViewRequest()
                .name(nameAsString)
                .parentProductCategoryId(parentProductCategoryUUID);
        ProductCategory productCategory = ProductCategory.builder()
                .name(Name.of(Text.of(nameAsString)))
                .build();

        when(productCategoryViewMapper.toProductCategory(createProductCategoryViewRequest)).thenReturn(productCategory);
        when(productCategoryService.createProductCategory(productCategory)).thenThrow(exceptionClass);

        ArgumentCaptor<CreateProductCategoryViewRequest> createProductCategoryViewRequestArgumentCaptor =
                ArgumentCaptor.forClass(CreateProductCategoryViewRequest.class);
        ArgumentCaptor<ProductCategory> productCategoryArgumentCaptor = ArgumentCaptor.forClass(ProductCategory.class);

        //Act
        assertThatThrownBy(() -> productCategoryApiAdapterService.createProductCategory(createProductCategoryViewRequest))
                .isInstanceOf(exceptionClass.getClass())
                        .hasMessage(exceptionMessage);

        verify(productCategoryViewMapper, times(1))
                .toProductCategory(createProductCategoryViewRequestArgumentCaptor.capture());
        assertThat(createProductCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(createProductCategoryViewRequest);

        verify(productCategoryService, times(1))
                .createProductCategory(productCategoryArgumentCaptor.capture());
        assertThat(productCategoryArgumentCaptor.getValue()).isEqualTo(productCategory);

    }


}
