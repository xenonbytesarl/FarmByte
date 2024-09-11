package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.Uom;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomType;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import jakarta.annotation.Nonnull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author bamk
 * @version 1.0
 * @since 06/08/2024
 */
public class UomInMemoryRepositoryAdapter implements UomRepository {

    private final Map<UomId, Uom> uoms = new HashMap<>();

    @Override
    public boolean existsByCategoryIdAndUomTypeAndActive(@Nonnull UomCategoryId uomCategoryId, @Nonnull UomType uomType) {
        return uoms.values().stream()
                .anyMatch(uom ->
                        uom.getUomCategoryId().equals(uomCategoryId) &&
                        uom.getUomType().equals(uomType) &&
                        uom.getActive().equals(Active.with(true))
                );
    }

    @Override
    public Uom save(@Nonnull Uom uom) {
        uoms.put(uom.getId(), uom);
        return uom;
    }

    @Override
    public boolean existsByNameAndCategoryAndActive(@Nonnull Name name, @Nonnull UomCategoryId uomCategoryId) {
        return uoms.values().stream().anyMatch(uom ->
                uom.getName().equals(name) &&
                uom.getUomCategoryId().equals(uomCategoryId) &&
                uom.getActive().equals(Active.with(true))
        );
    }

    @Override
    public Optional<Uom> findById(@Nonnull UomId uomId) {
        return Optional.ofNullable(uoms.get(uomId));
    }

    @Override
    public boolean existsById(@Nonnull UomId uomId) {
        return uoms.containsKey(uomId);
    }

    @Override
    public PageInfo<Uom> findAll(int page, int size, String attribute, Direction direction) {
        Comparator<Uom> comparing = Comparator.comparing((Uom uom) -> uom.getName().getText().getValue());
        List<Uom> uomList = uoms.values().stream().sorted(Direction.ASC.equals(direction)? comparing: comparing.reversed()).toList();
        return new PageInfo<Uom>().with(page, size, uomList);
    }

    @Override
    public PageInfo<Uom> search(int page, int size, String attribute, Direction direction, Keyword keyword) {
        Comparator<Uom> comparing = Comparator.comparing((Uom uom) -> uom.getName().getText().getValue());
        List<Uom> uomList = uoms.values().stream()
                .filter(uom -> uom.getName().getText().getValue().toLowerCase().contains(keyword.getText().getValue().toLowerCase()))
                .sorted(Direction.ASC.equals(direction)? comparing: comparing.reversed())
                .toList();
        return new PageInfo<Uom>().with(page, size, uomList);
    }

    @Nonnull
    @Override
    public Uom update(@Nonnull Uom oldUom, @Nonnull Uom uomToUpdated) {
        uoms.replace(uomToUpdated.getId(), uomToUpdated);
        return uomToUpdated;
    }

    @Override
    public Optional<Uom> findByName(@Nonnull Name name) {
        return uoms.values().stream()
                .filter(uom -> uom.getName().getText().getValue().equalsIgnoreCase(name.getText().getValue()))
                .findFirst();
    }

    @Override
    public Optional<Uom> findByCategoryIdAndUomTypeAndActive(@Nonnull UomCategoryId uomCategoryId, @Nonnull UomType uomType) {
        return uoms.values().stream()
                .filter(uom ->
                        uom.getUomCategoryId().equals(uomCategoryId) &&
                                uom.getUomType().equals(uomType) &&
                                    uom.getActive().equals(Active.with(true)))
                .findFirst();
    }

}
