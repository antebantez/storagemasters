package storage_masters.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import storage_masters.demo.data.UserFile;

public interface FileRepository extends JpaRepository<UserFile, String> {
  
}
