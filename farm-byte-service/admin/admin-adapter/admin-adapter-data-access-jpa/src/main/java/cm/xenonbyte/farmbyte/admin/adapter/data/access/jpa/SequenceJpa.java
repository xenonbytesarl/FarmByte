package cm.xenonbyte.farmbyte.admin.adapter.data.access.jpa;

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
 * @since 29/10/2024
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_sequence")
public final class SequenceJpa extends BaseEntityJpa {
    @Column(name = "c_name", length = 128, nullable = false, unique = true)
    private String name;
    @Column(name = "c_code", length = 64, nullable = false, unique = true)
    private String code;
    @Column(name = "c_step", nullable = false)
    private Long step;
    @Column(name = "c_size", nullable = false)
    private Long size;
    @Column(name = "c_next", nullable = false)
    private Long next;
    @Column(name = "c_prefix", length = 64)
    private String prefix;
    @Column(name = "c_suffix", length = 64)
    private String suffix;
    @Column(name = "c_active", nullable = false)
    private Boolean active;
}
