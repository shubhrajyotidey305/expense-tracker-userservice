package com.example.userservice.repository;

import com.example.userservice.entities.UserInfo;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends CrudRepository<UserInfo, String>
{
    // write as it is; required from JPA
    Optional<UserInfo> findByUserId(String userId);

}
