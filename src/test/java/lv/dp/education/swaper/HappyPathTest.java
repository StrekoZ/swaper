package lv.dp.education.swaper;

import lv.dp.education.swaper.rest.AuthResource;
import lv.dp.education.swaper.rest.InvestmentResource;
import lv.dp.education.swaper.rest.LoanResource;
import lv.dp.education.swaper.rest.ProfileResource;
import lv.dp.education.swaper.rest.model.InvestmentRestPutModel;
import lv.dp.education.swaper.rest.model.LoanRestGetModel;
import lv.dp.education.swaper.rest.model.LoanRestPutModel;
import lv.dp.education.swaper.service.exception.EntityValidationException;
import lv.dp.education.swaper.service.exception.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.attribute.UserPrincipal;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Complex test to run a happy-path
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HappyPathTest {

    @Autowired
    private LoanResource loanResource;
    @Autowired
    private AuthResource authResource;
    @Autowired
    private InvestmentResource investmentResource;
    @Autowired
    private ProfileResource profileResource;

    @Test
    public void happyPath() throws ServiceException {
        // create Loan for investment
        authenticateAndGetMockRequest("admin", "admin", "ADMIN");
        loanResource.registerLoan(
                LoanRestPutModel.builder()
                        .interestPercent(BigDecimal.TEN)
                        .targetAmount(new BigDecimal(100))
                        .description("Happy-Path test")
                        .build(), new MockHttpServletResponse()
        );

        // get created Loan
        LoanRestGetModel loan = loanResource.listLoans().stream().filter(l -> "Happy-Path test".equals(l.getDescription())).findAny().orElse(null);

        // check loan is created
        assertNotNull(loan);
        assertEqualsBD(BigDecimal.TEN, loan.getInterestPercent());
        assertEqualsBD(new BigDecimal(100), loan.getTargetAmount());
        assertEqualsBD(BigDecimal.ZERO, loan.getInvestedAmount());
        assertEqualsBD(BigDecimal.ZERO, loan.getRemainingRepaymentAmount());


        // create investors
        authResource.register("investor1", "pass1", new MockHttpServletResponse());
        authResource.register("investor2", "pass2", new MockHttpServletResponse());

        // validate investor accounts
        BigDecimal investor1Account = profileResource.getProfile(authenticateAndGetMockRequest("investor1", "pass1", "INVESTOR")).getAccount();
        BigDecimal investor2Account = profileResource.getProfile(authenticateAndGetMockRequest("investor2", "pass2", "INVESTOR")).getAccount();
        assertEqualsBD(new BigDecimal(100), investor1Account);
        assertEqualsBD(new BigDecimal(100), investor2Account);


        // make investments
        investmentResource.invest(InvestmentRestPutModel.builder()
                        .loanUuid(loan.getUuid())
                        .amount(BigDecimal.TEN)
                        .build(),
                authenticateAndGetMockRequest("investor1", "pass1", "INVESTOR"),
                new MockHttpServletResponse());

        investmentResource.invest(InvestmentRestPutModel.builder()
                        .loanUuid(loan.getUuid())
                        .amount(new BigDecimal(50))
                        .build(),
                authenticateAndGetMockRequest("investor2", "pass2", "INVESTOR"),
                new MockHttpServletResponse());

        // Validate investor accounts
        investor1Account = profileResource.getProfile(authenticateAndGetMockRequest("investor1", "pass1", "INVESTOR")).getAccount();
        investor2Account = profileResource.getProfile(authenticateAndGetMockRequest("investor2", "pass2", "INVESTOR")).getAccount();
        assertEqualsBD(new BigDecimal(90), investor1Account);
        assertEqualsBD(new BigDecimal(50), investor2Account);

        // make Loan payment
        authenticateAndGetMockRequest("admin", "admin", "ADMIN");
        loanResource.makePayment(loan.getUuid(), new BigDecimal(33), new MockHttpServletResponse());

        // check, that it's not possible to invest anymore into Loan
        // validate behaviour on NULL object
        EntityValidationException exception = assertThrows(EntityValidationException.class, () ->
                investmentResource.invest(InvestmentRestPutModel.builder()
                                .loanUuid(loan.getUuid())
                                .amount(new BigDecimal(50))
                                .build(),
                        authenticateAndGetMockRequest("investor2", "pass2", "INVESTOR"),
                        new MockHttpServletResponse())
        );
        assertEquals(1, exception.getErrors().size());


        // make another Loan payment
        authenticateAndGetMockRequest("admin", "admin", "ADMIN");
        loanResource.makePayment(loan.getUuid(), new BigDecimal(33), new MockHttpServletResponse());

        // Validate investor accounts
        investor1Account = profileResource.getProfile(authenticateAndGetMockRequest("investor1", "pass1", "INVESTOR")).getAccount();
        investor2Account = profileResource.getProfile(authenticateAndGetMockRequest("investor2", "pass2", "INVESTOR")).getAccount();
        assertEqualsBD(new BigDecimal(101), investor1Account);
        assertEqualsBD(new BigDecimal(105), investor2Account);
    }

    private MockHttpServletRequest authenticateAndGetMockRequest(String username, String password, String role) {
        Authentication auth =
                new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(new GrantedAuthority() {
                    //anonymous inner type
                    public String getAuthority() {
                        return "ROLE_" + role;
                    }
                }));

        SecurityContextHolder.getContext().setAuthentication(auth);


        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(new UserPrincipal() {
            @Override
            public String getName() {
                return username;
            }
        });
        request.addUserRole(role);
        return request;
    }

    private void assertEqualsBD(BigDecimal expected, BigDecimal actual) {
        assertEquals(expected.setScale(2, RoundingMode.HALF_UP), actual.setScale(2, RoundingMode.HALF_UP));
    }

}
