package com.scm.scmproject.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = "contacts")

public class User implements UserDetails{
    @Id
    private String userId;
    @Column(name="name",nullable=false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Column(length = 1000)
    private String about;
    private String phoneNumber;
    @Getter(value=AccessLevel.NONE)
    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    private boolean emailVerified = false;
    @Builder.Default
    private boolean phoneVerified = false;
    @Builder.Default
    @Enumerated(value=EnumType.STRING)
    private Providers provider = Providers.SELF;
    private String providerUserId;
    @Builder.Default
    @OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch=FetchType.LAZY,orphanRemoval=true)
    private List<Contact> contacts=new ArrayList<>();

     private String profilePic;
     private String cloudinaryImagePublicId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }
    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public boolean isEnabled(){
        return this.enabled;
    }
}
