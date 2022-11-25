package storage_masters.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "A user with the specified name already exists.")
public class UserAlreadyExistsException extends Exception {
}
