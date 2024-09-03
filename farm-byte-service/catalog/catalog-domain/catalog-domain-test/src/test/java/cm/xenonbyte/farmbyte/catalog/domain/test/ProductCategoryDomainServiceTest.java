package cm.xenonbyte.farmbyte.catalog.domain.test;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.ProductCategoryInMemoryRepositoryAdapter;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ParentProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryDomainService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.primary.ProductCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public final class ProductCategoryDomainServiceTest {
    private ProductCategoryService productCategoryService;
    private ProductCategory parentProductCategory;
    private ProductCategoryRepository productCategoryRepository;

    @BeforeEach
    void setUp() {

        productCategoryRepository = new ProductCategoryInMemoryRepositoryAdapter();
        productCategoryService = new ProductCategoryDomainService(productCategoryRepository);

        parentProductCategory = ProductCategory.of(new Name(Text.of("Raw Material")));
        parentProductCategory.initiate();
        productCategoryRepository.save(parentProductCategory);
    }

    @Nested
    class CreateProductCategoryDomainServiceTest {
        @Test
        void should_create_product_category_as_root_category() {
            //Given
            Name productCategoryName = Name.of(Text.of("Unite"));
            ProductCategory productCategory = ProductCategory.of(productCategoryName);

            //Act
            ProductCategory createdProductCategory = productCategoryService.createProductCategory(productCategory);

            //Then
            assertThat(createdProductCategory).isNotNull();
            assertThat(createdProductCategory.getName()).isEqualTo(productCategoryName);
            assertThat(createdProductCategory.getActive().getValue()).isTrue();
            assertThat(createdProductCategory.getId().getValue())
                    .isInstanceOf(UUID.class)
                    .isNotNull();
        }

        @Test
        void should_create_child_uom_category_when_child_exist() {
            //Given
            Name productCategoryName = Name.of(Text.of("Manufactured"));
            ProductCategory productCategory = ProductCategory.of(productCategoryName, parentProductCategory.getId());

            //Act
            ProductCategory childProductCategory = productCategoryService.createProductCategory(productCategory);

            //Then
            assertThat(childProductCategory).isNotNull();
            assertThat(childProductCategory.getName()).isEqualTo(productCategoryName);
            assertThat(childProductCategory.getActive().getValue()).isTrue();
            assertThat(childProductCategory.getId().getValue())
                    .isInstanceOf(UUID.class)
                    .isNotNull();
            assertThat(childProductCategory.getParentProductCategoryId()).isEqualTo(parentProductCategory.getId());
        }

        @Test
        void should_throw_exception_when_uom_category_name_exists() {
            //Given
            Name productCategoryName = Name.of(Text.of("Raw Material"));
            ProductCategory productCategory = ProductCategory.of(productCategoryName);

            //Act + Then
            assertThatThrownBy(() -> productCategoryService.createProductCategory(productCategory))
                    .isInstanceOf(ProductCategoryNameConflictException.class)
                    .hasMessage(PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION);
        }

        @Test
        void should_throw_exception_when_create_child_category_with_non_existing_parent() {
            //Given
            Name productCategoryName = Name.of(Text.of("Raw Material"));
            ProductCategory productCategory = ProductCategory.of(productCategoryName, new ProductCategoryId(UUID.randomUUID()));

            assertThatThrownBy(() -> productCategoryService.createProductCategory(productCategory))
                    .isInstanceOf(ParentProductCategoryNotFoundException.class)
                    .hasMessage(PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION);
        }
    }

    @Nested
    class FindProductByIdCategoryDomainServiceTest {
        ProductCategoryId productCategoryId;
        ProductCategory productCategory;

        @BeforeEach
        void setUp() {
            productCategoryId = new ProductCategoryId(UUID.fromString("0191b7be-3e39-71d1-b15d-62363ad0871d"));



            productCategory = productCategoryRepository.save(ProductCategory.builder()
                    .id(productCategoryId)
                    .active(Active.with(true))
                    .name(Name.of(Text.of("Raw Material")))
                    .build());
        }

        @Test
        void should_success_when_find_product_with_existing_id() {
            //Act
            ProductCategory result = productCategoryService.findProductCategoryById(productCategoryId);

            assertThat(result)
                    .isNotNull()
                    .isEqualTo(productCategory);
        }

        @Test
        void should_fail_when_find_product_with_non_existing_id() {
            //Act
            assertThatThrownBy(() -> productCategoryService.findProductCategoryById(new ProductCategoryId(UUID.randomUUID())))
                    .isInstanceOf(ProductCategoryNotFoundException.class)
                    .hasMessage(PRODUCT_CATEGORY_NOT_FOUND_EXCEPTION);
        }
    }

    @Nested
    class FindProductCategoriesDomainServiceTest {
        @BeforeEach
        void setUp() {
            productCategoryRepository.save(ProductCategory.builder()
                    .id(new ProductCategoryId(UUID.fromString("0191b7d9-937e-7112-bbf3-a88f04541075")))
                    .name(Name.of(Text.of("Alcohol")))
                    .build());
            productCategoryRepository.save(ProductCategory.builder()
                    .id(new ProductCategoryId(UUID.fromString("0191b7d9-bb42-7093-b234-d2b32de6be3a")))
                    .name(Name.of(Text.of("Juice")))
                    .build());
            productCategoryRepository.save(ProductCategory.builder()
                    .id(new ProductCategoryId(UUID.fromString("0191b7d9-d7ed-7000-a975-e5b75795e5ba")))
                    .name(Name.of(Text.of("Pastry Shop")))
                    .build());
            productCategoryRepository.save(ProductCategory.builder()
                    .id(new ProductCategoryId(UUID.fromString("0191b7d9-f89b-7152-b952-9a0670bb6e0c")))
                    .name(Name.of(Text.of("Water")))
                    .build());
            productCategoryRepository.save(ProductCategory.builder()
                    .id(new ProductCategoryId(UUID.fromString("0191b7da-1d9d-7541-8a0c-97afa0528c3c")))
                    .name(Name.of(Text.of("Fresh")))
                    .build());
        }

        static Stream<Arguments> findUomCategoriesMethodSource() {
            return Stream.of(
                    Arguments.of(2, 2, "name", Direction.ASC, 3, 6l, 2,2),
                    Arguments.of(2, 2, "name", Direction.DSC, 3, 6l, 2,2)
            );
        }
        @ParameterizedTest
        @MethodSource("findUomCategoriesMethodSource")
        void should_success_when_find_all_product_categories(
                Integer page,
                Integer size,
                String sortAttribute,
                Direction direction,
                Integer totalPages,
                Long totalElements,
                Integer pageSize,
                Integer contentSize
        ) {
            //Then
            PageInfo<ProductCategory> result = productCategoryService.findProductCategories(page, size, sortAttribute, direction);

            assertThat(result.getTotalElements()).isEqualTo(totalElements);
            assertThat(result.getTotalPages()).isEqualTo(totalPages);
            assertThat(result.getPageSize()).isEqualTo(pageSize);
            assertThat(result.getContent().size()).isEqualTo(contentSize);
        }
    }

    @Nested
    class SearchProductCategoriesDomainServiceTest {
        @BeforeEach
        void setUp() {
            productCategoryRepository.save(ProductCategory.builder()
                    .id(new ProductCategoryId(UUID.fromString("0191b7d9-937e-7112-bbf3-a88f04541075")))
                    .name(Name.of(Text.of("Alcohol")))
                    .build());
            productCategoryRepository.save(ProductCategory.builder()
                    .id(new ProductCategoryId(UUID.fromString("0191b7d9-bb42-7093-b234-d2b32de6be3a")))
                    .name(Name.of(Text.of("Juice")))
                    .build());
            productCategoryRepository.save(ProductCategory.builder()
                    .id(new ProductCategoryId(UUID.fromString("0191b7d9-d7ed-7000-a975-e5b75795e5ba")))
                    .name(Name.of(Text.of("Pastry Shop")))
                    .build());
            productCategoryRepository.save(ProductCategory.builder()
                    .id(new ProductCategoryId(UUID.fromString("0191b7d9-f89b-7152-b952-9a0670bb6e0c")))
                    .name(Name.of(Text.of("Water")))
                    .build());
            productCategoryRepository.save(ProductCategory.builder()
                    .id(new ProductCategoryId(UUID.fromString("0191b7da-1d9d-7541-8a0c-97afa0528c3c")))
                    .name(Name.of(Text.of("Fresh")))
                    .build());
        }

        static Stream<Arguments> searchUomCategoriesMethodSource() {
            return Stream.of(
                    Arguments.of(1, 2, "name", Direction.ASC, "r", 2, 4l, 2,2),
                    Arguments.of(1, 2, "name", Direction.DSC, "r", 2, 4l, 2,2)
            );
        }
        @ParameterizedTest
        @MethodSource("searchUomCategoriesMethodSource")
        void should_success_when_search_product_categories_with_keyword(
                Integer page,
                Integer size,
                String sortAttribute,
                Direction direction,
                String keyword,
                Integer totalPages,
                Long totalElements,
                Integer pageSize,
                Integer contentSize
        ) {
            //Then
            PageInfo<ProductCategory> result = productCategoryService.searchProductCategories(page, size, sortAttribute, direction, Keyword.of(Text.of(keyword)));

            assertThat(result.getTotalElements()).isEqualTo(totalElements);
            assertThat(result.getTotalPages()).isEqualTo(totalPages);
            assertThat(result.getPageSize()).isEqualTo(pageSize);
            assertThat(result.getContent().size()).isEqualTo(contentSize);
        }
    }
}
