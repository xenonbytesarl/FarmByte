package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.product;

import cm.xenonbyte.farmbyte.adapter.data.access.jpa.entity.BaseEntityJpa;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom.UomJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @author bamk
 * @version 1.0
 * @since 23/08/2024
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_product")
public final class ProductJpa extends BaseEntityJpa {
    @Column(name = "c_name", nullable = false, unique = true, length = 64)
    private String name;
    @Column(name = "c_reference", length = 16, unique = true)
    private String reference;
    @Column(name = "c_purchase_price", nullable = false)
    private BigDecimal purchasePrice;
    @Column(name = "c_sale_price", nullable = false)
    private BigDecimal salePrice;
    @Column(name = "c_image", nullable = false)
    private String image;
    @Column(name = "c_purchasable", nullable = false)
    private Boolean purchasable;
    @Column(name = "c_sellable", nullable = false)
    private Boolean sellable;
    @Column(name = "c_active", nullable = false)
    private Boolean active;
    @Enumerated(EnumType.STRING)
    @Column(name = "c_type", nullable = false, length = 16)
    private ProductTypeJpa typeJpa;
    @ManyToOne
    @JoinColumn(name = "c_category_id", nullable = false)
    private ProductCategoryJpa categoryJpa;
    @ManyToOne
    @JoinColumn(name = "c_stock_uom_id")
    private UomJpa stockUomJpa;
    @ManyToOne
    @JoinColumn(name = "c_purchase_uom_id")
    private UomJpa purchaseUomJpa;
}
