package storage_masters.demo.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  - A Representation of the file that a user uploads
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFile {
  private String fileId;
  private String name;
  private String PostmanUrl;
  private String type;
  private long size;

  
}
