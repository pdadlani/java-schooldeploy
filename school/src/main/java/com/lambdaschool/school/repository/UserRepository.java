package com.lambdaschool.school.repository;

import com.lambdaschool.school.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>
{
    User findByUsername(String username);
}
