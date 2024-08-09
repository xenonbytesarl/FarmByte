package cm.xenonbyte.farmbyte.adapter.data.access.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 09/08/2024
 */
@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntityJpa {
    @Id
    @Column(name = "c_id", nullable = false, unique = true)
    private UUID id;
    @CreatedDate
    @Column(name = "c_created_at",  updatable = false, nullable = false)
    private ZonedDateTime createdAt;
    @LastModifiedDate
    @Column(name = "c_updated_at", insertable = false)
    private ZonedDateTime updatedAt;
}
