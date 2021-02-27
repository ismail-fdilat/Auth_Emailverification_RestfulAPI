package com.user.registration.registration.token;

import com.user.registration.AppUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public  interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {

    Optional<ConfirmationToken> findByToken(String Token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.expiresAt = ?3" +
            ",c.createdAt=?2 " +
            "WHERE c.token = ?1")
    int updateToken(String token,
                    LocalDateTime createdAt,
                    LocalDateTime expiresAt);


    @Query("select T FROM ConfirmationToken T" +
            " where T.appUser =" +
            " ?1 ")
     ConfirmationToken findByUser(AppUser user);
}

