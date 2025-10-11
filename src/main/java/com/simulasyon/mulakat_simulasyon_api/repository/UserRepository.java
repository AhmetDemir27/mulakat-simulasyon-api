package com.simulasyon.mulakat_simulasyon_api.repository;


import com.simulasyon.mulakat_simulasyon_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByGoogleId(String googleId);

}
