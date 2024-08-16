package cm.xenonbyte.farmbyte.catalog.domain.test;

import cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory.InMemoryProductCategoryRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ParentProductCategoryNotFoundException;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ProductCategoryNameConflictException;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ProductCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ports.primary.IProductCategoryService;
import cm.xenonbyte.farmbyte.catalog.domain.core.productcategory.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public final class ProductCategoryTest {
    private IProductCategoryService productCategoryService;
    private ProductCategory parentProductCategory;

    @BeforeEach
    void setUp() {
        ProductCategoryRepository productCategoryRepository;
        productCategoryRepository = new InMemoryProductCategoryRepository();
        productCategoryService = new ProductCategoryService(productCategoryRepository);

        parentProductCategory = ProductCategory.of(new Name("Raw Material"));
        parentProductCategory.initiate();
        productCategoryRepository.save(parentProductCategory);
    }

    @Test
    void should_create_product_category_as_root_category() {
        //Given
        Name productCategoryName = Name.of("Unite");
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
        Name productCategoryName = Name.of("Manufactured");
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
        Name productCategoryName = Name.of("Raw Material");
        ProductCategory productCategory = ProductCategory.of(productCategoryName);

        //Act + Then
        assertThatThrownBy(() -> productCategoryService.createProductCategory(productCategory))
                .isInstanceOf(ProductCategoryNameConflictException.class)
                .hasMessage(PRODUCT_CATEGORY_NAME_CONFLICT_EXCEPTION);
    }

    @Test
    void should_throw_exception_when_create_child_category_with_non_existing_parent() {
        //Given
        Name productCategoryName = Name.of("Raw Material");
        ProductCategory productCategory = ProductCategory.of(productCategoryName, new ProductCategoryId(UUID.randomUUID()));

        assertThatThrownBy(() -> productCategoryService.createProductCategory(productCategory))
                .isInstanceOf(ParentProductCategoryNotFoundException.class)
                .hasMessage(PARENT_PRODUCT_CATEGORY_WITH_ID_NOT_FOUND_EXCEPTION);
    }
}
