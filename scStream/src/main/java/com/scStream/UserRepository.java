package com.scStream;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByName(String name);

    void deleteByName(String name);
}
