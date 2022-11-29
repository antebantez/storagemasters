package storage_masters.demo.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * - A simple class with  String that is instantiated
 *     when we want to print a message to the user
 */
@Getter
@Setter
@AllArgsConstructor
public class ResponseMessage {
  private String message;
}
