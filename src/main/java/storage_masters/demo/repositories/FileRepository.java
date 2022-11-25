package storage_masters.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import storage_masters.demo.data.User;
import storage_masters.demo.data.UserFile;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Stream;

@Transactional
@Repository
public interface FileRepository extends JpaRepository<UserFile, String> {

    Optional<UserFile> findFileByUser(User user);
  
}
