package io.nology.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.nology.auth.auth.RegisterDTO;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public User create(RegisterDTO data) {
    User newUser = new User(data.getEmail(), data.getUsername(), data.getPassword());

    return this.userRepository.save(newUser);
  }
}
