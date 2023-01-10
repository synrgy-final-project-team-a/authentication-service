package com.synrgy.security.repository;

import com.synrgy.security.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

    public interface ProfileRepository extends JpaRepository<Profile, Long> {
//        @Query("FROM Profile p WHERE LOWER(p.id) = LOWER(?1)")
//        Profile findOneById(Long id);

    }
