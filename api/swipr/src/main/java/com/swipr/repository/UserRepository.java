package com.swipr.repository;

import com.swipr.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Interface for handling database related transactions
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);
    List<User> findByVenmo(String venmo);
    // Query to update a user's venmo account. This is the only field that a user can realistically update
    @Modifying
    @Query(value="UPDATE Users u SET venmo=:userVenmo WHERE u.email=:userEmail", nativeQuery=true)
    @Transactional
    void updateUserByEmail(@Param("userVenmo") String userVenmo, @Param("userEmail") String userEmail);
}