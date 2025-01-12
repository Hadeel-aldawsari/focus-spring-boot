package com.example.focus.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class MyUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //    @NotEmpty(message = "Email cannot be empty")
//    @Email(message = "Email should be valid")
    @Column(columnDefinition = "varchar(40) not null unique")
    private String email;

    //    @NotEmpty(message = "Username cannot be empty")
//    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Column(columnDefinition = "varchar(40) not null unique")
    private String username;

    //    @NotEmpty(message = "Please enter your password")
//    @Size(min = 6, message = "Password must be at least 6 characters")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$",
//            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    @Column(columnDefinition = "varchar(255) not null")
    private String password;

    @Pattern(regexp = "PHOTOGRAPHER|EDITOR|STUDIO|ADMIN",message = "role should be admin or user or employee")
    @Column(columnDefinition = "varchar(14) ")
    private String role;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Photographer photographer;


@JsonIgnore
    @OneToOne
    @PrimaryKeyJoinColumn
    private ProfilePhotographer profilePhotographer;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Editor editor;

    @OneToOne
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private ProfileEditor profileEditor;


    @OneToOne
    @PrimaryKeyJoinColumn
    private Studio studio;

    @OneToOne
    @PrimaryKeyJoinColumn
    private ProfileStudio profileStudio;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }


    public void setRole(String role){
        this.role=role;
    }


    public void setPassword(String passeord){
        this.password=passeord;
    }

}