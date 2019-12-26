package lv.dp.education.swaper.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lv.dp.education.swaper.service.UserService;
import lv.dp.education.swaper.service.exception.ServiceException;
import lv.dp.education.swaper.service.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("auth")
public class AuthResource {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public void register(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         HttpServletResponse response) throws ServiceException {
        userService.registerInvestor(username, password);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @ApiOperation("Login")
    @PostMapping("login")
    public void fakeLogin(@RequestParam("username") String username, @RequestParam("password")String password) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @ApiOperation("Logout")
    @PostMapping("logout")
    public void fakeLogout() {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

}
