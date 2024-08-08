package cm.xenonbyte.farmbyte.catalog.adapter.data.access.jpa.uomcategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

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
public final class UomCategoryJpa {
    @Id
    @Column(name = "c_id", nullable = false, unique = true)
    private UUID id;
    @CreationTimestamp
    @Column(name = "c_created_at",  updatable = false, nullable = false)
    private ZonedDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "c_updated_at", insertable = false)
    private ZonedDateTime updatedAt;
    @Column(name = "c_name", nullable = false, unique = true, length = 64)
    private String name;
    @Column(name = "c_active", nullable = false)
    private Boolean active;
}
