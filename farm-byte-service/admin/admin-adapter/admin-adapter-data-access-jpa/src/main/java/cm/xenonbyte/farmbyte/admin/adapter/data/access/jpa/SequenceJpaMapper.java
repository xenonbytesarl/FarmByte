package cm.xenonbyte.farmbyte.admin.adapter.data.access.jpa;

import cm.xenonbyte.farmbyte.admin.domain.core.Sequence;
import jakarta.annotation.Nonnull;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author bamk
 * @version 1.0
 * @since 29/10/2024
 */
@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SequenceJpaMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.text.value", target = "name")
    @Mapping(source = "code.text.value", target = "code")
    @Mapping(source = "next.value", target = "next")
    @Mapping(source = "step.value", target = "step")
    @Mapping(source = "size.value", target = "size")
    @Mapping(expression = "java(sequence.getPrefix() == null? null: sequence.getPrefix().text().getValue())", target = "prefix")
    @Mapping(expression = "java(sequence.getSuffix() == null? null: sequence.getSuffix().text().getValue())", target = "suffix")
    @Mapping(source = "active.value", target = "active")
    @Nonnull SequenceJpa toSequenceJpa(@Nonnull Sequence sequence);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id.value", source = "id")
    @Mapping(target = "name.text.value", source = "name")
    @Mapping(target = "code.text.value", source = "code")
    @Mapping(target = "next.value", source = "next")
    @Mapping(target = "step.value", source = "step")
    @Mapping(target = "size.value", source = "size")
    @Mapping(target = "prefix", expression = "java(sequenceJpa.getPrefix() == null? null: cm.xenonbyte.farmbyte.admin.domain.core.Prefix.of(cm.xenonbyte.farmbyte.common.domain.vo.Text.of(sequenceJpa.getPrefix())))")
    @Mapping(target = "suffix", expression = "java(sequenceJpa.getSuffix() == null? null: cm.xenonbyte.farmbyte.admin.domain.core.Suffix.of(cm.xenonbyte.farmbyte.common.domain.vo.Text.of(sequenceJpa.getSuffix())))")
    @Mapping(target = "active.value", source = "active")
    @Nonnull Sequence toSequence(SequenceJpa sequenceJpa);

}
