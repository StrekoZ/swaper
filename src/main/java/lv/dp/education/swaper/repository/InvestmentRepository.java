package lv.dp.education.swaper.repository;

import lv.dp.education.swaper.entities.InvestmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentEntity, Long>{

    List<InvestmentEntity> findByInvestorUsername(String investorUsername);

}