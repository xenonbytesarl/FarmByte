package cm.xenonbyte.farmbyte.adapter.data.access.jpa.uom;

import cm.xenonbyte.farmbyte.uom.entity.Uom;
import cm.xenonbyte.farmbyte.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.uom.vo.Name;
import cm.xenonbyte.farmbyte.uom.vo.UomCategoryId;
import cm.xenonbyte.farmbyte.uom.vo.UomType;
import groovy.util.logging.Slf4j;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
@Slf4j
@Service
public final class PostgreSqlUomRepository implements UomRepository {

    private final JpaUomRepository jpaUomRepository;
    private final UomMapper mapper;

    public PostgreSqlUomRepository(final @Nonnull JpaUomRepository jpaUomRepository, final @Nonnull UomMapper mapper) {
        this.jpaUomRepository = Objects.requireNonNull(jpaUomRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public boolean existsByCategoryIdAndUomTypeAndActive(UomCategoryId uomCategoryId, UomType uomType) {
        return jpaUomRepository.existsByUomCategoryJpaAndTypeAndActive(
                UomCategoryJpa.builder().id(uomCategoryId.getId()).build(),
                UomTypeJpa.valueOf(uomType.name()),
                true
        );
    }

    @Override
    public Uom save(Uom uom) {
        return mapper.fromUomJpa(jpaUomRepository.save(mapper.fromUom(uom)));
    }

    @Override
    public boolean existsByNameAndCategoryAndActive(Name name, UomCategoryId uomCategoryId) {
        return jpaUomRepository.existsByNameAndUomCategoryJpaAndActive(
                name.getValue(),
                UomCategoryJpa.builder().id(uomCategoryId.getId()).build(),
                true
        );
    }
}
