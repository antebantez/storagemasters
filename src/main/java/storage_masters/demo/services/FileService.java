package storage_masters.demo.services;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import storage_masters.demo.data.User;
import storage_masters.demo.data.UserFile;
import storage_masters.demo.repositories.FileRepository;
import storage_masters.demo.repositories.UserRepository;
import storage_masters.demo.security.UserObject;

@Service
public class FileService {



  @Autowired
  private FileRepository fileRepository;



  public UserFile store(MultipartFile file, UserObject user) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    UserFile userFile = new UserFile(fileName, file.getContentType(), file.getBytes(), user.getUser());

    return fileRepository.save(userFile);
  }

  public UserFile getFile(String id) {
    return fileRepository.findById(id).get();    
  }


  public Stream<UserFile> getMyFiles(UserObject user) {
    return fileRepository.findAllFilesByUser(user.getUser()).stream();
  }


  public void deleteFile(String id) {
    fileRepository.deleteFileById(id);
  }
}
