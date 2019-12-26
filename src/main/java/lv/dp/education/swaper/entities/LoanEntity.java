package lv.dp.education.swaper.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "loan")
@Data @EqualsAndHashCode(exclude = "investments")
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

    @OneToMany(mappedBy = "loan", fetch = FetchType.LAZY)
    private Set<InvestmentEntity> investments;

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
}
