package lv.dp.education.swaper.service;

import lv.dp.education.swaper.entities.InvestorEntity;
import lv.dp.education.swaper.service.exception.ServiceException;
import lv.dp.education.swaper.service.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDetailsManager userManager;
    @Autowired
    private InvestorService investorService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerInvestor(String username, String password) throws ServiceException {
        if (userManager.userExists(username)) {
            throw new UserAlreadyExistException(username);
        }
        userManager.createUser(
                User.withUsername(username)
                        .password(passwordEncoder.encode(password))
                        .roles("INVESTOR")
                        .build()
        );

        InvestorEntity investor = InvestorEntity.builder().username(username).build();

        investorService.createInvestor(investor);
        investorService.addFundsToAccount(investor, new BigDecimal(100));
    }

}
