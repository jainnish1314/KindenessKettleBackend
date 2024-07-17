package com.kindnesskattle.bddAtcProject.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String imageUrl;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", profileDescription='" + profileDescription + '\'' +
                ", is_active=" + is_active +
                '}';
    }

    public UserDto(Long id, String firstName, String lastName, String username, String imageUrl, String emailAddress, String profileDescription, boolean is_active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.imageUrl = imageUrl;
        this.emailAddress = emailAddress;
        this.profileDescription = profileDescription;
        this.is_active = is_active;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public boolean isIs_active() {
        return is_active;
    }

    private String emailAddress;

    private String profileDescription;
    private boolean is_active;


}
