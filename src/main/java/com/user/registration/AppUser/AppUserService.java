package com.user.registration.AppUser;

import com.user.registration.registration.token.ConfirmationToken;
import com.user.registration.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService{


    private final static String USER_NOT_FOUND_MSG="user with email %s" +
            " not found ";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(()->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }
    public String signUpUser(AppUser appuser){
        boolean userExist =appUserRepository
                .findByEmail(appuser.getEmail())
                .isPresent();
        if (userExist){
            final AppUser user = appUserRepository.getUserByEmail(appuser.getEmail());
            System.out.println(" \n         USER  ===>  "+ user.toString() + "  <===  \n");
            ConfirmationToken Token = confirmationTokenService.findByUser(user);

            System.out.println(" \n         Token  ===>  "+ Token.toString() + "  <===  \n");

             if (Token.getExpiresAt().isBefore(LocalDateTime.now())
                    && Token.getConfirmedAt() == null ){
                System.out.println("update token");
                confirmationTokenService.updatetoken(Token.getToken());
                return Token.getToken();}

             else if(Token.getExpiresAt().isBefore(LocalDateTime.now())
                         && Token.getConfirmedAt()  != null){

                throw new IllegalStateException("the email is already confirmed");
                }

             else {
                throw new IllegalStateException("the email is already taken");
            }
        }
        String encodedPassword =bCryptPasswordEncoder.encode(appuser.getPassword());
        appuser.setPassword(encodedPassword);
        appUserRepository.save(appuser);
        // TODO : SEND COFIRMATION TOKEN : done
       String  token =UUID.randomUUID().toString();
        ConfirmationToken confirmationToken =new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(2),
                appuser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        //TODO: SEND EMAIL
        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
