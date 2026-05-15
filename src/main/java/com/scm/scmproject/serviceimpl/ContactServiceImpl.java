package com.scm.scmproject.serviceimpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.scmproject.entities.Contact;
import com.scm.scmproject.entities.User;
import com.scm.scmproject.helpers.ResourceNotFoundException;
import com.scm.scmproject.repositories.ContactRepo;
import com.scm.scmproject.services.ActivityService;
import com.scm.scmproject.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private  ActivityService activityService;

    @Override
    public Contact save(Contact contact) {
       String contactId=UUID.randomUUID().toString();
       contact.setId(contactId);
       activityService.saveActivity(
    contact.getUser(),
    "Added",
    contact.getName()
);
       return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        var contactOld= contactRepo.findById(contact.getId()).orElseThrow(()->new ResourceNotFoundException("contact not found..."));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setProfilePic(contact.getProfilePic());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        activityService.saveActivity(
    contact.getUser(),
    "Updated",
    contact.getName()
);
        return contactRepo.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
       return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
       return contactRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("contact not found..."));
    }

    @Override
    public void delete(String id) {
        var contact=contactRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("contact not found..."));
        activityService.saveActivity(
    contact.getUser(),
    "Deleted",
    contact.getName()
);
        contactRepo.delete(contact);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
      return contactRepo.findByUserId(userId);
    }

   

    @Override
    public Page<Contact> getByUser(User user,int page,int size,String sortBy,String direction) {
        Sort sort=direction.equals("desc")?Sort.by(sortBy).descending() :Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page,size,sort);

        return contactRepo.findByUser(user,pageable);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order,User user) {
        Sort sort=order.equals("desc")?Sort.by(sortBy).descending() :Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page,size,sort);
        return contactRepo.findByNameContainingAndUser(nameKeyword,user, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,User user) {
        Sort sort=order.equals("desc")?Sort.by(sortBy).descending() :Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page,size,sort);
        return contactRepo.findByEmailContainingAndUser(emailKeyword,user, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy,String order,User user) {
         Sort sort=order.equals("desc")?Sort.by(sortBy).descending() :Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page,size,sort);
        return contactRepo.findByPhoneNumberContainingAndUser(phoneNumberKeyword,user, pageable);
    }

   @Override
public List<Contact> searchAllByName(String keyword, User user) {
    return contactRepo.findByNameContainingAndUser(keyword, user);
}

@Override
public List<Contact> searchAllByEmail(String keyword, User user) {
    return contactRepo.findByEmailContainingAndUser(keyword, user);
}

@Override
public List<Contact> searchAllByPhoneNumber(String keyword, User user) {
    return contactRepo.findByPhoneNumberContainingAndUser(keyword, user);
}

@Override
public long countByUser(User user) {
    return contactRepo.countByUser(user);
}

@Override
public long countFavoriteByUser(User user) {
   return contactRepo.countByUserAndFavorite(user, true);
}


    
}
