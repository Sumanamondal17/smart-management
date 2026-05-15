package com.scm.scmproject.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.scmproject.entities.Contact;
import com.scm.scmproject.entities.User;

public interface ContactService {
    Contact save(Contact contact);
    Contact update(Contact contact);
    List<Contact>getAll();
    Contact getById(String id);
    void delete(String id);
    Page<Contact>searchByName(String nameKeyword, int size,int page,String sortBy,String order,User user);
    Page<Contact>searchByEmail(String emailKeyword,int size,int page,String sortBy,String order,User user);
    Page<Contact>searchByPhoneNumber(String phoneNumberKeyword,int size,int page,String sortBy,String order,User user);
     // export all matched results
    List<Contact> searchAllByName(String keyword, User user);

    List<Contact> searchAllByEmail(String keyword, User user);

    List<Contact> searchAllByPhoneNumber(String keyword, User user);
    List<Contact>getByUserId(String userId);

    Page<Contact>getByUser(User user,int page,int size,String sortField,String sortDirection);
    long countByUser(User user);
    long countFavoriteByUser(User user);
}
