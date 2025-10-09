package com.simulasyon.mulakat_simulasyon_api.repository;


import com.simulasyon.mulakat_simulasyon_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
