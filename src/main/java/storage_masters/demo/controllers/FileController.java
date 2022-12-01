package storage_masters.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import storage_masters.demo.data.UserFile;
import storage_masters.demo.message.ResponseFile;
import storage_masters.demo.message.ResponseMessage;
import storage_masters.demo.security.UserObject;
import storage_masters.demo.services.FileService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;




@Controller
public class FileController {

  @Autowired
  private FileService fileService;


  /**
   *  Endpoint to upload a file to the database
   *
   * @param file - An object of the type MultipartFile.
   * @param user - An userObject of the currently logged-in user, taken in from the Bearer token.
   * @return - An ResponseEntity<ResponseMessage> with a response message to the user
   * @throws IOException
   */
  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestBody MultipartFile file, @AuthenticationPrincipal UserObject user) throws IOException {

    //Skickar filen och användaren till fileService
    String message = "";
    if(file.isEmpty()){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    else{
      try {
        fileService.store(file, user);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }



  }

  /**
   *  Endpoint to get all files of a logged-in user
   *
   * @param user - An userObject of the currently logged-in user, taken in from the Bearer token.
   * @return - A List of all the files owned by the logged-in user
   */
  @GetMapping("/myfiles")
  public ResponseEntity<List<ResponseFile>> myFiles(@AuthenticationPrincipal UserObject user) {
    List<ResponseFile> files = fileService.getMyFiles(user).map(dbFile -> {
      String fileDownloadUri = ServletUriComponentsBuilder
              .fromCurrentContextPath()
              .path("/files/")
              .path(dbFile.getId())
              .toUriString();

      System.out.println(fileDownloadUri);

      return new ResponseFile(
              dbFile.getId(),
              dbFile.getName(),
              fileDownloadUri,
              dbFile.getType(),
              dbFile.getData().length);
    }).collect(Collectors.toList());

    if(files.isEmpty()){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    else return ResponseEntity.status(HttpStatus.OK).body(files);
  }

  /**
   *  Endpoint to get one specific file from the database
   *
   * @param id - Takes the id of a file as a PathVariable
   * @param user - An userObject of the currently logged-in user, taken in from the Bearer token.
   *             Used to check if the file belongs to the logged-in user
   * @return - Returns a ByteArray of the selected file
   */
  @GetMapping("/files/{id}")
  public ResponseEntity<byte[]> getFile(@PathVariable String id, @AuthenticationPrincipal UserObject user) {
    UserFile fileDB = fileService.getFile(id);
    var realUser = user.getUser().getUserId();
    var fileUser = fileService.getFile(id).getUser().getUserId();



    if(fileUser.toString().equals(realUser.toString())){
      return ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
              .body(fileDB.getData());
    }
    else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

  }

  /**
   *  Endpoint to delete a file
   *
   * @param id - Takes the id of a file as a PathVariable
   * @param user - An userObject of the currently logged-in user, taken in from the Bearer token.
   *    *             Used to check if the file belongs to the logged-in user
   * @return  Returns a responsemessage, whether the deletion went through of not
   * @throws IOException
   */
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String id, @AuthenticationPrincipal UserObject user) throws IOException {
    var realUser = user.getUser().getUserId();
    var fileUser = fileService.getFile(id).getUser().getUserId();

    if(fileUser.toString().equals(realUser.toString())){
      fileService.deleteFile(id);
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("File deleted successfully"));
    }
    else {
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("You don't have permission to delete this file"));
    }

  }
}