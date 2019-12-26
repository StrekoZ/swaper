package lv.dp.education.swaper.service;

import lv.dp.education.swaper.model.LoanEntity;
import lv.dp.education.swaper.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository repository;

    public List<LoanEntity> listLoans() {
        return repository.findAll();
    }
}
