package lv.dp.education.swaper.entities;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "investment")
@Data @EqualsAndHashCode(exclude = {"loan", "investor"})
@Builder
@AllArgsConstructor @NoArgsConstructor
public class InvestmentEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type="uuid-char")
    private UUID uuid;

    private BigDecimal amount;

    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="loan_uuid", nullable=false)
    private LoanEntity loan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="investor_uuid", nullable=false)
    private InvestorEntity investor;
}
