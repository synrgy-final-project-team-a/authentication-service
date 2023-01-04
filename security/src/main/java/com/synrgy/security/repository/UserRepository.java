package com.synrgy.security.repository;

import com.synrgy.security.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query("FROM User u WHERE LOWER(u.email) = LOWER(?1)")
    User findOneByEmail(String email);

    @Query("FROM User u WHERE u.otp = ?1")
    User findOneByOTP(String otp);

    @Query("FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    User checkExistingEmail(String email);
//    @Query("FROM User u WHERE u.is_admin = ?1")
//    User findOneByIsAdmin(Boolean isAdmin);
}
