package com.swipr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.swipr.models.User;

/**
 * Interface for handling database related transactions
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Looks for a particular user based on their email
     * @param email the searched email parameter
     * @return a "list" of users that match the specified email. Will have at most a length of 1
     */
    List<User> findByEmail(String email);

    /**
     * Update the user's venmo account 
     * @param userVenmo venmo account URL to update the field with
     * @param userEmail user's email to look for
     */
    @Modifying
    @Query(value="UPDATE Users u SET venmo=:userVenmo WHERE u.email=:userEmail", nativeQuery=true)
    @Transactional
    void updateUserByEmail(@Param("userVenmo") String userVenmo, @Param("userEmail") String userEmail);

    /**
     * Delete a user from the database
     * @param user the user to delete from the database
     */
    void delete(User user);

    /**
     * Find a User/Buyer/Seller object based on their User ID. Returns a list of 1 user.
     * @param id the id of the users
     */
    List<User> findById(Integer id);
}