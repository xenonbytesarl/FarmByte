package cm.xenonbyte.farmbyte.catalog.adapter.data.access.test;

import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
public abstract class ProductRepositoryTest {

    protected ProductRepository productRepository;
    protected Name name;
    protected ProductCategoryId categoryId;
    protected Filename imageName;

    @Test
    protected void should_return_false_when_find_product_with_an_un_existing_name() {
        //Act
        Boolean result = productRepository.existsByName(Name.of(Text.of("Product.1")));

        //Then
        assertThat(result).isFalse();
    }

    @Test
    protected void should_return_true_when_find_product_with_an_existing_name() {
        //Act
        Boolean result = productRepository.existsByName(name);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    protected void should_create_product() {
        //Given
        Name name1 = Name.of(Text.of("Product.3"));
        Product product = Product.builder()
                .name(name1)
                .categoryId(categoryId)
                .type(ProductType.SERVICE)
                .imageName(imageName)
                .build();
        product.validate();
        product.initiate();

        //Act
        Product result = productRepository.save(product);
        Boolean isExistByName = productRepository.existsByName(name1);

        //Then
        assertThat(result)
                .isNotNull()
                .isEqualTo(product);
        assertThat(isExistByName).isTrue();
    }
}
