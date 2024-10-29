package cm.xenonbyte.farmbyte.admin.domain.core;


import cm.xenonbyte.farmbyte.common.domain.entity.BaseEntity;
import cm.xenonbyte.farmbyte.common.domain.vo.Active;
import cm.xenonbyte.farmbyte.common.domain.vo.Name;

import java.util.UUID;

/**
 * @author bamk
 * @version 1.0
 * @since 28/10/2024
 */
public final class Sequence extends BaseEntity<SequenceId> {

    public static final String PATTERN_YEAR_WITH_CENTURY="%(year)s";
    public static final String PATTERN_YEAR_WITHOUT_CENTURY="%(y)s";
    public static final String PATTERN_MONTH="%(month)s";
    public static final String PATTERN_DAY="%(day)s";
    public static final String PATTERN_DAY_OF_YEAR="%(doy)s";
    public static final String PATTERN_WEEK_OF_YEAR="%(doy)s";
    public static final String PATTERN_DAY_OF_WEEK="%(weekday)s";
    public static final String PATTERN_HOUR_24="%(h24)s";
    public static final String PATTERN_HOUR_12="%(h12)s";
    public static final String PATTERN_MINUTE="%(min)s";
    public static final String PATTERN_SECOND="%(sec)s";

    private final Name name;
    private final Code code;
    private Step step;
    private Size size;
    private Next next;
    private Prefix prefix;
    private Suffix suffix;
    private Active active;


    public Sequence(Name name, Code code) {
        this.name = name;
        this.code = code;
    }

    private Sequence(Builder builder) {
        setId(builder.id);
        name = builder.name;
        code = builder.code;
        step = builder.step;
        size = builder.size;
        next = builder.next;
        prefix = builder.prefix;
        suffix = builder.suffix;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Active getActive() {
        return active;
    }

    public void initializeWithDefaults() {
        setId(new SequenceId(UUID.randomUUID()));
        active = Active.with(true);
        if (size == null) {
            size = Size.of(5L);
        }
        if (step == null) {
            step = Step.of(1L);
        }
        if (next == null) {
            next = Next.of(1L);
        }
    }

    public Size getSize() {
        return size;
    }

    public Next getNext() {
        return next;
    }

    public Step getStep() {
        return step;
    }

    public Name getName() {
        return name;
    }

    public Code getCode() {
        return code;
    }

    public Prefix getPrefix() {
        return prefix;
    }

    public Suffix getSuffix() {
        return suffix;
    }

    public static final class Builder {
        private SequenceId id;
        private Name name;
        private Code code;
        private Step step;
        private Size size;
        private Next next;
        private Prefix prefix;
        private Suffix suffix;
        private Active active;

        private Builder() {
        }

        public Builder id(SequenceId val) {
            id = val;
            return this;
        }

        public Builder name(Name val) {
            name = val;
            return this;
        }

        public Builder code(Code val) {
            code = val;
            return this;
        }

        public Builder step(Step val) {
            step = val;
            return this;
        }

        public Builder size(Size val) {
            size = val;
            return this;
        }

        public Builder next(Next val) {
            next = val;
            return this;
        }

        public Builder prefix(Prefix val) {
            prefix = val;
            return this;
        }

        public Builder suffix(Suffix val) {
            suffix = val;
            return this;
        }

        public Builder active(Active val) {
            active = val;
            return this;
        }

        public Sequence build() {
            return new Sequence(this);
        }
    }
}
