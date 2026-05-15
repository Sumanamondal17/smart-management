package com.scm.scmproject.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.scmproject.entities.User;
import com.scm.scmproject.forms.UserForm;
import com.scm.scmproject.helpers.Message;
import com.scm.scmproject.helpers.MessageType;
import com.scm.scmproject.services.ImageService;
import com.scm.scmproject.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;


    @Autowired
    private ImageService imageService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handled");
        model.addAttribute("name", "Technology");
        model.addAttribute("youtube", "httpyoutube.com");
        model.addAttribute("github", "GithubRepo");
        return "home";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/service")
    public String service() {
        return "service";
    }

    @RequestMapping("/contact")
    public String contact() {
        return "contact";
    }

    // login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // registration page
    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();

        model.addAttribute("userForm", userForm);
        return "register";
    }

    // for processing register
    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session ) {
        // fetching data
        System.out.println(userForm);
        // validate data
        if (rBindingResult.hasErrors()) {
            return "register";
        }
        // save into database
        /*
         * User user=User.builder()
         * .name(userForm.getName())
         * .email(userForm.getEmail())
         * .password(userForm.getPassword())
         * .about(userForm.getAbout())
         * .phoneNumber(userForm.getPhoneNumber())
         * .build();
         */
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());

         if(userForm.getUserImage()!=null && !userForm.getUserImage().isEmpty()){
            String filename=UUID.randomUUID().toString();

            String fileURL=imageService.uploadImage(userForm.getUserImage(),filename);
            user.setProfilePic(fileURL);
            user.setCloudinaryImagePublicId(filename);
        }
        
        User savedUser = userService.saveUser(user);
        System.out.println("user Saved");
        Message message = Message.builder().content("Registration Successful").type(MessageType.blue).build();
        // message "registration done"
        session.setAttribute("message", message);
        // redirect to login page

        return "redirect:/register";
    }
}
