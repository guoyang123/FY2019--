package security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {

    public MyDaoAuthenticationProvider(UserDetailsService u){
        super();
        setUserDetailsService(u);

    }
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
       // super.additionalAuthenticationChecks(userDetails, authentication);

       String password= userDetails.getPassword();
       String authentication_password=(String) authentication.getCredentials();
       if(password==null||authentication_password==null||!password.equals(authentication_password)){
           throw new BadCredentialsException("密码错误");
       }

    }
}
