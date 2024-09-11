package cm.xenonbyte.farmbyte.catalog.adapter.data.access.test;

import cm.xenonbyte.farmbyte.catalog.domain.core.product.Product;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductId;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ProductType;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Purchasable;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.Sellable;
import cm.xenonbyte.farmbyte.catalog.domain.core.product.ports.secondary.ProductRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Money;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Reference;
import cm.xenonbyte.farmbyte.common.domain.vo.Text;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

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
    protected ProductId productId;
    protected ProductId oldProductId;
    protected Product oldProduct;
    protected Reference reference;

    @Nested
    class CreateProductRepositoryTest {
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

    @Nested
    class FindProductByIdRepositoryTest {

        @Test
        void should_success_when_find_product_with_existing_id() {
            //Given + Act
            Optional<Product> result = productRepository.findById(productId);
            //Then
            assertThat(result.isPresent()).isTrue();
            assertThat(result.get()).isNotNull();
        }

        @Test
        void should_fail_when_find_product_with_non_existing_id() {

            //Given + Act
            Optional<Product> result = productRepository.findById(new ProductId(UUID.randomUUID()));

            //Then
            assertThat(result.isEmpty()).isTrue();
        }
    }

    @Nested
    class FindProductsRepositoryTest {
        @Test
        void should_success_when_find_products() {

            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;

            //Act
            PageInfo<Product> result = productRepository.findAll(page, size, attribute, direction);

            //Then
            assertThat(result.getContent().size()).isGreaterThan(0);
            assertThat(result.getFirst()).isTrue();
            assertThat(result.getLast()).isFalse();
            assertThat(result.getPageSize()).isEqualTo(size);
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class SearchProductsRepositoryTest {

        @Test
        void should_success_when_search_products_by_keyword() {
            //Given
            int page = 0;
            int size = 2;
            String attribute = "name";
            Direction direction = Direction.ASC;
            String keyword = "p";

            //Act
            PageInfo<Product> result = productRepository.search(page, size, attribute, direction, Keyword.of(Text.of(keyword)));

            //Then
            assertThat(result.getContent().size()).isGreaterThan(0);
            assertThat(result.getFirst()).isTrue();
            assertThat(result.getLast()).isFalse();
            assertThat(result.getPageSize()).isEqualTo(size);
            assertThat(result.getTotalElements()).isGreaterThan(0);
            assertThat(result.getTotalPages()).isGreaterThan(0);
        }
    }

    @Nested
    class UpdateProductRepositoryTest {

        @Test
        void should_success_when_update_product() {
            //Given
            Product newProduct = Product.builder()
                    .id(oldProductId)
                    .name(Name.of(Text.of("New HP Pro")))
                    .categoryId(new ProductCategoryId(UUID.randomUUID()))
                    .type(ProductType.CONSUMABLE)
                    .reference(Reference.of(Text.of("64548968799")))
                    .purchasePrice(Money.of(BigDecimal.valueOf(175.47)))
                    .salePrice(Money.of(BigDecimal.valueOf(350.12)))
                    .sellable(Sellable.with(true))
                    .purchasable(Purchasable.with(true))
                    .active(Active.with(true))
                    .build();

            //Act
            Product result = productRepository.update(oldProduct, newProduct);

            //Then
            assertThat(result).isNotNull().isEqualTo(newProduct);
        }

        @Test
        void should_true_when_find_product_by_name() {
            //Given + Act
            Optional<Product> result = productRepository.findByName(name);

            //Then
            assertThat(result.isPresent()).isTrue();
        }

        @Test
        void should_empty_when_find_product_by_name() {
            //Given + Act
            Optional<Product> result = productRepository.findByName(Name.of(Text.of("NaN")));

            //Then
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        void should_true_when_find_product_by_reference() {
            //Given + Act
            Optional<Product> result = productRepository.findByReference(reference);

            //Then
            assertThat(result.isPresent()).isTrue();
        }

        @Test
        void should_empty_when_find_product_by_reference() {
            //Given + Act
            Optional<Product> result = productRepository.findByReference(Reference.of(Text.of("NaN")));

            //Then
            assertThat(result.isEmpty()).isTrue();
        }

        @Test
        void should_true_when_exists_product_by_reference() {
            //Given + Act
            boolean result = productRepository.existsByReference(reference);

            //Then
            assertThat(result).isTrue();
        }

        @Test
        void should_empty_when_exists_product_by_reference() {
            //Given + Act
            boolean result = productRepository.existsByReference(Reference.of(Text.of("NaN")));

            //Then
            assertThat(result).isFalse();
        }


    }
}
