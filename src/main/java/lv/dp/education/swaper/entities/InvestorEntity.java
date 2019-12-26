package lv.dp.education.swaper.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "investor")
@ToString(exclude = "investments")
@EqualsAndHashCode(exclude = "investments")
@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class InvestorEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    private UUID uuid;

    private String username;

    private BigDecimal account = BigDecimal.ZERO;

    @OneToMany(mappedBy = "investor", fetch = FetchType.LAZY)
    private Set<InvestmentEntity> investments;

}
