package cm.xenonbyte.farmbyte.common.domain.mapper;

import cm.xenonbyte.farmbyte.common.domain.annotation.DomainMapper;
import cm.xenonbyte.farmbyte.common.domain.vo.Money;

import java.math.BigDecimal;

/**
 * @author bamk
 * @version 1.0
 * @since 26/08/2024
 */
@DomainMapper
public final class MoneyMapper {

    public Money map(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return new Money(amount);
    }

    public BigDecimal map(Money money) {
        if (money == null) {
            return null;
        }
        return money.getAmount();
    }
}
