package io.nology.auth.auth;

import lombok.Getter;
import lombok.Setter;

public class RegisterDTO {

  @Getter
  @Setter
  private String username;

  @Getter
  @Setter
  private String email;

  @Getter
  @Setter
  private String password;
}
