package storage_masters.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import storage_masters.demo.data.User;
import storage_masters.demo.exceptions.UserAlreadyExistsException;
import storage_masters.demo.repositories.UserRepository;
import storage_masters.demo.security.UserObject;

@Service("customUserServiceDetails")
@Slf4j
public class UserService implements UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;


  @Autowired
  public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  /**
   *
   * @param username - A username of the typ String
   * @param password - A password of the type String, this is encoded before it is saved in the database
   * @return - Returns a User object after we save it in the database
   * @throws UserAlreadyExistsException
   */
  public User registerUser(String username, String password)
          throws UserAlreadyExistsException
  {
    var existing = userRepository.findByName(username);
    if (existing.isPresent()) {
      log.info("Failed to register user since name '" + username + "' already exists.");
      throw new UserAlreadyExistsException();
    }

    var user = new User(username, passwordEncoder.encode(password));
    log.info("Successfully registered user with id '" + user.getUserId() + "'.");
    return userRepository.save(user);

  }

  /**
   *
   * @param username - A username of the type String
   * @return - Returns a user of the type User
   * @throws UsernameNotFoundException
   */
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userRepository
            .findByName(username)
            .orElseThrow(() -> new UsernameNotFoundException("A user with username '" + username + "' could not be found."));

    return new UserObject(user);
  }

}
