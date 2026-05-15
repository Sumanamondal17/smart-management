package com.scm.scmproject.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scmproject.entities.Contact;
import com.scm.scmproject.entities.User;
import com.scm.scmproject.forms.ContactForm;
import com.scm.scmproject.forms.ContactSearchForm;
import com.scm.scmproject.helpers.AppConstants;
import com.scm.scmproject.helpers.Helper;
import com.scm.scmproject.helpers.Message;
import com.scm.scmproject.helpers.MessageType;
import com.scm.scmproject.services.ContactService;
import com.scm.scmproject.services.ImageService;
import com.scm.scmproject.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger=LoggerFactory.getLogger(ContactController.class);
    
    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    //add contact page handler
    @RequestMapping("/add")
    public String addContactView(Model model){
        ContactForm contactForm=new ContactForm();
        contactForm.setFavorite(false);
        model.addAttribute("contactForm",contactForm);
        return "user/add_contact";
    }
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm,BindingResult result,Authentication authentication,HttpSession session) {
        
        //validate form
        if(result.hasErrors()){
            result.getAllErrors().forEach(error->logger.info(error.toString()));
            return "user/add_contact";
        }


        String userName=Helper.getEmailOfLoggedInUser(authentication);
        //form->contact
        User user=userService.getUserByEmail(userName);

        //image processing

        //code for uploading
        

        Contact contact=new Contact();

        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);


        if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){
            String filename=UUID.randomUUID().toString();

            String fileURL=imageService.uploadImage(contactForm.getContactImage(),filename);
            contact.setProfilePic(fileURL);
            contact.setCloudinaryImagePublicId(filename);
        }
       

       contactService.save(contact);
        System.out.println(contactForm);

        session.setAttribute("message", Message.builder().content("You have successfully created a new contact").type(MessageType.green).build());
        return "redirect:/user/contacts/add";
    }
    
    //view contacts
    @RequestMapping
    public String viewContacts(
        @RequestParam(value="page",defaultValue="0") int page,
        @RequestParam(value="size",defaultValue=AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value="sortBy",defaultValue="name") String sortBy,
        @RequestParam(value="direction",defaultValue="asc") String direction
        ,Model model,Authentication authentication){
        //loading all the contacts
        String username=Helper.getEmailOfLoggedInUser(authentication);
        
        User user=userService.getUserByEmail(username);

        Page<Contact> pageContact=contactService.getByUser(user,page,size,sortBy,direction);
        List<Contact> allContacts =
        contactService.getByUserId(user.getUserId());

        model.addAttribute("allContacts", allContacts);

        model.addAttribute("pageContact",pageContact);
        model.addAttribute("pageSize",AppConstants.PAGE_SIZE);
       
        model.addAttribute("contactSearchForm",new ContactSearchForm());

        return "user/contacts";
    }
    //search contacts
    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value="page",defaultValue="0") int page,
        @RequestParam(value="size",defaultValue=AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value="sortBy",defaultValue="name") String sortBy,
        @RequestParam(value="direction",defaultValue="asc") String direction,
        Model model, Authentication authentication
    ){
        
        logger.info("field {} keyword {}",contactSearchForm.getField(),contactSearchForm.getValue());

        var user=userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact>pageContact=null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContact=contactService.searchByName(contactSearchForm.getValue(),size,page,sortBy,direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContact=contactService.searchByEmail(contactSearchForm.getValue(),size,page,sortBy,direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phoneNumber")){
            pageContact=contactService.searchByPhoneNumber(contactSearchForm.getValue(),size,page,sortBy,direction,user);
        }

        logger.info("pageContact {}",pageContact);

        model.addAttribute("contactSearchForm",contactSearchForm);

        model.addAttribute("pageContact",pageContact);

        model.addAttribute("pageSize",AppConstants.PAGE_SIZE);
        List<Contact> allContacts = null;

if(contactSearchForm.getField().equalsIgnoreCase("name")){
    allContacts = contactService.searchAllByName(
        contactSearchForm.getValue(), user);

}else if(contactSearchForm.getField().equalsIgnoreCase("email")){
    allContacts = contactService.searchAllByEmail(
        contactSearchForm.getValue(), user);

}else if(contactSearchForm.getField().equalsIgnoreCase("phoneNumber")){
    allContacts = contactService.searchAllByPhoneNumber(
        contactSearchForm.getValue(), user);
}

model.addAttribute("allContacts", allContacts);
        return "user/search";
    }


    //delete contact
    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId,HttpSession session) {
        contactService.delete(contactId);
        logger.info("contactId {} deleted",contactId);
        session.setAttribute("message", Message.builder().content("Contact is deleted").type(MessageType.green).build());
        return "redirect:/user/contacts";
    }

    //update contact
    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable String contactId,Model model){
        
        
        var contact=contactService.getById(contactId);
        ContactForm contactForm =new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setPicture(contact.getProfilePic());
        
        model.addAttribute("contactForm",contactForm);
        model.addAttribute("contactId",contactId);
        
        
        return "user/update_contact_view";
    }

    //update contact
    @RequestMapping(value="/update/{contactId}", method=RequestMethod.POST)
    public String updateContact(@PathVariable String contactId,@Valid @ModelAttribute ContactForm contactForm,BindingResult bindingResult, Model model,HttpSession session){
        
        if(bindingResult.hasErrors()){
            return "user/update_contact_view";
        }


        var con=contactService.getById(contactId);

        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setDescription(contactForm.getDescription());
        con.setAddress(contactForm.getAddress());
        con.setFavorite(contactForm.isFavorite());
        
        //process image
        if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){
        String fileName=UUID.randomUUID().toString();
        String imageURL=imageService.uploadImage(contactForm.getContactImage(),fileName);
        con.setCloudinaryImagePublicId(fileName);
        con.setProfilePic(imageURL);
        contactForm.setPicture(imageURL);
        }
        var updatedCon=contactService.update(con);
        logger.info("updated contact{}",updatedCon);
        session.setAttribute("message", Message.builder().content("Contact is updated").type(MessageType.green).build());
        return "redirect:/user/contacts/view/"+contactId;
    }

}
