package com.epam.esm.repository.springdata;

import com.epam.esm.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u, sum(o.price) as price from User u join Order o on (u = o.user) group by u order by price DESC")
    List<User> getUsersOrderedByOrdersCostDesc(Pageable page);

}
