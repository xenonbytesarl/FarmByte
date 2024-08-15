package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.IUomService;
import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
@DomainService
public final class UomService implements IUomService {
    private final UomRepository uomRepository;

    public UomService(final @Nonnull UomRepository uomRepository) {
        this.uomRepository = Objects.requireNonNull(uomRepository);
    }

    @Override
    public @Nonnull Uom createUom(@Nonnull Uom uom) {
        //TODO check if category exists
        validateUom(uom);
        uom.initiate();
        return uomRepository.save(uom);
    }

    private void validateUom(Uom uom) {
        if (uom.getUomType().equals(UomType.REFERENCE) && uomRepository.existsByCategoryIdAndUomTypeAndActive(uom.getUomCategoryId(), uom.getUomType())) {
            throw new UomReferenceConflictException();
        }

        if(uomRepository.existsByNameAndCategoryAndActive(uom.getName(), uom.getUomCategoryId())) {
            throw  new UomNameConflictException(new Object[]{uom.getName().getValue()});
        }
    }
}
