package cm.xenonbyte.farmbyte.common.domain.vo;

import cm.xenonbyte.farmbyte.common.domain.validation.Assert;
import jakarta.annotation.Nonnull;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author bamk
 * @version 1.0
 * @since 20/08/2024
 */
public final class Money {

    public static final Money ZERO = Money.of(BigDecimal.ZERO);
    private final BigDecimal amount;

    public Money(@Nonnull BigDecimal amount) {
        this.amount = Objects.requireNonNull(amount);
    }

    public static Money of(@Nonnull BigDecimal amount) {
        Assert.field("Money amount", amount)
                .notNull();
        return new Money(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isNegative() {
        return lessThan(ZERO);
    }

    private Boolean lessThan(Money money) {
        return amount.compareTo(money.amount) < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

}
