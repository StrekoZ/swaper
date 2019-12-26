package lv.dp.education.swaper.repository;

import lv.dp.education.swaper.entities.InvestorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvestorRepository extends JpaRepository<InvestorEntity, UUID>{

    InvestorEntity findByUsername(String username);

}