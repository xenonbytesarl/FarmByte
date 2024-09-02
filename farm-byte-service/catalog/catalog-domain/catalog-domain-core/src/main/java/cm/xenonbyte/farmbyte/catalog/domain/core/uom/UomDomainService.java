package cm.xenonbyte.farmbyte.catalog.domain.core.uom;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.primary.UomService;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.common.domain.annotation.DomainService;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
@DomainService
public final class UomDomainService implements UomService {
    private final UomRepository uomRepository;
    private final UomCategoryRepository uomCategoryRepository;

    public UomDomainService(
            final @Nonnull UomRepository uomRepository,
            final @Nonnull UomCategoryRepository uomCategoryRepository) {
        this.uomRepository = Objects.requireNonNull(uomRepository);
        this.uomCategoryRepository = Objects.requireNonNull(uomCategoryRepository);
    }

    @Override
    public @Nonnull Uom createUom(@Nonnull Uom uom) {
        verifyUomCategory(uom);
        validateUom(uom);
        uom.initiate();
        return uomRepository.save(uom);
    }

    @Nonnull
    @Override
    public Uom findUomById(@Nonnull UomId uomId) {
        return uomRepository.findById(uomId)
                .orElseThrow(() -> new UomNotFoundException(new String[]{uomId.getValue().toString()}));
    }

    @Nonnull
    @Override
    public PageInfo<Uom> findUoms(int page, int size, String attribute, Direction direction) {
        return uomRepository.findAll(page, size, attribute, direction);
    }

    @Nonnull
    @Override
    public PageInfo<Uom> searchUoms(int page, int size, String attribute, Direction direction, @Nonnull Keyword keyword) {
        return uomRepository.search(page, size, attribute, direction, keyword);
    }

    private void verifyUomCategory(Uom uom) {
        if(uom.getUomCategoryId() != null && !uomCategoryRepository.existsById(uom.getUomCategoryId())) {
            throw new UomCategoryNotFoundException(new String[] {uom.getUomCategoryId().getValue().toString()});
        }
    }

    private void validateUom(Uom uom) {
        if (uom.getUomType().equals(UomType.REFERENCE) && uomRepository.existsByCategoryIdAndUomTypeAndActive(uom.getUomCategoryId(), uom.getUomType())) {
            throw new UomReferenceConflictException();
        }

        if(uomRepository.existsByNameAndCategoryAndActive(uom.getName(), uom.getUomCategoryId())) {
            throw  new UomNameConflictException(new Object[]{uom.getName().getText().getValue()});
        }
    }
}
