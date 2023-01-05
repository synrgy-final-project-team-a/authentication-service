package com.synrgy.security.repository;

import com.synrgy.security.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

    public interface ProfileRepository extends JpaRepository<Profile, Long> {

    }
