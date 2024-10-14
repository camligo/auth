package io.nology.auth.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.nology.auth.user.User;
import io.nology.auth.user.UserService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User register(RegisterDTO data) {
    String encodedPass = passwordEncoder.encode(data.getPassword());
    data.setPassword(encodedPass);

    User newUser = this.userService.create(data);

    return newUser;
  }
}
