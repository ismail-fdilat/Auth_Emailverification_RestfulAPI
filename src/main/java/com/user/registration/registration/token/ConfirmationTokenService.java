package com.user.registration.registration.token;

import com.user.registration.AppUser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){

        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){

        return confirmationTokenRepository.findByToken(token);
    }
    public int setConfirmedAt(String Token){

        return confirmationTokenRepository.updateConfirmedAt(Token, LocalDateTime.now());
    }
    public ConfirmationToken findByUser(AppUser User){
        return confirmationTokenRepository.findByUser(User);
    }
    public int updatetoken(String Token ){
            return confirmationTokenRepository.updateToken(Token,LocalDateTime.now(),LocalDateTime.now().plusMinutes(15));
    }
}
