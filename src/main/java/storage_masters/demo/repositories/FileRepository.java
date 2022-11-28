package storage_masters.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import storage_masters.demo.data.User;
import storage_masters.demo.data.UserFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface FileRepository extends JpaRepository<UserFile, String> {
    List<UserFile> findFileByUser(User user);

    List<UserFile> findAllFilesByUser(User user);


    UserFile findFileById(String id);

    Optional<UserFile> deleteFileById(String id);
}
