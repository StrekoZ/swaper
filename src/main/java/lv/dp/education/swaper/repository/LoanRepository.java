package lv.dp.education.swaper.repository;

import lv.dp.education.swaper.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long>{

    LoanEntity findByUuid(UUID loanUuid);
}