package com.scm.scmproject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.scmproject.entities.User;
import com.scm.scmproject.helpers.Helper;
import com.scm.scmproject.services.ActivityService;
import com.scm.scmproject.services.ContactService;
import com.scm.scmproject.services.UserService;



@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger=LoggerFactory.getLogger(UserController.class);



    @Autowired
    private UserService userService;

    @Autowired
private ContactService contactService;

@Autowired
private ActivityService activityService;
  

    //user dashboard
    @RequestMapping(value="/dashboard")
    public String userDashboard(Model model, Authentication authentication) {

        String email = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(email);

        long totalContacts = contactService.countByUser(user);

        model.addAttribute("totalContacts", totalContacts);

        long favoriteContacts = contactService.countFavoriteByUser(user);

        model.addAttribute("favoriteContacts", favoriteContacts);
        model.addAttribute(
    "recentActivities",
    activityService.getRecentActivities(user)
);
        return "user/dashboard";
    }
     //user profile
    @RequestMapping(value="/profile")
    public String userProfile(Model model,Authentication authentication) {
       
      

        return "user/profile";
    }
    
    //user add contacts page

    //user view contacts
}
