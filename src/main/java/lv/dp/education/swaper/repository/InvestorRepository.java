package lv.dp.education.swaper.repository;

import lv.dp.education.swaper.entities.InvestorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorRepository extends JpaRepository<InvestorEntity, Long>{

    InvestorEntity findByUsername(String username);

}