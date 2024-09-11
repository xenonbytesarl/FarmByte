package cm.xenonbyte.farmbyte.catalog.adapter.rest.api.product;

import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.CreateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.FindProductCategoryByIdViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.SearchProductCategoriesPageInfoViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.SearchProductCategoriesViewResponse;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.UpdateProductCategoryViewRequest;
import cm.xenonbyte.farmbyte.catalog.adapter.rest.api.generated.productcategory.view.UpdateProductCategoryViewResponse;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductParentCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductCategoryService;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PARENT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION;
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

    @Nested
    class CreateProductCategoryDomainServiceApiAdapterTest {

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
                            new ProductParentCategoryNotFoundException(new String[]{PRODUCT_CATEGORY_PARENT_ID_IS_REQUIRED}),
                            PRODUCT_PARENT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION
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

    @Nested
    class FindProductCategoryByIdDomainServiceApiAdapterTest {
        @Test
        void should_success_when_find_product_category_with_existing_id() {
            //Given
            UUID productCategoryIdUUID = UUID.fromString("0191ab3c-1a4b-7202-935e-4eb5d7710981");
            ProductCategoryId productCategoryId = new ProductCategoryId(productCategoryIdUUID);
            String name = "Unite";
            ProductCategory productCategory = ProductCategory.builder()
                    .name(Name.of(Text.of(name)))
                    .id(new ProductCategoryId(productCategoryIdUUID))
                    .build();

            FindProductCategoryByIdViewResponse findProductCategoryByIdViewResponse = new FindProductCategoryByIdViewResponse()
                    .id(productCategoryIdUUID)
                    .name(name);

            when(productCategoryService.findProductCategoryById(productCategoryId)).thenReturn(productCategory);
            when(productCategoryViewMapper.toFindProductCategoryViewResponse(productCategory)).thenReturn(findProductCategoryByIdViewResponse);

            ArgumentCaptor<ProductCategoryId> productCategoryIdArgumentCaptor = ArgumentCaptor.forClass(ProductCategoryId.class);
            ArgumentCaptor<ProductCategory> productCategoryArgumentCaptor = ArgumentCaptor.forClass(ProductCategory.class);

            //Act
            FindProductCategoryByIdViewResponse result = productCategoryApiAdapterService.findProductCategoryById(productCategoryIdUUID);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(findProductCategoryByIdViewResponse);

            verify(productCategoryService, times(1)).findProductCategoryById(productCategoryIdArgumentCaptor.capture());
            verify(productCategoryViewMapper, times(1)).toFindProductCategoryViewResponse(productCategoryArgumentCaptor.capture());

            assertThat(productCategoryIdArgumentCaptor.getValue()).isEqualTo(productCategoryId);
            assertThat(productCategoryArgumentCaptor.getValue()).isEqualTo(productCategory);


        }

        @Test
        void should_fail_when_find_product_category_with_non_existing_id() {
            //Given
            UUID productCategoryIdUUID = UUID.randomUUID();
            ProductCategoryId productCategoryId = new ProductCategoryId(productCategoryIdUUID);

            when(productCategoryService.findProductCategoryById(productCategoryId)).thenThrow(ProductCategoryNotFoundException.class);
            ArgumentCaptor<ProductCategoryId> productCategoryIdArgumentCaptor = ArgumentCaptor.forClass(ProductCategoryId.class);

            //Act + Then
            assertThatThrownBy(() -> productCategoryApiAdapterService.findProductCategoryById(productCategoryIdUUID))
                    .isInstanceOf(ProductCategoryNotFoundException.class);

            verify(productCategoryService, times(1)).findProductCategoryById(productCategoryIdArgumentCaptor.capture());

            assertThat(productCategoryIdArgumentCaptor.getValue()).isEqualTo(productCategoryId);
        }
    }

    @Nested
    class FindProductCategoriesDomainServiceApiAdapterTest {
        @Test
        void should_success_when_find_product_category_with_existing_id() {
            //Given

            int page = 1;
            int pageSize = 2;
            String attribute = "name";

            PageInfo<ProductCategory> productCategoriesPageInfo =  new PageInfo<ProductCategory>().with(
                    page, pageSize,
                    List.of(
                            ProductCategory.builder()
                                    .name(Name.of(Text.of("Alcohol")))
                                    .id(new ProductCategoryId(UUID.randomUUID()))
                                    .build(),
                            ProductCategory.builder()
                                    .name(Name.of(Text.of("Juice")))
                                    .id(new ProductCategoryId(UUID.randomUUID()))
                                    .build(),
                            ProductCategory.builder()
                                    .name(Name.of(Text.of("Water")))
                                    .id(new ProductCategoryId(UUID.randomUUID()))
                                    .build(),
                            ProductCategory.builder()
                                    .name(Name.of(Text.of("Fresh")))
                                    .id(new ProductCategoryId(UUID.randomUUID()))
                                    .build(),
                            ProductCategory.builder()
                                    .name(Name.of(Text.of("Meet")))
                                    .id(new ProductCategoryId(UUID.randomUUID()))
                                    .build()
                    )
            );

            FindProductCategoriesPageInfoViewResponse findCategoriesPageInfoViewResponse = new FindProductCategoriesPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
                    .content(
                            List.of(
                                    new FindProductCategoriesViewResponse()
                                            .name("Alcohol")
                                            .id(UUID.randomUUID())
                                            .active(true),
                                    new FindProductCategoriesViewResponse()
                                            .name("Juice")
                                            .id(UUID.randomUUID())
                                            .active(true),
                                    new FindProductCategoriesViewResponse()
                                            .name("Water")
                                            .id(UUID.randomUUID())
                                            .active(true),
                                    new FindProductCategoriesViewResponse()
                                            .name("Fresh")
                                            .id(UUID.randomUUID())
                                            .active(true),
                                    new FindProductCategoriesViewResponse()
                                            .name("Meet")
                                            .id(UUID.randomUUID())
                                            .active(true)
                            )
                    );


            when(productCategoryService.findProductCategories(page, pageSize, "name", Direction.ASC)).thenReturn(productCategoriesPageInfo);
            when(productCategoryViewMapper.toFindProductCategoriesPageInfoViewResponse(productCategoriesPageInfo)).thenReturn(findCategoriesPageInfoViewResponse);

            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);

            //Act
            FindProductCategoriesPageInfoViewResponse result = productCategoryApiAdapterService.findProductCategories(page, pageSize, attribute, "ASC");

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(findCategoriesPageInfoViewResponse);

            verify(productCategoryService, times(1))
                    .findProductCategories(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture());

            verify(productCategoryViewMapper, times(1)).toFindProductCategoriesPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(pageSize);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().get(0)).isEqualTo(Direction.ASC);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(productCategoriesPageInfo);


        }
    }


    @Nested
    class SearchProductCategoriesDomainServiceRestApiAdapterTest {

        @Test
        void should_success_when_search_product_category_with_existing_keyword() {
            //Given

            int page = 1;
            int pageSize = 2;
            String keyword = "e";
            String attribute = "name";

            PageInfo<ProductCategory> productCategoriesPageInfo =  new PageInfo<ProductCategory>().with(
                    1, 2,
                    List.of(
                            ProductCategory.builder()
                                    .name(Name.of(Text.of("Alcohol")))
                                    .id(new ProductCategoryId(UUID.randomUUID()))
                                    .active(Active.with(true))
                                    .build(),
                            ProductCategory.builder()
                                    .name(Name.of(Text.of("Juice")))
                                    .id(new ProductCategoryId(UUID.randomUUID()))
                                    .active(Active.with(true))
                                    .build(),
                            ProductCategory.builder()
                                    .name(Name.of(Text.of("Water")))
                                    .id(new ProductCategoryId(UUID.randomUUID()))
                                    .active(Active.with(true))
                                    .build(),
                            ProductCategory.builder()
                                    .name(Name.of(Text.of("Fresh")))
                                    .id(new ProductCategoryId(UUID.randomUUID()))
                                    .active(Active.with(true))
                                    .build(),
                            ProductCategory.builder()
                                    .name(Name.of(Text.of("Meet")))
                                    .id(new ProductCategoryId(UUID.randomUUID()))
                                    .active(Active.with(true))
                                    .build()
                    )
            );

            SearchProductCategoriesPageInfoViewResponse searchCategoriesPageInfoViewResponse = new SearchProductCategoriesPageInfoViewResponse()
                    .first(true)
                    .last(false)
                    .pageSize(2)
                    .totalElements(5L)
                    .totalPages(3)
                    .content(
                            List.of(
                                    new SearchProductCategoriesViewResponse()
                                            .name("Alcohol")
                                            .id(UUID.randomUUID())
                                            .active(true),
                                    new SearchProductCategoriesViewResponse()
                                            .name("Juice")
                                            .id(UUID.randomUUID())
                                            .active(true),
                                    new SearchProductCategoriesViewResponse()
                                            .name("Water")
                                            .id(UUID.randomUUID())
                                            .active(true),
                                    new SearchProductCategoriesViewResponse()
                                            .name("Fresh")
                                            .id(UUID.randomUUID())
                                            .active(true),
                                    new SearchProductCategoriesViewResponse()
                                            .name("Meet")
                                            .id(UUID.randomUUID())
                                            .active(true)
                            )
                    );


            when(productCategoryService.searchProductCategories(page, pageSize, "name", Direction.ASC, Keyword.of(Text.of(keyword)))).thenReturn(productCategoriesPageInfo);
            when(productCategoryViewMapper.toSearchProductCategoriesPageInfoViewResponse(productCategoriesPageInfo)).thenReturn(searchCategoriesPageInfoViewResponse);

            ArgumentCaptor<PageInfo> pageInfoArgumentCaptor = ArgumentCaptor.forClass(PageInfo.class);
            ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Direction> directionArgumentCaptor = ArgumentCaptor.forClass(Direction.class);
            ArgumentCaptor<Keyword> keywordArgumentCaptor = ArgumentCaptor.forClass(Keyword.class);

            //Act
            SearchProductCategoriesPageInfoViewResponse result = productCategoryApiAdapterService.searchProductCategories(page, pageSize, attribute, "ASC", keyword);

            //Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(searchCategoriesPageInfoViewResponse);
            verify(productCategoryService, times(1))
                    .searchProductCategories(integerArgumentCaptor.capture(), integerArgumentCaptor.capture(),
                            stringArgumentCaptor.capture(), directionArgumentCaptor.capture(), keywordArgumentCaptor.capture());
            verify(productCategoryViewMapper, times(1)).toSearchProductCategoriesPageInfoViewResponse(pageInfoArgumentCaptor.capture());

            assertThat(integerArgumentCaptor.getAllValues().get(0)).isEqualTo(page);
            assertThat(integerArgumentCaptor.getAllValues().get(1)).isEqualTo(pageSize);
            assertThat(stringArgumentCaptor.getAllValues().get(0)).isEqualTo(attribute);
            assertThat(directionArgumentCaptor.getAllValues().get(0)).isEqualTo(Direction.ASC);
            assertThat(keywordArgumentCaptor.getValue().getText().getValue()).isEqualTo(keyword);
            assertThat(pageInfoArgumentCaptor.getValue()).isEqualTo(productCategoriesPageInfo);


        }

    }

    @Nested
    class UpdateProductCategoryDomainServiceRestApiAdapterTest {
        @Test
        void should_success_when_update_product_category() {
            //Given
            UUID productCategoryIdUUID = UUID.fromString("0191e0c2-91ab-7f43-bbb4-085f715d1136");
            String name = "New Raw Of Material";
            boolean active = true;
            ProductCategoryId productCategoryId = new ProductCategoryId(productCategoryIdUUID);
            UpdateProductCategoryViewRequest updateProductCategoryViewRequest = new UpdateProductCategoryViewRequest()
                    .id(productCategoryIdUUID)
                    .name(name)
                    .active(active);
            ProductCategory productCategory = ProductCategory.builder()
                    .id(productCategoryId)
                    .name(Name.of(Text.of(name)))
                    .active(Active.with(active))
                    .build();
            UpdateProductCategoryViewResponse updateProductCategoryViewResponse = new UpdateProductCategoryViewResponse()
                    .id(productCategoryIdUUID)
                    .name(name)
                    .active(active);

            when(productCategoryViewMapper.toProductCategory(updateProductCategoryViewRequest)).thenReturn(productCategory);
            when(productCategoryService.updateProductCategory(productCategoryId, productCategory)).thenReturn(productCategory);
            when(productCategoryViewMapper.toUpdateProductCategoryViewResponse(productCategory)).thenReturn(updateProductCategoryViewResponse);

            ArgumentCaptor<ProductCategoryId> productCategoryIdArgumentCaptor = ArgumentCaptor.forClass(ProductCategoryId.class);
            ArgumentCaptor<UpdateProductCategoryViewRequest> updateProductCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(UpdateProductCategoryViewRequest.class);
            ArgumentCaptor<ProductCategory> productCategoryArgumentCaptor = ArgumentCaptor.forClass(ProductCategory.class);

            //Act
            UpdateProductCategoryViewResponse result = productCategoryApiAdapterService.updateProductCategory(productCategoryIdUUID, updateProductCategoryViewRequest);

            //Then
            assertThat(result).isNotNull().isEqualTo(updateProductCategoryViewResponse);

            verify(productCategoryService, times(1)).updateProductCategory(productCategoryIdArgumentCaptor.capture(), productCategoryArgumentCaptor.capture());
            verify(productCategoryViewMapper, times(1)).toProductCategory(updateProductCategoryViewRequestArgumentCaptor.capture());
            verify(productCategoryViewMapper, times(1)).toUpdateProductCategoryViewResponse(productCategoryArgumentCaptor.capture());

            assertThat(productCategoryIdArgumentCaptor.getValue()).isEqualTo(productCategoryId);
            assertThat(updateProductCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(updateProductCategoryViewRequest);
            assertThat(productCategoryArgumentCaptor.getValue()).isEqualTo(productCategory);
        }

        @Test
        void should_fail_when_update_product_category_with_non_existing_id() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            UUID productCategoryIdUUID = UUID.fromString("0191e0c2-c8db-7cce-b4d4-9476228bacf4");
            String name = "New Raw Of Material";
            boolean active = true;
            ProductCategoryId productCategoryId = new ProductCategoryId(productCategoryIdUUID);
            UpdateProductCategoryViewRequest updateProductCategoryViewRequest = new UpdateProductCategoryViewRequest()
                    .id(productCategoryIdUUID)
                    .name(name)
                    .active(active);
            ProductCategory productCategory = ProductCategory.builder()
                    .id(productCategoryId)
                    .name(Name.of(Text.of(name)))
                    .active(Active.with(active))
                    .build();

            when(productCategoryViewMapper.toProductCategory(updateProductCategoryViewRequest)).thenReturn(productCategory);
            when(productCategoryService.updateProductCategory(productCategoryId, productCategory))
                    .thenThrow(ProductCategoryNotFoundException.class.getConstructor(Object[].class)
                            .newInstance(new Object[]{new String[]{productCategoryIdUUID.toString()}}));

            ArgumentCaptor<ProductCategoryId> productCategoryIdArgumentCaptor = ArgumentCaptor.forClass(ProductCategoryId.class);
            ArgumentCaptor<UpdateProductCategoryViewRequest> updateProductCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(UpdateProductCategoryViewRequest.class);
            ArgumentCaptor<ProductCategory> productCategoryArgumentCaptor = ArgumentCaptor.forClass(ProductCategory.class);

            //Act + Then
            assertThatThrownBy(() -> productCategoryApiAdapterService.updateProductCategory(productCategoryIdUUID, updateProductCategoryViewRequest))
                    .isInstanceOf(ProductCategoryNotFoundException.class)
                    .hasMessage(PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION);

            verify(productCategoryService, times(1)).updateProductCategory(productCategoryIdArgumentCaptor.capture(), productCategoryArgumentCaptor.capture());
            verify(productCategoryViewMapper, times(1)).toProductCategory(updateProductCategoryViewRequestArgumentCaptor.capture());

            assertThat(productCategoryIdArgumentCaptor.getValue()).isEqualTo(productCategoryId);
            assertThat(updateProductCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(updateProductCategoryViewRequest);
            assertThat(productCategoryArgumentCaptor.getValue()).isEqualTo(productCategory);
        }

        @Test
        void should_fail_when_update_product_category_with_non_parent_product_id() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            UUID productCategoryIdUUID = UUID.fromString("0191e0c2-e802-7c33-ab66-6f8e1746ca09");
            String name = "New Raw Of Material";
            boolean active = true;
            UUID parentProductCategoryIdUUID = UUID.randomUUID();
            ProductCategoryId productCategoryId = new ProductCategoryId(productCategoryIdUUID);
            UpdateProductCategoryViewRequest updateProductCategoryViewRequest = new UpdateProductCategoryViewRequest()
                    .id(productCategoryIdUUID)
                    .parentProductCategoryId(parentProductCategoryIdUUID)
                    .name(name)
                    .active(active);
            ProductCategory productCategory = ProductCategory.builder()
                    .id(productCategoryId)
                    .name(Name.of(Text.of(name)))
                    .parentProductCategoryId(new ProductCategoryId(parentProductCategoryIdUUID))
                    .active(Active.with(active))
                    .build();

            when(productCategoryViewMapper.toProductCategory(updateProductCategoryViewRequest)).thenReturn(productCategory);
            when(productCategoryService.updateProductCategory(productCategoryId, productCategory))
                    .thenThrow(ProductParentCategoryNotFoundException.class.getConstructor(Object[].class)
                            .newInstance(new Object[]{new String[]{parentProductCategoryIdUUID.toString()}}));

            ArgumentCaptor<ProductCategoryId> productCategoryIdArgumentCaptor = ArgumentCaptor.forClass(ProductCategoryId.class);
            ArgumentCaptor<UpdateProductCategoryViewRequest> updateProductCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(UpdateProductCategoryViewRequest.class);
            ArgumentCaptor<ProductCategory> productCategoryArgumentCaptor = ArgumentCaptor.forClass(ProductCategory.class);

            //Act + Then
            assertThatThrownBy(() -> productCategoryApiAdapterService.updateProductCategory(productCategoryIdUUID, updateProductCategoryViewRequest))
                    .isInstanceOf(ProductParentCategoryNotFoundException.class)
                    .hasMessage(PRODUCT_PARENT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION);

            verify(productCategoryService, times(1)).updateProductCategory(productCategoryIdArgumentCaptor.capture(), productCategoryArgumentCaptor.capture());
            verify(productCategoryViewMapper, times(1)).toProductCategory(updateProductCategoryViewRequestArgumentCaptor.capture());

            assertThat(productCategoryIdArgumentCaptor.getValue()).isEqualTo(productCategoryId);
            assertThat(updateProductCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(updateProductCategoryViewRequest);
            assertThat(productCategoryArgumentCaptor.getValue()).isEqualTo(productCategory);
        }

        @Test
        void should_fail_when_update_product_category_with_duplicate_name() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            //Given
            UUID productCategoryIdUUID = UUID.fromString("0191e0c3-0f6d-7daf-b1d7-5c073f69bf02");
            String name = "New Raw Of Material";
            boolean active = true;
            UUID parentProductCategoryIdUUID = UUID.randomUUID();
            ProductCategoryId productCategoryId = new ProductCategoryId(productCategoryIdUUID);
            UpdateProductCategoryViewRequest updateProductCategoryViewRequest = new UpdateProductCategoryViewRequest()
                    .id(productCategoryIdUUID)
                    .parentProductCategoryId(parentProductCategoryIdUUID)
                    .name(name)
                    .active(active);
            ProductCategory productCategory = ProductCategory.builder()
                    .id(productCategoryId)
                    .name(Name.of(Text.of(name)))
                    .parentProductCategoryId(new ProductCategoryId(parentProductCategoryIdUUID))
                    .active(Active.with(active))
                    .build();

            when(productCategoryViewMapper.toProductCategory(updateProductCategoryViewRequest)).thenReturn(productCategory);
            when(productCategoryService.updateProductCategory(productCategoryId, productCategory))
                    .thenThrow(ProductCategoryNameConflictException.class.getConstructor(Object[].class)
                            .newInstance(new Object[]{new String[]{name}}));

            ArgumentCaptor<ProductCategoryId> productCategoryIdArgumentCaptor = ArgumentCaptor.forClass(ProductCategoryId.class);
            ArgumentCaptor<UpdateProductCategoryViewRequest> updateProductCategoryViewRequestArgumentCaptor =
                    ArgumentCaptor.forClass(UpdateProductCategoryViewRequest.class);
            ArgumentCaptor<ProductCategory> productCategoryArgumentCaptor = ArgumentCaptor.forClass(ProductCategory.class);

            //Act + Then
            assertThatThrownBy(() -> productCategoryApiAdapterService.updateProductCategory(productCategoryIdUUID, updateProductCategoryViewRequest))
                    .isInstanceOf(ProductCategoryNameConflictException.class)
                    .hasMessage(PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION);

            verify(productCategoryService, times(1)).updateProductCategory(productCategoryIdArgumentCaptor.capture(), productCategoryArgumentCaptor.capture());
            verify(productCategoryViewMapper, times(1)).toProductCategory(updateProductCategoryViewRequestArgumentCaptor.capture());

            assertThat(productCategoryIdArgumentCaptor.getValue()).isEqualTo(productCategoryId);
            assertThat(updateProductCategoryViewRequestArgumentCaptor.getValue()).isEqualTo(updateProductCategoryViewRequest);
            assertThat(productCategoryArgumentCaptor.getValue()).isEqualTo(productCategory);
        }
    }

}
