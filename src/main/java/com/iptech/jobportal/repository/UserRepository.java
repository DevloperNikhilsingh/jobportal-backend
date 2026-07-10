package com.iptech.jobportal.repository;

import com.iptech.jobportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

     // Naya — sirf verified job seekers ko fetch karne ke liye
    List<User> findByRoleAndIsVerifiedTrue(User.Role role);
}