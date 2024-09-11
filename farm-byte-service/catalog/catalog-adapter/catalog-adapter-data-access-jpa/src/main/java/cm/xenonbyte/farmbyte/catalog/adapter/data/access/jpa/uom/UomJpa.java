package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

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
 * @since 06/08/2024
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_uom")
public final class UomJpa extends BaseEntityJpa {

    @Column(name = "c_name", nullable = false, unique = true, length = 64)
    private String name;
    @Column(name = "c_ratio", nullable = false)
    private Double ratio;
    @Column(name = "c_active", nullable = false)
    private Boolean active;
    @Enumerated(EnumType.STRING)
    @Column(name = "c_type", length = 16, nullable = false)
    private UomTypeJpa uomTypeJpa;
    @ManyToOne
    @JoinColumn(name = "c_uom_category_id", nullable = false)
    private UomCategoryJpa uomCategoryJpa;

}
