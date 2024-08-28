package cm.xenonbyte.farmbyte.catalog.domain.core.product;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.common.domain.entity.AggregateRoot;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Filename;
import cm.xenonbyte.farmbyte.common.domain.vo.Money;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Reference;

import java.util.UUID;

import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_CATEGORY_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_NAME_IS_REQUIRED;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PURCHASE_PRICE_SHOULD_BE_GREATER_THAN_ZERO;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_SALE_PRICE_SHOULD_BE_GREATER_THAN_ZERO;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_STOCK_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK;
import static cm.xenonbyte.farmbyte.catalog.domain.core.constant.CatalogDomainCoreConstant.PRODUCT_TYPE_IS_REQUIRED;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public final class Product extends AggregateRoot<ProductId> {


    private final Name name;
    private final ProductCategoryId categoryId;
    private final UomId stockUomId;
    private final UomId purchaseUomId;
    private final ProductType type;
    private Reference reference;
    private Filename imageName;
    private Money salePrice;
    private Money purchasePrice;
    private Active active;
    private Sellable sellable;
    private Purchasable purchasable;

    private Product(Builder builder) {
        setId(builder.id);
        name = builder.name;
        categoryId = builder.categoryId;
        stockUomId = builder.stockUomId;
        purchaseUomId = builder.purchaseUomId;
        type = builder.type;
        reference = builder.reference;
        imageName = builder.imageName;
        salePrice = builder.salePrice;
        purchasePrice = builder.purchasePrice;
        active = builder.active;
        sellable = builder.sellable;
        purchasable = builder.purchasable;
    }

    public void validate() {
        if(name == null) {
            throw new IllegalArgumentException(PRODUCT_NAME_IS_REQUIRED);
        }

        if(categoryId == null) {
            throw new IllegalArgumentException(PRODUCT_CATEGORY_IS_REQUIRED);
        }

        if(type == null) {
            throw new IllegalArgumentException(PRODUCT_TYPE_IS_REQUIRED);
        }

        if(type.equals(ProductType.STOCK) && stockUomId == null) {
            throw new IllegalArgumentException(PRODUCT_STOCK_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK);
        }

        if(type.equals(ProductType.STOCK) && purchaseUomId == null) {
            throw new IllegalArgumentException(PRODUCT_PURCHASE_UOM_IS_REQUIRED_WHEN_TYPE_IS_STOCK);
        }

        if(purchasePrice != null && purchasePrice.isNegative()) {
            throw new IllegalArgumentException(PRODUCT_PURCHASE_PRICE_SHOULD_BE_GREATER_THAN_ZERO);
        }

        if(salePrice != null && salePrice.isNegative()) {
            throw new IllegalArgumentException(PRODUCT_SALE_PRICE_SHOULD_BE_GREATER_THAN_ZERO);
        }
    }

    public boolean isStorable() {
        return type != null && type.equals(ProductType.STOCK);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initiate() {
        setId(new ProductId(UUID.randomUUID()));
        this.active = Active.with(true);

        if (this.salePrice == null) {
            this.salePrice = Money.ZERO;
        }

        if(this.purchasePrice == null) {
            this.purchasePrice = Money.ZERO;
        }

        if(this.purchasable == null) {
            this.purchasable = Purchasable.of(false);
        }

        if(this.sellable == null) {
            this.sellable = Sellable.of(false);
        }
    }


    public Name getName() {
        return name;
    }

    public ProductCategoryId getCategoryId() {
        return categoryId;
    }

    public UomId getStockUomId() {
        return stockUomId;
    }

    public UomId getPurchaseUomId() {
        return purchaseUomId;
    }

    public ProductType getType() {
        return type;
    }

    public Reference getReference() {
        return reference;
    }

    public Filename getImageName() {
        return imageName;
    }

    public Money getSalePrice() {
        return salePrice;
    }

    public Money getPurchasePrice() {
        return purchasePrice;
    }

    public Active getActive() {
        return active;
    }

    public Sellable getSellable() {
        return sellable;
    }

    public Purchasable getPurchasable() {
        return purchasable;
    }

    public static final class Builder {
        private ProductId id;
        private Name name;
        private ProductCategoryId categoryId;
        private UomId stockUomId;
        private UomId purchaseUomId;
        private ProductType type;
        private Reference reference;
        private Filename imageName;
        private Money salePrice;
        private Money purchasePrice;
        private Active active;
        private Sellable sellable;
        private Purchasable purchasable;

        private Builder() {
        }

        public Builder id(ProductId val) {
            id = val;
            return this;
        }

        public Builder name(Name val) {
            name = val;
            return this;
        }

        public Builder categoryId(ProductCategoryId val) {
            categoryId = val;
            return this;
        }

        public Builder stockUomId(UomId val) {
            stockUomId = val;
            return this;
        }

        public Builder purchaseUomId(UomId val) {
            purchaseUomId = val;
            return this;
        }

        public Builder type(ProductType val) {
            type = val;
            return this;
        }

        public Builder reference(Reference val) {
            reference = val;
            return this;
        }

        public Builder imageName(Filename val) {
            imageName = val;
            return this;
        }

        public Builder salePrice(Money val) {
            salePrice = val;
            return this;
        }

        public Builder purchasePrice(Money val) {
            purchasePrice = val;
            return this;
        }

        public Builder active(Active val) {
            active = val;
            return this;
        }

        public Builder sellable(Sellable val) {
            sellable = val;
            return this;
        }

        public Builder purchasable(Purchasable val) {
            purchasable = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
