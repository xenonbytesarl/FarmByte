package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.PageInfo;
import cm.xenonbyte.farmbyte.common.domain.vo.Direction;
import jakarta.annotation.Nonnull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author bamk
 * @version 1.0
 * @since 14/08/2024
 */
public final class UomCategoryInMemoryRepositoryAdapter implements UomCategoryRepository {

    private final Map<UomCategoryId, UomCategory> uomCategories = new HashMap<>();

    @Override
    public Boolean existsByName(@Nonnull Name name) {
        return uomCategories.values().stream().anyMatch(uomCategory -> uomCategory.getName().equals(name));
    }

    @Override
    public UomCategory save(@Nonnull UomCategory uomCategory) {
        uomCategories.put(uomCategory.getId(), uomCategory);
        return uomCategory;
    }

    @Override
    public Boolean existsById(UomCategoryId parentCategoryId) {
        return uomCategories.get(parentCategoryId) != null;
    }

    @Override
    public Optional<UomCategory> findById(UomCategoryId uomCategoryId) {
        return Optional.ofNullable(uomCategories.get(uomCategoryId));
    }

    @Override
    public PageInfo<UomCategory> findAll(@Nonnull Integer page, @Nonnull Integer size, @Nonnull String sortAttribute, @Nonnull Direction direction) {
        PageInfo<UomCategory> uomCategoryPageInfo = new PageInfo<>();
        Comparator<UomCategory> comparing = Comparator.comparing((UomCategory a) -> a.getName().getText().getValue());
        return uomCategoryPageInfo.with(
                page,
                size,
                uomCategories.values().stream()
                    .sorted(Direction.ASC.equals(direction) ? comparing : comparing.reversed())
                    .toList()
        );
    }

    @Override
    public PageInfo<UomCategory> search(int page, int size, String sortAttribute, Direction direction, Keyword keyword) {
        Predicate<UomCategory> uomCategoryNammePredicate = uomCategory -> uomCategory.getName().getText().getValue().toLowerCase().contains(keyword.getText().getValue().toLowerCase());
        Comparator<UomCategory> comparing = Comparator.comparing((UomCategory a) -> a.getName().getText().getValue());
        PageInfo<UomCategory> uomCategoryPageInfo = new PageInfo<>();
        return uomCategoryPageInfo.with(
                page,
                size,
                uomCategories.values().stream()
                        .filter(uomCategoryNammePredicate)
                        .sorted(Direction.ASC.equals(direction) ? comparing: comparing.reversed())
                        .toList()
        );
    }
}
