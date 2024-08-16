package cm.xenonbyte.farmbyte.catalog.adapter.data.access.test;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bamk
 * @version 1.0
 * @since 16/08/2024
 */
public abstract class ProductCategoryRepositoryTest {

    protected ProductCategoryRepository productCategoryRepository;
    protected Name name;
    protected ProductCategoryId parentProductCategoryId;

    @Test
    protected void should_return_false_when_product_category_with_given_name_not_exist() {
        //Act
        Boolean result = productCategoryRepository.existsByName(Name.of("Anything"));

        //Then
        assertThat(result).isFalse();

    }

    @Test
    protected void should_return_true_when_product_category_with_given_name_exist() {
        //Act
        Boolean result = productCategoryRepository.existsByName(name);

        //Then
        assertThat(result).isTrue();

    }

    @Test
    protected void should_return_false_when_given_parent_product_category_id_does_not_exist() {
        //Act
        Boolean result = productCategoryRepository.existsById(new ProductCategoryId(UUID.randomUUID()));

        //Then
        assertThat(result).isFalse();

    }

    @Test
    protected void should_return_true_when_given_parent_product_category_id__exist() {
        //Act
        Boolean result = productCategoryRepository.existsById(parentProductCategoryId);

        //Then
        assertThat(result).isTrue();

    }

    @Test
    protected void should_create_new_uom_category() {
        //Given
        Name volumeCategory = Name.of("Raw Material");
        ProductCategory productCategory = ProductCategory.of(volumeCategory);
        productCategory.initiate();

        //Act
        ProductCategory createdProductCategory = productCategoryRepository.save(productCategory);
        Boolean result = productCategoryRepository.existsByName(volumeCategory);

        //Then
        assertThat(createdProductCategory.getId()).isNotNull();
        assertThat(result).isTrue();

    }



}
