package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uomcategory;

import cm.xenonbyte.farmbyte.adapter.data.access.jpa.entity.BaseEntityJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "t_uom_category")
public final class UomCategoryJpa extends BaseEntityJpa {

    @Column(name = "c_name", nullable = false, unique = true, length = 64)
    private String name;
    @Column(name = "c_active", nullable = false)
    private Boolean active;
}
