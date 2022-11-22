package storage_masters.demo.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import storage_masters.demo.data.User;
import storage_masters.demo.repositories.UserRepository;

@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Autowired
  public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  public User registerUser(String username, String password) {
    var user = new User(UUID.randomUUID().toString(), username, password);
    return userRepository.save(user);

  }

  public String login(String username, String password) {
    var userOpt = getByUserName(username);
    if (userOpt.isEmpty()) {
      return null;

    }
    var user = userOpt.get();
    if (!user.getPassword().equals(password)) {
      return null;
    }
    var token = UUID.randomUUID().toString();
    tokens.put(token, username);
    return token;
  }

  public User verifyToken(String token) {
    var username = tokens.get(token);
    if (username == null)
      return null;

    var user = getByUserName(username);
    return user.orElse(null);
  }

  public Optional<User> getByUserName(String username) {
    return userRepository.findByName(username);
  }
}
