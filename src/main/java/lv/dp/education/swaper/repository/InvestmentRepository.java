package lv.dp.education.swaper.repository;

import lv.dp.education.swaper.entities.InvestmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentEntity, UUID>{

    List<InvestmentEntity> findByInvestorUsername(String investorUsername);

}