package lv.dp.education.swaper.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payment")
@ToString(exclude = {"loan"})
@EqualsAndHashCode(exclude = {"loan"})
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    private UUID uuid;

    private BigDecimal amount;

    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_uuid", nullable = false)
    private LoanEntity loan;
}
