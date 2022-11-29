package storage_masters.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import storage_masters.demo.data.UserFile;
import storage_masters.demo.repositories.FileRepository;
import storage_masters.demo.security.UserObject;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileService {



  @Autowired
  private FileRepository fileRepository;


  /**
   *
   * @param file - An object of the type MultipartFile.
   * @param user A userObject that is put as the foreign key of the file
   * @return - Returns the file of the Class UserFile(That is put in the database)
   * @throws IOException
   */
  public UserFile store(MultipartFile file, UserObject user) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    UserFile userFile = new UserFile(fileName, file.getContentType(), file.getBytes(), user.getUser());

    return fileRepository.save(userFile);
  }

  /**
   *
   * @param id - A specific fileId
   * @return -  Returns a UserFile with a specific id
   */
  public UserFile getFile(String id) {
    return fileRepository.findById(id).get();    
  }

  /**
   *
   * @param user - A userObject that is used to get all the files
   * @return -  Returns a Stream of all the files
   * that the specific user owns
   */
  public Stream<UserFile> getMyFiles(UserObject user) {
    return fileRepository.findAllFilesByUser(user.getUser()).stream();
  }

  /**
   *  - Deletes a file based on the id of the file
   *
   * @param id - An id of a specific file
   */
  public void deleteFile(String id) {
    fileRepository.deleteFileById(id);
  }
}
