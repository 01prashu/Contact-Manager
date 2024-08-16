package com.example.Contact_Manager.service;

import com.example.Contact_Manager.entities.Contact;
import com.example.Contact_Manager.entities.Myuser;
import com.example.Contact_Manager.repositories.MyuserRepositories;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServicesImpl implements  UserServices{
    @Autowired
    private MyuserRepositories myuserRepositories;
    @Autowired
    public UserServicesImpl(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    PasswordEncoder passwordEncoder;
    public  Myuser FindUser(int id)
    {
        Optional<Myuser>myuser_opt = myuserRepositories.findById((long) id);
        return  myuser_opt.get();
    }

    @Transactional
    @Override
    public Myuser SaveUser(Myuser myuser) {
        if(myuser.getContacts() != null)
        {
            for(Contact contact : myuser.getContacts())
            {
                contact.setUser(myuser);
            }
        }
        myuser.setPassword(passwordEncoder.encode(myuser.getPassword()));
        myuser.setRepassword(passwordEncoder.encode(myuser.getRepassword()));
        return myuserRepositories.save(myuser);

    }

    @Override
    public Myuser UpdateUser( Myuser myuser,int user_id) {
        Optional<Myuser>optional_user = myuserRepositories.findById((long) user_id);
        if(optional_user.isPresent())
        {
            Myuser old_user = optional_user.get();
            old_user.setAbout(myuser.getAbout());
            old_user.setDob(myuser.getDob());
            old_user.setEmail(myuser.getEmail());
            old_user.setAddress(myuser.getAddress());
            old_user.setGender(myuser.getGender());
            old_user.setPassword(myuser.getPassword());
            old_user.setRepassword(myuser.getRepassword());
            old_user.setFirstName(myuser.getFirstName());
            old_user.setLastName(myuser.getLastName());
            old_user.getContacts().clear();

            for(Contact contact:myuser.getContacts())
            {
                contact.setUser(old_user);
                old_user.getContacts().add(contact);
            }

            return myuserRepositories.save(old_user);
        }
        else {
            return  null;
        }
    }

    @Override
    public List<Myuser> RetriveAll() {
        Iterable<Myuser>iter = myuserRepositories.findAll();
        List<Myuser>list = new ArrayList<>();
        iter.forEach(list::add);
        return list;
    }

    @Override
    public void DeleteUser(int user_Id) {
        Optional<Myuser>optional_user = myuserRepositories.findById((long) user_Id);
        if(optional_user.isEmpty())
        {
            throw new UsernameNotFoundException("User not found ....");
        }
        Myuser myuser = optional_user.get();
        myuserRepositories.delete(myuser);
        return;
    }

    @Override
    public List<Contact> retriveContact(Long UserId) {
        Optional<Myuser>optionalMyuser = myuserRepositories.findById(UserId);
        Myuser myuser = optionalMyuser.get();
        List<Contact>contactList = myuser.getContacts();
        return contactList;
    }
    @Override
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if( principal instanceof CustomUserDetails)
            {
                return ((CustomUserDetails) principal).getUsername();
            }
        }
        return null; // Or throw an exception if preferred
    }

    @Override
    public Myuser retriveUserByEmail(String email) {
       try {
           Optional<Myuser>optionalMyuser =  myuserRepositories.findByEmail(email);
           return optionalMyuser.get();
       }
       catch (Exception e)
       {
           e.getMessage();
           throw  new UsernameNotFoundException("User not found");
       }
    }

    @Override
    public Myuser findOrCreateUser(String email, String name) {

        Optional<Myuser>op_user = myuserRepositories.findByEmail(email);
        if(op_user.isPresent())
        {
            return op_user.get();
        }
        else {
            Myuser newUser = new Myuser();
            newUser.setContacts(null);
            newUser.setEmail(email);
            newUser.setFirstName(name);
            newUser.setPassword(passwordEncoder.encode("password"));
            newUser.setRepassword(passwordEncoder.encode("password"));
            return myuserRepositories.save(newUser);
        }

    }

    public  boolean checkEmailIsRegistered(String email)
    {
        Myuser user = retriveUserByEmail(email);
        if(user != null)
        {
            return true;
        }
        return false;
    }

    public void updateUserContacts(Long userId, List<Contact> contacts) {
        Myuser myuser = myuserRepositories.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        myuser.setContacts(contacts);
        myuserRepositories.save(myuser); // This will only update the contacts field
    }
}
