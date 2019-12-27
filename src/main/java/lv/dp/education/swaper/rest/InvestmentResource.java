package lv.dp.education.swaper.rest;

import io.swagger.annotations.ApiOperation;
import lv.dp.education.swaper.OrikaMapper;
import lv.dp.education.swaper.entities.InvestmentEntity;
import lv.dp.education.swaper.rest.model.InvestmentRestGetModel;
import lv.dp.education.swaper.rest.model.InvestmentRestPutModel;
import lv.dp.education.swaper.service.InvestmentService;
import lv.dp.education.swaper.service.InvestorService;
import lv.dp.education.swaper.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("investments")
public class InvestmentResource {
    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private InvestorService investorService;
    @Autowired
    private OrikaMapper orikaMapper;

    @GetMapping
    @ApiOperation(
            value = "List investments",
            notes = "List investments of a current user"
    )
    @RolesAllowed("INVESTOR")
    public List<InvestmentRestGetModel> listInvestments(HttpServletRequest request) {
        return investmentService.listInvestmentsForUser(request.getUserPrincipal().getName()).stream()
                .map(o -> orikaMapper.map(o, InvestmentRestGetModel.class))
                .collect(Collectors.toList());
    }


    @PutMapping
    @ApiOperation(value = "Invest",
            notes = "Make investment into a loan")
    @RolesAllowed("INVESTOR")
    public void invest(@RequestBody InvestmentRestPutModel restModel,
                       HttpServletRequest request,
                       HttpServletResponse response) throws ServiceException {
        InvestmentEntity entity = orikaMapper.map(restModel, InvestmentEntity.class);
        entity.setInvestor(investorService.findInvestorByUsername(request.getUserPrincipal().getName()));

        investmentService.invest(entity);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}