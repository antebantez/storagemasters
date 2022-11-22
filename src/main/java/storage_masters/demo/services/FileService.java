package storage_masters.demo.services;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import storage_masters.demo.data.UserFile;
import storage_masters.demo.repositories.FileRepository;

@Service
public class FileService {


  @Autowired
  private FileRepository fileRepository;

  public UserFile store(MultipartFile file) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    UserFile userFile = new UserFile(fileName, file.getContentType(), file.getBytes());

    return fileRepository.save(userFile);
  }

  public UserFile getFile(String id) {
    return fileRepository.findById(id).get();    
  }

  public Stream<UserFile> getAllFiles() {
    return fileRepository.findAll().stream();
  }
  
}
