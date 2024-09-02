package cm.xenonbyte.farmbyte.common.domain.vo;

import cm.xenonbyte.farmbyte.common.domain.exception.PageInitializationBadException;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 30/08/2024
 */
public final class PageInfo<T> {
    private Boolean first;
    private Boolean last;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private List<T> content;

    public PageInfo() {
    }

    public PageInfo(Boolean first, Boolean last, Integer pageSize, Long totalElements,
                     Integer totalPages, List<T> content) {
        this.first = first;
        this.last = last;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }

    public PageInfo<T> with(@Nonnull Integer page, @Nonnull Integer pageSize, @Nonnull List<T> items) {

        if(items == null || items.isEmpty()) {
            return new PageInfo<>(false, false, 0, 0l, 0, new ArrayList<>());
        }
        long totalElements = items.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        if(page < 0 || page - 1 > totalPages) {
            throw new PageInitializationBadException(new String[]{String.valueOf(page), String.valueOf(totalElements)});
        }
        int startIndex = page * pageSize;
        int stopIndex = Math.min(startIndex + pageSize, (int)totalElements);
        if( startIndex <= totalElements ) {
            return new PageInfo<>(
                    page == 0,
                    page == totalPages - 1,
                    pageSize,
                    totalElements,
                    totalPages,
                    items.subList(startIndex, stopIndex));
        }
        throw new PageInitializationBadException();
    }

    public Boolean getFirst() {
        return first;
    }

    public Boolean getLast() {
        return last;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageInfo<?> pageInfo = (PageInfo<?>) o;
        return Objects.equals(first, pageInfo.first) && Objects.equals(last, pageInfo.last) &&
                Objects.equals(pageSize, pageInfo.pageSize) && Objects.equals(totalElements, pageInfo.totalElements) &&
                Objects.equals(totalPages, pageInfo.totalPages) && Objects.equals(content, pageInfo.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, last, pageSize, totalElements, totalPages, content);
    }
}
