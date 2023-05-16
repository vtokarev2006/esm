package com.epam.esm.repository.springdata;

import com.epam.esm.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}