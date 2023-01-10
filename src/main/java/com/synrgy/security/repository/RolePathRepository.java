package com.synrgy.security.repository;

import com.synrgy.security.entity.RolePath;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RolePathRepository extends PagingAndSortingRepository<RolePath, Long> {
    RolePath findOneByName(String rolePathName);

    @Query(value = "SELECT p.* FROM oauth_role_path p " +
            "JOIN oauth_role r ON r.id = p.role_id " +
            "JOIN oauth_user_role ur ON ur.role_id = r.id " +
            "WHERE ur.user_id = ?1", nativeQuery = true)
    <T extends UserDetails> List<RolePath> findByUser(T user);
}
