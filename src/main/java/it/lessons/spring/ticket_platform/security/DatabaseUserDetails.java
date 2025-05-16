package it.lessons.spring.ticket_platform.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.lessons.spring.ticket_platform.model.Role;
import it.lessons.spring.ticket_platform.model.User;

public class DatabaseUserDetails implements UserDetails {
    
    private final Integer id;
    private final String username;
    private final String mail;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public DatabaseUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.mail = user.getMail();
        this.password = user.getPassword();
        this.authorities = new ArrayList<>();
        for(Role ruolo : user.getRoles()) {
            this.authorities.add(new SimpleGrantedAuthority(ruolo.getName()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public String getMail() {
    return this.mail;
    }

     public Integer getId() {
        return this.id;
    }
}