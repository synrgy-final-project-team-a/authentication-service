package com.synrgy.security.repository;

import com.synrgy.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query(value = "SELECT * FROM oauth_user WHERE LOWER(email) = LOWER(:email)", nativeQuery = true)
    User findOneByEmail(@Param("email") String username);

    @Query("FROM User u WHERE u.otp = ?1")
    User findOneByOTP(String otp);

    @Query("FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    User checkExistingEmail(String username);
//    @Query("FROM User u WHERE u.is_admin = ?1")
//    User findOneByIsAdmin(Boolean isAdmin);
}
