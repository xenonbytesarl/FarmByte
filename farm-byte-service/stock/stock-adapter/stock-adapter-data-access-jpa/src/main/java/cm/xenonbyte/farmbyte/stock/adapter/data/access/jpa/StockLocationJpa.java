package cm.xenonbyte.farmbyte.stock.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.adapter.data.access.jpa.entity.BaseEntityJpa;
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

/**
 * @author bamk
 * @version 1.0
 * @since 16/10/2024
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_stock_location")
public final class StockLocationJpa extends BaseEntityJpa {
    @Column(name = "c_name", nullable = false, unique = true, length = 64)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "c_type", length = 64, nullable = false)
    private StockLocationTypeJpa type;
    @Column(name = "c_active", nullable = false)
    private Boolean active;
    @ManyToOne
    @JoinColumn(name = "c_parent_id")
    private StockLocationJpa parentJpa;
}
