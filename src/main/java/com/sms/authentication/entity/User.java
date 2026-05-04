package com.sms.authentication.entity;

import com.sms.authentication.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
