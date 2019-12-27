package lv.dp.education.swaper.rest;

import io.swagger.annotations.ApiOperation;
import lv.dp.education.swaper.OrikaMapper;
import lv.dp.education.swaper.rest.model.InvestorRestGetModel;
import lv.dp.education.swaper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("profile")
public class ProfileResource {

    @Autowired
    private UserService userService;
    @Autowired
    private OrikaMapper orikaMapper;

    @GetMapping
    @ApiOperation(
            value = "Get user profile",
            notes = "Get user profile with all investments and account"
    )
    @RolesAllowed("INVESTOR")
    public InvestorRestGetModel getProfile(HttpServletRequest request) {
        return orikaMapper.map(userService.getInvestor(request.getUserPrincipal().getName()), InvestorRestGetModel.class);
    }
}