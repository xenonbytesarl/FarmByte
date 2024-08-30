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
public final class Page<T> {
    private Boolean first;
    private Boolean last;
    private Integer pageSize;
    private Integer totalElements;
    private Integer totalPages;
    private List<T> content;

    public Page() {
    }

    private Page(Boolean first, Boolean last, Integer pageSize, Integer totalElements,
                 Integer totalPages, List<T> content) {
        this.first = first;
        this.last = last;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }

    public Page<T> with(@Nonnull Integer page, @Nonnull Integer pageSize, @Nonnull List<T> items) {

        if(items == null || items.isEmpty()) {
            return new Page<>(false, false, 0, 0, 0, new ArrayList<>());
        }
        int totalElements = items.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        if(page < 1 || page > totalPages) {
            throw new PageInitializationBadException(new String[]{String.valueOf(page), String.valueOf(totalElements)});
        }
        int startIndex = (page - 1) * pageSize;
        int stopIndex = Math.min(startIndex + pageSize, totalElements);
        if( startIndex < totalElements ) {
            return new Page<>(
                    page == 1,
                    page == totalPages,
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

    public Integer getSize() {
        return pageSize;
    }

    public Integer getTotalElements() {
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
        Page<?> page = (Page<?>) o;
        return Objects.equals(first, page.first) && Objects.equals(last, page.last) && Objects.equals(pageSize, page.pageSize) && Objects.equals(totalElements, page.totalElements) && Objects.equals(totalPages, page.totalPages) && Objects.equals(content, page.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, last, pageSize, totalElements, totalPages, content);
    }
}
