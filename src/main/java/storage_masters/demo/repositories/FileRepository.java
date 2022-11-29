package storage_masters.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import storage_masters.demo.data.User;
import storage_masters.demo.data.UserFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


/**
 * In the repository we can make our own custom queries
 */
@Transactional
@Repository
public interface FileRepository extends JpaRepository<UserFile, String> {

    List<UserFile> findAllFilesByUser(User user);

    Optional<UserFile> deleteFileById(String id);
}
