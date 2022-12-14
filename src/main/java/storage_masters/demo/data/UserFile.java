package storage_masters.demo.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 *  - This is the File entity of the
 * @Id - Randomly generated UUID-Value
 * @Lob - Makes us able to save binary data to the database
 * @ManyToOne - Makes a connection to the user that uploads the file
 */
public class UserFile {


  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;


  private String name;

  private String type;

  @ManyToOne
  private User user;

  @Lob
  private byte[] data;

  public UserFile(String name, String type, byte[] data, User user) {
    this.name = name;
    this.type = type;
    this.data = data;
    this.user = user;
    
  }
  
}
