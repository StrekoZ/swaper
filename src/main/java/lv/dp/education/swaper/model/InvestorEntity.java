package lv.dp.education.swaper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "investor")
@Data
@AllArgsConstructor @NoArgsConstructor
public class InvestorEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type="uuid-char")
    private UUID uuid;

    private String email;

    private BigDecimal account;

    @OneToMany(mappedBy = "investor")
    private Set<InvestmentEntity> investments;

}
