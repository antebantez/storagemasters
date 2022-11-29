package storage_masters.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  - Custom exception if user already exists in the database
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "A user with the specified name already exists.")
public class UserAlreadyExistsException extends Exception {
}
