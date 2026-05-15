package com.scm.scmproject.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserForm {
    @NotBlank(message="Username is required")
    @Size(min=3,message="Min 3 characters name is required")
    private String name;
    @Email(message="Invlid Email Address")
    private String email;
    @NotBlank(message="Password is required")
    @Size(min=3,message="Min 3 characters name is required")
    private String password;
    @Size(min=8,max=12,message="Invalid Phone Number")
    private String phoneNumber;
    private String about;
    private MultipartFile userImage;

    private String picture;
}
