package io.nology.auth.greeting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
public class GreetingController {

  @GetMapping("/public")
  public String publicHello() {
    return "Hello from public endpoint";
  }

  @GetMapping("/private")
  public String privateHello() {
    return "Hello from private endpoint";
  }
}
