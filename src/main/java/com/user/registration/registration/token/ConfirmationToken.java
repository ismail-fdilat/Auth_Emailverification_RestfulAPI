package com.user.registration.registration.token;

import com.user.registration.AppUser.AppUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class ConfirmationToken {
    @Id
    @SequenceGenerator(
            name="confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator ="confirmation_token_sequence"
    )
    private Long ID;
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,
                             AppUser appuser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.appUser =appuser;
    }
}
