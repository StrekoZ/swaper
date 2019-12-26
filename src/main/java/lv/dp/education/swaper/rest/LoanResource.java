package lv.dp.education.swaper.rest;

import io.swagger.annotations.ApiOperation;
import lv.dp.education.swaper.mapper.ModelConverter;
import lv.dp.education.swaper.model.LoanEntity;
import lv.dp.education.swaper.rest.model.LoanRestGetModel;
import lv.dp.education.swaper.rest.model.LoanRestPutModel;
import lv.dp.education.swaper.service.LoanService;
import lv.dp.education.swaper.service.exception.EntityValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoanResource {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoanService loanService;

    @GetMapping("/loans")
    @ApiOperation(
            value = "List loans",
            notes = "List all loans registered in application"
    )
    public List<LoanRestGetModel> listLoans() {
        return loanService.listLoans().stream()
                .map(o -> ModelConverter.map(o, LoanRestGetModel.class))
                .collect(Collectors.toList());
    }


    @PutMapping("/loans")
    @RolesAllowed("ADMIN")
    @ApiOperation(value = "Register Loan",
            notes = "Create new Loan in application")
    public void registerLoan(@RequestBody LoanRestPutModel restModel, HttpServletResponse response) throws EntityValidationException {
        loanService.createLoan(ModelConverter.map(restModel, LoanEntity.class));
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}