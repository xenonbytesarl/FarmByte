package cm.xenonbyte.farmbyte.catalog.adapter.data.access.inmemory;

import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategory;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.UomCategoryId;
import cm.xenonbyte.farmbyte.catalog.domain.core.uom.ports.secondary.UomCategoryRepository;
import cm.xenonbyte.farmbyte.common.domain.vo.Keyword;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;
import cm.xenonbyte.farmbyte.common.domain.vo.Page;
import cm.xenonbyte.farmbyte.common.domain.vo.Sort;
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
    public Page<UomCategory> findAll(@Nonnull Integer page, @Nonnull Integer size, @Nonnull String sortAttribute, @Nonnull Sort sortDirection) {
        Page<UomCategory> uomCategoryPage = new Page<>();
        Comparator<UomCategory> comparing = Comparator.comparing((UomCategory a) -> a.getName().getText().getValue());
        return uomCategoryPage.with(
                page,
                size,
                uomCategories.values().stream()
                    .sorted(Sort.ASC.equals(sortDirection) ? comparing : comparing.reversed())
                    .toList()
        );
    }

    @Override
    public Page<UomCategory> findByKeyWord(int page, int size, String sortAttribute, Sort sortDirection, Keyword keyword) {
        Predicate<UomCategory> uomCategoryNammePredicate = uomCategory -> uomCategory.getName().getText().getValue().contains(keyword.getText().getValue());
        Comparator<UomCategory> comparing = Comparator.comparing((UomCategory a) -> a.getName().getText().getValue());
        Page<UomCategory> uomCategoryPage = new Page<>();
        return uomCategoryPage.with(
                page,
                size,
                uomCategories.values().stream()
                        .filter(uomCategoryNammePredicate)
                        .sorted(Sort.ASC.equals(sortDirection) ? comparing: comparing.reversed())
                        .toList()
        );
    }
}
