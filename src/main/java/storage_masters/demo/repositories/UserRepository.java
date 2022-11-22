package storage_masters.demo.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import storage_masters.demo.data.User;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByName(String name);
  
}
