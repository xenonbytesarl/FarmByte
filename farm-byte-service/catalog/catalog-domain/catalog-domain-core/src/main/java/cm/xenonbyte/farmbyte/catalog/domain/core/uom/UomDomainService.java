package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.IUomDomainService;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public final class UomDomainService implements IUomDomainService {
    private final UomRepository uomRepository;

    public UomDomainService(final @Nonnull UomRepository uomRepository) {
        this.uomRepository = Objects.requireNonNull(uomRepository);
    }

    @Override
    public @Nonnull Uom createUom(Uom uom) {
        if (uom.getUomType().equals(UomType.REFERENCE) && uomRepository.existsByCategoryIdAndUomTypeAndActive(uom.getUomCategoryId(), uom.getUomType())) {
            throw new UomDomainException("We can't have two units of measure with type reference in the same category");
        }
        if(uomRepository.existsByNameAndCategoryAndActive(uom.getName(), uom.getUomCategoryId())) {
            throw  new UomDomainException(String.format("An unit of measure with the name '%s' already exists", uom.getName().getValue()));
        }
        uom.initiate();
        return uomRepository.save(uom);
    }
}
