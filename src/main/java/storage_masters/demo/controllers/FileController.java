package storage_masters.demo.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import storage_masters.demo.data.User;
import storage_masters.demo.data.UserFile;
import storage_masters.demo.message.ResponseFile;
import storage_masters.demo.message.ResponseMessage;
import storage_masters.demo.security.UserObject;
import storage_masters.demo.services.FileService;
import storage_masters.demo.services.UserService;

import javax.servlet.http.HttpServletRequest;


@Controller
public class FileController {

  @Autowired
  private final UserService userService;

  @Autowired
  private FileService fileService;

  public FileController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestBody MultipartFile file, @AuthenticationPrincipal UserObject user) throws IOException {

    //Skickar filen och användaren till fileService
    String message = "";
    try {
      fileService.store(file, user);

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }

  }

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
              dbFile.getName(),
              fileDownloadUri,
              dbFile.getType(),
              dbFile.getData().length);
    }).collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(files);
  }

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


  /*@GetMapping("/files")
  public ResponseEntity<List<ResponseFile>> getListFiles() {
    List<ResponseFile> files = fileService.getAllFiles().map(dbFile -> {
      String fileDownloadUri = ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/files/")
          .path(dbFile.getId())
          .toUriString();

      return new ResponseFile(
          dbFile.getName(),
          fileDownloadUri,
          dbFile.getType(),
          dbFile.getData().length);
    }).collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(files);
  }*/

  @GetMapping("/files/{id}")
  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
    UserFile fileDB = fileService.getFile(id);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
        .body(fileDB.getData());
  }


}