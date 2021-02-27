package com.user.registration.registration;

import com.user.registration.AppUser.AppUser;
import com.user.registration.AppUser.AppUserRole;
import com.user.registration.AppUser.AppUserService;
import com.user.registration.email.EmailSender;
import com.user.registration.registration.token.ConfirmationToken;
import com.user.registration.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appuserservice;
    private final EmailValidator emailvalidator;
    private final ConfirmationTokenService  confirmationTokenService;
    private final EmailSender emailSender;
    public String register(RegistrationRequest request) {

       boolean isValidEmail = emailvalidator.test(request.getEmail());
       if (!isValidEmail) {
           throw new IllegalStateException("email not valid");
       }
        String token = appuserservice.signUpUser(new AppUser(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.User));
       String Link ="http://localhost:8080/api/v1/registration/confirmation?token="+token;

        emailSender.send(
                request.getEmail(),
                buildEmail(request.getFirstname(), Link));
       return token;
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appuserservice.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "<h1>confirmed</h1>";
    }
    private String buildEmail(String name, String link) {
        int counter=60 * 15;
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in <span id=\"minutes\">15:00</span> minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>" +
               "<script language=\"JavaScript\">\n" +
                "// la date à partir de laquelle on compte\n" +
                "var cible = new Date(\"March 06, 2008 10:00:00\");\n" +
                "\n" +
                "\n" +
                "// nombre de millisecondes par jour, heure, minute et seconde\n" +
                "var mms_jour = 24 * 60 * 60 * 1000;\n" +
                "var mms_heure = 60 * 60 * 1000;\n" +
                "var mms_minute = 60 * 1000;\n" +
                "var mms_seconde = 1000;\n" +
                "\n" +
                "\n" +
                "function decompte() {\n" +
                "\n" +
                "\n" +
                "// la date courante\n" +
                "var aujourdhui = new Date();\n" +
                "\n" +
                "\n" +
                "// on crée les variables qui accueilleront les différences entre aujourd'hui et la date à atteindre\n" +
                "var change_j = -1;\n" +
                "var change_h = -1;\n" +
                "var change_m = -1;\n" +
                "\n" +
                "\n" +
                "// le total de millisecondes de différences\n" +
                "var diff_mms = cible.getTime() - aujourdhui.getTime();\n" +
                "\n" +
                "\n" +
                "// pareil pour les jours\n" +
                "diff_jours = Math.floor(diff_mms / mms_jour);\n" +
                "diff_mms -= diff_jours * mms_jour;\n" +
                "\n" +
                "\n" +
                "// pour les heures\n" +
                "diff_heures = Math.floor(diff_mms / mms_heure);\n" +
                "diff_mms -= diff_heures * mms_heure;\n" +
                "\n" +
                "\n" +
                "// les... minutes, bravo ;-)\n" +
                "diff_minutes = Math.floor(diff_mms / mms_minute);\n" +
                "diff_mms -= diff_minutes * mms_minute;\n" +
                "\n" +
                "\n" +
                "// les secondes, ce qui reste en fait.\n" +
                "var diff_secondes = Math.floor(diff_mms / mms_seconde);\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "// on affecte nos résultats aux champs du formulaire\n" +
                "document.getElementById(\"jours\").innerHTML = diff_jours;\n" +
                "document.getElementById(\"heures\").innerHTML  = diff_heures;\n" +
                "document.getElementById(\"minutes\").innerHTML  = diff_minutes;\n" +
                "document.getElementById(\"secondes\").innerHTML = diff_secondes;\n" +
                "\n" +
                "\n" +
                "// on relance la fonction pour actualiser à la seconde\n" +
                "setTimeout(\"decompte()\",1000);\n" +
                "}\n" +
                "</script>\n";
    }
}
