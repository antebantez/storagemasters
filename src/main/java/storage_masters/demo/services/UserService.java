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

import java.util.Optional;

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

  public User registerUser(String username, String password, boolean admin)
          throws UserAlreadyExistsException
  {
    var existing = userRepository.findByName(username);
    if (existing.isPresent()) {
      log.info("Failed to register user since name '" + username + "' already exists.");
      throw new UserAlreadyExistsException();
    }

    var user = new User(username, passwordEncoder.encode(password), admin);
    log.info("Successfully registered user with id '" + user.getUserId() + "'.");
    return userRepository.save(user);

  }


  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userRepository
            .findByName(username)
            .orElseThrow(() -> new UsernameNotFoundException("A user with username '" + username + "' could not be found."));

    return new UserObject(user);
  }


  /*public String login(String username, String password) {
    var userOpt = getByUserName(username);
    if (userOpt.isEmpty()) {
      return null;

    }
    var user = userOpt.get();
    if (!user.getPassword().equals(password)) {
      return null;
    }
    var token = UUID.randomUUID().toString();
    //tokens.put(token, username);
    return token;
  }*/



  /*public Optional<User> getByUserName(String username) {
    return userRepository.findByName(username);
  }*/
}
