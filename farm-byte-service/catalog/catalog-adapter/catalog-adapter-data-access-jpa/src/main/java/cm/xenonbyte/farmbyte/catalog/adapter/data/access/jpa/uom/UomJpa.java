package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.adapter.data.access.jpa.entity.BaseEntityJpa;
import cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uomcategory.UomCategoryJpa;
import jakarta.persistence.*;
import lombok.*;
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
