package com.kindnesskattle.bddAtcProject.Repository;


import com.kindnesskattle.bddAtcProject.DTO.UserDto;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByEmailAddress(String emailAddress);



    UserAccount findByUsername(String username);

    @Procedure(name = "insert_User")
    UserDto insertUser(
            @Param("first_name") String first_name,
            @Param("last_name") String last_name,
            @Param("username") String username,
            @Param("image_url") String image_url,
            @Param("email_address") String email_address,
            @Param("profile_description") String profile_description,
            @Param("is_active") boolean is_active
    );
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM user_accounts u WHERE u.email_address = :email", nativeQuery = true)
    public Integer existsByEmail(String email);

    @Query("SELECT u.emailAddress FROM UserAccount u")
    List<String> findAllEmails();

    @Query("SELECT u.emailAddress FROM UserAccount u WHERE u.id = :userId")
    String findEmailById(@Param("userId") Long userId);

}

