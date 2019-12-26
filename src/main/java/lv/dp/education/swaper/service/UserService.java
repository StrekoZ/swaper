package lv.dp.education.swaper.service;

import lv.dp.education.swaper.service.exception.ServiceException;
import lv.dp.education.swaper.service.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDetailsManager userManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerInvestor(String username, String password) throws ServiceException {
        if (userManager.userExists(username)) {
            throw new UserAlreadyExistException(username);
        }
        userManager.createUser(User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("INVESTOR")
                .build()
        );
    }

}
