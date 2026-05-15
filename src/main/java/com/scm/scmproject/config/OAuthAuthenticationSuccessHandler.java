package com.scm.scmproject.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scmproject.entities.Providers;
import com.scm.scmproject.entities.User;
import com.scm.scmproject.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    Logger logger=LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
            logger.info("OAuthAuthenticationSuccessHandler");

              //identification of provider
            var oauth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;
            String authorizedClientRegistrationId=oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
            logger.info(authorizedClientRegistrationId);
            var oAuthUser=(DefaultOAuth2User)authentication.getPrincipal();
            oAuthUser.getAttributes().forEach((key,value)->{
              logger.info(key+":"+value);
            });
            User user=new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setEmailVerified(true);
            user.setEnabled(true);
            user.setPassword("dummy");

            if(authorizedClientRegistrationId.equalsIgnoreCase("google")){

    Object emailAttr = oAuthUser.getAttribute("email");
    Object nameAttr = oAuthUser.getAttribute("name");

    if (emailAttr == null) {
        logger.error("Email not found from Google");
        throw new RuntimeException("Email not found from Google");
    }

    user.setEmail(emailAttr.toString());
    user.setName(nameAttr != null ? nameAttr.toString() : "Google User");
    user.setProviderUserId(oAuthUser.getName());
    user.setProvider(Providers.GOOGLE);
    user.setAbout("This is created using google");
}else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){

    String email = oAuthUser.getAttribute("email") != null
            ? oAuthUser.getAttribute("email").toString()
            : oAuthUser.getAttribute("login").toString() + "@gmail.com";

    Object nameAttr = oAuthUser.getAttribute("name");

    user.setEmail(email);
    user.setName(nameAttr != null ? nameAttr.toString() : "GitHub User");
    user.setProviderUserId(oAuthUser.getName());
    user.setProvider(Providers.GITHUB);
    user.setAbout("This is created using github");
}else{
              logger.info("OAuthAuthenticationSuccessHandler:Unknown provider");
            }
            
            //save the user

            User user2=userRepo.findByEmail(user.getEmail()).orElse(null);

            if(user2==null)
                userRepo.save(user);
            
           /* 
            DefaultOAuth2User user=(DefaultOAuth2User)authentication.getPrincipal();
          //  logger.info(user.getName());
         user.getAttributes().forEach((key,value)->{
           logger.info("{}=>{}",key,value);
         });
            logger.info(user.getAuthorities().toString());

            //data database save
            String email=user.getAttribute("email").toString();
            String name=user.getAttribute("name").toString();

            //create user and save in database
            User user1=new User();
            user1.setEmail(email);
            user1.setName(name);
            user1.setPassword("password");
            user1.setUserId(UUID.randomUUID().toString());
            user1.setProvider(Providers.GOOGLE);
            user1.setEnabled(true);
            user1.setEmailVerified(true);
            user1.setProviderUserId(user.getName());
           // user1.setRoleList(List.of(AppConstants.ROLE_USER));
            user1.setAbout("This account is created using google");

            User user2=userRepo.findByEmail(email).orElse(null);

            if(user2==null){
                userRepo.save(user1);
                logger.info("User Saved"+email);
            }*/
            new DefaultRedirectStrategy().sendRedirect(request,response,"/user/profile");
    }
    
}
