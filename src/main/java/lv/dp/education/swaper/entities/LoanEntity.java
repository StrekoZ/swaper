package lv.dp.education.swaper.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "loan")
@ToString(exclude = {"investments", "payments"})
@EqualsAndHashCode(exclude = {"investments", "payments"})
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class LoanEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type="uuid-char")
    private UUID uuid;

    private BigDecimal targetAmount;

    private BigDecimal interestPercent;

    private String description;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @OneToMany(mappedBy = "loan", fetch = FetchType.LAZY)
    private Set<InvestmentEntity> investments;

    @OneToMany(mappedBy = "loan", fetch = FetchType.LAZY)
    private Set<PaymentEntity> payments;

    public BigDecimal totalInvestmentAmount() {
        if (getInvestments() == null) {
            return BigDecimal.ZERO;
        }
        return getInvestments().stream()
                .map(InvestmentEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal remainingInvestmentAmount() {
        return targetAmount.subtract(totalInvestmentAmount());
    }

    public BigDecimal totalRepaymentAmount() {
        return totalInvestmentAmount().add(
                totalInvestmentAmount().multiply(interestPercent).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)
        );
    }

    public BigDecimal remainingRepaymentAmount() {
        if (getPayments() == null) {
            return totalRepaymentAmount();
        }
        return totalRepaymentAmount().subtract(
                getPayments().stream()
                        .map(PaymentEntity::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

}
