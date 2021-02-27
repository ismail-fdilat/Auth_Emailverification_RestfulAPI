package com.user.registration.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
   private RegistrationService registrationService;
   @PostMapping
   public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }
   @GetMapping(path="confirmation")
    public String confirm(@RequestParam("token") String token){
       return registrationService.confirmToken(token);
   }
}
