package cm.xenonbyte.farmbyte.common.domain.validation;

import java.util.List;

/**
 * @author bamk
 * @version 1.0
 * @since 13/10/2024
 */
public final class Assert {


    public static ObjectAssert field(String field, Object value) {
        return new ObjectAssert(field, value);
    }


    public static class ObjectAssert {

        private final String field;
        private final Object value;
        private Object target;

        public ObjectAssert(String field, Object value) {
            this.field = field;
            this.value = value;
        }

        public ObjectAssert notNull() {
            if(value == null) {
                throw InvalidFieldBadException.forNullValue(field);
            }
            return this;
        }

        public ObjectAssert notNull(Object value) {
            if(value == null) {
                throw InvalidFieldBadException.forNullValue(field);
            }
            target = value;
            return this;
        }

        public ObjectAssert isOneOf(List<Object> values) {
            if(values.stream().noneMatch(val -> val.equals(target))) {
                throw InvalidFieldBadException.forIsOneOfValue(field, (String)target);
            }
            return null;
        }
    }
}
